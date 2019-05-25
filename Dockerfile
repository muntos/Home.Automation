# Start with a base image containing Java runtime
FROM java:8

# Make port 88888 available to the world outside this container
EXPOSE 8888

ADD target/my-home-automation.jar my-home-automation.jar

RUN echo "Europe/Bucharest" > /etc/timezone && dpkg-reconfigure -f noninteractive tzdata

ARG spring_profile=prod
ENV SPRING_PROFILES_ACTIVE=$spring_profile

# Run the jar file
ENTRYPOINT ["java","-jar","my-home-automation.jar"]