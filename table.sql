CREATE TABLE `cert_message` ( -- 인증 메세지 테이블 --
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(30) NOT NULL,
  `content` text NOT NULL,
  `cert_num` varchar(100) DEFAULT NULL,
  `phone_num` varchar(30) NOT NULL,
  `cert_yn` tinyint(1) NOT NULL DEFAULT 0,
  `create_dt` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`idx`)
)

CREATE TABLE `file` ( -- 공통 파일 테이블 --
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `file_group_no` int(11) NOT NULL,
  `url` text NOT NULL,
  `file_ori_nm` varchar(200) NOT NULL,
  `file_sys_nm` varchar(100) NOT NULL,
  `extension` varchar(30) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `file_size` mediumtext NOT NULL,
  `file_size_unit` varchar(10) NOT NULL,
  `create_dt` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`idx`)
);

CREATE TABLE member (   -- 회원 테이블 --
  `m_username` VARCHAR(100) NOT NULL,
  `m_pass` VARCHAR(100) NOT NULL,
  `m_name` VARCHAR(20) NOT NULL,
  `m_gender`  char(1) not NULL,
  `m_nickname` VARCHAR(20) NOT NULL,
  `m_birth` date NOT NULL,
  `m_phone` varchar(20) NOT NULL,
  `m_addr` VARCHAR(100) NOT NULL,
  `m_email` VARCHAR(20) NOT NULL,
  `m_category` VARCHAR(20) default '개인',
  `m_enabled` tinyint(1) DEFAULT TRUE,
  `m_account_non_lock` tinyint(1) DEFAULT TRUE,
  `m_account_non_expired` tinyint(1) DEFAULT TRUE,
  `m_pass_fail_count` int(11) DEFAULT 0,
  `m_refresh_token` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`m_username`)
);

CREATE TABLE `authorities` (
  `username` varchar(100) NOT NULL,
  `authority` varchar(100) NOT NULL,
  PRIMARY KEY (`username`,`authority`)
);

CREATE TABLE `token_black` (
  `username` varchar(50) NOT NULL,
  `refresh_token` varchar(200) NOT NULL,
  `create_dt` datetime NOT NULL DEFAULT current_timestamp()
);
  
CREATE TABLE `company` (
  `c_pk_num` int(11) NOT NULL AUTO_INCREMENT COMMENT '영업장 PK',
  `c_cnum` varchar(100) DEFAULT NULL COMMENT '사업자번호',
  `c_fk_id` varchar(20) DEFAULT NULL COMMENT '영업장 주인 회원 아이디',
  `c_name` varchar(30) DEFAULT NULL COMMENT '영업장 이름',
  `c_phone` varchar(20) DEFAULT NULL COMMENT '영업장 연락처',
  `c_category` varchar(10) DEFAULT NULL COMMENT '영업장 분류',
  `c_addr` varchar(200) DEFAULT NULL COMMENT '영업장 주소',
  `c_contents` text DEFAULT NULL COMMENT '영업장 소개글',
  `c_condition` tinyint(1) DEFAULT NULL COMMENT '영업장 노출여부',
  `c_check` tinyint(1) DEFAULT NULL COMMENT '영업장 승인여부',
  `c_img` varchar(100) DEFAULT NULL COMMENT '업체 대문 이미지',
  `c_type` varchar(30) DEFAULT NULL COMMENT '업체 종목',
  `c_lat` double DEFAULT NULL COMMENT '위도',
  `c_lon` double DEFAULT NULL COMMENT '경도',
  `c_file_group_no` varchar(100) DEFAULT NULL COMMENT '파일 그룹번호',
  PRIMARY KEY (`c_pk_num`),
  KEY `company_FK` (`c_fk_id`),
  CONSTRAINT `company_FK` FOREIGN KEY (`c_fk_id`) REFERENCES `member` (`m_username`)
)

CREATE TABLE cimg ( -- 업체 게시글 사진 테이블 --
  `ci_pk_num` INT NOT NULL AUTO_INCREMENT,
  `ci_fk_num` INT DEFAULT NULL,
  `ci_oriname` VARCHAR(100),
  `ci_sysname` VARCHAR(100),
  PRIMARY KEY (`ci_pk_num`),
  FOREIGN KEY (ci_fk_num) REFERENCES company(c_pk_cnum)
  );

CREATE TABLE `product` (
  `idx` int(11) NOT NULL AUTO_INCREMENT COMMENT '상품 PK',
  `p_fk_cnum` int(11) NOT NULL COMMENT '상품업체 일련번호',
  `p_name` varchar(100) NOT NULL COMMENT '상품명',
  `p_price` int(11) NOT NULL COMMENT '상품비용',
  `p_content` text DEFAULT NULL COMMENT '상품 설명',
  `p_reserver_status` tinyint(1) NOT NULL COMMENT '예약상품인지 여부',
  `p_exposure` tinyint(1) NOT NULL DEFAULT 0 COMMENT '상품 노출여부',
  `file_group_no` int(11) DEFAULT NULL COMMENT '파일 그룹번호',
  `create_dt` datetime NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
  `update_dt` datetime NOT NULL DEFAULT current_timestamp() COMMENT '수정일시',
  PRIMARY KEY (`idx`),
  KEY `product_FK` (`p_fk_cnum`),
  CONSTRAINT `product_FK` FOREIGN KEY (`p_fk_cnum`) REFERENCES `company` (`c_pk_num`)
);
-- jeju.schudule definition

