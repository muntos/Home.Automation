# Start with a base image containing Java runtime
FROM --platform=linux/amd64 openjdk:11-jdk-slim

# Make port 88888 available to the world outside this container
EXPOSE 8888

ADD target/my-home-automation.jar my-home-automation.jar

ENV TZ=Europe/Bucharest
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#RUN echo "Europe/Bucharest" > /etc/timezone && dpkg-reconfigure -f noninteractive tzdata

RUN echo $TZ >/etc/timezone && \
ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && \
dpkg-reconfigure -f noninteractive tzdata

ARG spring_profile=prod
ENV SPRING_PROFILES_ACTIVE=$spring_profile

# Run the jar file
ENTRYPOINT ["java","-jar","my-home-automation.jar"]
