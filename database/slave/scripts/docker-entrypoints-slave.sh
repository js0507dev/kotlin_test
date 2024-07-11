#!/bin/bash
set -e

# (1)
until mysqladmin -u root -p"${MYSQL_ROOT_PASSWORD}" -h 172.28.0.2 ping; do
  echo "# waiting for master - $(date)"
  sleep 3
done

# (2)
mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "CREATE USER 'replUser'@'%' IDENTIFIED BY '${MYSQL_USER_PASSWORD}'"
mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "GRANT ALL PRIVILEGES ON *.* TO 'replUser'@'%' WITH GRANT OPTION"
mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "FLUSH PRIVILEGES"

# (3)
source_log_file=$(mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -h 172.28.0.2 -e "SHOW MASTER STATUS\G" | grep mysql-bin)
re="[a-z]*-bin.[0-9]*"
if [[ $source_log_file =~ $re ]];then
  source_log_file=${BASH_REMATCH[0]}
fi

# (4)
source_log_pos=$(mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -h 172.28.0.2 -e "SHOW MASTER STATUS\G" | grep Position)
re="[0-9]+"
if [[ $source_log_pos =~ $re ]];then
  source_log_pos=${BASH_REMATCH[0]}
fi

# (5)
sql="CHANGE REPLICATION SOURCE TO SOURCE_HOST='172.28.0.2', SOURCE_USER='replUser', SOURCE_PASSWORD='${MYSQL_USER_PASSWORD}', SOURCE_LOG_FILE='${source_log_file}', SOURCE_LOG_POS=${source_log_pos}, GET_SOURCE_PUBLIC_KEY=1"

mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "${sql}"
mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "START REPLICA"

# (6)
mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -h 172.28.0.2 -e "CREATE DATABASE ${MYSQL_DB}"
