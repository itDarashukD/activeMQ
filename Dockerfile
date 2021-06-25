FROM openjdk:11

ENV FFMPEG_VERSION=3.4

WORKDIR /tmp/ffmpeg

# Install ffmpeg
RUN apt-get update && apt-get install -y --no-install-recommends \
  ffmpeg \
&& rm -rf /var/lib/apt/lists/*

RUN apt-get install -y libmp3lame0

WORKDIR /


VOLUME /tmp
ADD target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-jar","/app.jar"]
EXPOSE 8081

# "-Dactivemq.broker-url=tcp://activemq:61616",