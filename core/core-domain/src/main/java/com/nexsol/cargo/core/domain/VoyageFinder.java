package com.nexsol.cargo.core.domain;

import com.nexsol.cargo.core.error.CoreErrorType;
import com.nexsol.cargo.core.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoyageFinder {

	private final VoyageRepository voyageRepository;

	public String find(String origin) {
		String voyageCode = voyageRepository.findVoyageCodeByOrigin(origin)
			.orElseThrow(() -> new CoreException(CoreErrorType.VOYAGE_NOT_FOUND));

		return voyageCode;
	}

}
