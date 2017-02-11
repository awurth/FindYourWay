
############################################################
########    DEBIAN + JAVA + WILDFLY + POSTGRESQL    ########
############################################################

FROM debian:jessie

ENV JAVA_VERSION_MAJOR=8 \
    JAVA_VERSION_MINOR=92 \
    JAVA_VERSION_BUILD=14 \
    JAVA_PACKAGE=server-jre \
    JAVA_HOME=/opt/java \
    JVM_OPTS="" \
    PATH=${PATH}:/opt/java/bin \
    LANG=C.UTF-8

ENV WILDFLY_VERSION 10.1.0.Final
ENV JBOSS_HOME /opt/jboss/wildfly
ENV PATH ${PATH}:${JAVA_HOME}/bin
ENV POSTGRESQL_VERSION 9.4-1201-jdbc41

ARG DB_HOST=postgresql
ARG DB_NAME=campstur
ARG DB_USER=campstur
ARG DB_PASS=campstur

RUN apt-get update -q && \
    apt-get install -q -y --no-install-recommends ca-certificates curl unzip && \
    curl -jksSLH "Cookie: oraclelicense=accept-securebackup-cookie" -o /tmp/java.tar.gz \
      http://download.oracle.com/otn-pub/java/jdk/${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-b${JAVA_VERSION_BUILD}/${JAVA_PACKAGE}-${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-linux-x64.tar.gz && \
    gunzip /tmp/java.tar.gz && \
    tar -C /opt -xf /tmp/java.tar && \
    ln -s /opt/jdk1.${JAVA_VERSION_MAJOR}.0_${JAVA_VERSION_MINOR} ${JAVA_HOME} && \
    find ${JAVA_HOME} -maxdepth 1 -mindepth 1 | grep -v jre | xargs rm -rf && \
    cd ${JAVA_HOME} && ln -s ./jre/bin ./bin && \
    curl -jksSLH "Cookie: oraclelicense=accept-securebackup-cookie" -o /tmp/unlimited_jce_policy.zip "http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip" && \
    unzip -jo -d ${JAVA_HOME}/jre/lib/security /tmp/unlimited_jce_policy.zip && \
    rm -rf ${JAVA_HOME}/*src.zip \
           ${JAVA_HOME}/lib/missioncontrol \
           ${JAVA_HOME}/lib/visualvm \
           ${JAVA_HOME}/lib/*javafx* \
           ${JAVA_HOME}/jre/plugin \
           ${JAVA_HOME}/jre/bin/javaws \
           ${JAVA_HOME}/jre/bin/jjs \
           ${JAVA_HOME}/jre/bin/keytool \
           ${JAVA_HOME}/jre/bin/orbd \
           ${JAVA_HOME}/jre/bin/pack200 \
           ${JAVA_HOME}/jre/bin/policytool \
           ${JAVA_HOME}/jre/bin/rmid \
           ${JAVA_HOME}/jre/bin/rmiregistry \
           ${JAVA_HOME}/jre/bin/servertool \
           ${JAVA_HOME}/jre/bin/tnameserv \
           ${JAVA_HOME}/jre/bin/unpack200 \
           ${JAVA_HOME}/jre/lib/javaws.jar \
           ${JAVA_HOME}/jre/lib/deploy* \
           ${JAVA_HOME}/jre/lib/desktop \
           ${JAVA_HOME}/jre/lib/*javafx* \
           ${JAVA_HOME}/jre/lib/*jfx* \
           ${JAVA_HOME}/jre/lib/amd64/libdecora_sse.so \
           ${JAVA_HOME}/jre/lib/amd64/libprism_*.so \
           ${JAVA_HOME}/jre/lib/amd64/libfxplugins.so \
           ${JAVA_HOME}/jre/lib/amd64/libglass.so \
           ${JAVA_HOME}/jre/lib/amd64/libgstreamer-lite.so \
           ${JAVA_HOME}/jre/lib/amd64/libjavafx*.so \
           ${JAVA_HOME}/jre/lib/amd64/libjfx*.so \
           ${JAVA_HOME}/jre/lib/ext/jfxrt.jar \
           ${JAVA_HOME}/jre/lib/ext/nashorn.jar \
           ${JAVA_HOME}/jre/lib/oblique-fonts \
           ${JAVA_HOME}/jre/lib/plugin.jar \
           /tmp/* /var/cache/apk/*

# Install Wildfly
RUN cd /tmp && \
  curl -O http://download.jboss.org/wildfly/10.1.0.Final/wildfly-10.1.0.Final.tar.gz && \
  tar xf wildfly-10.1.0.Final.tar.gz && \
  mkdir -p $JBOSS_HOME && \
  mv /tmp/wildfly-10.1.0.Final/* $JBOSS_HOME/ && \
  rm -r wildfly-*

# Add the application and configurate CORS for Swagger.io
ADD findyourway.war $JBOSS_HOME/standalone/deployments/
RUN rm -f $JBOSS_HOME/standalone/configuration/standalone.xml
ADD standalone.xml $JBOSS_HOME/standalone/configuration/standalone.xml
# Add an admin user with the hackerman credentials (admin admin)
RUN $JBOSS_HOME/bin/add-user.sh admin -p admin -s

# Install postgres drivers and datasource
 RUN /bin/sh -c '$JBOSS_HOME/bin/standalone.sh &' && \
  sleep 10 && \
  cd /tmp && \
  curl --location --output postgresql-${POSTGRESQL_VERSION}.jar --url http://search.maven.org/remotecontent?filepath=org/postgresql/postgresql/${POSTGRESQL_VERSION}/postgresql-${POSTGRESQL_VERSION}.jar && \
  $JBOSS_HOME/bin/jboss-cli.sh --connect --command="deploy /tmp/postgresql-${POSTGRESQL_VERSION}.jar" && \
  $JBOSS_HOME/bin/jboss-cli.sh --connect --command="xa-data-source add --name=campstur --jndi-name=java:/jdbc/datasources/campsturDS --user-name=${DB_USER} --password=${DB_PASS} --driver-name=postgresql-9.4-1201-jdbc41.jar --xa-datasource-class=org.postgresql.xa.PGXADataSource --xa-datasource-properties=ServerName=${DB_HOST},PortNumber=5432,DatabaseName=${DB_NAME} --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter" && \
  $JBOSS_HOME/bin/jboss-cli.sh --connect --command=:shutdown && \
  rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/ $JBOSS_HOME/standalone/log/* && \
  rm /tmp/postgresql-9.4*.jar && \
  rm -rf /tmp/postgresql-*.jar

# Expose http and admin ports
EXPOSE 8080 9990


# Set the default command to run on boot this will boot WildFly in the standalone mode and bind to all interfaces
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-Djboss.http.port=8080"]
