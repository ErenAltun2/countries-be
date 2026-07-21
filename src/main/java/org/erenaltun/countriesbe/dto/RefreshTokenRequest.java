package org.erenaltun.countriesbe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class RefreshTokenRequest {
    @NotBlank(message = "refresh token gereklı")
    private String refreshToken;

}
