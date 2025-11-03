package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.BaseCoverage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "coverage_rider")
@Getter
@NoArgsConstructor
public class CoverageRiderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    public BaseCoverage toDomain() {
        return new BaseCoverage(this.code, this.name);
    }
}