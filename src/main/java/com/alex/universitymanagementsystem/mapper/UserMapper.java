package com.alex.universitymanagementsystem.mapper;


import org.springframework.security.core.userdetails.UserDetails;

import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.dto.Builder;
import com.alex.universitymanagementsystem.dto.UserDto;

public class UserMapper {

    // private constructor
    private UserMapper() {}

    public static User toEntity(UserDto dto) {
        if(dto == null) return null;
        Builder builder = new Builder();
        builder.setUsername(dto.getUsername());
        builder.setFirstName(dto.getFirstName());
        builder.setLastName(dto.getLastName());
        builder.setDob(dto.getDob());
        builder.setFiscalCode(dto.getFiscalCode());
        builder.setPhone(dto.getPhone());
        builder.setRole(dto.getRole());
        builder.setStreet(dto.getAddress().getStreet());
        builder.setCity(dto.getAddress().getCity());
        builder.setState(dto.getAddress().getState());
        builder.setZip(dto.getAddress().getZipCode());
        return new User(builder);
    }

    public static UserDto toDto(User user) {
        if(user == null) return null;
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDob(user.getDob());
        dto.setFiscalCode(user.getFiscalCode().toString());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setAddress(user.getAddress());
        return dto;
    }

    public static UserDetails toUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.getAuthorities()
        );
    }
}

