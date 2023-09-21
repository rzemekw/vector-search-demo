CREATE DATABASE vector_search_demo;

CREATE USER vector_search_demo_admin WITH PASSWORD 'PostgresPassword123!';

GRANT ALL PRIVILEGES ON DATABASE vector_search_demo TO vector_search_demo_admin;