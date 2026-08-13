-- Migrate Mongo documents to MySQL tables for portal module

CREATE TABLE IF NOT EXISTS member_read_history (
  id VARCHAR(64) NOT NULL PRIMARY KEY,
  member_id BIGINT NOT NULL,
  member_nickname VARCHAR(128),
  member_icon VARCHAR(512),
  product_id BIGINT,
  product_name VARCHAR(512),
  product_pic VARCHAR(1024),
  product_sub_title VARCHAR(1024),
  product_price VARCHAR(64),
  create_time TIMESTAMP,
  INDEX idx_member_create_time (member_id, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS member_product_collection (
  id VARCHAR(64) NOT NULL PRIMARY KEY,
  member_id BIGINT NOT NULL,
  member_nickname VARCHAR(128),
  member_icon VARCHAR(512),
  product_id BIGINT,
  product_name VARCHAR(512),
  product_pic VARCHAR(1024),
  product_sub_title VARCHAR(1024),
  product_price VARCHAR(64),
  create_time TIMESTAMP,
  INDEX idx_member_product (member_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS member_brand_attention (
  id VARCHAR(64) NOT NULL PRIMARY KEY,
  member_id BIGINT NOT NULL,
  member_nickname VARCHAR(128),
  member_icon VARCHAR(512),
  brand_id BIGINT,
  brand_name VARCHAR(512),
  brand_logo VARCHAR(1024),
  brand_city VARCHAR(128),
  create_time TIMESTAMP,
  INDEX idx_member_brand (member_id, brand_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
