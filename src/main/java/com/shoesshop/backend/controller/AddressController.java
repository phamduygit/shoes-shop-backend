package com.shoesshop.backend.controller;

import com.shoesshop.backend.dto.AddressRequest;
import com.shoesshop.backend.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@PreAuthorize(value = "hasRole('USER')")
@RequestMapping("/api/v1/address")
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('user:read')")
    public ResponseEntity<Map<String, Object>> getAddress() {
        return ResponseEntity.ok(addressService.getAddress());
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('user:create')")
    public ResponseEntity<Map<String, Object>> addAddress(@RequestBody AddressRequest addressRequest) {
        return new ResponseEntity<>(addressService.addAddress(addressRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('user:update')")
    public ResponseEntity<Map<String, Object>> updateAddress(@RequestBody AddressRequest addressRequest, @PathVariable int id) {
        return ResponseEntity.ok(addressService.updateAddress(id, addressRequest));
    }

    public ResponseEntity<Void> deleteAddress(@RequestParam int id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
