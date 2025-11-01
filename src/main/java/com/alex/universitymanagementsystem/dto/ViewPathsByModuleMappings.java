package com.alex.universitymanagementsystem.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ViewPathsByModuleMappings {

    private Map<String, List<String>> pathsByModule = new HashMap<>();

    public Map<String, List<String>> getPathsByModule() {
        return pathsByModule;
    }

    public void setPathsByModule(Map<String, List<String>> pathsByModule) {
        this.pathsByModule = pathsByModule;
    }

    public Stream<String> streamAllViewPaths() {
        return pathsByModule.values().stream().flatMap(List::stream);
    }


}

