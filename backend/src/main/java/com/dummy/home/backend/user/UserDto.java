package com.dummy.home.backend.user;

public record UserDto(Long id, String name, String email) {
    public static UserDto from(UserEntity e) {
        return new UserDto(e.getId(), e.getName(), e.getEmail());
    }
}
