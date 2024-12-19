CREATE TABLE IF NOT EXISTS tariffs (
    id uuid PRIMARY KEY,
    name varchar,
    start_date timestamptz,
    end_date timestamptz,
    description text,
    product uuid,
    version bigint NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS tariffs_AUD (
    id uuid,
    name varchar,
    start_date timestamptz,
    end_date timestamptz,
    description text,
    product uuid,
    version bigint NOT NULL DEFAULT 0,
    rev bigint NOT NULL,
    revtype smallint,
    PRIMARY KEY (id, rev)
);

CREATE TABLE IF NOT EXISTS revinfo (
    revtstmp bigint PRIMARY KEY,
    rev bigserial NOT NULL
);

ALTER SEQUENCE IF EXISTS revinfo_rev_seq RENAME TO revinfo_seq;
ALTER SEQUENCE IF EXISTS revinfo_seq INCREMENT 50;

CREATE TABLE IF NOT EXISTS notifications (
    id bigserial PRIMARY KEY,
    tariff uuid,
    version bigint,
    product uuid NOT NULL,
    processed_date timestamptz
);