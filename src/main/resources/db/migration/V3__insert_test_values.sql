insert into tags(name) values('CODE');
insert into tags(name) values('TEST');
insert into users(email, is_moderator, name, password, reg_time) values('dexter_aljp1@gmail.com', 1, 'Alex', '$2a$12$p8c88WRUSCLVhpUKdWefEu4FzVUFN5tCr/TaP08Y7iMevtfyppFmu', CURRENT_TIMESTAMP);
insert into posts(is_active, moderation_status, text, time, title, view_count, user_id) values(1, 'NEW', 'новый текст ради теста', CURRENT_TIMESTAMP, 'тестовый заголовок', 0, 1);
insert into tag2post(tag_id, post_id) values(1,1);
insert into post_votes(time, value, post_id, user_id) values(CURRENT_TIMESTAMP, 1, 1,1);
insert into post_comments(text, time, post_id, user_id) values('ставлю тестовый лайк', CURRENT_TIMESTAMP, 1,1);
insert into captcha_codes(code, secret_code, time) values('test code', 'test secret code', CURRENT_TIMESTAMP);