package com.sjsu.cmpe272.prodwell.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCompletionStats {
    private long completedBeforeTime;
    private long completedOnTime;
    private long completedLate;
}