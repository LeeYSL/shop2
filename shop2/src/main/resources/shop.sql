create table useraccount (
   userid varchar(10) primary key,
   password varchar(15),
   username varchar(20),
   phoneno varchar(20),
   postcode varchar(7),
   address varchar(30),
   email varchar(50),
   birthday datetime
);

drop table sale;

CREATE TABLE sale ( --주문 테이블(주문정보)
	saleid int PRIMARY KEY,--주문번호
	userid varchar(10) NOT NULL,--주문 고객 아이디
	saledate DATETIME ,--주문일자
	foreign KEY (userid) REFERENCES useraccount (userid)
);

drop table saleitem; --주문 상품 테이블
CREATE TABLE saleitem (

	saleid int ,
	seq int ,
	itemid int NOT NULL,
	quantity int,
	PRIMARY KEY (saleid, seq),
	foreign key (saleid) references sale (saleid),
	foreign key (itemid) references item (id)
);
create table board (
   num int primary key, --게시글 번호. 기본키
   writer varchar(30), -- 작성자 이름
   pass varchar(20), -- 비밀번호
   title varchar(100), --글 제목
   content varchar(2000),  --글내용
   file1 varchar(200), --첨부파일 이름
   boardid varchar(2), --게시물종류 1.공지사항 2.자유게시판 3.Q&A
   regdate datetime, --등록일시
   readcnt int(10), --조회수, 상세보기 시 1씩 증가
   grp int,         --답글 작성 시 원글의 게시글 번호
   grplevel int(3), --답글의 레벨.
   grpstep  int(5)  --해당 그룹의 출력 순서
   );
   
   create table board (
   num int primary key, 
   writer varchar(30), 
   pass varchar(20), 
   title varchar(100), 
   content varchar(2000), 
   file1 varchar(200),
   boardid varchar(2), 
   regdate datetime, 
   readcnt int(10), 
   grp int,         
   grplevel int(3), 
   grpstep  int(5)  
   );
   
   select * from board;