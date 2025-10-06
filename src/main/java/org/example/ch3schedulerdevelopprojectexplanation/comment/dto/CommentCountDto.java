package org.example.ch3schedulerdevelopprojectexplanation.comment.dto;

import lombok.Getter;

@Getter
public class CommentCountDto {

    private final Long scheduleId;
    private final Long count;

    public CommentCountDto(Long scheduleId, Long count) {
        this.scheduleId = scheduleId;
        this.count = count;
    }
}