CREATE TABLE `schudule` (
  `idx` int(11) NOT NULL AUTO_INCREMENT COMMENT '일정테이블 PK',
  `sc_fk_product_num` int(11) NOT NULL COMMENT '상품번호',
  `sc_start_date_time` datetime NOT NULL COMMENT '예약 시작 날짜 및 시간',
  `sc_end_date_time` datetime NOT NULL COMMENT '예약 끝 날짜 및 시간',
  `create_dt` varchar(100) NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
  `sc_fk_order_num` int(11) DEFAULT NULL COMMENT '주문 일련번호',
  PRIMARY KEY (`idx`)
)
  
  CREATE TABLE review ( -- 리뷰 테이블 -- 
  `rv_pk_num` INT NOT NULL AUTO_INCREMENT,
  `rv_fk_cnum` varchar(100) NOT NULL,
  `rv_fk_id` VARCHAR(20) NOT NULL,
  `rv_img` VARCHAR(45),
  `rv_date` TIMESTAMP(6) NOT NULL,
  `rv_contents` VARCHAR(500) NOT NULL,
  `rv_star` INT default 5,
  `rv_answer` text,
  `rv_updatedate` datetime,
  `rv_check` tinyint(1),
  `file_group_no` int(11) DEFAULT 0
  PRIMARY KEY (`rv_pk_num`),
  FOREIGN KEY (rv_fk_id) REFERENCES member(m_username) ,
  FOREIGN KEY (rv_fk_cnum) REFERENCES company(c_pk_cnum) 
  );

  CREATE TABLE loption ( -- 숙박 옵션 테이블, DTO는 모두 기본 false로 설정해서 클릭된 값만 true로 insert할거임 -- 
  `L_Fk_cnum` INT(100) NOT NULL,
  `L_Cityview` BOOLEAN,
  `L_Seeview` BOOLEAN,
  `L_Skyview` BOOLEAN,
  `L_Outdoor` BOOLEAN,
  `L_Petok` BOOLEAN,
  `L_Bbq` BOOLEAN,
  `L_Cooking` BOOLEAN,
  `L_Pool` BOOLEAN,
  `L_Spa` BOOLEAN,
  `L_Wallpool` BOOLEAN,
  `L_Garaoke` BOOLEAN,
  FOREIGN KEY (L_Fk_cnum) REFERENCES company(C_pk_cnum)  
  );
  
CREATE TABLE room ( -- 방(숙박아이템) 테이블 -> 예약로직이후 만들어야겠당 -- 
  `r_pk_num` INT NOT NULL AUTO_INCREMENT,
  `r_fk_cnum` INT NOT NULL,
  `r_name` VARCHAR(50) NOT NULL,
  `r_img` VARCHAR(45) ,
  `r_contents` TEXT NOT NULL,
  `r_price` INT default 0,
  `r_in` VARCHAR(10),
  `r_out` VARCHAR(10),
  `r_hidden` BOOLEAN default 0,
  PRIMARY KEY (`r_pk_num`),
  FOREIGN KEY (r_fk_cnum) REFERENCES company(c_pk_cnum)  
  );
  
CREATE TABLE rimg ( -- 방(숙박아이템) 사진 테이블 --
  `ri_pk_num` INT NOT NULL AUTO_INCREMENT,
  `ri_fk_num` INT NOT NULL,
  `ri_oriname` VARCHAR(100),
  `ri_sysname` VARCHAR(100),
  PRIMARY KEY (`ri_pk_num`),
  FOREIGN KEY (ri_fk_num) REFERENCES room(r_pk_num)
  );
  
CREATE TABLE Aactivity ( -- 레저 상품 --
  `AT_pk_num` INT NOT NULL AUTO_INCREMENT,
  `AT_fk_cnum` INT NOT NULL,
  `AT_name` VARCHAR(50) NOT NULL,
  `AT_img` VARCHAR(45),
  `AT_contents` text NOT NULL,
  `AT_price` INT default 0,
  `AT_in` VARCHAR(10),
  `AT_out` VARCHAR(10),
  `AT_rtch` BOOLEAN default false,
  `AT_hidden` boolean default 0,
  PRIMARY KEY (`AT_pk_num`),
  FOREIGN KEY (AT_fk_cnum) REFERENCES company(C_pk_cnum)  
);
  
  CREATE TABLE aoption ( -- 레저 옵션 --
  `A_fk_cnum` INT(100) NOT NULL,
  `a_waterpark` BOOLEAN,
  `a_spa` BOOLEAN,
  `a_surfing` BOOLEAN,
  `a_snowcooling` BOOLEAN,
  `a_rafting` BOOLEAN,
  `a_aquarium` BOOLEAN,
  `a_flowers` BOOLEAN,
  `a_zoo` BOOLEAN,
  `a_shooting` BOOLEAN,
  `a_horse` BOOLEAN,
  `a_paragliding` BOOLEAN,
  `a_zipline` BOOLEAN,
  `a_bike` BOOLEAN,
  `a_cart` BOOLEAN,
  `a_atv` BOOLEAN,
  `a_tourpass` BOOLEAN,
  `a_yort` BOOLEAN,
  `a_shipfishing` BOOLEAN,
  `a_cablecar` BOOLEAN,
  `a_showpainting` BOOLEAN,
  `a_fishinglounge` BOOLEAN,
  `a_show` BOOLEAN,
  FOREIGN KEY (A_fk_cnum) REFERENCES company(C_pk_cnum)
);



