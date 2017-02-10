FROM mheider/wildfly-postgres

ADD findyourway.war /opt/jboss/wildfly/standalone/deployments/findyourway.war
RUN rm -f /opt/jboss/wildfly/standalone/configuration/standalone.xml
ADD standalone.xml /opt/jboss/wildfly/standalone/configuration


EXPOSE 8080 8080
