create table role
(
    id   bigint not null primary key,
    name varchar(255)
);

create table user_credential
(
    id       bigint not null primary key,
    password varchar(255),
    username varchar(255)
);

create table user_credential_roles
(
    user_credential_id bigint not null,
    role_id            bigint not null,
    foreign key (user_credential_id) REFERENCES user_credential (id),
    foreign key (role_id) references role (id),
    primary key (user_credential_id, role_id)
);

create table banners (
    id bigint not null primary key auto_increment,
    name varchar(50) not null,
    deleted bit(1) not null
);

create table categories (
    id bigint not null primary key auto_increment,
    name varchar(50) not null,
    requestID varchar(50) not null,
    deleted bit(1) not null
);

INSERT INTO role (id, name)
VALUES (1, 'ROLE_USER');
INSERT INTO role (id, name)
VALUES (2, 'ROLE_ADMIN');

INSERT INTO user_credential(id, username, password)
##password = 'user'
VALUES (1, 'user', '$2y$11$IHcD/aGGOwQcCvwLS.e3tuP4ObWNWBStsjnv4qEaokO5daRPmrmxi');
Insert into user_credential(id, username, password)
##password = 'admin'
values (2,'admin','$2y$11$8ERFgmpE5vMdeiETXHQYHOB8TQ5j.ypyX7ZJntEQ0MIpDLYmzNDou');

INSERT INTO user_credential_roles VALUES (1,1);
INSERT INTO user_credential_roles VALUES (2,2);





DELIMITER $$
create trigger update_banners_names
    before update
    on banners
    for each row
begin
    declare unique_name varchar(40);
    declare b boolean default 0;
    declare unique_name_cur cursor for
        Select name from banners where deleted = false and NEW.name = name and NEW.id != banner;
    DECLARE EXIT HANDLER FOR NOT FOUND Set b = 1;
    open unique_name_cur;
    repeat
        fetch unique_name_cur into unique_name;
        if NEW.deleted = false and not b then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Names of not deleted banners should be unique';
        end if;
    until b end repeat;
    close unique_name_cur;
end$$
DELIMITER ;

DELIMITER $$
create trigger insert_banners_names
    before insert
    on banners
    for each row
begin
    declare unique_name varchar(40);
    declare b boolean default 0;
    declare unique_name_cur cursor for
        Select name from banners where deleted = false and NEW.name = name;
    DECLARE EXIT HANDLER FOR NOT FOUND Set b = 1;
    open unique_name_cur;
    repeat
        fetch unique_name_cur into unique_name;
        if NEW.deleted = false and not b then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Names of not deleted banners should be unique';
        end if;
    until b end repeat;
    close unique_name_cur;
end$$
DELIMITER ;


DELIMITER $$
create trigger insert_categories_names
    before insert
    on categories
    for each row
begin
    declare unique_name varchar(40);
    declare b boolean default 0;
    declare unique_name_cur cursor for
        Select categories.name from categories where deleted = false and NEW.name = BINARY categories.name;
    DECLARE EXIT HANDLER FOR NOT FOUND Set b = 1;
    open unique_name_cur;
    repeat
        fetch unique_name_cur into unique_name;
        if NEW.deleted = false and not b then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Names of not deleted categories should be unique';
        end if;
    until b end repeat;
    close unique_name_cur;
end$$
DELIMITER ;

DELIMITER $$
create trigger insert_categories_requestIDs
    before insert
    on categories
    for each row
begin
    declare unique_requestID varchar(40);
    declare b boolean default 0;
    declare unique_requestID_cur cursor for
        Select categories.requestID from categories where deleted = false and NEW.requestID = BINARY categories.requestID;
    DECLARE EXIT HANDLER FOR NOT FOUND Set b = 1;
    open unique_requestID_cur;
    repeat
        fetch unique_requestID_cur into unique_requestID;
        if NEW.deleted = false and not b then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'RequestID of not deleted categories should be unique';
        end if;
    until b end repeat;
    close unique_requestID_cur;
end$$
DELIMITER ;

DELIMITER $$
create trigger update_categories_names
    before update
    on categories
    for each row
begin
    declare unique_name varchar(40);
    declare b boolean default 0;
    declare unique_name_cur cursor for
        Select categories.name from categories where deleted = false and NEW.name = BINARY categories.name and NEW.id != categories.id;
    DECLARE EXIT HANDLER FOR NOT FOUND Set b = 1;
    open unique_name_cur;
    repeat
        fetch unique_name_cur into unique_name;
        if NEW.deleted = false and not b then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Names of not deleted categories should be unique';
        end if;
    until b end repeat;
    close unique_name_cur;
end$$
DELIMITER ;

DELIMITER $$
create trigger update_categories_requestIDs
    before update
    on categories
    for each row
begin
    declare unique_requestID varchar(40);
    declare b boolean default 0;
    declare unique_requestID_cur cursor for
        Select categories.requestID
        from categories
        where deleted = false
          and NEW.requestID = BINARY categories.requestID
          and NEW.id != categories.id;
    DECLARE EXIT HANDLER FOR NOT FOUND Set b = 1;
    open unique_requestID_cur;
    repeat
        fetch unique_requestID_cur into unique_requestID;
        if NEW.deleted = false and not b then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'RequestID of not deleted categories should be unique';
        end if;
    until b end repeat;
    close unique_requestID_cur;
end$$
DELIMITER ;