package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoyageFinder {

	private final VoyageRepository voyageRepository;

	public String find(String origin) {
		String voyageCode = voyageRepository.findVoyageCodeByOrigin(origin)
			.orElseThrow(() -> new IllegalArgumentException("출발지에 해당하는 항해구간코드를 찾을 수 없습니다: " + origin));

		return voyageCode;
	}

}
