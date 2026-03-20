FROM tomcat:9.0-jdk8

LABEL maintainer="trade-system"

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/stock-trading-1.0.0.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
