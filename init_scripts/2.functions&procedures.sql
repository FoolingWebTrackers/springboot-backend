--==================================================
-- User Stuff
--==================================================
-- Create User --
CREATE PROCEDURE create_user(uname TEXT, pwd TEXT, slt TEXT)
    LANGUAGE plpgsql
AS $$
BEGIN
INSERT INTO users (username, password, salt)
VALUES (uname, encode(digest(CONCAT(pwd, slt), 'sha256'), 'hex'), slt);
END;
$$;

-- Auth User --
CREATE FUNCTION authenticate_user(uname TEXT, pwd TEXT)
    RETURNS BOOLEAN
    LANGUAGE plpgsql
AS $$
DECLARE
is_authenticated BOOLEAN;
BEGIN
SELECT EXISTS (
    SELECT 1
    FROM users
    WHERE username = uname
      AND encode(digest(CONCAT(pwd, salt), 'sha256'), 'hex') = password
)
INTO is_authenticated;

RETURN is_authenticated;
END;
$$;

-- Get Personas of a User--
CREATE FUNCTION get_user_personas(uname TEXT)
    RETURNS TABLE (
                      persona_name TEXT,
                      persona_description TEXT,
                      is_favorite BOOLEAN,
                      first_access_date DATE
                  )
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        SELECT
            p.name,
            p.description,
            up.is_favorite,
            up.first_access_date
        FROM
            user_personas up
                JOIN persona p ON up.persona_id = p.id
                JOIN users u ON up.user_id = u.id
        WHERE
            u.username = uname;
END;
$$;


--==================================================
-- Persona Stuff
--==================================================

-- Create Persona --
CREATE PROCEDURE create_persona(uname TEXT, pname TEXT, pDesc TEXT)
    LANGUAGE plpgsql
AS $$
BEGIN
INSERT INTO persona (name, description)
VALUES (pname, pDesc);

INSERT INTO user_personas (user_id, persona_id, first_access_date)
SELECT u.id, p.id, CURRENT_DATE
FROM users u
         JOIN persona p ON u.username = uname AND p.name = pname;
END;
$$;

-- Insert Links Into A Persona --
CREATE PROCEDURE insert_persona_links(pname TEXT, links TEXT[])
    LANGUAGE plpgsql
AS $$
DECLARE
txt TEXT;
	pid INT;
BEGIN
SELECT id INTO pid FROM persona WHERE persona.name = pname;

FOREACH txt IN ARRAY links
    LOOP
        INSERT INTO persona_links (persona_id, link) VALUES (pid, txt);
END LOOP;
END;
$$;

-- Get Links Of a Persona--
CREATE FUNCTION get_persona_links(pname TEXT)
    RETURNS TABLE (
                      id INT,
                      link TEXT
                  )
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        SELECT
            p.id,
            pl.link
        FROM
            persona_links pl
                JOIN persona p ON p.id = pl.persona_id
        WHERE
            p.name = pname;
END;
$$;

--==================================================
-- Market Place Stuff
--==================================================
CREATE PROCEDURE sell_persona(uname TEXT, pname TEXT)
    LANGUAGE plpgsql
AS $$
DECLARE
uid INT;
	pid INT;
BEGIN
SELECT id INTO pid FROM persona WHERE persona.name = pname;
SELECT id INTO uid FROM users WHERE users.username = uname;

INSERT INTO marketplace (seller_id, persona_id, user_num)
VALUES (uid, pid, 1);
END;
$$;

CREATE PROCEDURE buy_persona(uname TEXT, pname TEXT)
    LANGUAGE plpgsql
AS $$
DECLARE
uid INT;
	pid INT;
BEGIN
SELECT id INTO pid FROM persona WHERE persona.name = pname;
SELECT id INTO uid FROM users WHERE users.username = uname;

INSERT INTO user_personas (user_id, persona_id, first_access_date)
VALUES (uid, pid, CURRENT_DATE);

UPDATE marketplace
SET user_num = user_num + 1
WHERE seller_id = uid AND persona_id = pid;
END;
$$;

CREATE PROCEDURE remove_from_marketplace(uname TEXT, pname TEXT)
    LANGUAGE plpgsql
AS $$
DECLARE
uid INT;
	pid INT;
BEGIN
SELECT id INTO pid FROM persona WHERE persona.name = pname;
SELECT id INTO uid FROM users WHERE users.username = uname;

DELETE FROM marketplace
WHERE seller_id = uid AND persona_id = pid;
END;
$$;




CREATE FUNCTION get_marketplace(uname TEXT)
    RETURNS TABLE (
                      seller_id INT,
                      persona_id INT,
                      user_num INT
                  )
    LANGUAGE plpgsql
AS $$
DECLARE
uid INT;
BEGIN
SELECT id INTO uid FROM users WHERE username = uname;

RETURN QUERY
SELECT
    m.seller_id,
    m.persona_id,
    m.user_num
FROM
    marketplace m
WHERE
    m.seller_id != uid;
END;
$$;

CREATE FUNCTION get_seller_personas(uname TEXT)
    RETURNS TABLE (
                      seller_id INT,
                      persona_id INT,
                      user_num INT
                  )
    LANGUAGE plpgsql
AS $$
DECLARE
uid INT;
BEGIN
SELECT id INTO uid FROM users WHERE username = uname;

RETURN QUERY
SELECT
    m.seller_id,
    m.persona_id,
    m.user_num
FROM
    marketplace m
WHERE
    m.seller_id = uid;
END;
$$;
