package org.example.ch3schedulerdevelopprojectexplanation.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ch3schedulerdevelopprojectexplanation.common.entity.BaseEntity;
import org.example.ch3schedulerdevelopprojectexplanation.schedule.entity.Schedule;
import org.example.ch3schedulerdevelopprojectexplanation.user.entity.User;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(
            Schedule schedule,
            User user,
            String content
    ) {
        this.schedule = schedule;
        this.user = user;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
