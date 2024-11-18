package com.grimoire.loyalty.utils.variants;

import java.util.function.Function;

public record Success<V, E>(V value) implements Result<V, E> {

    @Override
    public <N> Result<N, E> map(Function<V, N> mapper) {
        return new Success<N, E>(mapper.apply(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <N> Result<V, N> mapError(Function<E, N> mapper) {
        return (Result<V, N>) this;
    }

    @Override
    public V unwrap() {
        return value;
    }

    @Override
    public E unwrapError() {
        throw new IllegalStateException("Failed to unwrap because value is a Success<%s>".formatted(value));
    }
    
}
