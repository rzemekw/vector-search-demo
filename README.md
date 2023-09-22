# 1. docker-compose
run docker-compose.up
# 2. setup elastic-search index
cd elasticsearch
./init.search
# 2. flyway migration
run flywayMigrate gradle task