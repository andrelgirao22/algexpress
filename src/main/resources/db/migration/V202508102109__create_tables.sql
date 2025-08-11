-- AlgExpress Database Schema Creation
-- Migration: V202508102109__create_tables.sql

-- Create customers table
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    loyalty_points INTEGER NOT NULL DEFAULT 0,
    
    CONSTRAINT ck_customer_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'BLOCKED'))
);

-- Create addresses table
CREATE TABLE addresses (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    street VARCHAR(200) NOT NULL,
    number VARCHAR(10) NOT NULL,
    complement VARCHAR(100),
    neighborhood VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    distance_km DECIMAL(8,3),
    delivery_fee DECIMAL(8,2),
    type VARCHAR(20) NOT NULL DEFAULT 'RESIDENTIAL',
    reference_points VARCHAR(200),
    
    CONSTRAINT fk_address_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    CONSTRAINT ck_address_type CHECK (type IN ('RESIDENTIAL', 'COMMERCIAL', 'OTHER'))
);

-- Create ingredients table
CREATE TABLE ingredients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    additional_price DECIMAL(8,2) DEFAULT 0.00,
    category VARCHAR(20) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    allergenic BOOLEAN NOT NULL DEFAULT FALSE,
    
    CONSTRAINT ck_ingredient_category CHECK (category IN ('PROTEIN', 'CHEESE', 'VEGETABLE', 'SAUCE', 'SEASONING', 'OTHER'))
);

-- Create pizzas table
CREATE TABLE pizzas (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(300),
    price_small DECIMAL(8,2),
    price_medium DECIMAL(8,2),
    price_large DECIMAL(8,2),
    price_extra_large DECIMAL(8,2),
    category VARCHAR(20) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    image_url VARCHAR(500),
    preparation_time_minutes INTEGER,
    
    CONSTRAINT ck_pizza_category CHECK (category IN ('TRADITIONAL', 'SPECIAL', 'PREMIUM', 'SWEET', 'VEGAN', 'LACTOSE_FREE'))
);

-- Create pizza_ingredients junction table
CREATE TABLE pizza_ingredients (
    pizza_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    
    PRIMARY KEY (pizza_id, ingredient_id),
    CONSTRAINT fk_pizza_ingredients_pizza FOREIGN KEY (pizza_id) REFERENCES pizzas(id) ON DELETE CASCADE,
    CONSTRAINT fk_pizza_ingredients_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
);

-- Create delivery_persons table
CREATE TABLE delivery_persons (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    document VARCHAR(20) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    vehicle_type VARCHAR(20) NOT NULL,
    vehicle_plate VARCHAR(10),
    shift_start TIMESTAMP,
    shift_end TIMESTAMP,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT ck_delivery_person_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'ON_DELIVERY', 'ON_BREAK')),
    CONSTRAINT ck_vehicle_type CHECK (vehicle_type IN ('MOTORCYCLE', 'CAR', 'BICYCLE', 'ON_FOOT'))
);

-- Create orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    delivery_address_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    type VARCHAR(20) NOT NULL,
    order_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estimated_date_time TIMESTAMP,
    completion_date_time TIMESTAMP,
    subtotal DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    delivery_fee DECIMAL(8,2) DEFAULT 0.00,
    discount DECIMAL(8,2) DEFAULT 0.00,
    total DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    observations VARCHAR(300),
    estimated_time_minutes INTEGER,
    
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_order_delivery_address FOREIGN KEY (delivery_address_id) REFERENCES addresses(id),
    CONSTRAINT ck_order_status CHECK (status IN ('PENDING', 'CONFIRMED', 'PREPARING', 'READY', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED')),
    CONSTRAINT ck_order_type CHECK (type IN ('DELIVERY', 'PICKUP', 'DINE_IN'))
);

-- Create order_items table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    pizza_id BIGINT NOT NULL,
    size VARCHAR(20) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(8,2) NOT NULL,
    total_price DECIMAL(8,2) NOT NULL,
    observations VARCHAR(300),
    
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_pizza FOREIGN KEY (pizza_id) REFERENCES pizzas(id),
    CONSTRAINT ck_pizza_size CHECK (size IN ('SMALL', 'MEDIUM', 'LARGE', 'EXTRA_LARGE')),
    CONSTRAINT ck_quantity_positive CHECK (quantity > 0)
);

