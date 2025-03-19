package com.example.demo.others;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pair <T1, T2> {
    private final T1 first;
    private final T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
}
