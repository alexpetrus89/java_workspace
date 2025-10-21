package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.dto.AdminDto;
import com.alex.universitymanagementsystem.entity.Admin;

public class AdminMapper {

    private AdminMapper() {} // private constructor to prevent instantiation

    public static Admin toEntity(AdminDto dto) {
        if(dto == null) return null;
        return new Admin(
            dto.getUsername(),
            dto.getFirstName(),
            dto.getLastName(),
            dto.getFiscalCode(),
            dto.getAdminCode()

        );
    }

    public static AdminDto toDto(Admin admin) {
        if(admin == null) return null;
        return new AdminDto(
            admin.getUsername(),
            admin.getFirstName(),
            admin.getLastName(),
            admin.getFiscalCode().toString(),
            admin.getAdminCode().toString()
        );
    }

}
