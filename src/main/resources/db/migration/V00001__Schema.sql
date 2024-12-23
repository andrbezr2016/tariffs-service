CREATE TABLE IF NOT EXISTS tariffs (
    id uuid NOT NULL,
    name varchar NOT NULL,
    start_date timestamp NOT NULL,
    end_date timestamp,
    description text,
    product uuid,
    version bigint NOT NULL,
    state varchar NOT NULL,
    PRIMARY KEY (id, version)
);