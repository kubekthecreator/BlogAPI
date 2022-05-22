CREATE TABLE POST (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(400) NOT NULL,
    content VARCHAR(2000) NULL,
    created timestamp
);

CREATE TABLE COMMENT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULl,
    content VARCHAR(2000) NULL,
    created timestamp
);

ALTER TABLE COMMENT
    ADD CONSTRAINT comment_post_id
    FOREIGN KEY (post_id) REFERENCES post(id)