package com.example.barberbooking.controller;

import com.example.barberbooking.dto.response.DashboardResponse;
import com.example.barberbooking.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class DashBoardController {

private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardResponse>getStats(){
        return ResponseEntity.ok(dashboardService.getStats());
    }


}
