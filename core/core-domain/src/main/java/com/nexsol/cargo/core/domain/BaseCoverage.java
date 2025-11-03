package com.nexsol.cargo.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class BaseCoverage {
    private final String code;
    private final String name;
}
