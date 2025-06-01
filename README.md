main-library-service
====

*by Arthur Hutagalung*

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

### Book Details per Item

Request:

```shell
curl --location '{{BASE_URL}}/book/{{BOOK_ID}}' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```

### Book Checkout

Request:

```shell
curl --location --request POST '{{BASE_URL}}/book/{{BOOK_ID}}/checkout/self' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}'
```

Response includes info on `expectedReturnDate`.

### Book Return

Request:

```shell
curl --location '{{BASE_URL}}/book/{{BOOK_ID}}/return' \
--header 'sessionId: {{USER_LOGIN_SESSION_ID}}' \
--header 'Content-Type: application/json' \
--data '{
    "returnStatus": {{RETURN_STATUS}},
    "remarks": {{OPTIONAL_REMARKS}}
}'
```

`"remarks"` is optional.

For possible return status values see section below.

### Return Status

Request:

```shell
curl --location '{{BASE_URL}}/master/returnStatus'
```

As of the writing of this, the possible return statuses are:

- `RETURNED`: Book is returned without issue.
- `RETURNED_WITH_DELAY`: Book is returned with noticeable delay. 
- `SLIGHT_RUINED`: Book is returned in bad condition, but still fixable.
- `RUINED`: Book is returned in bad condition. Replacement should be demanded.
- `MISSING`: Book is not returned/missing, but the user reports back. Replacement should be demanded.
- `USER_AWOL`: Book is not returned/missing, and the user failed to report back. 
- `OTHERS`: Other situation not covered above. `remarks` should be expected.
