# Build with multistage build on jdk-17
FROM gradle:7.6.0-jdk17 as java_build

ARG NEXUS_USERNAME
ARG NEXUS_PASSWORD

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN --mount=type=cache,target=/home/gradle/.gradle gradle --parallel -S --no-daemon distTar

## Build production image with only outputs
FROM quay.io/beekeeper/integration-connector-base:3.7.4

LABEL Description="Sample User Sync Connector"

USER root

COPY --from=java_build /home/gradle/src/**/build/distributions/connector*.tar /opt/bkpr/
RUN ls /opt/bkpr/connector*.tar | xargs -n1 -I {} tar -xvf {} --directory /opt/bkpr && rm /opt/bkpr/connector-*.tar

RUN ls /opt/bkpr/connector-*/lib/*.jar | xargs -n1 -I {} mv {} /opt/bkpr/service/lib/connector/

USER app
