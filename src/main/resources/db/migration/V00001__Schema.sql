CREATE TABLE IF NOT EXISTS tariffs (
    id uuid,
    name varchar,
    start_date timestamptz,
    end_date timestamptz,
    description text,
    product uuid,
    version bigint,
    deleted boolean,
    PRIMARY KEY (id, version)
);