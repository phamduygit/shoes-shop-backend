package com.shoesshop.backend.service;

import com.shoesshop.backend.dto.CartItemRequest;
import com.shoesshop.backend.dto.CartItemResponse;
import com.shoesshop.backend.entity.Cart;
import com.shoesshop.backend.entity.CartItem;
import com.shoesshop.backend.entity.Shoes;
import com.shoesshop.backend.entity.User;
import com.shoesshop.backend.exception.NotFoundException;
import com.shoesshop.backend.repository.CartItemRepository;
import com.shoesshop.backend.repository.CartRepository;
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

import java.util.*;

@Service
@Log4j2
@AllArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ShoesRepository shoesRepository;

    public Map<String, Object> getCart(int pageNumber, int pageSize) {
        Map<String, Object> responseResult = new LinkedHashMap<>();
        // get card id for user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found by email: " + authentication.getName()));
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            // If cart isn't existed create new cart for user
            Cart newCart = Cart.builder().user(user).items(new ArrayList<>()).totalPrice(0).build();
            return cartRepository.save(newCart);
        });

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CartItem> items = cartItemRepository.findAllByCartId(cart.getId(), pageable);

        // Create response object
        List<CartItemResponse> listItemResponse = new ArrayList<>();
        for (CartItem item : items.getContent()) {
            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setValue(item);
            listItemResponse.add(itemResponse);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("hasNextPage", items.hasNext());
        data.put("hasPreviousPage", items.hasPrevious());
        data.put("totalPages", items.getTotalPages());
        data.put("length", listItemResponse.size());
        data.put("list", listItemResponse);

        responseResult.put("cardId", cart.getId());
        responseResult.put("totalPrice", cart.getTotalPrice());
        responseResult.put("data", data);

        return responseResult;
    }

    public CartItemResponse getCartDetail(int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("Not found cart item by id: " + cartItemId));

        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setValue(cartItem);
        return cartItemResponse;
    }

    public CartItemResponse addCartItem(CartItemRequest cartItemRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found by email: " + authentication.getName()));
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            // If cart isn't existed create new cart for user
            Cart newCart = Cart.builder().user(user).items(new ArrayList<>()).totalPrice(0).build();
            return cartRepository.save(newCart);
        });
        Shoes shoes = shoesRepository.findById(cartItemRequest.getShoesId())
                .orElseThrow(() -> new NotFoundException("Shoes not found by id: " + cartItemRequest.getShoesId()));

        // If cart item is existed, increase quantity
        CartItem cartItem = cartItemRepository.findByShoesIdAndCartId(shoes.getShoesId(), cart.getId())
                .orElse(CartItem.builder().shoes(shoes).cart(cart).quantity(0).build());
        cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // Create response object
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setValue(savedCartItem);
        return cartItemResponse;
    }

    public CartItemResponse updateCartItem(CartItemRequest cartItemRequest, int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("Not found cart item by id: " + cartItemId));
        cartItem.setQuantity(cartItemRequest.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // Create response object
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setValue(savedCartItem);
        return cartItemResponse;
    }

    public void deleteCartItem(int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("Not found cart item by id: " + cartItemId));
        CartItem foundedCartItem = cartItemRepository.save(cartItem);
        cartItemRepository.deleteById(foundedCartItem.getId());
    }
}
