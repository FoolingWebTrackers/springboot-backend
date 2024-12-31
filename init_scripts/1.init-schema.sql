CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    salt TEXT NOT NULL
);

CREATE TABLE persona (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    image TEXT
);

CREATE TABLE user_personas (
    user_id INT NOT NULL,
    persona_id INT NOT NULL,
    is_favorite BOOL,
    first_access_date DATE,
    PRIMARY KEY (user_id, persona_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (persona_id) REFERENCES persona(id) ON DELETE CASCADE
);

CREATE TABLE persona_links(
	persona_id INT NOT NULL,
	link TEXT UNIQUE NOT NULL,
	PRIMARY KEY (persona_id, link),
	FOREIGN KEY (persona_id) REFERENCES persona(id) ON DELETE CASCADE
);

CREATE TABLE marketplace(
	seller_id INT NOT NULL,
	persona_id INT NOT NULL,
	user_num INT,
	PRIMARY KEY (seller_id, persona_id),
	FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (persona_id) REFERENCES persona(id) ON DELETE CASCADE
);

CREATE INDEX persona_index ON persona_links (persona_id);
