package com.nexsol.cargo.storage.db.core.entity;

import com.nexsol.cargo.core.domain.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class AddressEmbeddable {

	@Column(name = "zip_code", length = 10)
	private String zipCode;

	@Column(name = "address_line")
	private String addressLine;

	@Column(name = "address_detail")
	private String addressDetail;

	public static AddressEmbeddable fromDomain(Address address) {
		if (address == null)
			return null;

		AddressEmbeddable embeddable = new AddressEmbeddable();
		embeddable.zipCode = address.getZipCode();
		embeddable.addressLine = address.getAddressLine();
		embeddable.addressDetail = address.getAddressDetail();
		return embeddable;
	}

	public Address toDomain() {
		return Address.builder()
			.zipCode(this.zipCode)
			.addressLine(this.addressLine)
			.addressDetail(this.addressDetail)
			.build();
	}

}
