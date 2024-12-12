-- Create the "role" table
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Create the "player_role" join table for the many-to-many relationship
CREATE TABLE player_role (
    player_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (player_id, role_id),
    FOREIGN KEY (player_id) REFERENCES player(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);
