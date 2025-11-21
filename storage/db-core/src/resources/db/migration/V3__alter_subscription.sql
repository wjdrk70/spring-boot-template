ALTER TABLE subscription
    ADD COLUMN manager_name VARCHAR(255) NULL COMMENT '계약 담당자명';

ALTER TABLE subscription
    ADD COLUMN manager_phone LONGTEXT NULL COMMENT '계약 담당자 연락처';

ALTER TABLE subscription
    ADD COLUMN manager_email VARCHAR(50) NULL COMMENT '계약 담당자 이메일';

ALTER TABLE subscription
    ADD COLUMN canceled_at DATETIME NULL COMMENT '계약 취소/해지 일시';