create table aimg ( -- 레저 상품 사진 --
  `ai_pk_num` int not NULL auto_increment,
  `ai_fk_num` int not NULL,
  `ai_oriname` varchar(100) not NULL,
  `ai_sysname` varchar(100) not NULL,
  primary key (`ai_pk_num`),
  foreign key (ai_fk_num) references aactivity(at_pk_num)
);
  
  CREATE TABLE travelroute ( -- 여행 테이블 --
  `tr_pk_num` INT NOT NULL AUTO_INCREMENT,
  `tr_fk_id` VARCHAR(50) NOT NULL,
  `tr_title` VARCHAR(20) NOT NULL,
  `tr_in` date not NULL,
  `tr_out` date not NULL,
  `tr_relationship` varchar(30) not NULL,
  `tr_tf` boolean default false,
  PRIMARY KEY (`tr_pk_num`),
  FOREIGN KEY (tr_fk_id) REFERENCES member(m_username)
  );
  
CREATE TABLE Blog ( -- 후기 테이블 --
  `b_pk_num` INT NOT NULL AUTO_INCREMENT,
  `b_fk_tnum` INT NOT NULL,
  `b_fk_id` VARCHAR(50) NOT NULL,
  `b_img` VARCHAR(50),
  `b_create_dt` datetime DEFAULT NOW(),
  `b_title` VARCHAR(100) NOT NULL,
  `b_cost` INT default 0,
  `file_group_no` int DEFAULT NULL,
  `b_contents` TEXT NOT NULL,
  PRIMARY KEY (`b_pk_num`),
  FOREIGN KEY (b_fk_tnum) REFERENCES travelroute(tr_pk_num),  
  FOREIGN KEY (b_fk_id) REFERENCES member(m_username) 
);

CREATE TABLE BIMG ( -- 후기 사진 테이블 --
  `bi_pk_num` INT NOT NULL AUTO_INCREMENT,
  `bi_fk_num` INT NOT NULL,
  `bi_oriname` VARCHAR(50),
  `bi_sysname` VARCHAR(50),
  PRIMARY KEY (`bi_pk_num`),
  FOREIGN KEY (bi_fk_num) REFERENCES Blog(b_pk_num)
  );
  
CREATE TABLE Breply ( -- 후기 리뷰 --
  `br_pk_num` INT NOT NULL,
  `br_fk_num` INT NOT NULL,
  `br_fk_id` VARCHAR(20),
  `br_date` DATE,
  `br_contents` VARCHAR(500),
  PRIMARY KEY (`br_pk_num`),
  FOREIGN KEY (br_fk_id) REFERENCES member(m_username),
  FOREIGN KEY (br_fk_num) REFERENCES Blog(b_pk_num)
  );
  
  CREATE TABLE event ( -- 제주 행사 테이블 --
  `e_pk_enum` INT NOT NULL AUTO_INCREMENT,
  `e_date` datetime NOT NULL,
  `e_title` VARCHAR(20) NOT NULL,
  `e_addr` VARCHAR(20) NOT NULL,
  `e_eday` varchar(30) NOT NULL,
  `e_img` VARCHAR(40) not NULL,
  `e_info` text NOT NULL,
  `e_eventing` BOOLEAN,
  PRIMARY KEY (`e_pk_enum`)
  );
  
  CREATE TABLE eimg ( -- 제주행사 이미지 테이블 -- 
  `ei_pk_num` INT NOT NULL AUTO_INCREMENT,
  `ei_fk_num` INT NOT NULL,
  `ei_oriname` VARCHAR(40),
  `ei_sysname` VARCHAR(40),
  PRIMARY KEY (`ei_pk_num`),
  FOREIGN KEY (ei_fk_num) REFERENCES event(e_pk_enum)
);
  
CREATE TABLE spot ( -- 관광지 테이블 --
  `s_pk_num` INT NOT NULL AUTO_INCREMENT,
  `s_tittle` VARCHAR(100) NOT NULL,
  `s_addr` VARCHAR(100) NOT NULL,
  `s_info` text NOT NULL,
  `s_img` VARCHAR(100),
  `s_guide` text NOT NULL,
  PRIMARY KEY (`s_pk_num`)
  );
 
 CREATE TABLE simg ( -- 관광지 사진 테이블 --
  `si_pk_num` INT NOT NULL AUTO_INCREMENT,
  `si_fk_num` INT NOT NULL,
  `si_oriname` VARCHAR(40),
  `si_sysname` VARCHAR(40),
  PRIMARY KEY (`si_pk_num`),
  FOREIGN KEY (si_fk_num) REFERENCES spot(s_pk_num)
);

 CREATE TABLE sreiview ( -- 관광지 리뷰 --
  `sv_pk_num` INT NOT NULL AUTO_INCREMENT,
  `sv_fk_id` VARCHAR(20) NOT NULL,
  `sv_fk_num`INT NOT NULL,
  `sv_img` VARCHAR(40),
  `sv_contents` text NOT NULL,
  `sv_star` VARCHAR(20),
  `sv_date` date,
  PRIMARY KEY (`sv_pk_num`),
  FOREIGN KEY (sv_fk_id) REFERENCES member(m_username),
  FOREIGN KEY (sv_fk_num) REFERENCES spot(s_pk_num)
);


