# Basis-Image
FROM payara/server-full:5.2021.6-jdk11

# Debug-Modus aktivieren
ENV PAYARA_ARGS --debug

# MYSQL-JDBC-Driver 
COPY mariadb-java-client-2.7.2.jar /opt/payara/appserver/glassfish/domains/domain1/lib
COPY guava-28.0-jre.jar /opt/payara/appserver/glassfish/domains/domain1/lib

# ASADMIN Commands to create JDBC-Resource
COPY --chown=payara:payara ./post-boot-commands.asadmin /opt/payara/config/

