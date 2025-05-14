package com.lazyvic.reflection.service;


import com.lazyvic.reflection.dto.DailyPlanDto;
import com.lazyvic.reflection.model.DailyPlan;
import com.lazyvic.reflection.model.User;
import com.lazyvic.reflection.repository.DailyPlanRepository;
import com.lazyvic.reflection.repository.UserRepository;
import com.lazyvic.reflection.scheduler.DailyPlanScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DailyPlanService {

    private static final Logger logger = LoggerFactory.getLogger(DailyPlanService.class);

    private UserRepository userRepository;
    private DailyPlanRepository dailyPlanRepository;
    private RedisService redisService;

    @Autowired
    public DailyPlanService(UserRepository userRepository, DailyPlanRepository dailyPlanRepository, RedisService redisService) {
        this.userRepository = userRepository;
        this.dailyPlanRepository = dailyPlanRepository;
        this.redisService = redisService;
    }


    @Transactional
    public boolean trySaveUserPlan(DailyPlanDto dailyPlanDto) {
        if (!redisService.allowAdd("addPlan", dailyPlanDto.getTelegramId(), 3)) {
            logger.warn("LIMIT!：user {} exceeded the allowed frequency to add plan ", dailyPlanDto.getTelegramId());
            return false;
        }
        saveUserPlan(dailyPlanDto);
        return true;
    }


    private void saveUserPlan(DailyPlanDto dailyPlanDto) {
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

    public User loadUser(Long userTelegramId) {
        Optional<User> optionalUser = userRepository.findByTelegramId(userTelegramId);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    }

    public List<DailyPlan> getUserDailyPlansByJPQL(Long userTelegramId) {
        return dailyPlanRepository.findDailyPlanByTelegramId(userTelegramId);
    }


    @Transactional
    public Set<DailyPlan> getUserDailyPlans(Long userTelegramId) {
        Optional<User> optionalUser = userRepository.findByTelegramId(userTelegramId);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();
        return user.getDailyPlans();
    }
}