-- --- member ---
CREATE TABLE app_user
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_code VARCHAR(50)  NOT NULL UNIQUE COMMENT '사업자번호 (로그인 ID)',
    password     VARCHAR(255) NOT NULL,
    role         ENUM('ADMIN') NOT NULL,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at   DATETIME NULL DEFAULT NULL
) COMMENT '사업자 회원';


CREATE TABLE user_profile
(
    user_id        BIGINT PRIMARY KEY COMMENT 'app_user.id 참조',
    company_name   VARCHAR(255) NOT NULL COMMENT '상호명',
    user_name           VARCHAR(255) NOT NULL COMMENT '담당자 이름',
    manager_name   VARCHAR(255) NOT NULL COMMENT '담당자 명',
    phone_number   VARCHAR(20)  NOT NULL COMMENT '휴대폰번호',
    email          VARCHAR(255) NOT NULL COMMENT '이메일',

    zip_code       VARCHAR(10)  NOT NULL COMMENT '우편번호',
    address_line   VARCHAR(255) NOT NULL COMMENT '기본 주소',
    address_detail VARCHAR(255) NULL COMMENT '상세 주소',

    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_profile_to_app_user
        FOREIGN KEY (user_id) REFERENCES `app_user` (id) ON DELETE CASCADE
);

-- --- 기본 담보 (Master) ---
CREATE TABLE coverage_rider
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50)  NOT NULL UNIQUE COMMENT '담보특약 코드 (e.g., 101662)',
    name VARCHAR(255) NOT NULL COMMENT '담보특약 명칭 (e.g., ICC(A))'
);

-- --- 옵션 담보 (Master) ---
CREATE TABLE coverage_option
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL UNIQUE COMMENT '하위 조건 코드 (e.g., 102077)',
    name        VARCHAR(255) NOT NULL COMMENT '하위 조건 명칭 (e.g., WAR CLAUSES)',
    option_type ENUM('REFERENCE', 'ADDITIONAL', 'EXTENSION') NOT NULL COMMENT '옵션 타입'
);

-- --- 적하품목 meta data ---
CREATE TABLE cargo_item
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     varchar(50) NOT NULL UNIQUE,
    hs_code  varchar(50) NOT NULL UNIQUE,
    rider_id BIGINT      NOT NULL,
    INDEX            idx_hs_code (hs_code),
    CONSTRAINT fk_cargo_rider FOREIGN KEY (rider_id) REFERENCES coverage_rider (id)
) COMMENT '적하품목';


-- --- 옵션 매핑 (M:N) ---
-- (cargo_item 1건에 엮인 N개의 옵션들을 저장)
CREATE TABLE cargo_item_option
(
    cargo_item_id      BIGINT NOT NULL COMMENT 'AI 계약 ID (cargo_item.id)',
    coverage_option_id BIGINT NOT NULL COMMENT '담보 옵션 ID (coverage_option.id)',

    PRIMARY KEY (cargo_item_id, coverage_option_id),
    CONSTRAINT fk_item_option_item FOREIGN KEY (cargo_item_id) REFERENCES cargo_item (id) ON DELETE CASCADE,
    CONSTRAINT fk_item_option_option FOREIGN KEY (coverage_option_id) REFERENCES coverage_option (id)
);


-- --- 보험 요율표 ---
CREATE TABLE premium_rate
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    coverage_code VARCHAR(50)    NOT NULL UNIQUE COMMENT '담보 코드 (rider.code 또는 option.code)',
    rate          DECIMAL(10, 5) NOT NULL COMMENT '보험 요율 (e.g., 0.0015)',

    -- (향후 확장을 위해 주석 처리)
    -- min_amount   DECIMAL(19, 4) NOT NULL DEFAULT 0.0 COMMENT '송장가액 최소',
    -- max_amount   DECIMAL(19, 4) NOT NULL DEFAULT 999999999.0 COMMENT '송장가액 최대',
    -- ... (구간, 선박 등에 따른 조건 컬럼 추가 가능)

    INDEX idx_coverage_code (coverage_code)
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
    hs_code                   VARCHAR(50) NULL COMMENT '청약 시점의 HS Code (플랜 추천 분석용)',
    created_at                DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_subscription_user FOREIGN KEY (user_id) REFERENCES `app_user` (id)
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
        FOREIGN KEY (user_id) REFERENCES `app_user` (id) ON DELETE CASCADE,

    INDEX idx_user_id (user_id)
) COMMENT '사용자 리프레시 토큰 (멀티 디바이스 지원)';