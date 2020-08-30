package com.matheuscordeiro.myfinancesapi.model.entities;

import com.matheuscordeiro.myfinancesapi.model.entities.enums.*;
import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="entries", schema = "finances")
public class Entries {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String description;

    @Column
    private Integer month;

    @Column
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column
    private BigDecimal value;

    @Column(name = "date_register")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dateRegister;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private EntriesType type;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private EntriesStatus status;
}
