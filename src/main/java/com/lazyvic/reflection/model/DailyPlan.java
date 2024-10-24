package com.lazyvic.reflection.model;


import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "daily_plan")
public class DailyPlan extends BasePlan {

    @Column(name = "date")
    private String date; // 使用合適的日期格式

    @ManyToMany(mappedBy = "dailyPlans", fetch = FetchType.EAGER) // 反向關聯到 User 實體中的 plans
    private Set<User> users = new HashSet<>();
    // Getters and Setters

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "DailyPlan{" +
                super.toString() +
                "date='" + date + '\'' +
                ", users=" + users +
                '}';
    }
}