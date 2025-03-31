package com.stepup.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDTO {
    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    // Password may not be needed for social login but required for traditional login
    @NotBlank(message = "Password cannot be blank")
    private String password;

//    @Min(value = 1, message = "You must enter role's Id")
//    @JsonProperty("role_id")
//    private Long roleId;


    // Google Account Id, not mandatory, can be blank
    @JsonProperty("google_account_id")
    private String googleAccountId;

    //For Google, Facebook login
    // Full name, not mandatory, can be blank
    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("given_name ")
    private String givenName;

    @JsonProperty("family_name  ")
    private String familyName;

    // Profile image URL, not mandatory, can be blank
    @JsonProperty("profile_image")
    private String profileImage;

    // Kiểm tra googleAccountId có hợp lệ không
    public boolean isGoogleAccountIdValid() {
        return googleAccountId != null && !googleAccountId.isEmpty();
    }
}
