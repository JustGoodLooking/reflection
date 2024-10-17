//package com.lazyvic.reflection.controller;
//
//import com.lazyvic.reflection.model.DailyPlan;
//import com.lazyvic.reflection.model.YearlyPlan;
//import com.lazyvic.reflection.repository.DailyPlanRepository;
//import com.lazyvic.reflection.repository.YearlyPlanRepository;
//import com.lazyvic.reflection.service.BasePlanService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/plans")
//public class PlanController {
//    private final BasePlanService<DailyPlan> dailyPlanService;
//    private final BasePlanService<YearlyPlan> yearlyPlanService;
//
//
//    @Autowired
//    public PlanController(BasePlanService<DailyPlan> dailyPlanService, BasePlanService<YearlyPlan> yearlyPlanService) {
//        this.dailyPlanService = dailyPlanService;
//        this.yearlyPlanService = yearlyPlanService;
//    }
//}
