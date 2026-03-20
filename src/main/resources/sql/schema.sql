-- 공통 코드 그룹 관리
CREATE TABLE IF NOT EXISTS t_code_group (
    group_cd     VARCHAR(30)  NOT NULL                                                          COMMENT '코드 그룹 식별자',
    group_nm     VARCHAR(50)  NOT NULL                                                          COMMENT '코드 그룹명',
    description  VARCHAR(200) NULL                                                              COMMENT '그룹 설명',
    use_yn       CHAR(1)      NOT NULL DEFAULT 'Y'                                              COMMENT '사용 여부 (Y/N)',
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP                                COMMENT '등록 일시',
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP    COMMENT '수정 일시',
    PRIMARY KEY (group_cd)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='공통 코드 그룹 관리';

-- 공통 코드 관리 (주문상태, 거래유형, 정산유형 등)
CREATE TABLE IF NOT EXISTS t_code (
    group_cd     VARCHAR(30) NOT NULL                                                           COMMENT '코드 그룹 식별자 (FK)',
    code         VARCHAR(30) NOT NULL                                                           COMMENT '코드 값',
    code_nm      VARCHAR(50) NOT NULL                                                           COMMENT '코드 표시명',
    sort_order   INT         NOT NULL DEFAULT 0                                                 COMMENT '정렬 순서',
    use_yn       CHAR(1)     NOT NULL DEFAULT 'Y'                                               COMMENT '사용 여부 (Y/N)',
    created_at   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP                                 COMMENT '등록 일시',
    updated_at   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP     COMMENT '수정 일시',
    PRIMARY KEY (group_cd, code),
    CONSTRAINT fk_code_group FOREIGN KEY (group_cd)
        REFERENCES t_code_group (group_cd)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='공통 코드 관리';


-- 주식 종목 정보/관리
CREATE TABLE IF NOT EXISTS t_stock_info (
    stock_cd        VARCHAR(20)   NOT NULL                                                              COMMENT '종목 코드',
    stock_nm        VARCHAR(100)  NOT NULL                                                              COMMENT '종목명',
    market_type_cd  VARCHAR(10)   NOT NULL                                                              COMMENT '시장 구분 (KOSPI/KOSDAQ)',
    current_price   DECIMAL(18,2) NOT NULL DEFAULT 0.00                                                 COMMENT '현재가',
    use_yn          CHAR(1)       NOT NULL DEFAULT 'Y'                                                  COMMENT '거래 가능 여부 (Y/N)',
    updated_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP        COMMENT '최종 갱신 일시',
    PRIMARY KEY (stock_cd)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='주식 종목 정보/관리';


-- 회원 기본 정보
CREATE TABLE IF NOT EXISTS t_member (
    member_key   BIGINT       NOT NULL AUTO_INCREMENT                                               COMMENT '회원 고유키',
    user_id      VARCHAR(50)  NOT NULL                                                              COMMENT '로그인 계정 ID',
    password     VARCHAR(200) NOT NULL                                                              COMMENT '비밀번호 ( 암호화 처리 : BCrypt )',
    user_nm      VARCHAR(50)  NOT NULL                                                              COMMENT '회원명',
    status_cd        VARCHAR(10)   NOT NULL DEFAULT 'ACTIVE'                                         COMMENT '회원 상태 (ACTIVE / SUSPENDED / WITHDRAWN)',
    commission_rate  DECIMAL(10,6) NOT NULL DEFAULT 0.000150                                         COMMENT '매매 수수료율 (기본 0.015%)',
    created_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP                                COMMENT '가입 일시',
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP        COMMENT '수정 일시',
    PRIMARY KEY (member_key),
    UNIQUE KEY uk_member_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='회원 기본 정보';


-- 회원 예수금 잔액 (FOR UPDATE 대상)
CREATE TABLE IF NOT EXISTS t_member_bal (
    bal_seq      BIGINT        NOT NULL AUTO_INCREMENT                                              COMMENT '잔액 PK',
    member_key   BIGINT        NOT NULL                                                             COMMENT '회원 고유키 (FK)',
    balance      DECIMAL(18,2) NOT NULL DEFAULT 0.00                                                COMMENT '예수금 잔액',
    updated_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP       COMMENT '최종 갱신 일시',
    PRIMARY KEY (bal_seq),
    UNIQUE KEY uk_member_bal (member_key),
    CONSTRAINT fk_bal_member FOREIGN KEY (member_key)
        REFERENCES t_member (member_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='회원 예수금 잔액';


-- 회원 예수금 변동 내역
CREATE TABLE IF NOT EXISTS t_member_bal_his (
    his_seq        BIGINT        NOT NULL AUTO_INCREMENT                COMMENT '변동 이력 PK',
    member_key     BIGINT        NOT NULL                               COMMENT '회원 고유키',
    change_type_cd VARCHAR(20)   NOT NULL                               COMMENT '변동 유형 (DEPOSIT / WITHDRAW / BUY/SELL / SETTLEMENT / MARGIN_CALL)',
    change_amt     DECIMAL(18,2) NOT NULL                               COMMENT '변동 금액',
    before_bal     DECIMAL(18,2) NOT NULL                               COMMENT '변동 전 잔액',
    after_bal      DECIMAL(18,2) NOT NULL                               COMMENT '변동 후 잔액',
    ref_id         BIGINT        NULL                                   COMMENT '참조 ID (order_id, settlement_seq 등)',
    ref_type_cd    VARCHAR(20)   NULL                                   COMMENT '참조 유형 (ORDER / SETTLEMENT / DEPOSIT 등)',
    created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '발생 일시',
    PRIMARY KEY (his_seq),
    INDEX idx_bal_his_member (member_key),
    CONSTRAINT fk_bal_his_member FOREIGN KEY (member_key)
        REFERENCES t_member (member_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='회원 예수금 변동 내역';


-- 매수 / 매도 주문
CREATE TABLE IF NOT EXISTS t_trade_order (
    order_id        BIGINT        NOT NULL AUTO_INCREMENT                   COMMENT '주문 PK',
    member_key      BIGINT        NOT NULL                                  COMMENT '회원 고유키 (FK)',
    stock_cd        VARCHAR(20)   NOT NULL                                  COMMENT '종목 코드',
    order_type_cd   VARCHAR(10)   NOT NULL                                  COMMENT '주문 유형 (BUY : 매수, SELL : 매도)',
    order_src_cd    VARCHAR(20)   NOT NULL DEFAULT 'MANUAL'                 COMMENT '주문 출처 (MANUAL : 일반, MARGIN_CALL : 반대매매)',
    quantity        INT           NOT NULL                                  COMMENT '주문 수량',
    price           DECIMAL(18,2) NOT NULL                                  COMMENT '주문 가격',
    total_amt       DECIMAL(18,2) NOT NULL                                  COMMENT '주문 총액 (price × quantity)',
    order_status_cd VARCHAR(20)   NOT NULL DEFAULT 'PENDING'                COMMENT '주문 상태 (PENDING / FILLED / PARTIAL / CANCELLED / REJECTED)',
    leverage_yn     CHAR(1)       NOT NULL DEFAULT 'N'                      COMMENT '레버리지 사용 여부 (Y/N)',
    ordered_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP        COMMENT '주문 일시',
    filled_at       DATETIME      NULL                                      COMMENT '체결 완료 일시',
    PRIMARY KEY (order_id),
    INDEX idx_order_member (member_key),
    CONSTRAINT fk_order_member FOREIGN KEY (member_key)
        REFERENCES t_member (member_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='매수 / 매도 주문';


-- 증권사 체결 결과 수신 (정산 배치 처리 대상)
CREATE TABLE IF NOT EXISTS t_trade_execution (
    exec_seq       BIGINT        NOT NULL AUTO_INCREMENT                COMMENT '체결 PK',
    order_id       BIGINT        NOT NULL                               COMMENT '주문 PK (FK)',
    member_key     BIGINT        NOT NULL                               COMMENT '회원 고유키 (FK)',
    stock_cd       VARCHAR(20)   NOT NULL                               COMMENT '종목 코드',
    exec_price     DECIMAL(18,2) NOT NULL                               COMMENT '체결 가격',
    exec_qty       INT           NOT NULL                               COMMENT '체결 수량',
    exec_amt       DECIMAL(18,2) NOT NULL                               COMMENT '체결 금액 (exec_price × exec_qty)',
    broker_exec_id VARCHAR(50)   NOT NULL                               COMMENT '증권사 체결 고유번호',
    settled_yn     CHAR(1)       NOT NULL DEFAULT 'N'                   COMMENT '정산 배치 처리 여부 (Y/N)',
    received_at    DATETIME      NOT NULL                               COMMENT '웹훅 수신 시각',
    created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '등록 일시',
    PRIMARY KEY (exec_seq),
    UNIQUE KEY uk_broker_exec (broker_exec_id),
    INDEX idx_exec_settled (settled_yn),
    INDEX idx_exec_order (order_id),
    CONSTRAINT fk_exec_order FOREIGN KEY (order_id)
        REFERENCES t_trade_order (order_id),
    CONSTRAINT fk_exec_member FOREIGN KEY (member_key)
        REFERENCES t_member (member_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='증권사 체결 결과 수신';


-- 정산 내역 (일반정산 + 반대매매 결과 통합 적재)
CREATE TABLE IF NOT EXISTS t_settlement_his (
    settlement_seq     BIGINT        NOT NULL AUTO_INCREMENT                COMMENT '정산 PK',
    exec_seq           BIGINT        NOT NULL                               COMMENT '체결 PK (FK)',
    order_id           BIGINT        NOT NULL                               COMMENT '주문 PK (FK)',
    member_key         BIGINT        NOT NULL                               COMMENT '회원 고유키 (FK)',
    settlement_type_cd VARCHAR(20)   NOT NULL                               COMMENT '정산 유형 (NORMAL: 일반, MARGIN_CALL: 반대매매)',
    settlement_amt     DECIMAL(18,2) NOT NULL                               COMMENT '정산 대상 금액 (체결 금액)',
    commission_rate    DECIMAL(10,6) NOT NULL                               COMMENT '적용 수수료율',
    commission         DECIMAL(18,2) NOT NULL DEFAULT 0.00                  COMMENT '수수료',
    tax                DECIMAL(18,2) NOT NULL DEFAULT 0.00                  COMMENT '세금',
    net_amt            DECIMAL(18,2) NOT NULL                               COMMENT '최종 정산 금액 (수수료/세금 차감 후)',
    leverage_yn        CHAR(1)       NOT NULL DEFAULT 'N'                   COMMENT '레버리지 사용 여부 (Y/N)',
    due_date           DATE          NULL                                   COMMENT '정산 기한 (미수금 판단 기준일)',
    settled_at         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '정산 처리 일시',
    created_at         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '등록 일시',
    PRIMARY KEY (settlement_seq),
    INDEX idx_sttl_member (member_key),
    INDEX idx_sttl_due (due_date),
    CONSTRAINT fk_sttl_exec FOREIGN KEY (exec_seq)
        REFERENCES t_trade_execution (exec_seq),
    CONSTRAINT fk_sttl_order FOREIGN KEY (order_id)
        REFERENCES t_trade_order (order_id),
    CONSTRAINT fk_sttl_member FOREIGN KEY (member_key)
        REFERENCES t_member (member_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='정산 내역';


-- 회원 보유 주식 (반대매매 배치 조회 대상)
CREATE TABLE IF NOT EXISTS t_member_stock (
    hold_seq     BIGINT        NOT NULL AUTO_INCREMENT                                         COMMENT '보유 PK',
    member_key   BIGINT        NOT NULL                                                        COMMENT '회원 고유키 (FK)',
    stock_cd     VARCHAR(20)   NOT NULL                                                        COMMENT '종목 코드',
    quantity     INT           NOT NULL DEFAULT 0                                              COMMENT '보유 수량',
    avg_price    DECIMAL(18,2) NOT NULL DEFAULT 0.00                                           COMMENT '평균 매입 단가',
    updated_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '최종 갱신 일시',
    PRIMARY KEY (hold_seq),
    UNIQUE KEY uk_member_stock (member_key, stock_cd),
    CONSTRAINT fk_hold_member FOREIGN KEY (member_key)
        REFERENCES t_member (member_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='회원 보유 주식';


-- 초기 공통 코드 데이터
INSERT IGNORE INTO t_code_group (group_cd, group_nm, description) VALUES
('ORDER_TYPE',       '주문 유형',     '매수/매도 구분'),
('ORDER_STATUS',     '주문 상태',     '주문 처리 상태'),
('ORDER_SOURCE',     '주문 출처',     '일반주문/반대매매 구분'),
('CHANGE_TYPE',      '잔액 변동 유형', '예수금 변동 사유'),
('SETTLEMENT_TYPE',  '정산 유형',     '일반정산/반대매매 정산 구분'),
('MEMBER_STATUS',    '회원 상태',     '회원 활성/정지/탈퇴'),
('MARKET_TYPE',      '시장 구분',     'KOSPI/KOSDAQ');

INSERT IGNORE INTO t_code (group_cd, code, code_nm, sort_order) VALUES
('ORDER_TYPE',      'BUY',         '매수',          1),
('ORDER_TYPE',      'SELL',        '매도',          2),
('ORDER_STATUS',    'PENDING',     '대기',          1),
('ORDER_STATUS',    'FILLED',      '체결완료',       2),
('ORDER_STATUS',    'PARTIAL',     '부분체결',       3),
('ORDER_STATUS',    'CANCELLED',   '취소',          4),
('ORDER_STATUS',    'REJECTED',    '거부',          5),
('ORDER_SOURCE',    'MANUAL',      '일반주문',       1),
('ORDER_SOURCE',    'MARGIN_CALL', '반대매매',       2),
('CHANGE_TYPE',     'DEPOSIT',     '입금',          1),
('CHANGE_TYPE',     'WITHDRAW',    '출금',          2),
('CHANGE_TYPE',     'BUY',         '매수차감',       3),
('CHANGE_TYPE',     'SELL',        '매도입금',       4),
('CHANGE_TYPE',     'SETTLEMENT',  '정산반영',       5),
('CHANGE_TYPE',     'MARGIN_CALL', '반대매매',       6),
('SETTLEMENT_TYPE', 'NORMAL',      '일반정산',       1),
('SETTLEMENT_TYPE', 'MARGIN_CALL', '반대매매정산',    2),
('MEMBER_STATUS',   'ACTIVE',      '활성',          1),
('MEMBER_STATUS',   'SUSPENDED',   '정지',          2),
('MEMBER_STATUS',   'WITHDRAWN',   '탈퇴',          3),
('MARKET_TYPE',     'KOSPI',       '코스피',         1),
('MARKET_TYPE',     'KOSDAQ',      '코스닥',         2);
