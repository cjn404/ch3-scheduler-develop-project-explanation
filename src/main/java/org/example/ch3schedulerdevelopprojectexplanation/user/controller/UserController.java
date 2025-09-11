package org.example.ch3schedulerdevelopprojectexplanation.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerdevelopprojectexplanation.common.consts.Const;
import org.example.ch3schedulerdevelopprojectexplanation.user.dto.request.UserSaveRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.user.dto.request.UserUpdateRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.user.dto.response.UserResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserSaveRequestDto dto) {
        return ResponseEntity.ok(userService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findOne(id));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateMe(
            @RequestBody UserUpdateRequestDto dto,
            @SessionAttribute(name = Const.LOGIN_USER) Long userId
    ) {
        return ResponseEntity.ok(userService.update(userId, dto));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute(Const.LOGIN_USER);
        userService.deleteById(userId);
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
