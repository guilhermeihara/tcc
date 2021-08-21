FROM openjdk:11
WORKDIR /app
VOLUME /app
COPY target/ .
COPY wallet/ ./wallet/
COPY org1msp_profile.json .
ENTRYPOINT ["java","-jar", "tcc-0.0.1-SNAPSHOT.jar"]