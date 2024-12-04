package com.sjsu.cmpe272.prodwell.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StressLevelTrend {
    private String date;
    private double averageStressLevel;
}