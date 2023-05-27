create schema if not exists weather;

CREATE TABLE geography
(
    id                     UUID NOT NULL,
    country                VARCHAR(255),
    city                   VARCHAR(255),
    latitude               DOUBLE PRECISION,
    longitude              DOUBLE PRECISION,
    creation_timestamp     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modification_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT geography_pk PRIMARY KEY (id)
);

CREATE TABLE weather_data
(
    id                     UUID NOT NULL,
    current_temperature    DOUBLE PRECISION,
    min_temperature        DOUBLE PRECISION,
    max_temperature        DOUBLE PRECISION,
    feels_like             DOUBLE PRECISION,
    humidity               INT,
    pressure               INT,
    visibility             INT,
    wind_speed             DOUBLE PRECISION,
    creation_timestamp     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modification_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    geography_id           UUID,
    CONSTRAINT weather_data_pk PRIMARY KEY (id),
    CONSTRAINT weather_data_fk_geography FOREIGN KEY (geography_id) REFERENCES geography (id)
);


