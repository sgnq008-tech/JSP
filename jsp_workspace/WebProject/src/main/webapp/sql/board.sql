select * from tab;

drop table board;

create table board(

num        number(7) not null,
writer     varchar2(12) not null,
email      varchar2(40) not null,
subject    varchar2(50) not null,
pass       varchar2(10) not null,
readcount  number(5) default 0 not null,
ref        number(5) default 0 not null,
step       number(3) default 0 not null,
depth      number(3) default 0 not null,
regdate    timestamp(6) default sysdate not null,
content    varchar2(4000) not null,
ip         varchar2(20) not null,
constraint BOARD_PK primary key(num)
);

create sequence board_seq
start with 1
increment by 1
nomaxvalue
nocache
nocycle;

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