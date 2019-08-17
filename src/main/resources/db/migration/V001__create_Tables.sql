CREATE TABLE notes (
  id              BIGINT       NOT NULL AUTO_INCREMENT
 ,reference       VARCHAR(36)  NOT NULL UNIQUE
 ,subject         VARCHAR(255) NOT NULL
 ,body            TEXT
 ,last_updated_at TIMESTAMP NOT NULL
)