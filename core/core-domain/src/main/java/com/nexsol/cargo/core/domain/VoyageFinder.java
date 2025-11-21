package com.nexsol.cargo.core.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoyageFinder {

	private final VoyageRepository voyageRepository;

	public String find(String origin) {
		try {

			return voyageRepository.findVoyageCodeByOrigin(origin).orElse(null);
		}
		catch (Exception e) {

			log.error("항해구간 코드 조회 중 오류 발생 (Origin: {})", origin, e);
			return null;
		}
	}

}
