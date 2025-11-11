FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache \
    git \
    maven \
    bash \
    tini \
    postgresql-client

WORKDIR /app

RUN git clone https://github.com/CTISM-Prof-Henry/trab-final-spi-integer-s.git ./

RUN mvn clean package -DskipTests

VOLUME ["/var/log/app"]
RUN mkdir -p /var/log/app

ENTRYPOINT ["java", "-jar", "target/Salas-0.0.1-SNAPSHOT.jar"]
