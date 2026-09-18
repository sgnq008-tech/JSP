select*from tab;

create table tempmember(
id varchar2(20) not null,
passwd varchar2(20),
name varchar2(20),
mem_num1 varchar2(6),
mem_num2 varchar2(7),
e_mail varchar2(30),
phone varchar2(30),
zipcode varchar2(7),
address varchar2(60),
job varchar2(30),
primary key(id)
);

insert into TEMPMEMBER values('aaaa','1111','가길동','123456','7654321','ga@naver.com',
'02-12314-1234','100-100','서울특별시 영등포구 영등포동 포동리','백수');

insert into TEMPMEMBER values('bbbb','2222','나길동','234567','8765432','na@naver.com',
'02-12314-2222','200-200','전라남도 구례군 지리산','등산가');

insert into TEMPMEMBER values('cccc','3333','나길동','234567','8765432','da@naver.com',
'064-12314-2222','300-300','제주시 한라동 한라산','백수 카이도우');

select*from TEMPMEMBER;

create table student (
id varchar2(12) not null, 
pass varchar2(12) not null,
name	varchar2(10) not null,
phone1  varchar2(3) not null,  
phone2  varchar2(4) not null, 
phone3  varchar2(4) not null, 
email   varchar2(30) not null,
zipcode varchar2(7) not null,
address1 varchar2(120) not null,
address2 varchar2(50) not null
);

create table zipcode (
seq   number(5) not null,
zipcode  varchar2(7) not null,
sido     varchar2(6) not null,
gugun 	 varchar2(30) not null,
dong 	 varchar2(50) not null,
ri		 varchar2(80),
bunji		 varchar2(80),
constraint ZIPCODE_PK primary key(seq)
);

show user;

BEGIN

FOR i IN 1..50 LOOP

INSERT INTO board (num, writer, email, subject, pass, readcount, ref, step, depth, regdate, content, ip)

VALUES (

board_seq.NEXTVAL, -- num: 시퀀스를 사용하여 자동 증가

'user' || i, -- writer: user1, user2, ..., user50

'user' || i || '@example.com', -- email: user1@example.com, user2@example.com, ..., user50@example.com

'Subject ' || i, -- subject: Subject 1, Subject 2, ..., Subject 50

'pass' || i, -- pass: pass1, pass2, ..., pass50

0, -- readcount: 0

0, -- ref: 0

0, -- step: 0

0, -- depth: 0

sysdate, -- regdate: 현재 날짜

'Content for board ' || i, -- content: Content for board 1, Content for board 2, ..., Content for board 50

'192.168.1.' || i -- ip: 192.168.1.1, 192.168.1.2, ..., 192.168.1.50

);

END LOOP;

COMMIT; -- 변경 사항을 저장

END;

select * from board;