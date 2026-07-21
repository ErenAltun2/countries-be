package org.erenaltun.countriesbe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "username girmek zorundasınız")
    @Size(min = 3 , max = 50,message = "3 ila 50 karakter arasında değer girebilirsiniz.")
    private String username;

    @NotBlank(message = "password girmek zorundasınız")
    private String password;
}
