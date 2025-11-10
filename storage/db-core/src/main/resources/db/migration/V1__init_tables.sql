-- --- 1. User (회원) ---
-- '회원' 개념의 핵심 테이블 (인증 정보)
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


-- '회원' 개념의 상세 정보 (프로필)
CREATE TABLE user_profile
(
    user_id        BIGINT PRIMARY KEY COMMENT 'app_user.id 참조 (1:1)',
    company_name   VARCHAR(255) NOT NULL COMMENT '상호명',
    user_name      VARCHAR(255) NOT NULL COMMENT '담당자 이름',
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
) COMMENT '회원 프로필 상세';

-- '회원' 개념의 인증 토큰
CREATE TABLE user_refresh_token
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT       NOT NULL COMMENT 'app_user.id 참조',
    refresh_token VARCHAR(500) NOT NULL COMMENT '해싱된 리프레시 토큰',
    expiry_at     DATETIME     NOT NULL COMMENT '토큰 만료 시간',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_refresh_token_user
        FOREIGN KEY (user_id) REFERENCES `app_user` (id) ON DELETE CASCADE,
    INDEX         idx_user_refresh_token_user_id (user_id)
) COMMENT '사용자 리프레시 토큰';


-- --- Quotation (가견적) ---
-- OCR 분석 후 '플랜 추천' 단계로 넘어가기 전, 임시 저장되는 화물 데이터 (Key 역할)
CREATE TABLE quotation
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT         NOT NULL COMMENT 'OCR 요청 회원 (app_user.id)',
    quotation_key        VARCHAR(50)    NOT NULL UNIQUE COMMENT '클라이언트 전달용 Key (UUID)',

    -- OCR 및 플랜 분석 핵심 정보 (ocr.png 참조)
    hs_code              VARCHAR(50)    NOT NULL COMMENT 'HS Code',
    invoice_amount       DECIMAL(19, 4) NOT NULL COMMENT '송장가액 (외화 기준)',
    currency_unit        VARCHAR(10)    NOT NULL COMMENT '화폐 단위 (USD, KRW 등)',
    exchange_rate_amount DECIMAL(15, 4) NOT NULL COMMENT '적용 환율',

    -- 운송 상세 정보 (ocr.png 참조)
    ref_no               VARCHAR(50)    NOT NULL COMMENT 'Ref No',
    bl_no                VARCHAR(50)    NOT NULL COMMENT 'B/L No',
    outbound_date        DATE           NOT NULL COMMENT '출발일자',
    origin               VARCHAR(255)   NOT NULL COMMENT '출발지',
    destination          VARCHAR(255)   NOT NULL COMMENT '도착지',
    conveyance           ENUM('AIR','SHIP')  NOT NULL COMMENT '운송용구',
    packing_type         VARCHAR(50)    NOT NULL COMMENT '포장 구분',
    cargo_item_name      VARCHAR(255)   NOT NULL COMMENT '품목 상세',

    status               ENUM('PENDING', 'SUBSCRIBED') NOT NULL DEFAULT 'PENDING' COMMENT '상태 (청약 완료 여부)',
    created_at           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_quotation_user FOREIGN KEY (user_id) REFERENCES `app_user` (id)
) COMMENT '가견적 (OCR 임시 저장)';


-- --- 2. Coverage (담보 마스터) ---
-- '담보' 개념의 기본 담보 (e.g., ICC(A))
CREATE TABLE coverage_base
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50)    NOT NULL UNIQUE COMMENT '담보특약 코드 (e.g., 101662)',
    name VARCHAR(255)   NOT NULL COMMENT '담보특약 명칭 (e.g., ICC(A))',
    rate DECIMAL(10, 5) NOT NULL COMMENT '기본 요율(e.g., 0,015)'
) COMMENT '기본 담보 마스터';

-- '담보' 개념의 옵션 담보 (e.g., WAR CLAUSES)
CREATE TABLE coverage_option
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(50)    NOT NULL UNIQUE COMMENT '하위 조건 코드 (e.g., 102077)',
    name        VARCHAR(255)   NOT NULL COMMENT '하위 조건 명칭 (e.g., WAR CLAUSES)',
    option_type ENUM('REFERENCE', 'ADDITIONAL', 'EXTENSION') NOT NULL COMMENT '옵션 타입 (참조, 추가, 확장)',
    rate        DECIMAL(10, 5) NOT NULL DEFAULT 0.0 COMMENT '부가/확장 요율 (e.g., 0.001)',
    rate_type   ENUM('ADDITIONAL_RISK', 'EXTENSION', 'TOTAL_LOSS', 'SPECIAL_CLAUSE', 'SURCHARGE') NOT NULL COMMENT '요율의 타입 (부가위험,확장담보,전체 loss,전쟁/파업,추가할증)'
) COMMENT '옵션 담보 마스터';


