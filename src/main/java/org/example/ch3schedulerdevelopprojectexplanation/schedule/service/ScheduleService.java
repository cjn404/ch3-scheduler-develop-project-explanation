package org.example.ch3schedulerdevelopprojectexplanation.schedule.service;

import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.request.ScheduleSaveRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.response.ScheduleResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.response.ScheduleSaveResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.entity.Schedule;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.repository.ScheduleRepository;
import org.example.ch3schedulerdevelopprojectexplanation.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleSaveResponseDto save(Long userId, ScheduleSaveRequestDto dto) {
        User user = User.from(userId);
        Schedule schedule = new Schedule(
                user,
                dto.getTitle(),
                dto.getContent()
        );
        scheduleRepository.save(schedule);
        return new ScheduleSaveResponseDto(
                schedule.getId(),
                user.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> findAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleResponseDto> dtos = new ArrayList<>();

        for (Schedule schedule : schedules) {
            ScheduleResponseDto dto = new ScheduleResponseDto(
                    schedule.getId(),
                    schedule.getUser().getId(),
                    schedule.getTitle(),
                    schedule.getContent(),
                    schedule.getCreatedAt(),
                    schedule.getUpdatedAt()
            );
            dtos.add(dto);
        }
        return dtos;
    }
}
