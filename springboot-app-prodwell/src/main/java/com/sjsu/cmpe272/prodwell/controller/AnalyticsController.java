// AnalyticsController.java
package com.sjsu.cmpe272.prodwell.controller;

import com.sjsu.cmpe272.prodwell.dto.ManagerAnalyticsDTO;
import com.sjsu.cmpe272.prodwell.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
//@CrossOrigin(origins = "${cors.allowed-origins}")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalyticsController {
    
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/manager/{projectCode}")
    public ResponseEntity<ManagerAnalyticsDTO> getManagerAnalytics(@PathVariable String projectCode) {
        return ResponseEntity.ok(analyticsService.getAnalytics(projectCode, false));
    }

    @GetMapping("/hr")
    public ResponseEntity<ManagerAnalyticsDTO> getHRAnalytics() {
        return ResponseEntity.ok(analyticsService.getAnalytics(null, true));
    }

    @GetMapping("/team-insights/{projectCode}")
    public ResponseEntity<String> getTeamInsights(@PathVariable String projectCode) {
        return ResponseEntity.ok(analyticsService.teamInsights(projectCode, false));
    }

    @GetMapping("/hr-insights")
    public ResponseEntity<String> getHRInsights() {
        return ResponseEntity.ok(analyticsService.teamInsights(null, true));
    }
}