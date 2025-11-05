- --- 1. subscription 테이블에 핵심 분석 컬럼 추가 ---
-- 송장가액 (보험가입금액의 원본값), 화폐단위
ALTER TABLE subscription
    ADD COLUMN invoice_amount DECIMAL(19, 4) NOT NULL COMMENT '송장가액 (외화 기준)',
    ADD COLUMN currency_unit  VARCHAR(10)    NOT NULL COMMENT '화폐 단위 (USD, KRW 등)',
    DROP COLUMN ocr_data_snapshot;


-- --- 2. 청약 운송 상세 정보 (Cargo/Transit Detail) 테이블 추가 ---
CREATE TABLE subscription_cargo_detail
(
    subscription_id   BIGINT PRIMARY KEY COMMENT 'subscription.id 참조',

    -- 운송 상세 정보
    ref_no            VARCHAR(50)  NOT NULL COMMENT 'Ref No',
    bl_no             VARCHAR(50)  NOT NULL COMMENT 'B/L No',
    outbound_date     DATE         NOT NULL COMMENT '출발일자',
    origin            VARCHAR(255) NOT NULL COMMENT '출발지',
    destination       VARCHAR(255) NOT NULL COMMENT '도착지',
    conveyance        ENUM('AIR','SHIP')  NOT NULL COMMENT '운송용구 (선박, 항공 등)',
    packing_type      VARCHAR(50)  NOT NULL COMMENT '포장 구분',
    cargo_item_name VARCHAR(255) NOT NULL COMMENT '품목 상세',

    -- 타임스탬프
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_cargo_detail_subscription
        FOREIGN KEY (subscription_id) REFERENCES `subscription` (id) ON DELETE CASCADE
) COMMENT '청약 운송 상세 정보';


-- --- 3. 청약 결제 상세 정보 (Payment Detail) 테이블 추가 ---
CREATE TABLE payment_detail
(
    subscription_id      BIGINT PRIMARY KEY COMMENT 'subscription.id 참조',

    -- 결제 방식 (TODO:외부 Payment API 추후 연동)
    payment_method       VARCHAR(50) NOT NULL COMMENT '결제 수단 (CARD, PAY, BANK)',
    card_type            VARCHAR(50) NULL COMMENT '카드 종류 (선택)',
    card_number_masked   VARCHAR(50) NULL COMMENT '마스킹된 카드 번호 (1234-****-****-5678)',
    expiry_date          VARCHAR(10) NULL COMMENT '유효기간 (MM/YYYY)',

    -- 결제 상태 (Business Layer에서 관리하는 상태와는 분리)
    external_payment_key VARCHAR(255) NULL COMMENT '외부 결제사 고유키 (추후 연동용)',
    payment_status       ENUM('READY', 'SUCCESS', 'FAILED', 'CANCELLED') NOT NULL COMMENT '결제 상태',

    -- 타임스탬프
    created_at           DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_detail_subscription
        FOREIGN KEY (subscription_id) REFERENCES `subscription` (id) ON DELETE CASCADE
) COMMENT '청약 결제 상세 정보';