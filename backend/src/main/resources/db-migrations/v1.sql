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

INSERT INTO role (id, name)
VALUES (1, 'ROLE_USER');
INSERT INTO role (id, name)
VALUES (2, 'ROLE_ADMIN');

INSERT INTO user_credential(id, username, password)
##password = 'user'
VALUES (1, 'user', '$2y$11$IHcD/aGGOwQcCvwLS.e3tuP4ObWNWBStsjnv4qEaokO5daRPmrmxi');
Insert into user_credential(id, username, password)
values (2,'admin','$2y$11$8ERFgmpE5vMdeiETXHQYHOB8TQ5j.ypyX7ZJntEQ0MIpDLYmzNDou');

INSERT INTO user_credential_roles VALUES (1,1);
INSERT INTO user_credential_roles VALUES (2,2);