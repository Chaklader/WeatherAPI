ALTER TABLE weather_data ADD COLUMN query_timestamp TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE geography ADD COLUMN query_timestamp TIMESTAMP WITHOUT TIME ZONE;

CREATE INDEX idx_geography_ip_address ON geography (ip_address);
CREATE INDEX idx_geography_coordinates ON geography (latitude, longitude);
