# Simple Spring Boot app with AWS S3 backend 
Just a small example how AWS S3 can be used as storage. Don't forget to adjust the application.properties first ;)

# LocalStack
This example shows also how [LocalStack](https://localstack.cloud) can be used to run your code against a local AWS S3 mock.

1. Start the application using `docker-compose up`, localstack will be started too.
2. Create a test S3 bucket using `aws --endpoint-url=http://localhost:4572 s3 mb s3://test`
3. Allow public access `aws --endpoint-url=http://localhost:4572 s3api put-bucket-acl --bucket test --acl public-read`
4. Got to `localhost:8080` to access the application.


