package com.github.bosamatheus.reactivewebflux.payment.repositories;

import java.util.Optional;

public interface Database {

    <T> T save(String key, T value);

    <T> Optional<T> get(String key, Class<T> clazz);

}
