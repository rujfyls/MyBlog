create table captcha_codes (id  serial not null, code smallint not null, secret_code smallint not null, time timestamp not null, primary key (id));
create table global_settings (id  serial not null, code varchar(255) not null, name varchar(255) not null, value varchar(255) not null, primary key (id));
create table tags (id  serial not null, name varchar(255) not null, primary key (id));
create table users (id  serial not null, code varchar(255), email varchar(255) not null, is_moderator smallint not null, name varchar(255) not null, password varchar(255) not null, photo TEXT, reg_time timestamp not null, primary key (id));
create table posts (id  serial not null, is_active smallint not null, moderation_status varchar(255) not null, moderator_id int4, text TEXT not null, time timestamp not null, title varchar(255) not null, view_count int4 not null, user_id int4 not null, primary key (id), FOREIGN KEY (moderator_id) REFERENCES users(id), FOREIGN KEY (user_id) REFERENCES users(id));
create table post_comments (id  serial not null, text TEXT not null, time timestamp not null, parent_id int4, post_id int4 not null, user_id int4 not null, primary key (id), FOREIGN KEY (post_id) REFERENCES posts(id), FOREIGN KEY (user_id) REFERENCES users(id), FOREIGN KEY (parent_id) REFERENCES post_comments(id));
create table post_votes (id  serial not null, time timestamp not null, value smallint not null, post_id int4 not null, user_id int4 not null, primary key (id), FOREIGN KEY (post_id) REFERENCES posts(id), FOREIGN KEY (user_id) REFERENCES users(id));
create table tag2post (id  serial not null, tag_id int4 not null, post_id int4 not null, primary key (id), FOREIGN KEY (tag_id) REFERENCES tags(id), FOREIGN KEY (post_id) REFERENCES posts(id));