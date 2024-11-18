package com.grimoire.loyalty.products;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ProductSkuGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ALPHABET_SIZE = ALPHABET.length();
    
    public static String generate(){
        return generate(10);
    }

    public static String generate(int length){
        final var random = new Random();
        return IntStream
            .range(0, length)
            .<String>mapToObj(_ -> {
                return String.valueOf(ALPHABET.charAt(random.nextInt(ALPHABET_SIZE)));
            })
            .collect(Collectors.joining());
    }
}
