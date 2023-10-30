package com.wanted.preonboarding.statistics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortType {
    DATE("date"),
    HOUR("hour");

    private final String value;
}
