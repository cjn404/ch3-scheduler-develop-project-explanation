package org.example.ch3schedulerdevelopprojectexplanation.schedule.service;

import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerdevelopprojectexplanation.comment.dto.CommentCountDto;
import org.example.ch3schedulerdevelopprojectexplanation.comment.repository.CommentRepository;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.request.ScheduleSaveRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.request.ScheduleUpdateRequestDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.response.SchedulePageResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.response.ScheduleResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.response.ScheduleSaveResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.dto.response.ScheduleUpdateResponseDto;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.entity.Schedule;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.repository.ScheduleRepository;
import org.example.ch3schedulerdevelopprojectexplanation.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

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

    @Transactional(readOnly = true)
    public ScheduleResponseDto findOne(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다."));
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }

    @Transactional
    public ScheduleUpdateResponseDto update(
            Long scheduleId,
            Long userId,
            ScheduleUpdateRequestDto dto
    ) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다."));
        if (!userId.equals(schedule.getUser().getId())) {
            throw new IllegalArgumentException("본인이 작성한 스케줄만 수정할 수 있습니다.");
        }
        return new ScheduleUpdateResponseDto(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteById(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new IllegalArgumentException("해당 스케줄이 존재하지 않습니다."));
        if (!userId.equals(schedule.getUser().getId())) {
            throw new IllegalArgumentException("본인이 작성한 스케줄만 삭제할 수 있습니다.");
        }
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public Page<SchedulePageResponseDto> findAllPage(int page, int size) {
        // 클라이언트에서 1부터 전달된 페이지 번호를 0 기반으로 조정
        int adjustedPage = (page > 0) ? page -1 : 0;
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("updatedAt").descending());
        // 1. Schedule Page 조회
        Page<Schedule> schedulePage = scheduleRepository.findAll(pageable);
        // 2. 일정 ID 리스트 추출
        List<Long> scheduleIds = new ArrayList<>();
        for (Schedule schedule : schedulePage.getContent()) {
            scheduleIds.add(schedule.getId());
        }
        // 3. 별도 쿼리로 댓글 수 조회
        List<CommentCountDto> countResults = commentRepository.countByScheduleIds(scheduleIds);
        // 4. 댓글 수 Map으로 변환
        Map<Long, Long> commentCountMap = new HashMap<>();
        for (CommentCountDto dto : countResults) {
            commentCountMap.put(dto.getScheduleId(), dto.getCount());
        }
        // 5. Schedule -> SchedulePageResponseDto 변환
        List<SchedulePageResponseDto> dtoList = new ArrayList<>();
        for (Schedule schedule : schedulePage.getContent()) {
            Long scheduleId = schedule.getId();
            Long commentCount = commentCountMap.getOrDefault(scheduleId, 0L);

            SchedulePageResponseDto dto = new SchedulePageResponseDto(
                    scheduleId,
                    schedule.getTitle(),
                    schedule.getContent(),
                    commentCount.intValue(),
                    schedule.getCreatedAt(),
                    schedule.getUpdatedAt(),
                    schedule.getUser().getUserName()
            );
            dtoList.add(dto);
        }
        // 6. 새로운 Page 객제로 반환
        return new PageImpl<>(dtoList, pageable, schedulePage.getTotalElements());
    }
}
