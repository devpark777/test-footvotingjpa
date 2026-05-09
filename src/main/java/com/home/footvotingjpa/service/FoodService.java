package com.home.footvotingjpa.service;

import com.home.footvotingjpa.entity.Food;
import com.home.footvotingjpa.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public void addFood(String name, MultipartFile image) {
        String imageName = null;

        String uploadDir2 = System.getProperty("user.dir") + "/uploads/";

        try {
            if (!image.isEmpty()) {
                String originalName = image.getOriginalFilename();
                imageName = UUID.randomUUID() + "_" + originalName;

//                File uploadDir = new File("uploads");
                File uploadDir = new File(uploadDir2);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                File saveFile = new File(uploadDir, imageName);
                image.transferTo(saveFile);
            }

            Food food = Food.builder()
                    .name(name)
                    .voteCount(0)
                    .imageName(imageName)
                    .build();

            foodRepository.save(food);

        } catch (Exception e) {
            throw new RuntimeException("Food image upload error", e);
        }
    }

    public List<Food> findAllFoods() {
        return foodRepository.findAllByOrderByVoteCountDescIdDesc();
    }

    public Food findWinner() {
        List<Food> foods = foodRepository.findAllByOrderByVoteCountDescIdDesc();

        if (foods.isEmpty()) {
            return null;
        }

        return foods.get(0);
    }

    public void vote(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Food not found"));

        food.setVoteCount(food.getVoteCount() + 1);

        foodRepository.save(food);
    }

    public void delete(Long id) {
        foodRepository.deleteById(id);
    }
}