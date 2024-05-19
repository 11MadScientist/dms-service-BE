create or replace trigger TR_USER_PROFILE_ID
before insert on USER_PROFILE
for each row
begin
  select SEQ_USER_PROFILE_ID.nextval
  into :new.ID
  from dual;
end;