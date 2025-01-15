package org.example.easyrecruitbackend.controller;

import org.example.easyrecruitbackend.dto.DashboardDTO;
import org.example.easyrecruitbackend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{recruteurId}")
    public DashboardDTO getDashboardData(@PathVariable Long recruteurId) {
        return dashboardService.getDashboardData(recruteurId);
    }
}
