
CREATE TABLE IF NOT EXISTS image(
    image_id        SERIAL          PRIMARY KEY,
    bytes           BYTEA           NOT NULL,
    data_type       VARCHAR(50)     NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
    user_id        SERIAL         PRIMARY KEY,
    email          VARCHAR(100)   UNIQUE NOT NULL,
    username       VARCHAR(50)    UNIQUE ,
    pass           VARCHAR(200)   ,
    status         TEXT           NOT NULL,
    image_id       INTEGER        ,
    FOREIGN KEY (image_id) REFERENCES image (image_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS news (
    news_id           SERIAL          PRIMARY KEY,
    body              TEXT            NOT NULL,
    title             VARCHAR(200)    NOT NULL,
    subtitle          VARCHAR(400)    NOT NULL,
    creator           INTEGER         NOT NULL,
    creation_date     TIMESTAMP       NOT NULL,
    accesses          INTEGER         NOT NULL DEFAULT 0,
    image_id          INTEGER         ,

    FOREIGN KEY (creator) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS news_category(
    category_id     INTEGER      NOT NULL,
    news_id         INTEGER      NOT NULL,

    PRIMARY KEY (category_id, news_id),
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS email_verification_token (
    token_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    token TEXT NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_role (
    user_id INT NOT NULL,
    user_role VARCHAR(30) NOT NULL,
    PRIMARY KEY (user_id, user_role),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS upvotes (
    news_id           INTEGER         NOT NULL,
    user_id           INTEGER         NOT NULL,
    upvote            BOOLEAN         NOT NULL,
    interaction_date  TIMESTAMP       NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE SET NULL,
    PRIMARY KEY (news_id, user_id)

);

CREATE TABLE IF NOT EXISTS saved_news (
    news_id           INTEGER         NOT NULL,
    user_id           INTEGER         NOT NULL,
    saved_date        TIMESTAMP       NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE CASCADE,
    PRIMARY KEY (news_id, user_id)

    );

