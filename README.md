# Rest API for User entity

### Requirements
#### 1. User has the following fields:
   1.1. Email (required). Add validation against email pattern
   1.2. First name (required)
   1.3. Last name (required)
   1.4. Birth date (required). Value must be earlier than current date
   1.5. Address (optional)
   1.6. Phone number (optional)
#### 2. API has the following functionality:
   2.1. Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file.
   2.2. Update one/some user fields
   2.3. Update all user fields
   2.4. Delete user
   2.5. Search for users by birth-date range. Add the validation which checks that “From” is less than “To”.  Should return a list of objects
#### 3. Code is covered by unit tests using Spring
#### 4. Code has error handling for REST
#### 5. API responses are in JSON format
#### 6. Use of database is not necessary. The data persistence layer is not required.
