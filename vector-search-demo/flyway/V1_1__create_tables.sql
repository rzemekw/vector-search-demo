CREATE TABLE image.image
(
    id bigserial PRIMARY KEY,
    data BYTEA not null,
    filename varchar(255) NOT NULL
);

CREATE TABLE vector.vector
(
    id bigserial PRIMARY KEY,
    data BYTEA not null
);

CREATE TABLE product.product
(
    id bigserial PRIMARY KEY,
    description varchar(2048),
    image_id bigint REFERENCES image.image(id),
    image_vector_id bigint REFERENCES vector.vector(id),
    description_vector_id bigint REFERENCES vector.vector(id)
);