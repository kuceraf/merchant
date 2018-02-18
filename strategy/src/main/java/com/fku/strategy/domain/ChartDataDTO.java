package com.fku.strategy.domain;

import lombok.Data;

import java.util.List;

@Data
public class ChartDataDTO {
    private String name;
    // data pair [X, Y]
    private List<int[]> data;
}
