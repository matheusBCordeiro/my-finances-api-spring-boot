package com.matheuscordeiro.myfinancesapi.api.dto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDTO {
    private String email;
    private String password;
}
