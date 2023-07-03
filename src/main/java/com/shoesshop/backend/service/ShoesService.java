package com.shoesshop.backend.service;

import com.shoesshop.backend.entity.BrandCategory;
import com.shoesshop.backend.entity.Shoes;
import com.shoesshop.backend.exception.NotFoundException;
import com.shoesshop.backend.repository.BrandCategoryRepository;
import com.shoesshop.backend.repository.ShoesRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class ShoesService {

    private final ShoesRepository shoesRepository;

    private final BrandCategoryRepository brandCategoryRepository;

    private static Map<String, Object> setUpPaging(Map<String, Object> responseObject, Page<Shoes> filteredListShoes) {
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

    public Shoes createNewShoes(Shoes shoes, int brandId) {
        BrandCategory brandCategory = brandCategoryRepository.findById(brandId).orElseThrow(() -> new NotFoundException("Brand not found with Id: " + brandId));
        shoes.setBrandCategory(brandCategory);
        return shoesRepository.save(shoes);
    }

    public Map<String, Object> getAllShoes(String name, int price, boolean newest, int brandId, int pageNumber, int pageSize) {
        Map<String, Object> responseObject = new LinkedHashMap<>();
        Page<Shoes> filteredListShoes = null;
        Pageable paging;
        if (price == 1) {
            paging = PageRequest.of(pageNumber, pageSize, Sort.by("price").ascending());
        } else if (price == -1) {
            paging = PageRequest.of(pageNumber, pageSize, Sort.by("price").descending());
        } else if (newest) {
            paging = PageRequest.of(pageNumber, pageSize, Sort.by("created_at").ascending());
        } else {
            paging = PageRequest.of(pageNumber, pageSize);
        }

        if (brandId != 0 && !name.equals("")) {
            filteredListShoes = shoesRepository.findByNameAndBrandId(name, brandId, paging);
        } else if (brandId != 0) {
            filteredListShoes = shoesRepository.findByBrandId(brandId, paging);
        } else {
            filteredListShoes = shoesRepository.findByName(name, paging);
        }

        return setUpPaging(responseObject, filteredListShoes);
    }

    public Shoes getShoes(int id) {
        Optional<Shoes> shoes = shoesRepository.findById(id);
        return shoes.orElse(null);
    }

    public Shoes updateUserById(int id, @Valid Shoes newShoes, int brandId) {
        Shoes foundedShoes = shoesRepository.findById(id).orElseThrow(() -> new NotFoundException("Shoes not found by id: " + id));
        BrandCategory brandCategory = brandCategoryRepository.findById(brandId).orElseThrow(() -> new NotFoundException("Brand not found with Id: " + brandId));
        foundedShoes.setName(newShoes.getName());
        foundedShoes.setPrice(newShoes.getPrice());
        foundedShoes.setCoverImage(newShoes.getCoverImage());
        foundedShoes.setColors(newShoes.getColors());
        foundedShoes.setSizes(newShoes.getSizes());
        foundedShoes.setStatus(newShoes.getStatus());
        foundedShoes.setDescription(newShoes.getDescription());
        foundedShoes.setPriceSales(newShoes.getPriceSales());
        foundedShoes.setBrandCategory(brandCategory);
        return shoesRepository.save(foundedShoes);
    }

    public boolean deleteShoesById(int id) {
        Optional<Shoes> shoes = shoesRepository.findById(id);
        if (shoes.isPresent()) {
            shoesRepository.delete(shoes.get());
            return true;
        }
        return false;
    }

    public Map<String, Object> getAllShoesByBrandId(int brandId, int pageNumber, int pageSize) {
        Map<String, Object> responseObject = new LinkedHashMap<>();
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Shoes> filteredListShoes = shoesRepository.findByBrandId(brandId, paging);
        return setUpPaging(responseObject, filteredListShoes);
    }
}

