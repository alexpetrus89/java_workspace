package com.alex.universitymanagementsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.universitymanagementsystem.component.SystemManager;


@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    @GetMapping("/restart")
    public ResponseEntity<String> restartApp() {
        SystemManager.restart();
        return ResponseEntity.ok("Restart triggered. Check logs for progress.");
    }

    @GetMapping("/shutdown")
    public ResponseEntity<String> shutdownApp() {
        SystemManager.shutdown();
        return ResponseEntity.ok("Shutdown triggered. Application will exit.");
    }


}

