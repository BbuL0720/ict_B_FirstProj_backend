# ict_B_FirstProj_backend
B조 1차 프로젝트 백엔드 코드입니다😊

테이블 정리

<details> <summary>전체 SQL 코드 보기</summary>

SQL
```/*******************************************************************************
 * 1. 회원 및 보안 시스템 (User & Security)
 *******************************************************************************/

-- [MEMBER] 서비스 이용자 정보 테이블
CREATE TABLE MEMBER (
    mnum      NUMBER            NOT NULL,          -- 회원 고유 번호 (PK)
    mname     VARCHAR2(50)      NOT NULL,          -- 실명 또는 닉네임
    mid       VARCHAR2(50)      NOT NULL,          -- 로그인 아이디 (Unique)
    PASSWORD  VARCHAR2(100)     NOT NULL,          -- 비밀번호 
    email     VARCHAR2(30)      NOT NULL,          -- 이메일
    mdate     DATE              DEFAULT SYSDATE,   -- 계정 생성일
    color     VARCHAR2(7),                         -- 사용자 프로필 테마 색상
    CONSTRAINT MEMBER_num_pk   PRIMARY KEY (mnum),
    CONSTRAINT member_id_UQ    UNIQUE (mid)
);
CREATE SEQUENCE MEMBER_num_seq INCREMENT BY 1 START WITH 1;--각 행의 고유한 번호를 주기 위해 시퀀스 생성


/*******************************************************************************
 * 2. 커뮤니티 및 소통 시스템 (Community & Board)
 *******************************************************************************/

-- [board] 카테고리별 일반 게시글 저장소 (자유/정보/홍보)
CREATE TABLE board(
    bnum NUMBER PRIMARY KEY,                       -- 게시글 고유 번호
    category VARCHAR2(10) NOT NULL,                -- 카테고리 구분 (free, info, prom)
    title VARCHAR2(50) NOT NULL,                   -- 게시글 제목
    writer VARCHAR2(50),                           -- 작성자 아이디 (Member 참조)
    udate DATE,                                    -- 최종 수정일
    image VARCHAR2(50),                            -- 이미지
    CONTENT VARCHAR2(600) NOT NULL,                -- 게시글 본문
    CONSTRAINT board_writer_FK FOREIGN KEY(writer) REFERENCES MEMBER(mid)
);
 CREATE SEQUENCE board_num_seq START WITH 1 INCREMENT BY 1;

-- [board_comments] 게시글에 대한 댓글 테이블
CREATE TABLE board_comments(
    bcnum NUMBER NOT NULL PRIMARY KEY,             -- 댓글 고유 번호
    bnum NUMBER NOT NULL,                          -- 부모 게시글 번호
    writer VARCHAR2(10) NOT NULL,                  -- 댓글 작성자 아이디
    udate DATE,                                    -- 댓글 작성 시각
    comment_text VARCHAR2(180) NOT NULL,           -- 댓글 내용
    CONSTRAINT board_comments_board_writer_fk FOREIGN KEY(writer) REFERENCES MEMBER(mid),
    CONSTRAINT board_comments_board_bnum_fk FOREIGN KEY (bnum) REFERENCES BOARD(bnum)
);

CREATE SEQUENCE board_comments_num_seq START WITH 1 INCREMENT BY 1; 


/*******************************************************************************
 * 3. 분실물 센터 시스템 (Lost & Found Service)
 *******************************************************************************/

-- [lost] 습득물/분실물 등록 및 위치 기반 정보 관리
CREATE TABLE lost(
    lnum NUMBER NOT NULL CONSTRAINT lost_num_pk PRIMARY KEY, -- 사건 고유 번호
    mnum NUMBER,                                   -- 작성자 회원 번호
    title VARCHAR2(150) NOT NULL,                  -- 게시글 제목
    category VARCHAR2(10) NOT NULL,                -- 구분 (lost:분실, found:습득)
    CONTENTS CLOB,                                 -- 상세 상황 설명
    lphone VARCHAR2(100),                          -- 연락처
    writer VARCHAR2(50),                           -- 작성자명
    litem VARCHAR2(200),                           -- 분실 물건
    reip VARCHAR2(100),                            -- 작성자 IP
    status VARCHAR2(50),                           -- 처리 현황 (발견 여부)
    itemcategory VARCHAR2(200),                    -- 물품 카테고리 (가방, 전자기기 등)
    losttime DATE,                                 -- 실제 물건을 잃어버린/주운 시각
    lostloc VARCHAR2(255),                         -- 분실 장소
    ldate DATE,                                    -- 게시글 등록 시각
    CONSTRAINT lost_code_fk FOREIGN KEY(mnum) REFERENCES MEMBER(mnum)
);
CREATE SEQUENCE lost_seq INCREMENT BY 1 START WITH 1;

-- [lostimgs] 분실물의 외관 확인을 위한 다중 이미지 관리 (1:N)
CREATE TABLE lostimgs(
    lostimgsid NUMBER,                             -- 작성 게시글 번호
    imgname VARCHAR2(255),                         -- 저장된 이미지 경로 및 파일명
    CONSTRAINT lostimgsid_fk FOREIGN KEY(lostimgsid) REFERENCES LOST(lnum) ON DELETE CASCADE
);
CREATE SEQUENCE lost_comm_seq INCREMENT BY 1 START WITH 1;

-- [lost_comments] 분실물 제보 및 소통을 위한 전용 댓글
CREATE TABLE lost_comments(
    lcnum NUMBER NOT null PRIMARY key,             -- 댓글 번호
    lnum NUMBER,                                   -- 분실물 게시글 번호
    mnum NUMBER,                                   -- 댓글 작성자 번호
    writer VARCHAR2(34) NOT NULL,                  -- 작성자 닉네임
    content VARCHAR2(400) NOT null,                -- 제보 내용
    lcdate DATE DEFAULT SYSDATE,                   -- 작성 시각
    CONSTRAINT lost_comments_lnum_fk FOREIGN KEY(lnum) REFERENCES lost(lnum) ON DELETE CASCADE,
    CONSTRAINT lost_comments_mnum_fk FOREIGN KEY(mnum) REFERENCES MEMBER(mnum) ON DELETE CASCADE
);

CREATE SEQUENCE lost_comm_seq INCREMENT BY 1 START WITH 1;

/*******************************************************************************
 * 4. 부가 서비스 (Social & Utility)
 *******************************************************************************/

-- [friend_req] 사용자 간 친구 신청
CREATE TABLE friend_req(
    frid NUMBER NOT null PRIMARY key,              -- 신청 요청 고유 번호
    req_id VARCHAR2(50) NOT null,                  -- 신청을 보낸 유저 아이디
    receiver_id VARCHAR2(50) NOT null,             -- 신청을 받은 유저 아이디
    status VARCHAR2(20) DEFAULT 'pending',         -- 진행 상태 (pending, accept, reject)
    frdate DATE DEFAULT sysdate,                   -- 요청 전송 시각
    CONSTRAINT requester_fk FOREIGN KEY (req_id) REFERENCES MEMBER(mid),
    CONSTRAINT receiver_fk FOREIGN KEY (receiver_id) REFERENCES MEMBER(mid),
    CONSTRAINT friend_uq UNIQUE (req_id, receiver_id) -- 동일 유저간 중복 요청 금지
);

CREATE SEQUENCE friend_req_seq START WITH 1 INCREMENT BY 1; --각 행의 고유한 번호를 주기 위해 시퀀스 생성

-- [friend_req_log] 친구 요청에 대한 처리 이력 기록
CREATE TABLE friend_req_log (
    log_id NUMBER NOT NULL PRIMARY KEY,            -- 로그 식별 번호
    friend_req_id NUMBER NOT null,                 -- 관련 요청 ID (FK)
    status VARCHAR2(20),                           -- 변경된 상태 (수락됨/거절됨)
    action_time TIMESTAMP DEFAULT SYSTIMESTAMP,    -- 액션 발생 시각
    CONSTRAINT log_to_request_fk FOREIGN KEY (friend_req_id) REFERENCES friend_req(frid) ON DELETE CASCADE
);

CREATE SEQUENCE friend_req_log_seq INCREMENT BY 1 START WITH 1;--각 행의 고유한 번호를 주기 위해 시퀀스 생성

-- [todo] 사용자별 개인 할 일 목록 관리
CREATE TABLE todo(
    tnum NUMBER NOT NULL PRIMARY KEY,              -- Todo 고유 번호
    tid VARCHAR2(50) NOT null,                     -- 작성자 아이디 (Member 참조)
    tdate DATE DEFAULT SYSDATE,                    -- 할 일 수행 예정일 또는 작성일
    description VARCHAR2(200) NOT NULL,            -- 할 일 상세 내용
    CONSTRAINT todo_tnum_FK FOREIGN KEY(tid) REFERENCES MEMBER(mid)
);

 CREATE SEQUENCE todo_num_seq INCREMENT BY 1 START WITH 1;
 
/*******************************************************************************
 * 5. 다이어리 시스템 (Diary & Gallery)
 *******************************************************************************/

-- [diarylist] 사용자의 다이어리 게시글 기본 정보 저장소
CREATE TABLE diarylist (
    num NUMBER           CONSTRAINT diarylist_num_pk PRIMARY KEY, -- 다이어리 고유 번호 (PK)
    writer VARCHAR2(10),                                          -- 작성자 아이디 (Member 참조)
    udate DATE,                                                   -- 다이어리 작성/수정일
    CONSTRAINT diarylist_writer_FK FOREIGN KEY(writer) REFERENCES MEMBER(mid)
);
-- diarylist의 num 번호 생성을 위한 시퀀스
CREATE SEQUENCE diarylist_seq INCREMENT BY 1 START WITH 1;

-- [diarypages] 다이어리별 상세 페이지 및 이미지 관리 (1:N)
CREATE TABLE diarypages (
    dnum NUMBER,                -- 부모 다이어리 번호 (FK)
    image VARCHAR2(250) NOT NULL, -- 저장된 다이어리 이미지 경로 및 파일명
    CONTENT VARCHAR2(600),      -- 해당 페이지의 일기 본문 내용
    CONSTRAINT diarypages_dnum_FK FOREIGN KEY(dnum) REFERENCES diarylist(num) ON DELETE CASCADE
);

/*******************************************************************************
 * 4. 설문 조사 시스템 (Survey System)
 *******************************************************************************/

-- [SURVEY] 설문 조사 관리
CREATE TABLE SURVEY(
    snum NUMBER CONSTRAINT survey_num_pk PRIMARY key, -- 설문 고유 번호
    mnum NUMBER,                                   -- 만든이 회원 번호
    code NUMBER,                                   -- 설문 분류 코드
    fin VARCHAR2(20),                              -- 설문 종료 여부
    sdate DATE,                                    -- 설문 시작일/등록일
    sub VARCHAR2(255),                             -- 설문 질문
    CONSTRAINT lost_mnum_fk FOREIGN KEY (mnum) REFERENCES MEMBER(mnum) ON DELETE CASCADE
);

-- 설문 번호 자동 생성을 위한 시퀀스
CREATE SEQUENCE survey_seq INCREMENT BY 1 START WITH 1;


-- [surveycontent] 설문 문항 및 개별 항목 상세 관리 (1:N)
CREATE TABLE surveycontent(
    subcode NUMBER(19) NOT null,                   -- 소속된 설문 번호 (FK)
    surveycnt NUMBER(10),                          -- 해당 항목의 득표 수
    surveytitle VARCHAR2(255),                     -- 질문 내용
    surveytype VARCHAR2(255),                      -- 문항 타입 (객관식, 주관식 등)
    CONSTRAINT surveycontent_surveycontent_fk FOREIGN KEY (subcode) REFERENCES survey(snum) ON DELETE CASCADE
);


-- [survey_history] 사용자의 설문 참여 이력 및 중복 투표 방지
CREATE TABLE survey_history (
    hnum NUMBER CONSTRAINT survey_history_pk PRIMARY key, -- 참여 이력 고유 번호
    snum NUMBER NOT NULL,                          -- 참여한 설문 번호
    mnum NUMBER NOT NULL,                          -- 참여한 회원 번호
    vote_date DATE DEFAULT SYSDATE,                -- 투표 일시
    CONSTRAINT survey_history_snum_fk FOREIGN KEY (snum) REFERENCES SURVEY(snum) ON DELETE cascade,
    CONSTRAINT survey_history_mnum_fk FOREIGN KEY (mnum) REFERENCES MEMBER(mnum) ON DELETE CASCADE,
    CONSTRAINT survey_history_unique UNIQUE(snum, mnum) -- 동일 설문에 대한 중복 참여 방지
);

-- 참여 이력 번호 자동 생성을 위한 시퀀스
CREATE SEQUENCE survey_history_seq INCREMENT BY 1 START WITH 1;

--AOP를 사용해서 로그인 정보 저장
 * */
CREATE TABLE login_logger (
    num NUMBER CONSTRAINT logger_pk PRIMARY key,
    idn VARCHAR2(50),
    reip VARCHAR2(50),
    uagent VARCHAR2(100),
    status VARCHAR2(50),
    sstime DATE DEFAULT SYSDATE,
    CONSTRAINT logger_member_id_fk FOREIGN KEY (idn) REFERENCES MEMBER(mid) ON DELETE CASCADE
);
CREATE SEQUENCE myloginlogs_seq INCREMENT BY 1 START WITH 1;
```
</details>