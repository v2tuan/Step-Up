package com.stepup.dtos.requests;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    @Valid
    private AddressDTO address;
    private boolean setDefault;

    // Getter & Setter
}