-- --- 3. Subscription (청약) ---
-- '청약' 개념의 핵심 (진행 상태 및 계약자 정보)
CREATE TABLE subscription
(
    id                        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                   BIGINT         NOT NULL COMMENT '청약 생성 회원 (app_user.id)',
    status                    ENUM('PAYMENT_PENDING', 'PAYMENT_COMPLETE', 'POLICY_ISSUED', 'CANCELLED') NOT NULL COMMENT '청약 상태',
    insurance_premium         DECIMAL(15, 2) NOT NULL COMMENT '확정 보험료 (원화)',
    -- 계약자/피보험자 정보
    is_same                   BOOLEAN        NOT NULL DEFAULT FALSE COMMENT '피보험자가 계약자와 동일한지 여부',
    policyholder_company_name VARCHAR(255)   NOT NULL COMMENT '계약자 상호명 ',
    policyholder_company_code VARCHAR(50)    NOT NULL COMMENT '계약자 사업자번호',
    insured_company_name      VARCHAR(255) NULL COMMENT '피보험자 상호명',
    insured_company_code      VARCHAR(50) NULL COMMENT '피보험자 사업자번호',

    created_at                DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_subscription_user FOREIGN KEY (user_id) REFERENCES `app_user` (id)
) COMMENT '청약서 (핵심)';

-- '청약' 개념의 운송 상세 (OCR 정보)
CREATE TABLE subscription_cargo
(
    subscription_id BIGINT PRIMARY KEY COMMENT 'subscription.id 참조 (1:1)',

    -- OCR 및 플랜 분석 핵심 정보
    hs_code         VARCHAR(50)    NOT NULL COMMENT 'HS Code (플랜 추천 분석용)',
    invoice_amount  DECIMAL(19, 4) NOT NULL COMMENT '송장가액 (외화 기준)',
    currency_unit   VARCHAR(10)    NOT NULL COMMENT '화폐 단위 (USD, KRW 등)',

    -- 운송 상세 정보 (OCR 결과)
    ref_no          VARCHAR(50)    NOT NULL COMMENT 'Ref No',
    bl_no           VARCHAR(50)    NOT NULL COMMENT 'B/L No',
    outbound_date   DATE           NOT NULL COMMENT '출발일자',
    origin          VARCHAR(255)   NOT NULL COMMENT '출발지',
    destination     VARCHAR(255)   NOT NULL COMMENT '도착지',
    conveyance      ENUM('AIR','SHIP')  NOT NULL COMMENT '운송용구 (선박, 항공 등)',
    packing_type    VARCHAR(50)    NOT NULL COMMENT '포장 구분',
    cargo_item_name VARCHAR(255)   NOT NULL COMMENT '품목 상세',

    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_cargo_subscription
        FOREIGN KEY (subscription_id) REFERENCES `subscription` (id) ON DELETE CASCADE
) COMMENT '청약 운송 상세 정보 (OCR)';

-- '청약' 개념의 선택 담보 내역 (스냅샷)
CREATE TABLE subscription_coverage
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    subscription_id BIGINT       NOT NULL COMMENT '청약서 ID (subscription.id 참조)',

    -- 증권 인쇄에 사용될 '스냅샷' 데이터
    condition_type  ENUM('BASE', 'REFERENCE', 'ADDITIONAL', 'EXTENSION') NOT NULL COMMENT '조건 유형 (기본, 참조, 추가, 확장)',
    condition_code  VARCHAR(50)  NOT NULL COMMENT '청약 시점의 조건 코드 (스냅샷)',
    condition_name  VARCHAR(255) NOT NULL COMMENT '청약 시점의 조건 명칭 (스냅샷)',

    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_coverage_subscription FOREIGN KEY (subscription_id) REFERENCES subscription (id) ON DELETE CASCADE,
    INDEX           idx_subscription_coverage_id (subscription_id)
) COMMENT '청약 담보 내역 (스냅샷)';

-- '청약' 개념의 결제 정보
CREATE TABLE payment
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    subscription_id      BIGINT         NOT NULL,

    insurance_premium    DECIMAL(15, 2) NOT NULL COMMENT '확정 보험료 (결제 대상 금액)',
    payment_status       ENUM('READY', 'SUCCESS') NOT NULL COMMENT '결제 상태',

    -- 결제 방식 (TODO: 외부 Payment API 추후 연동)
    payment_method       ENUM('CARD_KEY_IN','SIMPLE_PAY','BANK_TRANSFER') NULL COMMENT '결제 수단 (CARD, PAY, BANK)',
    card_type            VARCHAR(50) NULL COMMENT '카드 종류 (선택)',
    card_number_masked   VARCHAR(50) NULL COMMENT '마스킹된 카드 번호',
    expiry_date          VARCHAR(10) NULL COMMENT '유효기간 (MM/YYYY)',
    external_payment_key VARCHAR(255) NULL COMMENT '외부 결제사 고유키',

    created_at           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_subscription
        FOREIGN KEY (subscription_id) REFERENCES `subscription` (id) ON DELETE CASCADE
) COMMENT '결제내역';
