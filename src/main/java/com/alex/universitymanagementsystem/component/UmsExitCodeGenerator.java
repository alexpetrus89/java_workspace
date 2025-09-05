package com.alex.universitymanagementsystem.component;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class UmsExitCodeGenerator implements ExitCodeGenerator {

    @Override
    public int getExitCode() {
        return 0;
    }

}
