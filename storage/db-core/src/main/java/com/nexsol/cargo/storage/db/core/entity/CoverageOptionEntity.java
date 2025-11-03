package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.OptionCoverage;
import com.nexsol.cargo.core.enums.CoverageOptionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coverage_option")
@Getter
@NoArgsConstructor
public class CoverageOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_type", nullable = false)
    private CoverageOptionType optionType;

    public OptionCoverage toDomain() {
        return new OptionCoverage(this.code, this.name, this.optionType);
    }
}