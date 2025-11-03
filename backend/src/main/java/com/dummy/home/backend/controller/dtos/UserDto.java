package com.dummy.home.backend.controller.dtos;

import com.dummy.home.backend.repo.entities.UserEntity;

public record UserDto(Long id, String name, String email) {
    public static UserDto from(UserEntity e) {
        return new UserDto(e.getId(), e.getName(), e.getEmail());
    }
}
