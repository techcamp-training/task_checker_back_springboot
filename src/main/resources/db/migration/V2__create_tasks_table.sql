CREATE TABLE tasks (
  id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(255),
  explanation VARCHAR(255),
  deadline_date DATE,
  status INT,
  genre_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (genre_id) REFERENCES genres(id)
);
