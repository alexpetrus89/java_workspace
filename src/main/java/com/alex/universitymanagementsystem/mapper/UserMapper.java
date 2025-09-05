package com.alex.universitymanagementsystem.mapper;


import org.springframework.security.core.userdetails.UserDetails;

import com.alex.universitymanagementsystem.domain.Address;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
import com.alex.universitymanagementsystem.dto.UserDto;

public class UserMapper {

    // private constructor
    private UserMapper() {}

    public static User toEntity(UserDto dto) {
        if(dto == null) return null;
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDob(dto.getDob());
        user.setFiscalCode(new FiscalCode(dto.getFiscalCode()));
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        Address address = new Address();
        address.setStreet(dto.getAddress().getStreet());
        address.setCity(dto.getAddress().getCity());
        address.setState(dto.getAddress().getState());
        address.setZipCode(dto.getAddress().getZipCode());
        user.setAddress(address);
        return user;
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

