package com.thegame.ui.controller;


import com.thegame.business.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/timer")
public class TimerRestController {

    private final TimerService timerService;

    @Autowired
    public TimerRestController(TimerService timerService) {
        this.timerService = timerService;
    }

    @GetMapping("/{villageId}/buildings")
    public Map<String, Object> getBuildingsTimer(@PathVariable Long villageId) {
        Map<String, Object> response = new HashMap<>();
        response.put("timer", timerService.getBuildingTimerByVillageId(villageId));
        return response;
    }
}


