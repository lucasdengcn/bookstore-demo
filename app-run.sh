gradle clean build -x test

java -Dspring.config.additional-location=file:./dynamic-config/biz-config.yaml -jar ./build/libs/bookstore-demo-0.0.1-SNAPSHOT.jar
