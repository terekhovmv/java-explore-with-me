package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService service;
}
