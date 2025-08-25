FROM maven:3.9.9-eclipse-temurin-17
WORKDIR /app

COPY pom.xml ./
RUN mvn -q -B -DskipTests dependency:go-offline

COPY src ./src

EXPOSE 8080

CMD ["mvn","-q","-DskipTests","-Dspring-boot.run.profiles=dev","spring-boot:run"]
