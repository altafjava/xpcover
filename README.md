
## Requirements to execute the project

1. Clone project from [Github Link](https://github.com/altafjava/signup-login-forgot.git).
2. Import as Existing Maven Projects if it is Eclipse.
3. Install mongodb in your local system with default port 27017.
4. Run SignupLoginForgotApplication as Spring Boot App.


## How to test the Program

1. Install Postman.
2. Execute the Project. (Run com.gmc.Application as Spring Boot App or Java Application).
3. Go to [API Documentation](http://localhost:9999/swagger-ui.html) to know the API details.


## Example

**URL :** localhost:9999/api/v1/test

**Output :** This is for testing. Project working fine

## Secure Test Curl Example

```
curl --location --request GET 'localhost:9999/api/v1/test-secure' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2MTJkZmVhMjM1MDEwZjcwZjE4OWEyNDgiLCJpYXQiOjE2MzA1Nzg5NDYsImV4cCI6MTYzMDYyMjE0NiwiZW1haWwiOiJZN3lDZDVkVUlwR3hYenFwM1ZPaFl3PT0iLCJyb2xlcyI6WyJPV05FUiJdfQ.Za1DBCca5dyCU9zm1znclHG3mkChJey-VQ6d9GIwArhU-P3VldbElZVn_bVNzQJTEZrSjzjsEBHaV1gFlvZVGA'
```
## Users

**Default User : OWNER**

**Default Password : default** 


## Postman Collection

Added in the project main directory with name **xpcover.postman_collection.json**