package com.matheuscordeiro.myfinancesapi.api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusDTO {
    public String status;
}
