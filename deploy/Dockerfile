FROM maven:3-alpine
RUN mkdir src
COPY src ./src
COPY pom.xml .
RUN mvn install
EXPOSE 8080
CMD mvn exec:java -Dexec.mainClass="com.project.demo.SampleDemoMain"
