main-library-service
====

*by Uncleqrow*

# Setup

## Database configuration

Include this in the environment variables:

```properties
DB_HOST= // the IP host of your postgres database
DB_NAME= // the database name
DB_UNAME= // database username with enough privilege in said database
DB_PASS= // password for accessing the database
```

## Enable automated database setup

This setup includes:

- automatic creation of database schema `testlibrary`
- `auto-ddl`, that is the automatic creation of the database tables
- autorun of `data-postgresql.sql`, which loads initial application settings into table `master_setting`

To perform this auto setup feature, include this in your environment variables.

````properties
SPRING_PROFILES_ACTIVE=dev
````

However, automatic setup are not recommended for production environments. For production, you might want to run the provided `resources/db_init.sql` manually. 

# Features

## User Management

### New User Sign-Up

Request:

```shell
curl --location '{{BASE_URL}}/user' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": {{NEW_USER_EMAIL}},
    "fullName": {{NEW_USER_NAME}},
    "password": {{NEW_USER_PASSWORD}}
}'
```

*Notes:*

- Email must be a valid email format (i.e. `user@domain.com`).
- Password must be 8 letters alphanumeric with at least 1 capital.

### User Login

Request:

```shell
curl --location '{{BASE_URL}}/user/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": {{USER_EMAIL}},
    "password": {{USER_PASSWORD}}
}'
```

Response:

```json
{
  "userSession": {{USER_LOGIN_SESSION_ID}},
  "expiresAt": {{SESSION_EXPIRATION}}
}
```

*Notes:*

- Take note on the login session id, since it will be used in every other requests moving forward.
- The session will expire after a certain time. You will have to re-login after that to obtain a new session.

### User Logout

Request:

```shell
curl --location --request POST '{{BASE_URL}}/user/logout' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```

### See User Details

This includes info on unreturned checked out books.

Request:

```shell
curl --location '{{BASE_URL}}/user/self' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```

## Book Check Out & Returns

### Book Searching

Request:

```shell
curl --location '{{BASE_URL}}/book?page={{PAGINATION_NUMBER}}&pagesize={{PAGINATION_SIZE}}&title={{TITLE_SUBSTRING}}' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```

Response:

```json
{
  "content": [
    {
      "id": {{TITLE_ID}},
      "title": {{TITLE}},
      "author": {{AUTHOR}},
      "synopsis": {{SYNOPSIS}},
      "availableUnits": {{NUMBER_OF_AVAILABLE_UNITS}}
    },
    ....
  ],
  .... // pagination info
}
```

Possible search filters:

- `title`: substring search on the book title
- `author`: substring search on the author name
- `isUnreturned`: filter only unreturned books
- `checkOutStart`: filter based on last check out date
- `checkOutEnd`: filter based on last check out date
- `expectedReturnStart`: filter based on expected return date
- `expectedReturnEnd`: filter based on expected return date
- `returnStart`: filter based on actual last return date
- `returnEnd`: filter based on actual last return date
- `page`: page number (for pagination)
- `pagesize`: number of max items each page (for pagination)

### List Available Items per Title

Use `{{TITLE_ID}}` obtained from [Book Searching](#book-searching) section above.

Request:

```shell
curl --location '{{BASE_URL}}/book/{{TITLE_ID}}' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```

Response:

```json
{
  "content": [
    {
      "id": {{ITEM_ID}},
      "isAvailable": {{TRUE/FALSE}},
      "lastUnreturnedCheckoutAt": {{TIMESTAMP}},
      "expectedReturnDate": {{TIMESTAMP}},
      "currentBorrower": {
        // Info on current borrower
      }
    },
    ....
  ],
  .... // pagination info
}
```

### Book Details per Item

Use `{{ITEM_ID}}` obtained from [List Available Items per Title](#list-available-items-per-title) section above.

Request:

```shell
curl --location '{{BASE_URL}}/book/checkout/{{ITEM_ID}}' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```

### Book Checkout

Request:

```shell
curl --location --request POST '{{BASE_URL}}/book/checkout/{{ITEM_ID}}/self' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```

Response includes info on `expectedReturnDate`.

### Book Return

Request:

```shell
curl --location '{{BASE_URL}}/book/checkout/{{ITEM_ID}}/return' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}' \
--header 'Content-Type: application/json' \
--data '{
    "returnStatus": {{RETURN_STATUS_ID}},
    "remarks": {{OPTIONAL_REMARKS}} // optional
}'
```

For possible `{{RETURN_STATUS_ID}}` see [Return Status](#return-status) section below.

### Return Status

Request:

```shell
curl --location '{{BASE_URL}}/master/returnStatus'
```

Response:

```json
[
  {
    "id": {{RETURN_STATUS_ID}},
    "status": {{STATUS_NAME}},
    "description": {{DESCRIPTION}}
  },
  ....
]
```

## Administration Features

These features below can only be accessed with users of `ADMIN` role.

### Adding New Titles

Request:

```shell
curl --location '{{BASE_URL}}/book' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}' \
--header 'Content-Type: application/json' \
--data '{
    "title": {{TITLE}},
    "author": {{AUTHOR}},
    "synopsis": {{SYNOPSIS}}
}'
```

Response:

```json
{
  "id": {{TITLE_ID}},
  "title": {{TITLE}},
  "author": {{AUTHOR}},
  "synopsis": {{SYNOPSIS}},
  "availableUnits": {{NUMBER_OF_AVAILABLE_UNITS}}
}
```

### Adding New Item for a Title

Use `{{TITLE_ID}}` received from after [search result](#book-searching) or [adding new title](#adding-new-titles).

Request:

```shell
curl --location --request PUT '{{BASE_URL}}/book/{{TITLE_ID}}' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```

## Misc. Features

### Listing Unique Titles of Available Books

Request:

```shell
curl --location '{{BASE_URL}}/master/titles?titles={{TITLE_SUBSTRING}}' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```

### Listing Unique Authors of Available Books

Request:

```shell
curl --location '{{BASE_URL}}/master/author?author={{AUTHOR_NAME_SUBSTRING}}' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```
