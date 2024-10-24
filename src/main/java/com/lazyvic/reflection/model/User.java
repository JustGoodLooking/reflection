package com.lazyvic.reflection.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "telegram_id", unique = true)
    private Long telegramId;


    @Column(name = "username", nullable = false)
    private String username;


    @ManyToMany(cascade = CascadeType.ALL) // 自動管理多對多關聯
    @JoinTable(
            name = "user_yearly_plan",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "yearly_plan_id")
    )
    private Set<YearlyPlan> yearlyPlans = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) // 自動管理多對多關聯
    @JoinTable(
            name = "user_daily_plan",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "daily_plan_id")
    )
    private Set<DailyPlan> dailyPlans = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<YearlyPlan> getYearlyPlans() {
        return yearlyPlans;
    }

    public void setYearlyPlans(Set<YearlyPlan> yearlyPlans) {
        this.yearlyPlans = yearlyPlans;
    }

    public Set<DailyPlan> getDailyPlans() {
        return dailyPlans;
    }

    public void setDailyPlans(Set<DailyPlan> dailyPlans) {
        this.dailyPlans = dailyPlans;
    }
}
