package com.nexsol.cargo.storage.db.core;

import com.nexsol.cargo.core.domain.VoyageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VoyageRepositoryImpl implements VoyageRepository {

	private final VoyageJpaRepository voyageJpaRepository;

	@Override
	public Optional<String> findVoyageCodeByOrigin(String origin) {
		// TODO: 임시 구현. 'origin' 문자열(e.g., "BUSAN, KOREA")을
		// 'voyage_code'(e.g., "01")로 변환하는 로직이 필요
		if (origin != null && origin.contains("JAPAN")) {
			return Optional.of("03");
		}
		// 기본값 '보세외항'
		return Optional.of("01");
	}

}
