package com.grimoire.loyalty.utils.variants;

import java.util.function.Function;

public sealed interface Result<V, E> permits Success, Err {
    <N> Result<N, E> map(Function<V, N> mapper);
    <N> Result<V, N> mapError(Function<E, N> mapper);

    V unwrap();
    E unwrapError();
}
