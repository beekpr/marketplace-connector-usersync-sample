# syntax = docker/dockerfile:1.0-experimental
# Build with multistage build on jdk-11
FROM gradle:6.3.0-jdk11 as java_build

ARG MVN_S3_INTERNAL_ACCESS_KEY
ARG MVN_S3_INTERNAL_SECRET_KEY

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN --mount=type=cache,target=/home/gradle/.gradle gradle --parallel -S --no-daemon distTar

## Build production image with only outputs
FROM quay.io/beekeeper/integration-connector-base:0.0.30

LABEL Description="Sample User Sync Connector"

COPY --from=java_build /home/gradle/src/build/distributions/connector*.tar /opt/bkpr/
RUN tar -xvf /opt/bkpr/connector*.tar --directory /opt/bkpr  && \
       rm /opt/bkpr/connector-*.tar

RUN mv /opt/bkpr/connector*/lib/*.jar /opt/bkpr/service/lib/connector/
