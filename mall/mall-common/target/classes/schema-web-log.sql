-- WebLog custom table for persisting request logs
CREATE TABLE IF NOT EXISTS web_log (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(128),
  ip VARCHAR(64),
  method VARCHAR(16),
  parameter TEXT,
  result TEXT,
  spend_time INT,
  start_time BIGINT,
  uri VARCHAR(512),
  url VARCHAR(1024),
  description VARCHAR(512),
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
