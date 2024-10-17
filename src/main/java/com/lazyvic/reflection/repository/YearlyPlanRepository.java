package com.lazyvic.reflection.repository;

import com.lazyvic.reflection.model.DailyPlan;
import com.lazyvic.reflection.model.YearlyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YearlyPlanRepository extends JpaRepository<YearlyPlan, Long> {
}
