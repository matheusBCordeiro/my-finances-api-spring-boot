package com.matheuscordeiro.myfinancesapi.api.dto;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntriesDTO {
    private Long id;
    private String description;
    private Integer month;
    private Integer year;
    private BigDecimal value;
    private Long user;
    private String type;
    private String status;
}
