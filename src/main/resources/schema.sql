CREATE SEQUENCE IF NOT EXISTS customer_seq;

CREATE TABLE IF NOT EXISTS customer
(
    id    BIGINT NOT NULL DEFAULT nextval('customer_seq') PRIMARY KEY,
    fname VARCHAR(255),
    lname VARCHAR(255),
    email VARCHAR(255),
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS address_seq;

CREATE TABLE IF NOT EXISTS address
(
    id          BIGINT NOT NULL DEFAULT nextval('address_seq') PRIMARY KEY,
    state       VARCHAR(255),
    city        VARCHAR(255),
    street      VARCHAR(255),
    zip_code    VARCHAR(255),
    customer_id BIGINT,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

ALTER TABLE address
    ADD CONSTRAINT IF NOT EXISTS FK_ADDRESS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

CREATE SEQUENCE IF NOT EXISTS category_seq;

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT NOT NULL DEFAULT nextval('category_seq') PRIMARY KEY,
    name VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

ALTER TABLE category
    ADD CONSTRAINT IF NOT EXISTS uc_category_name UNIQUE (name);

CREATE SEQUENCE IF NOT EXISTS product_seq;

CREATE TABLE IF NOT EXISTS product
(
    id          BIGINT NOT NULL DEFAULT nextval('product_seq') PRIMARY KEY,
    name        VARCHAR(255),
    price       DECIMAL,
    category_id BIGINT,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

ALTER TABLE product
    ADD CONSTRAINT IF NOT EXISTS uc_product_name UNIQUE (name);

ALTER TABLE product
    ADD CONSTRAINT IF NOT EXISTS FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);


CREATE SEQUENCE IF NOT EXISTS order_seq;

CREATE TABLE IF NOT EXISTS order_
(
    id            BIGINT NOT NULL DEFAULT nextval('order_seq') PRIMARY KEY,
    status        VARCHAR(255),
    creation_date TIMESTAMP,
    price         DECIMAL,
    tax           DECIMAL,
    customer_id   BIGINT,
    address_id    BIGINT,
    CONSTRAINT pk_order_ PRIMARY KEY (id)
);

ALTER TABLE  order_
    ADD CONSTRAINT IF NOT EXISTS FK_ORDER__ON_ADDRESS FOREIGN KEY (address_id) REFERENCES address (id);

ALTER TABLE order_
    ADD CONSTRAINT IF NOT EXISTS FK_ORDER__ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);


CREATE TABLE IF NOT EXISTS order_product
(
    order_id            BIGINT NOT NULL,
    product_id            BIGINT NOT NULL
);
ALTER TABLE  order_product
    ADD CONSTRAINT IF NOT EXISTS FK_ORDER_PRODUCT_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE order_product
    ADD CONSTRAINT IF NOT EXISTS FK_ORDER_PRODUCT_ON_ORDER_ FOREIGN KEY (order_id) REFERENCES order_ (id);
