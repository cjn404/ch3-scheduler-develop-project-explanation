package org.example.ch3schedulerdevelopprojectexplanation.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerdevelopprojectexplanation.comment.dto.request.CommentSaveRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.comment.dto.response.CommentResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.comment.entity.Comment;
import org.example.ch3schedulerdevelopprojectexplanation.comment.repository.CommentRepository;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.entity.Schedule;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.repository.ScheduleRepository;
import org.example.ch3schedulerdevelopprojectexplanation.user.entity.User;
import org.example.ch3schedulerdevelopprojectexplanation.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CommentResponseDto save(
            Long userId,
            Long scheduleId,
            CommentSaveRequestDto dto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        Comment comment = new Comment(
                schedule,
                user,
                dto.getContent()
        );
        commentRepository.save(comment);
        return new CommentResponseDto(
                comment.getId(),
                user.getId(),
                schedule.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findBySchedule(Long scheduleId) {
        List<Comment> comments = commentRepository.findByScheduleId(scheduleId);
        List<CommentResponseDto> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseDto dto = new CommentResponseDto(
                    comment.getId(),
                    comment.getUser().getId(),
                    comment.getSchedule().getId(),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    comment.getUpdatedAt()
            );
            dtos.add(dto);
        }
        return dtos;
    }
}
