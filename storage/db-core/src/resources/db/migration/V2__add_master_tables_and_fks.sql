CREATE TABLE location_country
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    location_code   VARCHAR(50)  NOT NULL COMMENT '국가도시코드 (e.g., KO03)',
    country_name VARCHAR(255) NOT NULL COMMENT '국가영문명 (e.g., KOREA, SOUTH)',
    city_name    VARCHAR(255) NULL COMMENT '도시영문명 (e.g., BUSAN)',
    voyage_code     VARCHAR(10)  NOT NULL COMMENT '항해구간 코드 (voyage.voyage_code 참조)',

    UNIQUE KEY uq_location_code (location_code),
    INDEX idx_city_name_en (city_name) COMMENT '도시명(origin)으로 검색하기 위한 인덱스',
    CONSTRAINT fk_location_country_voyage_code
        FOREIGN KEY (voyage_code) REFERENCES voyage (voyage_code)
) COMMENT '국가/도시별 항해구간 매핑';

ALTER TABLE subscription
    ADD COLUMN signature_key VARCHAR(255) NULL COMMENT '전자 서명 MinIO 객체 키';

ALTER TABLE subscription
    ADD COLUMN signature_base64_temp LONGTEXT NULL COMMENT '결제 전 임시 서명 Base64 데이터';

ALTER TABLE subscription
    ADD COLUMN signature_content_type_temp VARCHAR(50) NULL COMMENT '결제 전 임시 서명 Content Type'
