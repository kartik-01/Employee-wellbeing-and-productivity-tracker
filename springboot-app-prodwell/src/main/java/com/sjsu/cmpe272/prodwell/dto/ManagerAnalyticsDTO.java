package com.sjsu.cmpe272.prodwell.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAnalyticsDTO {
    private TaskCompletionStats taskStats;
    private List<StressLevelTrend> teamStressLevels;
    private String projectCode;
    private int peopleCount;
}