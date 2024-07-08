FROM mysql:8.0.38

USER root
COPY ./my.cnf /etc/mysql/my.cnf
RUN mkdir /var/log/mysql && touch /var/log/mysql/error.log && chmod -R 777 /var/log/mysql/
