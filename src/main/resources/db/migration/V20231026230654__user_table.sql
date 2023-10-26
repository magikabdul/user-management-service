CREATE TABLE users (
    id INT PRIMARY KEY NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    isActive BOOLEAN NOT NULL DEFAULT FALSE
);