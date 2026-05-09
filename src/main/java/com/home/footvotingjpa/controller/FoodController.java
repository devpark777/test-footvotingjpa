package com.home.footvotingjpa.controller;

import com.home.footvotingjpa.entity.Food;
import com.home.footvotingjpa.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/")
    public String foodPage(Model model) {
        List<Food> foods = foodService.findAllFoods();
        Food winner = foodService.findWinner();

        model.addAttribute("foods", foods);
        model.addAttribute("winner", winner);

        return "food";
    }

    @PostMapping("/food/add")
    public String addFood(
            @RequestParam String name,
            @RequestParam MultipartFile image
    ) {
        foodService.addFood(name, image);
        return "redirect:/";
    }

    @PostMapping("/food/vote")
    public String voteFood(@RequestParam Long id) {
        foodService.vote(id);
        return "redirect:/";
    }

    @PostMapping("/food/delete")
    public String deleteFood(@RequestParam Long id) {
        foodService.delete(id);
        return "redirect:/";
    }
}