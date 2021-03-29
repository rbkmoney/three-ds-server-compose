package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonMapper {

    private final ObjectMapper objectMapper;

    private final ResourceLoader resourceLoader;

    @SneakyThrows
    public <T> T readFromFile(String fullPath, Class<T> valueType) {
        return objectMapper.readValue(readStringFromFile(fullPath), valueType);
    }

    @SneakyThrows
    public String readStringFromFile(String fullPath) {
        return IOUtils.toString(
                resourceLoader.getResource("classpath:__files/" + fullPath).getInputStream(),
                Charsets.UTF_8);
    }
}
