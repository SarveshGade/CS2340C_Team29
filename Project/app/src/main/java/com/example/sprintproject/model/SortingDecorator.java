package com.example.sprintproject.model;

import java.util.List;

public class SortingDecorator implements SortStrategy {
    private final SortStrategy wrappedStrategy;

    public SortingDecorator(SortStrategy wrappedStrategy) {
        this.wrappedStrategy = wrappedStrategy;
    }

    @Override
    public List<TravelCommunity> sort(List<TravelCommunity> forums) {
        return wrappedStrategy.sort(forums);
    }
}

