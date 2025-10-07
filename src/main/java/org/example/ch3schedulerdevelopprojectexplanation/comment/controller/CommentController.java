package org.example.ch3schedulerdevelopprojectexplanation.comment.controller;

import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerdevelopprojectexplanation.comment.dto.request.CommentSaveRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.comment.dto.response.CommentResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.comment.service.CommentService;
import org.example.ch3schedulerdevelopprojectexplanation.common.consts.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CommentResponseDto> save(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long scheduleId,
            @RequestBody CommentSaveRequestDto dto
    ) {
        return ResponseEntity.ok(commentService.save(userId, scheduleId, dto));
    }

    @GetMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<List<CommentResponseDto>> findBySchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(commentService.findBySchedule(scheduleId));
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentResponseDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findOne(id));
    }
}
