package org.example.ch3schedulerdevelopprojectexplanation.schedule.repository;

import org.example.ch3schedulerdevelopprojectexplanation.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
