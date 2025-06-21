BEGIN;

-- INITIALIZE master_setting
INSERT INTO "testlibrary"."master_setting" ("id", "key", "value", "created_date")
VALUES
(gen_random_uuid(), 'checkout.return.days', '10', now());

-- INITIALIZE return_status
INSERT INTO "testlibrary"."return_status" ("id", "created_date" , "status" , "description")
VALUES
(gen_random_uuid(), now(), 'RETURNED', 'Returned without issues'),
(gen_random_uuid(), now(), 'RETURNED_WITH_DELAY', 'Returned with noticable delay'),
(gen_random_uuid(), now(), 'SLIGHTLY_RUINED', 'Returned with noticable, but fixable, disformations.'),
(gen_random_uuid(), now(), 'RUINED', 'Returned with noticable-unfixable disformations. Replacement is expected.'),
(gen_random_uuid(), now(), 'MISSING', 'Not returned. User still reports back. Replacement is expected.'),
(gen_random_uuid(), now(), 'USER_AWOL', 'Not returned. User fails to report back.'),
(gen_random_uuid(), now(), 'OTHERS', 'Other situation not covered above. `remarks` should be expected')
;

COMMIT;
