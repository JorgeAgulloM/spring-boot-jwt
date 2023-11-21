/* Populate tables*/
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('John', 'Does', 'john@does.com', '2023-08-23', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Alice', 'Johnson', 'alice.johnson@email.com', '2023-09-05', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Bobi', 'Smith', 'bob.smith@email.com', '2023-09-10', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Catherine', 'Williams', 'catherine@email.com', '2023-09-15', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('David', 'Brown', 'david.brown@email.com', '2023-09-20', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Emily', 'Jones', 'emily.jones@email.com', '2023-09-25', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Frank', 'Miller', 'frank.miller@email.com', '2023-09-30', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Grace', 'Davis', 'grace.davis@email.com', '2023-10-05', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Henry', 'Martinez', 'henry.martinez@email.com', '2023-10-10', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Isabel', 'Clark', 'isabel.clark@email.com', '2023-10-15', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Jack', 'Adams', 'jack.adams@email.com', '2023-10-20', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Kelly', 'Leen', 'kelly.lee@email.com', '2023-10-25', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Leon', 'Wong', 'leo.wong@email.com', '2023-10-30', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Mias', 'Gomez', 'mia.gomez@email.com', '2023-11-05', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Nathan', 'Turner', 'nathan.turner@email.com', '2023-11-10', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Olivia', 'Hernandez', 'olivia.hernandez@email.com', '2023-11-15', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Paul', 'Roberts', 'paul.roberts@email.com', '2023-11-20', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Quinn', 'Fisher', 'quinn.fisher@email.com', '2023-11-25', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Ryan', 'Cooper', 'ryan.cooper@email.com', '2023-12-01', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Samantha', 'Lopez', 'samantha.lopez@email.com', '2023-12-05', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Tomi', 'Nguyen', 'tom.nguyen@email.com', '2023-12-10', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Ursula', 'Walker', 'ursula.walker@email.com', '2023-12-15', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Vincent', 'Chen', 'vincent.chen@email.com', '2023-12-20', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Wendy', 'Kimi', 'wendy.kim@email.com', '2023-12-25', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Xavier', 'Liun', 'xavier.liu@email.com', '2023-12-30', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Yasmine', 'Ramirez', 'yasmine.ramirez@email.com', '2024-01-05', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Zachary', 'Stewart', 'zachary.stewart@email.com', '2024-01-10', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Emma', 'Perez', 'emma.perez@email.com', '2024-01-15', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Felix', 'Rodriguez', 'felix.rodriguez@email.com', '2024-01-20', '');
INSERT INTO clientes (nombre, apellido, email, create_at, photo) VALUES ('Gabriela', 'Torres', 'gabriela.torres@email.com', '2024-01-25', '');

/* Populate tabla productos */
INSERT INTO productos (nombre, precio, create_at) VALUES ('Samsung Galaxy S21', 899, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('HP Envy x360', 1299, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('LG 55" OLED TV', 1499, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Canon EOS R5', 3499, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Sony WH-1000XM4', 349, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Samsung Side-by-Side Refrigerator', 1799, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Dell XPS 13', 1299, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Dyson V11 Vacuum Cleaner', 499, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('ASUS ROG Swift Gaming Monitor', 699, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Epson EcoTank Printer', 299, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Corsair K95 RGB Mechanical Keyboard', 169, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Trek Electric Bike', 2499, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Sonos Play:5 Speakers', 499, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Nespresso Vertuo Coffee Maker', 199, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Ikea Ekedalen Dining Table', 299, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Nike Air Max Sneakers', 129, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Sapiens: De Animales a Dioses', 25, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Ring Doorbell Security Camera', 199, NOW());

/* Facturas */
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura equipos de oficina', null, 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(3, 1, 5);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 7);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(1, 1, 3);

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura bicicleta', 'Alguna nota importante!', 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES(3, 2, 11);

/* Users */
INSERT INTO users (username, password, enabled) VALUES('Jorge', '$2a$10$WEx3gU0sAQmEpkr5Boznc.jbU02d5Ynn6l2O6j3fxmwvTE9EJCKA2', 1);
INSERT INTO users (username, password, enabled) VALUES('admin', '$2a$10$.qo.KaT9R5KUul11mEIauOh4gCf6xLyzRq1ZxnqfR7QN7u4kyAyhC', 1);

/* ROLES */
INSERT INTO authorities (user_id, authority) VALUES(1, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES(2, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES(2, 'ROLE_ADMIN');
