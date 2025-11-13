package com.nexsol.cargo.core.domain;

import java.util.Optional;

public interface CargoItemRepository {

	Optional<String> findMiddleCodeByHsCode(String hsCode);

}
