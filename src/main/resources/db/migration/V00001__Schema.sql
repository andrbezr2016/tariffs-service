CREATE TABLE IF NOT EXISTS tariffs (
    id uuid,
    name varchar,
    start_date timestamptz,
    end_date timestamptz,
    description text,
    product uuid,
    version bigint DEFAULT 0,
    PRIMARY KEY (id, version)
);

CREATE TABLE IF NOT EXISTS notifications (
    id bigserial PRIMARY KEY,
    tariff uuid,
    version bigint,
    product uuid NOT NULL,
    processed_date timestamptz
);