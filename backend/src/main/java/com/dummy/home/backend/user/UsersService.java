package com.dummy.home.backend.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepository repo;

    public UsersService(UsersRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<UserDto> list() {
        return repo.findAll().stream().map(UserDto::from).toList();
    }

    @Transactional
    public UserDto create(CreateUserRequest req) {
        if (req == null || req.name() == null || req.name().isBlank()
                || req.email() == null || req.email().isBlank()) {
            throw new IllegalArgumentException("name and email are required");
        }
        repo.findByEmail(req.email()).ifPresent(u -> {
            throw new IllegalArgumentException("email already exists");
        });

        UserEntity e = new UserEntity();
        e.setName(req.name().trim());
        e.setEmail(req.email().trim());
        e = repo.save(e);
        return UserDto.from(e);
    }
}
