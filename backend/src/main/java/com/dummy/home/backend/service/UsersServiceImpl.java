package com.dummy.home.backend.service;

import com.dummy.home.backend.controller.dtos.CreateUserRequest;
import com.dummy.home.backend.controller.dtos.UpdateUserRequest;
import com.dummy.home.backend.controller.dtos.UserDto;
import com.dummy.home.backend.repo.UsersRepository;
import com.dummy.home.backend.repo.entities.UserEntity;
import com.dummy.home.backend.service.interfaces.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UserService {

    private final UsersRepository userRepository;

    public UsersServiceImpl(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserDto> list() {
        return userRepository.findAll().stream().map(UserDto::from).toList();
    }

    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {
        return userRepository.findById(id)
                .map(UserDto::from)
                .orElse(null);
    }

    @Transactional
    public UserDto create(CreateUserRequest req) {
        if (req == null || req.name() == null || req.name().isBlank()
                || req.email() == null || req.email().isBlank()) {
            throw new IllegalArgumentException("name and email are required");
        }
        userRepository.findByEmail(req.email()).ifPresent(u -> {
            throw new IllegalArgumentException("email already exists");
        });

        UserEntity e = new UserEntity();
        e.setName(req.name().trim());
        e.setEmail(req.email().trim());
        e = userRepository.save(e);
        return UserDto.from(e);
    }

    @Transactional
    public UserDto update(Long id, UpdateUserRequest req) {
        Optional<UserEntity> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }
        UserEntity entity = opt.get();
        entity.setName(req.getName());
        entity.setEmail(req.getEmail());
        UserEntity saved = userRepository.save(entity);

        return new UserDto(saved.getId(), saved.getName(), saved.getEmail());
    }

    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
