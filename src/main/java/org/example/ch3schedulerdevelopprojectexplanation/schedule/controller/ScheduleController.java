package org.example.ch3schedulerdevelopprojectexplanation.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerdevelopprojectexplanation.common.consts.Const;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.request.ScheduleSaveRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.request.ScheduleUpdateRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.response.SchedulePageResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.response.ScheduleResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.response.ScheduleSaveResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.response.ScheduleUpdateResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.service.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleSaveResponseDto> save(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @RequestBody ScheduleSaveRequestDto dto
    ) {
        return ResponseEntity.ok(scheduleService.save(userId, dto));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findOne(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(scheduleService.findOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleUpdateResponseDto> update(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long id,
            @RequestBody ScheduleUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(scheduleService.update(id, userId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long id
    ) {
      scheduleService.deleteById(id, userId);
      return ResponseEntity.ok().build();
    }

    // 일정 페이지 API
    @GetMapping("/page")
    public ResponseEntity<Page<SchedulePageResponseDto>> findAllPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SchedulePageResponseDto> result = scheduleService.findAllPage(page, size);
        return ResponseEntity.ok(result);
    }
}
