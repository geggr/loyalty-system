package com.grimoire.loyalty.utils.variants;

import java.util.function.Function;

public record Err<V, E>(E error) implements Result<V, E> {
    @Override
    @SuppressWarnings("unchecked")
    public <N> Result<N, E> map(Function<V, N> mapper) {
        return (Result<N, E>) this;
    }

    @Override
    public <N> Result<V, N> mapError(Function<E, N> mapper) {
        return new Err<V,N>(mapper.apply(error));
    }

    @Override
    public V unwrap() {
        throw new IllegalStateException("Failed to unwrap becase its a Err<%s>".formatted(error));
    }

    @Override
    public E unwrapError() {
        return error;
    }
}
