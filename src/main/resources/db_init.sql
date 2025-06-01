BEGIN;

-- INITIALIZE SCHEMA, PLEASE ADJUST AS FIT
CREATE SCHEMA testlibrary AUTHORIZATION postgres;

-- INITIALIZE TABLES
CREATE TABLE testlibrary.master_book (
	id uuid NOT NULL,
	author varchar(255) NOT NULL,
	created_date timestamptz(6) NULL,
	synopsis varchar(255) NULL,
	title varchar(255) NOT NULL,
	CONSTRAINT book_title UNIQUE (title, author),
	CONSTRAINT master_book_pkey PRIMARY KEY (id)
);

CREATE TABLE testlibrary.master_person (
	id uuid NOT NULL,
	created_date timestamptz(6) NULL,
	email varchar(255) NOT NULL,
	full_name varchar(255) NOT NULL,
	CONSTRAINT master_person_pkey PRIMARY KEY (id),
	CONSTRAINT user_email UNIQUE (email)
);

CREATE TABLE testlibrary.master_setting (
	id uuid NOT NULL,
	created_date timestamptz(6) NULL,
	"key" varchar(255) NOT NULL,
	value varchar(255) NULL,
	CONSTRAINT master_setting_pkey PRIMARY KEY (id),
	CONSTRAINT ukc1bwbmbthao5f12xpxvw8wjnx UNIQUE (key)
);

CREATE TABLE testlibrary.book (
	id uuid NOT NULL,
	created_date timestamptz(6) NULL,
	is_available bool NOT NULL,
	master_book_id uuid NOT NULL,
	CONSTRAINT book_pkey PRIMARY KEY (id),
	CONSTRAINT fkhcwemxesikivju8r70q7voskg FOREIGN KEY (master_book_id) REFERENCES testlibrary.master_book(id)
);

CREATE TABLE testlibrary."user" (
	id uuid NOT NULL,
	created_date timestamptz(6) NULL,
	encrypted_password bytea NOT NULL,
	is_active bool NOT NULL,
	user_role varchar(255) NOT NULL,
	master_person_id uuid NOT NULL,
	CONSTRAINT ukekfj3q9of01t865nwbccs376w UNIQUE (master_person_id),
	CONSTRAINT user_pkey PRIMARY KEY (id),
	CONSTRAINT user_user_role_check CHECK (((user_role)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying])::text[]))),
	CONSTRAINT fkokt8rdpqjfn725wihugd8lpns FOREIGN KEY (master_person_id) REFERENCES testlibrary.master_person(id)
);

CREATE TABLE testlibrary.user_session (
	id uuid NOT NULL,
	created_date timestamptz(6) NULL,
	expires_at timestamptz(6) NULL,
	user_id uuid NOT NULL,
	CONSTRAINT user_session_pkey PRIMARY KEY (id),
	CONSTRAINT user_session_user_id UNIQUE (user_id),
	CONSTRAINT fks2btlvdomqggby8a3dnlq98ks FOREIGN KEY (user_id) REFERENCES testlibrary."user"(id)
);

CREATE TABLE testlibrary.check_out_history (
	id uuid NOT NULL,
	check_out_date timestamptz(6) NOT NULL,
	created_date timestamptz(6) NULL,
	expected_return_date timestamptz(6) NOT NULL,
	remarks varchar(255) NULL,
	return_date timestamptz(6) NULL,
	return_status varchar(255) NULL,
	book_id uuid NOT NULL,
	user_id uuid NOT NULL,
	CONSTRAINT check_out_history_pkey PRIMARY KEY (id),
	CONSTRAINT check_out_history_return_status_check CHECK (((return_status)::text = ANY ((ARRAY['RETURNED'::character varying, 'RETURNED_WITH_DELAY'::character varying, 'SLIGHT_RUINED'::character varying, 'RUINED'::character varying, 'MISSING'::character varying, 'USER_AWOL'::character varying, 'OTHERS'::character varying])::text[]))),
	CONSTRAINT fkhl1wwfoxoedrsjcj41vpas6ai FOREIGN KEY (book_id) REFERENCES testlibrary.book(id),
	CONSTRAINT fkr196bvpvnnqusx4i8yk6i1cav FOREIGN KEY (user_id) REFERENCES testlibrary."user"(id)
);

-- LOAD INITIAL SETTINGS
INSERT INTO "testlibrary"."master_setting" ("id", "key", "value", "created_date")
VALUES
(gen_random_uuid(), 'checkout.return.days', '10', now());

COMMIT;