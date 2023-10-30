package com.wanted.preonboarding.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortValue {
    COUNT("count"),
    VIEW_COUNT("view_count"),
    LIKE_COUNT("like_count"),
    SHARE_COUNT("share_count");

    private final String value;
}
