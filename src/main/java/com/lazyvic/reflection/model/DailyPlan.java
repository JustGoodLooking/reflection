package com.lazyvic.reflection.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "daily_plan")
public class DailyPlan extends BasePlan {

    @Column(name = "date")
    private String date; // 使用合適的日期格式

    @ManyToMany(mappedBy = "dailyPlans") // 反向關聯到 User 實體中的 plans
    private Set<User> users = new HashSet<>();
    // Getters and Setters
}