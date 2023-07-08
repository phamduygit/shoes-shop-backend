package com.shoesshop.backend.service;

import com.shoesshop.backend.dto.ShoesResponse;
import com.shoesshop.backend.entity.Favorite;
import com.shoesshop.backend.entity.Shoes;
import com.shoesshop.backend.entity.User;
import com.shoesshop.backend.exception.DuplicateEntryException;
import com.shoesshop.backend.exception.NotFoundException;
import com.shoesshop.backend.repository.FavoriteRepository;
import com.shoesshop.backend.repository.ShoesRepository;
import com.shoesshop.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class FavoriteService {
    private final ShoesRepository shoesRepository;

    private final UserRepository userRepository;

    private final FavoriteRepository favoriteRepository;

    public Map<String, Object> getAllFavoriteByUserId(int pageNumber, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User user = userRepository.findByEmail(currentUserName).orElseThrow();

            Map<String, Object> result = new LinkedHashMap<>();
            log.info("User Id: " + user.getId());
            Pageable paging = PageRequest.of(pageNumber, pageSize);
            Page<Favorite> listFavorite = favoriteRepository.findAllByUserId(user.getId(), paging);

            List<Map<String, Object>> arrShoes = new ArrayList<>();
            for (Favorite favorite : listFavorite.getContent()) {
                Shoes shoes = favorite.getShoes();
                Map<String, Object> data = new LinkedHashMap<>();
                data.put("favoriteId", favorite.getId());
                data.put("shoesId", shoes.getShoesId());
                data.put("name", shoes.getName());
                data.put("price", shoes.getPrice());
                data.put("coverImage", shoes.getCoverImage());
                data.put("colors", shoes.getColors());
                data.put("status", shoes.getStatus());
                data.put("priceSales", shoes.getPriceSales());
                arrShoes.add(data);
            }
            result.put("hasNextPage", listFavorite.hasNext());
            result.put("hasPreviousPage", listFavorite.hasPrevious());
            result.put("totalPages", listFavorite.getTotalPages());
            result.put("data", arrShoes);

            return result;
        }
        return null;
    }

    public Map<String, Object> addFavorite(int shoesId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        Map<String, Object> result = new LinkedHashMap<>();
        Shoes shoes = shoesRepository.findById(shoesId).orElseThrow(() -> new NotFoundException("Shoes not found by id: " + shoesId));
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("User not found by email: " + authentication.getName()));

        try {
            Favorite favorite = Favorite.builder().shoes(shoes).user(user).build();
            Favorite addedFavorite = favoriteRepository.save(favorite);
            result.put("favoriteId", favorite.getId());
            Shoes favoriteShoes = addedFavorite.getShoes();
            ShoesResponse shoesResponse = ShoesResponse.builder()
                    .id(favoriteShoes.getShoesId())
                    .name(favoriteShoes.getName())
                    .price(favoriteShoes.getPrice())
                    .coverImage(favoriteShoes.getCoverImage())
                    .colors(favoriteShoes.getColors())
                    .status(favoriteShoes.getStatus().name())
                    .priceSales(favoriteShoes.getPriceSales())
                    .build();
            result.put("shoes", shoesResponse);
            return result;
        } catch (Exception exception) {
            throw new DuplicateEntryException("Shoes is existed in user favorite list");
        }
    }

    public void deleteFavorite(int favoriteId) {
        shoesRepository.findById(favoriteId).orElseThrow(() -> new NotFoundException("Favorite not found by id: " + favoriteId));
        favoriteRepository.deleteById(favoriteId);
    }
}
