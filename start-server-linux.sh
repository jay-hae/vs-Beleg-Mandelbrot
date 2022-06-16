./mvnw clean
./mvnw package -DskipTests -T12
java -jar target/mandelbrotServer-server.jar
