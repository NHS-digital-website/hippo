FROM maven:3.6.0-jdk-8
RUN apt-get update && apt-get install -y wget

RUN groupadd -r -g 2000 mygrp && useradd -m -d /home/myuser/ -s /bin/bash -u 2000 -r -g mygrp myuser

# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get clean;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME
RUN apt-get install make
COPY settings.xml /root/.m2/settings.xml

WORKDIR /e2e

COPY ci-cd /e2e/ci-cd/
COPY build-tools /e2e/build-tools/
COPY conf /e2e/conf/
COPY *.* /e2e/
COPY Makefile /e2e/Makefile

COPY cms/target/cms.war /e2e/cms/target/cms.war
COPY site/webapp/target/site.war /e2e/site/webapp/target/site.war
COPY essentials/target/essentials.war /e2e/essentials/target/essentials.war

COPY /repository-data/development/target/website-repository-data-development-dev-SNAPSHOT.jar /e2e/repository-data/development/target/website-repository-data-development-dev-SNAPSHOT.jar
COPY /repository-data/local/target/website-repository-data-local-dev-SNAPSHOT.jar /e2e/repository-data/local/target/website-repository-data-local-dev-SNAPSHOT.jar
COPY /repository-data/application/target/website-repository-data-application-dev-SNAPSHOT.jar /e2e/repository-data/application/target/website-repository-data-application-dev-SNAPSHOT.jar
COPY /repository-data/site/target/website-repository-data-site-dev-SNAPSHOT.jar /e2e/repository-data/site/target/website-repository-data-site-dev-SNAPSHOT.jar
COPY /repository-data/site-development/target/website-repository-data-site-development-dev-SNAPSHOT.jar /e2e/repository-data/site-development/target/website-repository-data-site-development-dev-SNAPSHOT.jar
COPY /repository-data/webfiles/target/website-repository-data-webfiles-dev-SNAPSHOT.jar /e2e/repository-data/site/target/website-repository-data-webfiles-dev-SNAPSHOT.jar

COPY /s3connector/target/s3-connector-dev-SNAPSHOT.jar /e2e/s3connector/target/s3-connector-dev-SNAPSHOT.jar

CMD ["make","test.run"]