CREATE TABLE notice ( -- 공지사항 -- 
  `n_pk_num` INT NOT NULL AUTO_INCREMENT,
  `n_title` VARCHAR(200) NOT NULL,
  `n_contents` text NOT NULL,
  `n_views` INT default 0,
  `n_date` datetime NOT NULL,
  PRIMARY KEY (`n_pk_num`)
  );

create table complaint(  -- 건의사항 테이블 --
  `co_pk_conum` int not NULL auto_increment,
  `co_fk_id` varchar(30) not NULL,
  `co_title` varchar(50) not NULL,
  `co_contents` text not NULL,
  `co_create_dt` datetime not NULL,
  `co_check` boolean DEFAULT '0',
  `co_reply` text,
  `co_re_create_dt` datetime default NULL,
  primary key (`co_pk_conum`),
  foreign key (co_fk_id) references member(m_username)
);

create table police ( -- 신고테이블 --
  `pl_pk_num` int not NULL auto_increment,
  `pl_fk_id` varchar(30) not NULL,
  `pl_id` varchar(30) not NULL,
  `pl_title` varchar(100) not NULL,
  `pl_contents` text not NULL,
  `pl_category` varchar (20) not NULL,
  `pl_date` datetime not NULL,
  `pl_check` boolean default false,
  primary key(`pl_pk_num`),
  foreign key (pl_fk_id) references member(m_username)
);

