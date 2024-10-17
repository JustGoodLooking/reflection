package com.lazyvic.reflection.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "yearly_plan")
public class YearlyPlan extends BasePlan {

    @Column(name = "year")
    private int year;

    @ManyToMany(mappedBy = "yearlyPlans") // 反向關聯到 User 實體中的 plans
    private Set<User> users = new HashSet<>();
    // Getters and Setters
}
