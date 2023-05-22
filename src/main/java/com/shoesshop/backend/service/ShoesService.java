package com.shoesshop.backend.service;

import com.shoesshop.backend.respository.ShoesRepository;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

import com.shoesshop.backend.entity.Shoes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ShoesService {

    @Autowired
    private ShoesRepository shoesRepository;

    public Shoes createNewShoes(Shoes shoes) {
        return shoesRepository.save(shoes);
    }

    public Map<String, Object> getAllShoes(String name, int price, boolean newest, int pageNumber, int pageSize) {
        Map<String, Object> responseObject = new LinkedHashMap<>();
        Page<Shoes> filteredListShoes;
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        if (price == 1) {
            if (name != null && name != "") {
                log.info("Shoes name: " + name + ", Page number: " + pageNumber + ", Page size: " + pageSize);
                filteredListShoes = shoesRepository.findByNameContainingOrderByPriceAsc(name, paging);
            } else {
                filteredListShoes = shoesRepository.findAllByOrderByPriceAsc(paging);
            }
        } else if (price == -1) {
            if (name != null && name != "") {
                filteredListShoes = shoesRepository.findByNameContainingOrderByPriceDesc(name, paging);
            } else {
                filteredListShoes = shoesRepository.findAllByOrderByPriceDesc(paging);
            }
        } else if (newest) {
            if (name != null && name != "") {
                filteredListShoes = shoesRepository.findByNameContainingOrderByCreatedAtDesc(name, paging);
            } else {
                filteredListShoes = shoesRepository.findAllByOrderByCreatedAtDesc(paging);
            }
        } else {
            if (name != null) {
                filteredListShoes = shoesRepository.findByNameContaining(name, paging);
            } else {
                filteredListShoes = shoesRepository.findAll(paging);
            }
        }
        List<Map<String, Object>> arrShoes = new ArrayList<>();
        for (Shoes shoes : filteredListShoes.getContent()) {
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

        responseObject.put("hasNextPage", filteredListShoes.hasNext());
        responseObject.put("hasPreviousPage", filteredListShoes.hasPrevious());
        responseObject.put("totalPages", filteredListShoes.getTotalPages());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("length", arrShoes.size());
        data.put("data", arrShoes);
        responseObject.put("data", data);
        return responseObject;
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

