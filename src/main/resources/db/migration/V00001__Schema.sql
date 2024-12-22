CREATE TABLE IF NOT EXISTS tariffs (
    id uuid,
    name varchar,
    start_date timestamp,
    end_date timestamp,
    description text,
    product uuid,
    version bigint,
    deleted boolean,
    PRIMARY KEY (id, version)
);