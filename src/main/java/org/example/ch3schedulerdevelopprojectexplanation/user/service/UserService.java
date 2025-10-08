package org.example.ch3schedulerdevelopprojectexplanation.user.service;

import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerdevelopprojectexplanation.common.config.PasswordEncoder;
import org.example.ch3schedulerdevelopprojectexplanation.common.exception.InvalidCredentialException;
import org.example.ch3schedulerdevelopprojectexplanation.user.dto.request.LoginRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.user.dto.request.UserSaveRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.user.dto.request.UserUpdateRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.user.dto.response.UserResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.user.entity.User;
import org.example.ch3schedulerdevelopprojectexplanation.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto save(UserSaveRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("해당 이메일은 이미 사용중입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User(
                dto.getUserName(),
                dto.getEmail(),
                encodedPassword
        );
        userRepository.save(user);
        return new UserResponseDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> dtos = new ArrayList<>();
        for (User user : users) {
            UserResponseDto dto = new UserResponseDto(
                    user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
            dtos.add(dto);
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public UserResponseDto findOne(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return new UserResponseDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public UserResponseDto update(Long userId, UserUpdateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.update(
                dto.getUserName(),
                dto.getEmail(),
                encodedPassword);
        return new UserResponseDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long handleLogin(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialException("해당 이메일이 존재하지 않습니다.")
                );
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialException("비밀번호가 일치하지 않습니다.");
        }
        return user.getId();
    }
}
