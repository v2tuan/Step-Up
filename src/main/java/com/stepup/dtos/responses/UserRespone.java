package com.stepup.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRespone {
    private String fullName;
    private String givenName;
    private String familyName;
    private String profileImage;
    private String email;
    private String phone;
    private int points;
}
