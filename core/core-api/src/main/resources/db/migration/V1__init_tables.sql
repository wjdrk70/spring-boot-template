-- --- user ---
CREATE TABLE user
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id   VARCHAR(50)  NOT NULL UNIQUE COMMENT '사업자 번호=로그인시 ID',
    password   VARCHAR(255) NOT NULL,
    role       ENUM('ADMIN') NOT NULL COMMENT '현재 admin(사업자)만 사용',
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at DATETIME NULL DEFAULT NULL
);


CREATE TABLE user_profile
(
    user_id    BIGINT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
);


CREATE TABLE company_info
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT       NOT NULL,
    company_name   VARCHAR(255) NOT NULL COMMENT '상호명',
    company_code   VARCHAR(50)  NOT NULL,
    manager_name   VARCHAR(255) NOT NULL COMMENT '담당자 명',
    phone_number   VARCHAR(20)  NOT NULL COMMENT '휴대폰번호',
    email          VARCHAR(255) NOT NULL COMMENT '이메일',
    zip_code       VARCHAR(10)  NOT NULL COMMENT '우편번호',
    address_line  VARCHAR(255) NOT NULL COMMENT '기본 주소 (e.g., 도로명/지번)',
    address_detail VARCHAR(255) NULL COMMENT '상세 주소 (e.g., 201호)',
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at     DATETIME NULL DEFAULT NULL,
    CONSTRAINT fk_company_user FOREIGN KEY (user_id) REFERENCES user (id)
) COMMENT '회원가입시 회사정보';

-- --- 테이블 1: 기본 조건(담보특약) ---
CREATE TABLE coverage_rider
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50)  NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL
);

-- --- 적하품목 meta data ---
CREATE TABLE cargo_item
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    code     varchar(50) NOT NULL UNIQUE,
    hs_code  varchar(50) NOT NULL UNIQUE,
    rider_id BIGINT      NOT NULL,
    CONSTRAINT fk_cargo_rider FOREIGN KEY (rider_id) REFERENCES coverage_rider (id)
) COMMENT '적하품목';


-- --- 테이블 2: 하위 조건(담보 특약의 하위) ---
CREATE TABLE coverage_option
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL,
    option_type ENUM('REFERENCE', 'ADDITIONAL', 'EXTENSION') NOT NULL COMMENT '담보특약코드 의 참조,추가,확장 코드 타입'
) COMMENT '담보특약(기본코드)';

-- --- 테이블 3: N:M 매핑 테이블 (핵심) ---
CREATE TABLE coverage_rider_option
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    rider_id  BIGINT NOT NULL,
    option_id BIGINT NOT NULL,
    UNIQUE KEY uk_rider_option (rider_id, option_id),
    CONSTRAINT fk_map_rider FOREIGN KEY (rider_id) REFERENCES coverage_rider (id) ON DELETE CASCADE,
    CONSTRAINT fk_map_option FOREIGN KEY (option_id) REFERENCES coverage_option (id) ON DELETE CASCADE
);


-- --- 청약 테이블 ---
CREATE TABLE subscription
(
    id                        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                   BIGINT         NOT NULL COMMENT '이 청약을 생성한 ADMIN(사업자)',
    status                    ENUM('PAYMENT_PENDING', 'PAYMENT_COMPLETE', 'POLICY_ISSUED', 'CANCELLED') NOT NULL COMMENT '청약 상태',
    insurance_premium         DECIMAL(15, 2) NOT NULL COMMENT '확정 보험료',
    is_same                   BOOLEAN        NOT NULL DEFAULT FALSE COMMENT '피보험자가 계약자와 동일한지 여부',
    policyholder_company_name VARCHAR(255)   NOT NULL COMMENT '계약자 상호명 ',
    policyholder_company_code VARCHAR(50)    NOT NULL COMMENT '계약자 사업자번호',
    insured_company_name      VARCHAR(255) NULL COMMENT '피보험자 상호명',
    insured_company_code      VARCHAR(50) NULL COMMENT '피보험자 사업자번호',
    ocr_data_snapshot         JSON NULL COMMENT '청약 시점의 OCR 원본 데이터 스냅샷',
    created_at                DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_subscription_user FOREIGN KEY (user_id) REFERENCES user (id)
) COMMENT '청약서';

-- --- 청약 snapshot 테이블 ---
CREATE TABLE subscription_snapshot
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    subscription_id BIGINT       NOT NULL COMMENT '청약서 ID (subscription.id 참조)',

    -- 증권 인쇄에 사용될 '스냅샷' 데이터
    condition_type  ENUM('BASE', 'REFERENCE', 'ADDITIONAL', 'EXTENSION') NOT NULL COMMENT '조건 유형 (기본, 참조, 추가, 확장)',
    condition_code  VARCHAR(50)  NOT NULL COMMENT '청약 시점의 조건 코드 (스냅샷)',
    condition_name  VARCHAR(255) NOT NULL COMMENT '청약 시점의 조건 명칭 (스냅샷)',

    -- 타임스탬프
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- 외래 키 (Master 삭제 시 Detail도 함께 삭제)
    CONSTRAINT fk_subscription_condition_subscription FOREIGN KEY (subscription_id) REFERENCES subscription (id) ON DELETE CASCADE,

    -- 조회 성능을 위한 인덱스
    INDEX           idx_subscription_id (subscription_id)
) COMMENT '청약서 snapshot';


CREATE TABLE user_refresh_token
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    refresh_token VARCHAR(500) NOT NULL COMMENT '해싱된 리프레시 토큰',
    expiry_at    DATETIME     NOT NULL COMMENT '토큰 만료 시간',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_refresh_token_user
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,

    INDEX idx_user_id (user_id)
) COMMENT '사용자 리프레시 토큰 (멀티 디바이스 지원)';