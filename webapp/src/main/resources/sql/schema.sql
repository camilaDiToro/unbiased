
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

ALTER TABLE users ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE users ADD COLUMN IF NOT EXISTS pinged_news INTEGER;

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

ALTER TABLE upvotes DROP CONSTRAINT IF EXISTS upvotes_unique  CASCADE;
ALTER TABLE upvotes ADD CONSTRAINT upvotes_unique UNIQUE(news_id, user_id);
ALTER TABLE upvotes DROP CONSTRAINT IF EXISTS upvotes_pkey CASCADE;
ALTER TABLE upvotes ADD COLUMN IF NOT EXISTS id SERIAL PRIMARY KEY;

CREATE TABLE IF NOT EXISTS comment_upvotes (
   comment_id           INTEGER         NOT NULL,
   user_id           INTEGER         NOT NULL,
   upvote            BOOLEAN         NOT NULL,
   interaction_date  TIMESTAMP       NOT NULL,
   id SERIAL        PRIMARY KEY,

   FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    UNIQUE (comment_id, user_id)
    );


CREATE TABLE IF NOT EXISTS saved_news (
    news_id           INTEGER         NOT NULL,
    user_id           INTEGER         NOT NULL,
    saved_date        TIMESTAMP       NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE CASCADE,
    PRIMARY KEY (news_id, user_id)
);

ALTER TABLE saved_news DROP CONSTRAINT IF EXISTS saved_unique CASCADE;
ALTER TABLE saved_news ADD CONSTRAINT saved_unique UNIQUE(news_id, user_id);
ALTER TABLE saved_news DROP CONSTRAINT IF EXISTS saved_news_pkey CASCADE;
ALTER TABLE saved_news ADD COLUMN IF NOT EXISTS id SERIAL PRIMARY KEY;

CREATE TABLE IF NOT EXISTS report (
    news_id           INTEGER         NOT NULL,
    user_id           INTEGER         NOT NULL,
    report_date       TIMESTAMP       NOT NULL,
    reason            TEXT            NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE CASCADE,
    PRIMARY KEY (news_id, user_id)
);

ALTER TABLE report DROP CONSTRAINT IF EXISTS report_unique CASCADE;
ALTER TABLE report ADD CONSTRAINT report_unique UNIQUE(news_id, user_id);
ALTER TABLE report DROP CONSTRAINT IF EXISTS report_pkey CASCADE;
ALTER table report ADD COLUMN IF NOT EXISTS id SERIAL PRIMARY KEY;

CREATE TABLE IF NOT EXISTS follows(
    user_id           INTEGER     NOT NULL,
    follows            INTEGER     NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (follows) REFERENCES users(user_id) ON DELETE CASCADE,
    PRIMARY KEY (follows, user_id)
);

ALTER TABLE follows DROP CONSTRAINT IF EXISTS follows_unique CASCADE;
ALTER TABLE follows ADD CONSTRAINT follows_unique UNIQUE(user_id, follows);
ALTER TABLE follows DROP CONSTRAINT IF EXISTS follows_pkey CASCADE;
ALTER TABLE follows ADD COLUMN IF NOT EXISTS id SERIAL PRIMARY KEY;


CREATE TABLE IF NOT EXISTS comments (
    news_id           INTEGER         NOT NULL,
    user_id           INTEGER         NOT NULL,
    commented_date        TIMESTAMP       NOT NULL,
    comment        TEXT       NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (news_id) REFERENCES news(news_id) ON DELETE CASCADE,
    PRIMARY KEY (news_id, user_id, commented_date)
);

ALTER TABLE comments DROP CONSTRAINT IF EXISTS comments_unique CASCADE;
ALTER TABLE comments DROP CONSTRAINT IF EXISTS comments_pkey CASCADE;
ALTER TABLE comments ADD COLUMN IF NOT EXISTS id SERIAL PRIMARY KEY;

ALTER TABLE comments ADD COLUMN IF NOT EXISTS deleted BOOLEAN;
UPDATE comments set deleted = FALSE WHERE deleted IS NULL;

CREATE TABLE IF NOT EXISTS email_settings (
    id                SERIAL          PRIMARY KEY,
    user_id           INTEGER         UNIQUE NOT NULL,
    follow            BOOLEAN         NOT NULL,
    comment           BOOLEAN         NOT NULL,
    following_published BOOLEAN         NOT NULL,
    positivity_change   BOOLEAN         NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

alter table comment_report DROP constraint IF EXISTS uk_6fqa4f7msp9o6kjnooeedi6f5;










