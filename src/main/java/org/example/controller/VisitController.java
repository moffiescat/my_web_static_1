package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.service.VisitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @GetMapping("/today")
    public ApiResponse<Long> getTodayVisitCount() {
        return ApiResponse.success(visitService.getTodayVisitCount());
    }
}
