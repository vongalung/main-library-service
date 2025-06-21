BEGIN;

INSERT INTO "testlibrary"."master_book" ("id", "created_date", "title", "author", "synopsis")
VALUES
(gen_random_uuid(), now(), 'Book 01', 'Author 01', 'Syn Book 01'),
(gen_random_uuid(), now(), 'Book 02', 'Author 01', 'Syn Book 02'),
(gen_random_uuid(), now(), 'Book 03', 'Author 01', 'Syn Book 03'),
(gen_random_uuid(), now(), 'Book 04', 'Author 02', 'Syn Book 04'),
(gen_random_uuid(), now(), 'Book 05', 'Author 03', 'Syn Book 05'),
(gen_random_uuid(), now(), 'Book 06', 'Author 03', 'Syn Book 06'),
(gen_random_uuid(), now(), 'Book 07', 'Author 04', 'Syn Book 07'),
(gen_random_uuid(), now(), 'Book 08', 'Author 05', 'Syn Book 08'),
(gen_random_uuid(), now(), 'Book 09', 'Author 05', 'Syn Book 09'),
(gen_random_uuid(), now(), 'Book 10', 'Author 05', 'Syn Book 10'),
(gen_random_uuid(), now(), 'Book 11', 'Author 05', 'Syn Book 11'),
(gen_random_uuid(), now(), 'Book 12', 'Author 05', 'Syn Book 12'),
(gen_random_uuid(), now(), 'Book 13', 'Author 06', 'Syn Book 13'),
(gen_random_uuid(), now(), 'Book 14', 'Author 06', 'Syn Book 14')
;

INSERT INTO "testlibrary"."book" ("id", "created_date", "master_book_id", "is_available")
VALUES
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 01' AND mb."author"='Author 01'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 01' AND mb."author"='Author 01'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 01' AND mb."author"='Author 01'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 02' AND mb."author"='Author 01'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 02' AND mb."author"='Author 01'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 02' AND mb."author"='Author 01'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 03' AND mb."author"='Author 01'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 03' AND mb."author"='Author 01'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 04' AND mb."author"='Author 02'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 04' AND mb."author"='Author 02'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 05' AND mb."author"='Author 03'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 05' AND mb."author"='Author 03'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 06' AND mb."author"='Author 03'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 13' AND mb."author"='Author 06'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 13' AND mb."author"='Author 06'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 14' AND mb."author"='Author 06'), true),
(gen_random_uuid(), now(), (SELECT mb."id" FROM "testlibrary"."master_book" mb WHERE mb."title"='Book 14' AND mb."author"='Author 06'), true)
;

INSERT INTO "testlibrary"."master_person" ("id", "created_date", "email", "full_name")
VALUES
(gen_random_uuid(), now(), 'user01@email.com', 'User 01'),
(gen_random_uuid(), now(), 'user02@email.com', 'User 02'),
(gen_random_uuid(), now(), 'user03@email.com', 'User 03'),
(gen_random_uuid(), now(), 'user04@email.com', 'User 04'),
(gen_random_uuid(), now(), 'user05@email.com', 'User 05'),
(gen_random_uuid(), now(), 'user06@email.com', 'User 06'),
(gen_random_uuid(), now(), 'user07@email.com', 'User 07'),
(gen_random_uuid(), now(), 'user08@email.com', 'User 08')
;

INSERT INTO "testlibrary"."user" ("id", "created_date", "master_person_id", "is_active", "user_role", "encrypted_password")
VALUES
(gen_random_uuid(), now(), (SELECT mp."id" FROM "testlibrary"."master_person" mp WHERE mp."email"='user01@email.com'), true, 'ADMIN', encode('Pass01!'::bytea, 'base64')::bytea),
(gen_random_uuid(), now(), (SELECT mp."id" FROM "testlibrary"."master_person" mp WHERE mp."email"='user02@email.com'), true, 'USER', encode('Pass02!'::bytea, 'base64')::bytea),
(gen_random_uuid(), now(), (SELECT mp."id" FROM "testlibrary"."master_person" mp WHERE mp."email"='user03@email.com'), true, 'USER', encode('Pass03!'::bytea, 'base64')::bytea),
(gen_random_uuid(), now(), (SELECT mp."id" FROM "testlibrary"."master_person" mp WHERE mp."email"='user04@email.com'), true, 'USER', encode('Pass04!'::bytea, 'base64')::bytea),
(gen_random_uuid(), now(), (SELECT mp."id" FROM "testlibrary"."master_person" mp WHERE mp."email"='user06@email.com'), true, 'ADMIN', encode('Pass06!'::bytea, 'base64')::bytea),
(gen_random_uuid(), now(), (SELECT mp."id" FROM "testlibrary"."master_person" mp WHERE mp."email"='user07@email.com'), true, 'USER', encode('Pass07!'::bytea, 'base64')::bytea)
;

COMMIT;