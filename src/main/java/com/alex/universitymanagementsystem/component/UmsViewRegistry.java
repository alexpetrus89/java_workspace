package com.alex.universitymanagementsystem.component;


import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.dto.FieldErrorViewMappings;
import com.alex.universitymanagementsystem.dto.RoleBasedUrlMappings;
import com.alex.universitymanagementsystem.dto.ViewPathsByModuleMappings;

@Component
public class UmsViewRegistry {

    private final ViewPathsByModuleMappings viewPathsByModuleMappings;
    private final FieldErrorViewMappings fieldErrorViewMappings;
    private final RoleBasedUrlMappings roleBasedUrlMappings;

    public UmsViewRegistry(UmsJsonConfigLoader loader) {
        this.viewPathsByModuleMappings = loader.loadJson("views/view-paths-by-module-mappings.json", ViewPathsByModuleMappings.class);
        this.fieldErrorViewMappings = loader.loadJson("views/field-error-view-mappings.json", FieldErrorViewMappings.class);
        this.roleBasedUrlMappings = loader.loadJson("views/role-based-urls-mappings.json", RoleBasedUrlMappings.class);
    }

    public ViewPathsByModuleMappings getViewPathsByModuleMappings() {
        return viewPathsByModuleMappings;
    }

    public FieldErrorViewMappings getFieldErrorViewMappings() {
        return fieldErrorViewMappings;
    }

    public RoleBasedUrlMappings getRoleBasedUrlMappings() {
        return roleBasedUrlMappings;
    }


}

