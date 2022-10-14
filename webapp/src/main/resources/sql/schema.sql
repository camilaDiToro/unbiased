
CREATE TABLE IF NOT EXISTS image(
    image_id        SERIAL          PRIMARY KEY,
    bytes           BYTEA           NOT NULL,
    data_type       VARCHAR(50)     NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
    user_id        SERIAL         PRIMARY KEY,
    email          VARCHAR(100)   UNIQUE NOT NULL,
    username       VARCHAR(50)    UNIQUE ,
    description    VARCHAR(500)   ,
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

    FOREIGN KEY (creator) REFERENCES users(user_id) ON DELETE CASCADE
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

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE CASCADE,
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

CREATE TABLE IF NOT EXISTS report (
    news_id           INTEGER         NOT NULL,
    user_id           INTEGER         NOT NULL,
    report_date       TIMESTAMP       NOT NULL,
    reason            TEXT            NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE CASCADE,
    PRIMARY KEY (news_id, user_id)
    );

DROP VIEW IF EXISTS full_news_with_logged_params;


DROP VIEW IF EXISTS full_news;


DROP VIEW IF EXISTS news_stats;

DROP VIEW IF EXISTS user_positivity;

DROP VIEW IF EXISTS is_journalist;


CREATE OR REPLACE VIEW news_stats AS
            SELECT sum(case when upvote=true then 1 else 0 end) AS upvotes, sum(case when upvote=true then 0 else 1 end) AS downvotes, news_id FROM upvotes GROUP BY news_id;

CREATE OR REPLACE VIEW logged_news_parameters AS
SELECT user_id AS logged_user, news_id, upvote, saved_date
FROM upvotes NATURAL FULL JOIN
saved_news;

CREATE OR REPLACE VIEW full_news AS
SELECT  news.*, upvotes, downvotes, email, username, pass, status, users.image_id as user_image_id FROM news LEFT JOIN news_stats ON news_stats.news_id = news.news_id JOIN users ON creator = user_id;

CREATE OR REPLACE VIEW full_news_with_logged_params AS
SELECT upvote, saved_date, logged_news_parameters.logged_user, full_news.news_id, body, title, subtitle, creator, creation_date, accesses, image_id, upvotes, downvotes, email, username, pass, status, user_image_id  FROM logged_news_parameters RIGHT JOIN full_news ON full_news.news_id = logged_news_parameters.news_id;

CREATE OR REPLACE VIEW user_positivity AS
SELECT sum(case when upvote=true then 1 else 0 end) AS upvotes, sum(case when upvote=true then 0 else 1 end) AS downvotes, creator AS user_id FROM upvotes NATURAL JOIN news GROUP BY creator;

CREATE OR REPLACE VIEW is_journalist AS
SELECT user_id, true as is_journalist FROM user_role where user_role = 'ROLE_JOURNALIST';


CREATE TABLE IF NOT EXISTS follows(
                                      user_id           INTEGER     NOT NULL,
                                      follows            INTEGER     NOT NULL,
                                      FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (follows) REFERENCES users(user_id) ON DELETE CASCADE,
    PRIMARY KEY (follows, user_id)
    );

CREATE TABLE IF NOT EXISTS comments (
                                          news_id           INTEGER         NOT NULL,
                                          user_id           INTEGER         NOT NULL,
                                          commented_date        TIMESTAMP       NOT NULL,
                                          comment        TEXT       NOT NULL,

                                          FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE CASCADE,
    PRIMARY KEY (news_id, user_id, commented_date)
    );