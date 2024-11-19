rm -fr application

gradle clean build -x test

BUILD_FILE=./build/libs/bookstore-demo-0.0.1-SNAPSHOT.jar
RUN_JAR=bookstore-demo-0.0.1-SNAPSHOT.jar

java -Djarmode=tools -jar $BUILD_FILE extract --destination application

cd application

java -XX:ArchiveClassesAtExit=application.jsa -Dspring.context.exit=onRefresh -jar $RUN_JAR

java -Xmx512m -XX:SharedArchiveFile=application.jsa -Dspring.config.additional-location=file:../dynamic-config/biz-config.yaml -jar $RUN_JAR
