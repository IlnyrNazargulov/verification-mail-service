DROP TABLE IF EXISTS account
    , refresh_token
    , request_code
    ,contractor_email;

DELETE
FROM flyway_schema_history
WHERE version = '2021.03.05.01';