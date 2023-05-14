package com.shoesshop.backend.service;

import com.shoesshop.backend.respository.ShoesRepository;

import jakarta.validation.Valid;

import com.shoesshop.backend.entity.Shoes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoesService {

    @Autowired
    private ShoesRepository shoesRepository;

    public Shoes createNewShoes(Shoes shoes) {
        return shoesRepository.save(shoes);
    }

    public List<Map<String, Object>> getAllShoes() {
        List<Map<String, Object>> arrShoes = new ArrayList<>();
        for (Shoes shoes : shoesRepository.findAll()) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", shoes.getShoesId());
            data.put("name", shoes.getName());
            data.put("price", shoes.getPrice());
            data.put("coverImage", shoes.getCoverImage());
            data.put("colors", shoes.getColors());
            data.put("status", shoes.getStatus());
            data.put("priceSales", shoes.getPriceSales());
            arrShoes.add(data);
        }
        return arrShoes;
    }

    public Shoes getShoes(int id) {
        Optional<Shoes> shoes = shoesRepository.findById(id);
        if (shoes.isPresent()) {
            return shoes.get();
        } else {
            return null;
        }
    }

    public Shoes updateUserById(int id, @Valid Shoes newShoes) {
        Optional<Shoes> optionalShoes = shoesRepository.findById(id);
        if (optionalShoes.isPresent()) {
            Shoes shoes = optionalShoes.get();
            shoes.setName(newShoes.getName());
            shoes.setPrice(newShoes.getPrice());
            shoes.setCoverImage(newShoes.getCoverImage());
            shoes.setColors(newShoes.getColors());
            shoes.setSizes(newShoes.getSizes());
            shoes.setStatus(newShoes.getStatus());
            shoes.setDescription(newShoes.getDescription());
            shoes.setPriceSales(newShoes.getPriceSales());
            return shoesRepository.save(shoes);
        }
        return null;
    }

    public boolean deleteShoesById(int id) {
        Optional<Shoes> shoes = shoesRepository.findById(id);
        if (shoes.isPresent()) {
            shoesRepository.delete(shoes.get());
            return true;
        }
        return false;
    }
}

