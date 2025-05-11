package com.lazyvic.reflection.repository;

import com.lazyvic.reflection.model.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {
    Optional<DailyPlan> findByTitle(String title);

    @Query(
            """
SELECT dp FROM DailyPlan dp
JOIN dp.users u WHERE u.telegramId = :telegramId
"""
    )
    List<DailyPlan> findDailyPlanByTelegramId(@Param("telegramId") Long telegramId);


}
