package com.lazyvic.reflection.service;


import com.lazyvic.reflection.dto.DailyPlanDto;
import com.lazyvic.reflection.model.DailyPlan;
import com.lazyvic.reflection.model.User;
import com.lazyvic.reflection.repository.DailyPlanRepository;
import com.lazyvic.reflection.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DailyPlanService {

    private UserRepository userRepository;
    private DailyPlanRepository dailyPlanRepository;

    @Autowired
    public DailyPlanService(UserRepository userRepository, DailyPlanRepository dailyPlanRepository) {
        this.userRepository = userRepository;
        this.dailyPlanRepository = dailyPlanRepository;
    }

    @Transactional
    public void saveUserPlan(DailyPlanDto dailyPlanDto) {
        // 查找用户
        Optional<User> optionalUser = userRepository.findByTelegramId(dailyPlanDto.getTelegramId());
        User user;

        if (optionalUser.isEmpty()) {
            user = new User();
            user.setUsername(dailyPlanDto.getName());
            user.setTelegramId(dailyPlanDto.getTelegramId());
        } else {
            user = optionalUser.get(); // 从 Optional 中获取 User 对象
        }

        Optional<DailyPlan> loadedDailyPlan = dailyPlanRepository.findByTitle(dailyPlanDto.getTitle());
        if(loadedDailyPlan.isPresent()) {
            System.out.println("duplicate title");
            return;
        }

        DailyPlan dailyPlan = new DailyPlan();
        dailyPlan.setTitle(dailyPlanDto.getTitle());
        dailyPlan.setDate(dailyPlanDto.getDate());
        dailyPlan.setDescription(dailyPlanDto.getDescription());


        // Step 3: 将用户与计划关联
        user.getDailyPlans().add(dailyPlan);
        dailyPlan.getUsers().add(user);

        // 保存数据到数据库
        userRepository.save(user);

    }
}