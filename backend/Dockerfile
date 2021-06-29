FROM openjdk:15

WORKDIR /backend

VOLUME /tmp

COPY target/erp-0.0.1-SNAPSHOT.jar /backend/erp-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "erp-0.0.1-SNAPSHOT.jar"]

