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

CREATE TABLE IF NOT EXISTS category(
    category_id    SERIAL         PRIMARY KEY,
    description    VARCHAR(50)    UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS news (
    news_id           SERIAL          PRIMARY KEY,
    body              TEXT            NOT NULL,
    title             VARCHAR(200)    NOT NULL,
    subtitle          VARCHAR(200)    NOT NULL,
    creator           INTEGER         NOT NULL,
    creation_date     TIMESTAMP       NOT NULL,
    image_id          INTEGER         ,

    FOREIGN KEY (creator) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS news_category(
    category_id     INTEGER      NOT NULL,
    news_id         INTEGER      NOT NULL,

    PRIMARY KEY (category_id, news_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE CASCADE,
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE CASCADE
);