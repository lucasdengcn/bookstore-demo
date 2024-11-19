gradle clean build -x test

BUILD_FILE=./build/libs/bookstore-demo-0.0.1-SNAPSHOT.jar

java -Xmx512m -Dspring.config.additional-location=file:./dynamic-config/biz-config.yaml -jar $BUILD_FILE
