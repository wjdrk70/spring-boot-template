package com.nexsol.cargo.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class SubscriptionCoverageSet {

	// Set을 사용하여 중복 없는 담보 코드 조합을 보관
	private final Set<String> coverageCodes;

}