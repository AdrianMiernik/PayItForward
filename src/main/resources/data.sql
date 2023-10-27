--------------------------------------
--Credentials to run the appliction:
--login: maria.jordan@gmail.com, john.smith@gmail.com , katie.frank@gmail.com , frank.berry@gmail.com
--password (same for all): Password1!
--------------------------------------
--------------------------------------
INSERT INTO roles
    (id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

INSERT INTO users
    (id, first_name, last_name, password, email)
VALUES (1, 'Maria', 'Jordan', '$2a$10$OaqGAYeykUlJn5xvpO4t4.J9/tQriHWLKq9Nbhu3s7pHBVaWCi6qG', 'maria.jordan@gmail.com'),
       (2, 'John', 'Smith', '$2a$10$WiSt4mLngXhVMLHT89xyZ.60DEsz74VVkUKR2kbKvHJgQF.uB6Cde', 'john.smith@gmail.com'),
       (3, 'Katie', 'Frank', '$2a$10$M7LTPBw31dIaeHTf1wH0ROkrKfrLqk6Map/vvEShsnj9eTnj1tahy', 'katie.frank@gmail.com'),
       (4, 'Frank', 'Berry', '$2a$10$ORIpa5cziwdrG4pkUIXuN.y9Pov.qKRxJG1NLb/aoFMwGlnZlQz1e', 'frank.berry@gmail.com');

INSERT INTO users_roles
    (user_id, roles_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1);

INSERT INTO institutions
    (id, name, description, city)
VALUES (1, 'Unicef - Cracow',
        'Works in over 190 countries and territories to save childrens lives to defend their rights.', 'Cracow'),
       (2, 'Red Cross - Warsaw',
        'Ensuring humanitarian protection and assistance for victims of war and other situations of violence.',
        'Warsaw'),
       (3, 'Doctors without borders - Gdynia',
        'Treats people where the need is greatest. We are an international medical humanitarian organisation.',
        'Gdynia'),
       (4, 'WOSP - Wroclaw', 'Raising funds for the healthcare system in Poland.', 'Wroclaw'),
       (5, 'Unicef - Wroclaw',
        'Works in over 190 countries and territories to save childrens lives to defend their rights.', 'Wroclaw'),
       (6, 'Red Cross - Cracow',
        'Ensuring humanitarian protection and assistance for victims of war and other situations of violence.',
        'Cracow'),
       (7, 'Doctors without borders - Warsaw',
        'Treats people where the need is greatest. We are an international medical humanitarian organisation.',
        'Warsaw'),
       (8, 'WOSP - Gdynia', 'Raising funds for the healthcare system in Poland.', 'Gdynia');

INSERT INTO categories
    (id, name)
VALUES (1, 'Clothes'),
       (2, 'Toys'),
       (3, 'Electronics'),
       (4, 'Long shelf life food');

INSERT INTO donations
(id, quantity, user_id, institution_id, city, street, zip_code, phone_number, pick_up_date, pick_up_time,
 pick_up_comment, actual_pick_up_date, is_picked_up, created, updated)
VALUES (1, 7, 1, 8, 'Cracow', '5 Close Avenue', '30-320', '+48555666777', '2021-12-29', '20:00:00',
        'Please pick up after 6 pm.', NULL, 'FALSE', '2021-11-26', NULL),
       (2, 12, 2, 6, 'Warsaw', '10 High Street', '31-222', '+48555666888', '2021-12-28', '08:00:00',
        'Please pick up before 10 am.', NULL, 'FALSE', '2021-11-26', NULL),
       (3, 3, 3, 2, 'Lublin', '77 Downtown Street', '48-658', '+48555666999', '2021-12-26', '12:00:00',
        'Call me before pick up.', NULL, 'FALSE', '2021-11-26', NULL),
       (4, 5, 4, 1, 'Wroclaw', '15 Southhall Close', '50-501', '+48555666444', '2021-12-27', '15:00:00',
        'Door bell does not work.', NULL, 'FALSE', '2021-11-26', NULL);

INSERT INTO donations_categories
    (donation_id, categories_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 1),
       (3, 3),
       (4, 2);