-- Create order_item_additional_ingredients junction table
CREATE TABLE order_item_additional_ingredients (
    order_item_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    
    PRIMARY KEY (order_item_id, ingredient_id),
    CONSTRAINT fk_additional_ingredients_order_item FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE,
    CONSTRAINT fk_additional_ingredients_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- Create order_item_removed_ingredients junction table
CREATE TABLE order_item_removed_ingredients (
    order_item_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    
    PRIMARY KEY (order_item_id, ingredient_id),
    CONSTRAINT fk_removed_ingredients_order_item FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE,
    CONSTRAINT fk_removed_ingredients_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- Create payments table
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    amount DECIMAL(8,2) NOT NULL,
    amount_paid DECIMAL(8,2),
    change DECIMAL(8,2) DEFAULT 0.00,
    payment_date_time TIMESTAMP,
    due_date_time TIMESTAMP,
    transaction_id VARCHAR(100),
    authorization_code VARCHAR(50),
    observations VARCHAR(300),
    
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT ck_payment_method CHECK (payment_method IN ('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'PIX', 'MEAL_VOUCHER', 'FOOD_VOUCHER')),
    CONSTRAINT ck_payment_status CHECK (status IN ('PENDING', 'PROCESSING', 'APPROVED', 'REJECTED', 'CANCELLED', 'REFUNDED')),
    CONSTRAINT ck_amount_positive CHECK (amount > 0)
);

-- Create deliveries table
CREATE TABLE deliveries (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    delivery_person_id BIGINT,
    status VARCHAR(30) NOT NULL DEFAULT 'WAITING_DELIVERY_PERSON',
    departure_time TIMESTAMP,
    delivery_time TIMESTAMP,
    return_time TIMESTAMP,
    distance_km DECIMAL(8,3),
    delivery_fee DECIMAL(8,2),
    observations VARCHAR(300),
    delivery_attempts INTEGER NOT NULL DEFAULT 0,
    cancellation_reason VARCHAR(200),
    
    CONSTRAINT fk_delivery_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_delivery_person FOREIGN KEY (delivery_person_id) REFERENCES delivery_persons(id),
    CONSTRAINT ck_delivery_status CHECK (status IN ('WAITING_DELIVERY_PERSON', 'EN_ROUTE', 'DELIVERY_ATTEMPT', 'DELIVERED', 'CANCELLED', 'RETURNED'))
);

-- Create indexes for better performance
CREATE INDEX idx_customers_phone ON customers(phone);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_addresses_customer ON addresses(customer_id);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_date ON orders(order_date_time);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_payments_order ON payments(order_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_deliveries_order ON deliveries(order_id);
CREATE INDEX idx_deliveries_person ON deliveries(delivery_person_id);
CREATE INDEX idx_deliveries_status ON deliveries(status);

-- Insert initial data
INSERT INTO ingredients (name, description, category, additional_price) VALUES
('Mozzarella', 'Traditional mozzarella cheese', 'CHEESE', 0.00),
('Pepperoni', 'Spicy pepperoni slices', 'PROTEIN', 3.00),
('Mushrooms', 'Fresh mushrooms', 'VEGETABLE', 2.00),
('Tomato Sauce', 'Traditional tomato sauce', 'SAUCE', 0.00),
('Oregano', 'Dried oregano', 'SEASONING', 0.00);

INSERT INTO pizzas (name, description, category, price_small, price_medium, price_large, price_extra_large, preparation_time_minutes) VALUES
('Margherita', 'Traditional pizza with mozzarella, tomato sauce and basil', 'TRADITIONAL', 25.00, 35.00, 45.00, 55.00, 15),
('Pepperoni', 'Classic pepperoni pizza', 'TRADITIONAL', 30.00, 40.00, 50.00, 60.00, 18);

-- Link pizzas with their default ingredients
INSERT INTO pizza_ingredients (pizza_id, ingredient_id) 
SELECT p.id, i.id 
FROM pizzas p, ingredients i 
WHERE p.name = 'Margherita' AND i.name IN ('Mozzarella', 'Tomato Sauce', 'Oregano');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id) 
SELECT p.id, i.id 
FROM pizzas p, ingredients i 
WHERE p.name = 'Pepperoni' AND i.name IN ('Mozzarella', 'Tomato Sauce', 'Pepperoni', 'Oregano');