package com.github.bosamatheus.reactivewebflux.payment.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class InMemoryDatabase implements Database {

    private static final Map<String, String> DATABASE = new ConcurrentHashMap<>();

    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public <T> T save(final String key, final T value) {
        final var data = this.mapper.writeValueAsString(value);
        DATABASE.put(key, data);
        sleep(30);
        return value;
    }

    @Override
    public <T> Optional<T> get(final String key, final Class<T> clazz) {
        final String json = DATABASE.get(key);
        sleep(15);
        return Optional.ofNullable(json)
            .map(data -> mapJsonToObject(data, clazz));
    }

    @Override
    public Set<String> getKeys() {
        return DATABASE.keySet();
    }

    private <T> T mapJsonToObject(final String data, final Class<T> clazz) {
        try {
            return mapper.readValue(data, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
