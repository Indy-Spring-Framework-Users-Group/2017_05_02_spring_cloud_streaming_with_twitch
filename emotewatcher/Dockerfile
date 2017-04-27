FROM java:8-alpine

WORKDIR /app
COPY target/*.jar /app/
CMD exec java -Djava.security.edg=file:/dev/./urandom -server -jar /app/*.jar
