package com.shoesshop.backend.service;

import com.shoesshop.backend.dto.ShoesDetailResponse;
import com.shoesshop.backend.dto.ShoesRequest;
import com.shoesshop.backend.entity.BrandCategory;
import com.shoesshop.backend.entity.Favorite;
import com.shoesshop.backend.entity.Shoes;
import com.shoesshop.backend.entity.User;
import com.shoesshop.backend.exception.NotFoundException;
import com.shoesshop.backend.repository.BrandCategoryRepository;
import com.shoesshop.backend.repository.FavoriteRepository;
import com.shoesshop.backend.repository.ShoesRepository;
import com.shoesshop.backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ShoesService {

    private final ShoesRepository shoesRepository;

    private final BrandCategoryRepository brandCategoryRepository;

    private final FavoriteRepository favoriteRepository;

    private final UserRepository userRepository;

    private Map<String, Object> setUpPaging(Map<String, Object> responseObject, Page<Shoes> filteredListShoes) {
        int userId = 0;
        Map<String, Object> responseResult = new LinkedHashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new NotFoundException("User not found by email: " + authentication.getName()));
            userId = user.getId();
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
            if (userId == 0) {
                data.put("favorite", false);
            } else {
                Optional<Favorite> favorite = favoriteRepository.findByShoesIdAndUserId(shoes.getShoesId(), userId);
                data.put("favorite", favorite.isPresent());
            }


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

    public List<Shoes> addListShoes(List<ShoesRequest> listShoesRequest) {
        List<Shoes> listShoes = new ArrayList<>();
        for (ShoesRequest shoesRequest : listShoesRequest) {
            Shoes shoes = Shoes.builder()
                    .name(shoesRequest.getName())
                    .coverImage(shoesRequest.getCoverImage())
                    .price(shoesRequest.getPrice())
                    .colors(shoesRequest.getColors())
                    .priceSales(shoesRequest.getPriceSales())
                    .status(shoesRequest.getStatus())
                    .description(shoesRequest.getDescription())
                    .sizes(shoesRequest.getSizes())
                    .build();
            listShoes.add(shoes);
        }

        return shoesRepository.saveAll(listShoes);
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

    public ShoesDetailResponse getShoes(int id) {
        Shoes shoes = shoesRepository.findById(id).orElseThrow(() -> new NotFoundException("Shoes not found by id: " + id));
        int userId = 0;
        Map<String, Object> responseResult = new LinkedHashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new NotFoundException("User not found by email: " + authentication.getName()));
            userId = user.getId();
        }
        if (userId == 0) {
            return new ShoesDetailResponse(shoes, false);
        } else {
            Optional<Favorite> favorite = favoriteRepository.findByShoesIdAndUserId(shoes.getShoesId(), userId);
            return new ShoesDetailResponse(shoes, favorite.isPresent());
        }
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

