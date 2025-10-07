package org.example.ch3schedulerdevelopprojectexplanation.comment.repository;

import org.example.ch3schedulerdevelopprojectexplanation.comment.dto.CommentCountDto;
import org.example.ch3schedulerdevelopprojectexplanation.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
            SELECT new org.example.ch3schedulerdevelopprojectexplanation.comment.dto.CommentCountDto(c.schedule.id, count(c))
            FROM Comment c
            WHERE c.schedule.id IN :scheduleIds
            GROUP BY c.schedule.id
            """)
    List<CommentCountDto> countByScheduleIds(List<Long> scheduleIds);

    List<Comment> findByScheduleId(Long scheduleId);
}
