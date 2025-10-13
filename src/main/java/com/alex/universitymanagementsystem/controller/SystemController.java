package com.alex.universitymanagementsystem.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.component.SystemManager;


@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    @GetMapping
    public ModelAndView system() {
        return new ModelAndView("user_admin/system/system");
    }

    @GetMapping("/restart")
    public ResponseEntity<String> restartApp() throws URISyntaxException, IOException {
        SystemManager.restartByJVM();
        return ResponseEntity.ok("Restart triggered. Check logs for progress.");
    }

    @GetMapping("/shutdown")
    public ResponseEntity<String> shutdownApp() {
        SystemManager.shutdown();
        return ResponseEntity.ok("Shutdown triggered. Application will exit.");
    }


}

