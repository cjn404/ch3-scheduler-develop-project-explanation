package org.example.ch3schedulerdevelopprojectexplanation.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerdevelopprojectexplanation.user.dto.request.LoginRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.example.ch3schedulerdevelopprojectexplanation.common.consts.Const.LOGIN_USER;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequestDto dto,
            HttpServletRequest request
    ) {
        Long userId = userService.handleLogin(dto);

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_USER, userId);

        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 성공");
    }
}
