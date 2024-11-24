package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortByCheckOut implements SortStrategy {
    @Override
    public List<TravelCommunity> sort(List<TravelCommunity> forums) {
        List<TravelCommunity> sortedList = new ArrayList<>(forums);
        Collections.sort(sortedList, (a, b) -> a.getEndDate().compareTo(b.getEndDate()));
        return sortedList;
    }
}
