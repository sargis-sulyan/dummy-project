package com.dummy.home.backend.service.interfaces;

import com.dummy.home.backend.controller.dtos.CreateUserRequest;
import com.dummy.home.backend.controller.dtos.UpdateUserRequest;
import com.dummy.home.backend.controller.dtos.UserDto;

import java.util.List;

public interface UserService {
    public List<UserDto> list();

    public UserDto getUser(Long id);

    public UserDto create(CreateUserRequest req) ;

    public UserDto update(Long id, UpdateUserRequest req);

    public void deleteUser(Long id);
}
