CREATE TABLE IF NOT EXISTS image(
     image_id       SERIAL      PRIMARY KEY,
     bytes          BYTEA       NOT NULL
);

CREATE TABLE IF NOT EXISTS user_data (
    data_id        SERIAL          PRIMARY KEY,
    username       VARCHAR(50)     UNIQUE NOT NULL,
    pass           VARCHAR(200)    NOT NULL,
    image_id       INTEGER         ,
    FOREIGN KEY (image_id) REFERENCES image (image_id) ON DELETE SET NULL
);


CREATE TABLE IF NOT EXISTS users(
    user_id         SERIAL         PRIMARY KEY,
    email           VARCHAR(100)   UNIQUE NOT NULL,
    data_id         INTEGER        DEFAULT NULL,
    FOREIGN KEY (data_id) REFERENCES user_data(data_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS category(
    category_id    SERIAL         PRIMARY KEY,
    description     VARCHAR(50)    UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS news (
    news_id           SERIAL          PRIMARY KEY,
    body              TEXT            NOT NULL,
    title             VARCHAR(200)    NOT NULL,
    subtitle          VARCHAR(200)    NOT NULL,
    creator           INTEGER         NOT NULL,
    creation_date     TIMESTAMP       DEFAULT now(),
    image_id          INTEGER         ,

    FOREIGN KEY (image_id) REFERENCES image(image_id) ON DELETE SET NULL,
    FOREIGN KEY (creator) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS news_category(
    category_id     INTEGER      NOT NULL,
    news_id         INTEGER      NOT NULL,

    PRIMARY KEY (category_id, news_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE CASCADE,
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE CASCADE
);