create table DMS_ADAPTER_INFO (
  ADAPTER_NAME varchar2 (300 char) not null,
  USER_ID number(10) not null,
  EXTENDED_AUTH_INFO clob null,
  AUTH_INFO clob not null,
  MISC_INFO clob null,
  constraint DMS_ADAPTER_INFO$PK
    primary key (ADAPTER_NAME, USER_ID)
);  
commit;