package com.shoesshop.backend.service;

import com.shoesshop.backend.dto.AddressRequest;
import com.shoesshop.backend.dto.AddressResponse;
import com.shoesshop.backend.entity.Address;
import com.shoesshop.backend.entity.User;
import com.shoesshop.backend.exception.NotFoundException;
import com.shoesshop.backend.repository.AddressRepository;
import com.shoesshop.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    public Map<String, Object> addAddress(AddressRequest addressRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        Map<String, Object> responseResult = new LinkedHashMap<>();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("User not found by email: " + authentication.getName()));
        Address address = Address.builder()
                .address(addressRequest.getAddress())
                .user(user)
                .selected(addressRequest.isSelected()).build();
        addressRepository.updateSelectedForUserId(user.getId(), false);
        Address savedAddress = addressRepository.save(address);
        responseResult.put("id", savedAddress.getId());
        responseResult.put("userId", savedAddress.getUser().getId());
        responseResult.put("address", savedAddress.getAddress());
        responseResult.put("selected", savedAddress.isSelected());
        return responseResult;
    }

    public Map<String, Object> getAddress() {
        Map<String, Object> responseResult = new LinkedHashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found by email: " + authentication.getName()));
        List<Address> addresses = addressRepository.findAllByUserId(user.getId());
        List<AddressResponse> listAddress = new ArrayList<>();
        for (Address address : addresses) {
            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setAddressResponse(address);
            listAddress.add(addressResponse);
        }
        responseResult.put("data", listAddress);
        return responseResult;
    }

    @Transactional
    public Map<String, Object> updateAddress(int addressId, AddressRequest addressRequest) {
        log.info("Start updateAddress");
        Map<String, Object> responseResult = new LinkedHashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("User not found by email: " + authentication.getName()));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Address not found by addressId: " + addressId));
        address.setAddress(addressRequest.getAddress());
        address.setSelected(addressRequest.isSelected());
        addressRepository.updateSelectedForUserId(user.getId(), false);
        Address savedAddress = addressRepository.save(address);
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setAddressResponse(savedAddress);
        responseResult.put("data", addressResponse);
        return responseResult;
    }

    public void deleteAddress(int addressId) {
        if (addressRepository.existsById(addressId)) {
            addressRepository.deleteById(addressId);
        } else {
            throw new NotFoundException("Address not found by addressId: " + addressId);
        }
    }

}
