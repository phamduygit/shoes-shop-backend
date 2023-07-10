package com.shoesshop.backend.dto;

import com.shoesshop.backend.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private int id;
    private int userId;
    private String addressDetail;
    private String addressName;
    private boolean selected;

    public void setAddressResponse(Address address) {
        this.id = address.getId();
        this.userId = address.getUser().getId();
        this.addressName = address.getAddressName();
        this.addressDetail = address.getAddressDetail();
        this.selected = address.isSelected();
    }

    public  AddressResponse(Address address) {
        this.id = address.getId();
        this.userId = address.getUser().getId();
        this.addressName = address.getAddressName();
        this.addressDetail = address.getAddressDetail();
        this.selected = address.isSelected();
    }
}
