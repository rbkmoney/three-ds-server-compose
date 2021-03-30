package com.example.demo;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public class PreparationFlowCronScheduler {

    private final boolean isEnabledOnSchedule;
    private final PreparationFlowDsProviderProperties visaProperties;
    private final PreparationFlowDsProviderProperties mastercardProperties;
    private final PreparationFlowDsProviderProperties mirProperties;
    private final RestTemplate restTemplate;
    private final String url;

    @EventListener(value = ApplicationReadyEvent.class)
    public void onStartup() {
        try {
            updatePreparationFlow();
        } catch (ResourceAccessException ex) {
            log.warn("3DSS Storage connection refused at start,", ex);
        }
    }

    @Scheduled(cron = "${preparation-flow.scheduler.schedule.cron}")
    public void onSchedule() {
        if (isEnabledOnSchedule) {
            updatePreparationFlow();
        }
    }

    private void updatePreparationFlow() {
        updateJob(
                visaProperties.isEnabled(),
                DsProvider.VISA.getId(),
                visaProperties.getMessageVersion());

        updateJob(
                mastercardProperties.isEnabled(),
                DsProvider.MASTERCARD.getId(),
                mastercardProperties.getMessageVersion());

        updateJob(
                mirProperties.isEnabled(),
                DsProvider.MIR.getId(),
                mirProperties.getMessageVersion());
    }

    private void updateJob(boolean isEnabled, String dsProviderId, String messageVersion) {
        if (isEnabled) {
            initRBKMoneyPreparationFlow(dsProviderId, messageVersion);
        }
    }

    private void initRBKMoneyPreparationFlow(String dsProviderId, String messageVersion) {
        log.info("Init RBKMoney preparation flow, dsProviderId={}", dsProviderId);

        PreparationRequest request = PreparationRequest.builder()
                .providerId(dsProviderId)
                .messageVersion(messageVersion)
                .build();

        restTemplate.postForEntity(url, request, Void.class);
    }

    @Data
    @Builder
    public static class PreparationRequest {

        private String providerId;
        private String messageVersion;

    }
}
