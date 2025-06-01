BEGIN;

-- INITIALIZE master_setting
INSERT INTO "testlibrary"."master_setting" ("id", "key", "value", "created_date")
VALUES
(gen_random_uuid(), 'checkout.return.days', '10', now());

COMMIT;
