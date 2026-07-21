package org.erenaltun.countriesbe.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//user tablomuzu olusturduk sımdı bızım ıstedıgımız
// kullanıcı kayır olurken alacagımız bılgıler ıcın tablo olusturmak var
public class RegisterRequest {

    @NotBlank(message="Username Boş olamaz.")
    @Size(min=3 ,max=50,message="Kullanıcı adı 3 ile 50 karakter arasında olmak zorundadır.")
    private String username;


    @NotBlank(message = "şifre boş olamaz")
    @Size(min=6,message = "şifre en az 6 karakterden oluşmalıdır")
    private String password;

     @NotBlank(message = "mail adresi boş olamaz")
     @Email(message="geçerli bir mail adresi giriniz")
    private String mail;


}
