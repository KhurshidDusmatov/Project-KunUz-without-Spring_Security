CREATE TABLE if not exists brain
(
    id          serial,
    name varchar(255) DEFAULT NULL,
    title       varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);
