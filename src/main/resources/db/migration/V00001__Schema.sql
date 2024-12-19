CREATE TABLE IF NOT EXISTS tariffs (
    id uuid,
    name varchar,
    start_date timestamptz,
    end_date timestamptz,
    description text,
    product uuid,
    version bigint,
    PRIMARY KEY (id, version)
);

CREATE TABLE IF NOT EXISTS notifications (
    id bigserial PRIMARY KEY,
    tariff uuid,
    tariff_version bigint,
    product uuid NOT NULL,
    processed_date timestamptz
);