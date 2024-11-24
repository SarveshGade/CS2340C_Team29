package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortByCheckIn implements SortStrategy {
    @Override
    public List<TravelCommunity> sort(List<TravelCommunity> forums) {
        List<TravelCommunity> sortedList = new ArrayList<>(forums);
        Collections.sort(sortedList, (a, b) -> a.getStartDate().compareTo(b.getStartDate()));
        return sortedList;
    }
}
