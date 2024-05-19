create table USER_PROFILE (
  ID number(10) not null,
  USERNAME varchar2 (300 char) unique not null,
  PASSWORD varchar2 (300 char) not null,
  FIRST_NAME varchar2(300 char) null,
  LAST_NAME varchar2 (300 char) null,
  EMAIL_ADDRESS varchar2 (300 char) not null,
  ROLE varchar2 (25 char) not null,
  DISABLED varchar2 (1 char) default 'N',
  constraints USER_PROFILE$PK
    primary key (ID)
);