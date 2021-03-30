package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PreparationFlowScheduleConfig {

    @Bean
    public PreparationFlowCronScheduler preparationFlowScheduler(
            @Value("${preparation-flow.scheduler.enabled}") boolean isEnabledOnSchedule,
            PreparationFlowDsProviderProperties visaPreparationFlowProperties,
            PreparationFlowDsProviderProperties mastercardPreparationFlowProperties,
            PreparationFlowDsProviderProperties mirPreparationFlowProperties,
            RestTemplate restTemplate,
            @Value("${client.three-ds-server-storage.url}") String url) {
        return new PreparationFlowCronScheduler(
                isEnabledOnSchedule,
                visaPreparationFlowProperties,
                mastercardPreparationFlowProperties,
                mirPreparationFlowProperties,
                restTemplate,
                url);
    }

    @Bean
    @ConfigurationProperties("preparation-flow.ds-provider.visa")
    public PreparationFlowDsProviderProperties visaPreparationFlowProperties() {
        return new PreparationFlowDsProviderProperties();
    }

    @Bean
    @ConfigurationProperties("preparation-flow.ds-provider.mastercard")
    public PreparationFlowDsProviderProperties mastercardPreparationFlowProperties() {
        return new PreparationFlowDsProviderProperties();
    }

    @Bean
    @ConfigurationProperties("preparation-flow.ds-provider.mir")
    public PreparationFlowDsProviderProperties mirPreparationFlowProperties() {
        return new PreparationFlowDsProviderProperties();
    }
}
