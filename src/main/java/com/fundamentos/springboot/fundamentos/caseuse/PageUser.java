package com.fundamentos.springboot.fundamentos.caseuse;

import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageUser {
    private UserService userService;

    public PageUser(UserService userService) {
        this.userService = userService;
    }

    public List<User> getAllPagination(int page, int size) {
        return userService.getAllPagination(page, size);
    }
}