CREATE TABLE basket ( -- 장바구니 테이블 --
  `bk_pk_num` INT NOT NULL AUTO_INCREMENT,
  `bk_fk_tnum` INT NOT NULL,
  `bk_fk_cnum` varchar(30) NOT NULL,
  `bk_fk_id` varchar(30) not NULL,
  `bk_fk_num` INT, -- 상품 번호 --
  `bk_goods` varchar(50),
  `bk_price` int,
  `bk_in` datetime,
  `bk_out` datetime,
  `bk_name` VARCHAR(10),
  `bk_number` varchar(20),
  `bk_carch` BOOLEAN,
  `bk_paych` boolean,
  `bk_paytime` datetime,
  `bk_people` int,
  `bk_create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`bk_pk_num`),
  FOREIGN KEY (bk_fk_tnum) REFERENCES travelroute(tr_pk_num),
  FOREIGN KEY (bk_fk_id) REFERENCES member(m_username),
  FOREIGN KEY (bk_fk_cnum) REFERENCES company(c_pk_cnum)
  );
  
  CREATE TABLE favorites ( -- 즐겨찾기 --
  `fa_pk_num` INT NOT NULL AUTO_INCREMENT,
  `fa_fk_id` VARCHAR(20),
  `fa_fk_cnum` INT,
  PRIMARY KEY (`fa_pk_num`),
  FOREIGN KEY (fa_fk_id) REFERENCES member(m_username),
  FOREIGN KEY (fa_fk_cnum) REFERENCES company(C_pk_cnum)
  );


INSERT INTO `company` (`c_cnum`, `c_fk_id`, `c_name`, `c_phone`, `c_category`, `c_addr`, `c_contents`, `c_condition`, `c_check`, `c_img`, `c_type`, `c_lat`, `c_lon`) VALUES
('1121363079', 'lkd9125', '인스밀', '050713525661', '식당', '제주 서귀포시 대정읍 일과대수로27번길 22 1층', NULL, 1, 0, '1655429412797.png', '일식', 33.2384787715141, 126.229230662407),
('1208643350', 'lkd9125', '생각하는정원', '0647723701', '레저', '제주시 한경면 녹차분재로 675', NULL, 1, 0, '1655426171915.jpeg', '실외스포츠', 33.3255844945227, 126.255005128834),
('1208796892', '영빡이', '제주 마레보 리조트', '016703689', '숙박', '제주특별자치도 제주시 애월읍 신엄안3길 135', NULL, 1, 0, '1655443049815.png', '리조트', 33.4706419288473, 126.354447319405),
('1570101020', 'lkd9125', '고집돌우럭 중문점', '050714081540', '식당', '제주 서귀포시 일주서로 879', NULL, 1, 0, '1655428401824.png', '한식', 33.2579833781002, 126.416707323322),
('2068689631', 'lkd9125', '새별ATV', '0647928283', '레저', '제주특별자치도 제주시 한림읍 금악리 산 31-2', NULL, 1, 0, '1655423282832.jpeg', '실외스포츠', 33.3496487834802, 126.360348578212),
('2119275765', 'lkd9125', '부두식당', '0647995201', '식당', '제주 제주시 애월읍 애월로 33', NULL, 1, 0, '1655428676390.png', '중식', 33.4618688341742, 126.312015522939),
('2148863581', '영빡이', '제주도파티애월 한담누리', '050350512727', '숙박', '제주특별자치도 제주시 애월읍 일주서로 6158', NULL, 1, 0, '1655443311684.png', '호텔', 33.4592739520701, 126.31238355628),
('2208796800', 'lkd9125', '아쿠아플라넷', '18337001', '레저', '제주 서귀포시 성산읍 섭지코지로 95 아쿠아플라넷 제주', NULL, 1, 0, '1655425915940.jpeg', '실내스포츠', 33.4329768585971, 126.927795526895),
('2208883511', '영빡이', '제주 미소 게스트하우스', '050350514892', '숙박', '제주특별자치도 제주시 관덕로 41 (일도1동)', NULL, 1, 0, '1655442988844.jpeg', '게스트하우스', 33.5134116307233, 126.523773316336),
('2690402264', '영빡이', '별헤는밤펜션', '016449565', '숙박', '제주특별자치도 서귀포시 성산읍 환해장성로 465', NULL, 1, 0, '1655442962028.png', '펜션/풀빌라', 33.3985906779922, 126.905191363026),
('2731300404', 'lkd9125', '세컨드스토리', '050713129953', '식당', '제주 제주시 구좌읍 월정1길 1', NULL, 1, 0, '1655428461464.png', '양식', 33.5520364527082, 126.790953164698),
('3238101162', 'lkd9125', '우도다이브 스노클링', '0642344988', '레저', '제주특별자치도 제주시 우도면 우도해안길 1040', NULL, 1, 1, '1655424430527.jpeg', '수상', 33.5031411872043, 126.967669756686),
('3328700460', '영빡이', '제주 홍스랜드 글램핑', '050350589735', '숙박', '제주특별자치도 제주시 애월읍 어음13길 25-45', NULL, 1, 0, '1655443020949.png', '글램핑/캠핑', 33.4028909767263, 126.347278604775),
('3550801190', 'lkd9125', '참나무장작구이김서방', '050714110465', '식당', '제주 제주시 한림읍 한림북동길 15 1층', NULL, 1, 0, '1655428430994.png', '한식', 33.4164288991149, 126.263991766618),
('3640500699', 'lkd9125', '신화월드 워터파크', '16708800', '레저', '서귀포시 안덕면 신화역사로304번길 38', NULL, 1, 0, '1655423882418.jpeg', '실외스포츠', 33.3049726791712, 126.31635202613),
('4707000478', 'lkd9125', '파라토도스', '050713104331', '식당', '제주 제주시 한림읍 금능길 87', NULL, 1, 0, '1655428523886.png', '양식', 33.3892978970444, 126.232304144907),
('4892200864', 'lkd9125', '제주공항그때그집', '050714239983', '식당', '제주 제주시 서해안로 352-1', NULL, 1, 0, '1655428370910.png', '한식', 33.5103869145724, 126.482766699224),
('5231400475', 'lkd9125', '산방산 탄산온천', '0647928300', '레저', '제주특별자치도 제주시 청사로 11, 4층 (도남동)', NULL, 1, 0, '1655424169982.jpeg', '실외스포츠', 33.4900919876057, 126.520158686169),
('5393400070', '영빡이', '서귀포 그림그리는펜션', '050350579110', '숙박', '제주특별자치도 서귀포시 성산읍 금백조로131번길 181', NULL, 1, 0, '1655443339707.png', '펜션/풀빌라', 33.4312118880671, 126.843807076302),
('5461800194', '영빡이', '제주 썸스테이풀빌라', '050350589217', '숙박', '제주특별자치도 제주시 한림읍 한수1길 33', NULL, 1, 0, '1655443074124.jpg', '펜션/풀빌라', 33.4220130717815, 126.262142885604),
('5684800361', 'lkd9125', '우무', '050713270064', '식당', '제주 제주시 한림읍 한림로 542-1', NULL, 1, 0, '1655429380797.png', '일식', 33.4059002309384, 126.25644378043),
('6072851264', 'lkd9125', 'seaside 로스터리', '050713089072', '식당', '제주 제주시 애월읍 하귀14길 5-3', NULL, 1, 0, '1655429444099.png', '카페', 33.485065884759, 126.407673083837),
('6161999595', '영빡이', '제주 그림 리조트', '050350589562', '숙박', '제주특별자치도 제주시 서해안로 620-1', NULL, 1, 0, '1655442937710.png', '리조트', 33.516184958784, 126.503483773385),
('6162688873', 'lkd9125', '숙성도 중문점', '050713312285', '식당', '제주 제주시 중앙로 280', NULL, 1, 0, '1655428653937.png', '중식', 33.4952999450673, 126.532779640986),
('6162928779', 'lkd9125', '마리조아', '050714015257', '식당', '제주 제주시 삼봉로 99', NULL, 1, 0, '1655428594572.png', '중식', 33.5124590132569, 126.589553678194),
('6163027330', '영빡이', '제주 빅터펜션', '050350573600', '숙박', '제주특별자치도 제주시 서해안로 340', NULL, 1, 0, '1655442899855.jpg', '펜션/풃리라', 33.509982634611, 126.481449272997),
('6163085039', '영빡이', '물뜰에쉼팡', '0647960505', '숙박', '제주특별자치도 제주시 한림읍 귀덕로 73', NULL, 1, 0, '1655443388488.jpg', '모텔', 33.4378491962181, 126.297678357692),
('6168166922', 'lkd9125', '㈜김녕요트투어', '0647825271', '레저', '제주특별자치도 제주시 구좌읍 구좌해안로 229-16', NULL, 1, 0, '1655379757328.png', '수상', 33.5582479162063, 126.7372597689),
('6352601033', 'lkd9125', '힐링파크 비체올린 카약체험', '0647730000', '레저', '제주특별자치도 제주시 한경면 판조로 253-6', NULL, 1, 0, '1655424613540.jpg', '수상', 33.3494476861153, 126.218190777807),
('7840401518', 'lkd9125', '명원가오메기떡집', '050713312285', '식당', '제주 제주시 중앙로 280', NULL, 1, 0, '1655428626624.png', '한식', 33.4952999450673, 126.532779640986),
('8556600430', 'lkd9125', '서귀포 다이브센터 다이빙 체험', '0643924566', '레저', '제주특별자치도 서귀포시 남원읍 하례망장포로 65-13', NULL, 1, 0, '1655424744626.jpg', '수상', 33.25975689827, 126.6395296858),
('8556600431', 'lkd9125', '도치돌렌치 알파카목장', '0645984544', '레저', '제주특별자치도 제주시 애월읍 도치돌길 293', NULL, 1, 0, '1655426739311.crdownload', '실외스포츠', 33.4099506881118, 126.36573023443),
('8931300270', 'lkd9125', '88버거', '0647338488', '식당', '제주 서귀포시 동문로 63 ', NULL, 1, 0, '1655428492385.png', '양식', 33.2492475189286, 126.567122293048);



INSERT INTO room VALUES ("0","1208796892" ,"제주 씨스테이 스파", '1655441729801.jpeg',"숙박입니다","170000","10:00","15:00",0);
INSERT INTO room VALUES ("0","1208796892" ,"첨처럼방", '1655441411918.webp',"주의주의주의사항","1700000","11:00","16:00",0);
INSERT INTO room VALUES ("0","1208796892" ,"육빔방", '1655441411918.webp',"주의주의주의사항","1700000","11:00","16:00",0);
INSERT INTO room VALUES ("0","1208796892" ,"냉면방", '1655441411918.webp',"주의주의주의사항","1700000","11:00","16:00",0);

INSERT INTO rimg VALUES (NULL,1,'flower.jpg',"flower.jpg");
INSERT INTO rimg VALUES (NULL,1,'nature.jpg',"nature.jpg");
INSERT INTO rimg VALUES (NULL,1,'fox.jpg','fox.jpg');
INSERT INTO rimg VALUES (NULL,1,'lightning.jpg',"lightning.jpg");
INSERT INTO rimg VALUES (NULL,1,'moon.jpg',"moon.jpg");
INSERT INTO loption VALUES (1208796892,1,1,1,1,1,1,1,1,1,1,1);

INSERT INTO event VALUES(NULL,now(),"제주 비행기 할인","제주공항","2022-06-20~2022-08-30","93817967_M_350.jpeg","모든 대상 할인 가능",1);
INSERT INTO event VALUES(NULL,now(),"전국 제주 데일리 특가","제주","2022-07-13~2022-07-13","vendor.webp","모든 대상 할인 가능",1);
INSERT INTO event VALUES(NULL,now(),"오션파크 해양레저 할인","제주 서귀포시 성산읍 해안맞이해안로","2022-06-13~2022-07-22","99E957385B05668E2D.jpeg","모든 대상 할인 가능",1);
INSERT INTO event VALUES(NULL,now(),"제주 보트","제주 쇠소깍 해양레저타운","2022-04-13~2022-06-13","99B1C6445B0A8B5B22.jpeg","모든 대상 할인 가능",1);
INSERT INTO event VALUES(NULL,now(),"제주해양레저보트","제주 우도 234-32","2022-06-13~2022-07-13","990887415B6034BC32.jpeg","모든 대상 할인 가능",1);

INSERT INTO spot VALUES (NULL,"한라산","제주 서귀포시 토평동 산15-1","대한민국 제주도 중앙부에 있는 해발 1,947.06m, 면적 약 1,820km²의 화산이다.","한라산.png","현재 한라산국립공원은 정부의 사회적 거리두기 방역조치에 따라 운영되고 있습니다.");
INSERT INTO spot VALUES(NULL, '주상절리', '제주특별자치도 서귀포시 중문동','마치 예리한 조각 칼로 섬세하게 깎아낸 듯한 4~6각형 형태의 기둥으로, 현무암질 용암류에 나타나는 수직 절리를 말한다.', '주상절리.png','이곳은 애완동물도 출입할 수 없으니 많은 양해 바랍니다.');
INSERT INTO spot VALUES(NULL, '만장굴', '제주특별자치도 제주시 구좌읍 만장굴길 182','계적인 규모의 용암동굴, 유네스코 지정 세계자연유산', '만장굴.jpg','오후 5시(17시)까지 입장으로 운영되고 있으니 방문에 참고바랍니다.');
INSERT INTO spot VALUES(NULL, '천지연폭포', '제주특별자치도 서귀포시 남성중로 2-15','천지연은 하늘과 땅이 만나 이루어진 연못이라는 의미를 담고 있는데, 폭포의 길이 22m, 그 아래 못의 깊이가 20m로, 가히 하늘과 땅이 만나는 연못이라 불린다.', '천지연폭포.jpg','실내 취식 금지, 마스크 착용 의무 등의 방역 수칙은 여전히 유효하오니 주의 바랍니다.');
INSERT INTO spot VALUES(NULL, '성산일출봉', '제주특별자치도 서귀포시 성산읍 일출로 284-12','제주도의 다른 오름들과는 달리 마그마가 물속에서 분출하면서 만들어진 수성화산체다.', '성산일출봉.jpg','7시~19시(매표마감 17시50분)로 조정하여 운영하고 있사오니 참고 바랍니다.');
INSERT INTO spot VALUES (NULL,"우도","제주 제주시 우도면","제주도의 동쪽 끝에 접하는 섬으로, 제주특별자치도에서 제주도 다음으로 큰 섬이다.","우도.jpg","현재 우도는 정부의 사회적 거리두기 방역조치에 따라 운영되고 있습니다.");
INSERT INTO spot VALUES (NULL,"함덕 해수욕장","제주 제주시 조천읍 조함해안로 525","바로 옆에 위치한 서우봉 때문에 함덕서우봉해변이라고도 불린다.","함덕 해수욕장.jpg","현재 함덕 해수욕장은 정부의 사회적 거리두기 방역조치에 따라 운영되고 있습니다.");
INSERT INTO spot VALUES (NULL,"협재 해수욕장","제주 제주시 한림읍 협재리 2497-1","물이 맑고 투명하기로 소문난 해수욕장이다.","협재해수욕장.jpg","개장기간은 매년 6월말에서 8월이고 입장료는 무료입니다. 120대 주차 가능하고 샤워실, 탈의실, 화장실, 급수대, 매점, 파라솔, 야영장 서비스 시설을 제공합니다.");
INSERT INTO spot VALUES (NULL,"제주 동문시장","제주 제주시 관덕로14길 20","제주도에서 가장 오래된 상설 재래시장이다.","제주동문시장.jpg","매일 08:00 ~ 21:00 영업합니다. 문의 사항이 있을 시 064-752-3001로 연락 바랍니다.");
INSERT INTO spot VALUES (NULL,"한림공원","제주 제주시 한림읍 한림로 300","동굴과 동물원이 있는 광대한 정원입니다.","한림공원.jpg","매일 09:00 ~ 17:50(매표 마감), 19:30까지 관람 가능합니다. ");
INSERT INTO spot VALUES (NULL,"카멜리아 힐","제주 서귀포시 안덕면 병악로 166","제주 자연을 담은 동백 정원 카멜리아 힐은 30년 열정과 사랑으로 제주의 자연을 담은, 동양에서 가장 큰 동백 수목원입니다.","카멜리아 힐.png","하절기 입장 시간은 08:30 ~ 19:00, 간절기 입장 시간은 08:30 ~ 18:30 이오니 참고 바랍니다.");
INSERT INTO spot VALUES (NULL,"용두암","제주 제주시 용두암길 15","용연 부근의 바닷가에 위치한 바위로, 높이는 10m이다. 바위 모습이 용머리와 비슷하여 용두암이라 불린다.","용두암.jpg","현재 용두암은 정부의 사회적 거리두기 방역조치에 따라 운영되고 있습니다.");
INSERT INTO spot VALUES (NULL,"오설록 티 뮤지엄","제주 서귀포시 안덕면 신화역사로 15 오설록","아모레퍼시픽이 차와 한국 전통차 문화를 소개하고 보급하고자 2001년 9월에 개관한 국내 최초의 차 박물관입니다.","오설록.jpg","매일 09:00 ~ 19:00 까지 운영하고 있사오니 참고 바랍니다.");
INSERT INTO spot VALUES (NULL,"성읍민속마을","제주 서귀포시 표선면 성읍리 3294","1984년 6월 12일 대한민국의 국가민속문화재 제188호 성읍 민속마을로 지정되었다가, 2017년 2월 28일 현재의 명칭으로 변경되었다.","성읍민속마을.jpg","매일 10:00 ~ 17:00 까지 운영하고 휴게 시간은 12:00 ~ 13:00 이오니 참고 바랍니다.");
INSERT INTO spot VALUES (NULL,"제주 유리의 성","제주 제주시 한경면 녹차분재로 462","세상 모든 것이 블링블링 빛나는 환상의 나라입니다. 제주의 자연과 유리가 조화를 이뤄 설계한 세계적인 유리테마파크 유리의 성!","유리의성.jpg","매일 09:00 ~ 18:00(매표 마감) 19:00 까지 운영합니다. 체험 관련 단체(11~30명) 20%할인, 31명 이상은 30% 할인됩니다.");
INSERT INTO spot VALUES (NULL,"김녕미로공원","제주 제주시 구좌읍 만장굴길 122","50여 마리의 고양이가 자유롭게 살고 있고 인생 사진 찍기 좋은 인생 포토존과 아이들이 좋아하는 친환경 놀이터도 마련되어 재미있는 미로찾기 외에도 다양한 재미를 느낄 수 있습니다.","김녕 미로공원.jpg","매일 09:00 ~ 17:05(매포 마감) 17:50 까지 운영합니다. 자세한 입장 마감 시간은 매표소에 문의해주시길 바랍니다.");
INSERT INTO spot VALUES (NULL,"제주 돌문화공원","제주 제주시 조천읍 남조로 2023 교래자연휴양림","첫째도 환경, 둘째도 환경, 셋째도 환경 제주의 정체성, 향토성, 예술성을 살려서 탐라의 형성과정, 탐라의 신화와 역사와 민족문화를 시대별로 총정리하여 가장 아름다운 교육의 공간으로 조성하였습니다.","돌문화공원.jpg","매일 09:00 ~ 18:00 운영하고 매월 첫째주 월요일은 휴무이므로 참고 바랍니다.");
INSERT INTO spot VALUES (NULL,"에코랜드","제주 제주시 조천읍 번영로 1278-169","에코랜드 테마파크와 에코랜드 골프&리조트으로 나뉜다. 테마파크는 기차를 타며 숲을 구경할 수 있게 되어 있으며 숲은 각각 4개의 역으로 구성되어있다. 기차를 타고 간이역으로 내려 관람할 수 있다.","에코랜드.jpg","매일 08:30 ~ 18:30 까지 운영합니다. 입장 마감은 주로 17:30분이나 일몰에 따라 변경하므로 문의 바랍니다.");
INSERT INTO spot VALUES (NULL,"본태박물관","제주 서귀포시 안덕면 산록남로762번길 69","전통 및 현대 미술과 공예품을 볼 수 있는 갤러리이다.","본태박물관.jpg","매일 10:00 ~ 18:00 까지 운영합니다.");
INSERT INTO spot VALUES (NULL,"제주세계 자연유산센터","제주 제주시 조천읍 선교로 569-36","제주도의 지질학적 역사를 다룬 체험형 전시관이다.","유산센터.jpg","일 09:00 ~ 18:00 까지 운영하고 매월 첫째 화요일, 설, 추석은 휴관이오니 참고 바랍니다.");




CREATE OR REPLACE VIEW iambasket AS
SELECT 
  bk_fk_cnum, 
  bk_fk_num, 
  bk_goods, 
  bk_price, 
  bk_name, 
  bk_paych, 
  bk_paytime, 
  bk_fk_id,
  c_category,
  c_name
FROM basket JOIN company
  ON bk_fk_cnum = c_pk_num;




CREATE OR REPLACE VIEW rev AS
SELECT 
  rv_pk_num, 
  rv_fk_cnum, 
  rv_fk_id, 
  rv_img, 
  rv_date, 
  rv_contents, 
  rv_star, 
  c_cnum, 
  c_name, 
  c_category
FROM review 
JOIN company
  ON rv_fk_cnum = c_pk_num;




CREATE OR REPLACE VIEW nowtrip AS
SELECT 
  bk_pk_num, 
  bk_fk_tnum, 
  bk_fk_cnum, 
  bk_fk_num, 
  bk_price, 
  bk_in, 
  bk_out, 
  bk_paych, 
  bk_fk_id, 
  bk_people, 
  tr_fk_id,
  tr_title,
  tr_pk_num, 
  tr_in, 
  tr_out, 
  tr_relationship, 
  tr_tf ,
  b_img, 
  b_fk_tnum , 
  b_fk_id
FROM basket 
JOIN travelroute 
JOIN Blog
  ON bk_fk_id = tr_fk_id 
  AND tr_fk_id = b_fk_id 
  AND tr_pk_num = b_fk_tnum 
  AND bk_fk_tnum = tr_pk_num;



CREATE OR REPLACE VIEW lastblog AS
SELECT 
  b_pk_num, 
  b_fk_tnum, 
  b_fk_id, 
  b_img, 
  b_create_dt, 
  b_title, 
  b_cost,
  b_contents, 
  bi_pk_num,
  bi_fk_num,
  bi_oriname,
  bi_sysname
FROM Blog 
JOIN BIMG 
  ON b_pk_num = bi_fk_num;



CREATE OR REPLACE VIEW pasttripblog AS
SELECT 
  b_pk_num, 
  b_fk_tnum, 
  b_fk_id, 
  b_img, 
  tr_title, 
  tr_in, 
  tr_out, 
  tr_tf 
FROM Blog 
JOIN travelroute 
  ON b_fk_tnum = tr_pk_num;


SELECT 
	*
FROM company c 
WHERE c.c_pk_cnum = '1208796892'
	

INSERT INTO cimg VALUES (NULL,3,'1655441729801.jpeg',"1655441729801.jpeg");
INSERT INTO cimg VALUES (NULL,3,'1655441729801.jpeg',"1655441729801.jpeg");
INSERT INTO cimg VALUES (NULL,3,'1655441729801.jpeg',"1655441729801.jpeg");
INSERT INTO cimg VALUES (NULL,3,'1655441729801.jpeg',"1655441729801.jpeg");




INSERT INTO aoption VALUES(28, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0);
INSERT INTO aoption VALUES(15, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO aoption VALUES(18, 0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO aoption VALUES(5, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0);
INSERT INTO aoption VALUES(12, 0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO aoption VALUES(29, 0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO aoption VALUES(31, 0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO aoption VALUES(8, 0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO aoption VALUES(2, 0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO aoption VALUES(32, 0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0);
INSERT INTO aoption VALUES(18, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);

INSERT INTO loption VALUES(7,1,0,1,0,0,0,1,0,0,0,0);
INSERT INTO loption VALUES(9,0,0,0,0,1,0,1,0,0,0,0);
INSERT INTO loption VALUES(10,0,0,0,0,0,1,1,0,1,0,0);
INSERT INTO loption VALUES(13,0,0,0,0,0,1,1,0,0,0,0);
INSERT INTO loption VALUES(19,0,0,0,0,1,0,1,0,1,0,0);
INSERT INTO loption VALUES(20,1,0,0,0,0,0,1,0,0,0,0);
INSERT INTO loption VALUES(23,0,1,1,0,0,0,1,1,0,1,1);
INSERT INTO loption VALUES(26,0,0,1,0,0,0,1,0,0,0,0);
INSERT INTO loption VALUES(27,1,0,0,0,0,0,0,0,0,0,1);



DROP TABLE favorites;
DROP TABLE basket;
DROP TABLE police;
DROP TABLE complaint;
DROP TABLE notice;
DROP TABLE sreiview;
DROP TABLE simg;
DROP TABLE spot;
DROP TABLE eimg;
DROP TABLE event;
DROP TABLE breply;
DROP TABLE bimg;
DROP TABLE blog;
DROP TABLE travelroute;
DROP TABLE aimg;
DROP TABLE aactivity;
DROP TABLE aoption;
DROP TABLE rimg;
DROP TABLE room;
DROP TABLE loption;
DROP TABLE cimg;
DROP TABLE review;
DROP TABLE company;
DROP TABLE member;