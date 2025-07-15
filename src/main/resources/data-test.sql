INSERT INTO role (id, name) VALUES
    (1, 'CLIENT'),
    (2, 'ADMIN'),
    (3, 'SELLER');

INSERT INTO user (id, email, password, role_id) VALUES
    (1, 'john.client@example.com', 'root', 1),
    (2, 'jane.admin@example.com', 'root', 2),
    (3, 'mike.seller@example.com', 'root', 3),
    (4, 'sarah.client@example.com', 'root', 1),
    (5, 'bob.admin@example.com', 'root', 2);

INSERT INTO product (id, name, price, description, stock, admin_id, seller_id, client_id) VALUES
    (1, 'Laptop Dell XPS 13', 1299.99, 'High-performance ultrabook with 13-inch display', 15, 2, 3, 1),
    (2, 'Wireless Mouse', 29.99, 'Ergonomic wireless mouse with USB receiver', 50, 2, 3, 4),
    (3, 'Mechanical Keyboard', 89.99, 'RGB backlit mechanical keyboard with blue switches', 25, 5, 3, 1),
    (4, 'Monitor 4K 27-inch', 399.99, '27-inch 4K UHD monitor with HDR support', 8, 2, 3, NULL),
    (5, 'USB-C Hub', 49.99, 'Multi-port USB-C hub with HDMI and ethernet', 30, 5, 3, 4),
    (6, 'Webcam HD', 79.99, '1080p HD webcam with auto-focus and noise reduction', 20, 2, 3, 1),
    (7, 'Smartphone Stand', 15.99, 'Adjustable aluminum smartphone and tablet stand', 100, 5, 3, NULL),
    (8, 'Bluetooth Headphones', 149.99, 'Noise-cancelling over-ear Bluetooth headphones', 12, 2, 3, 4);
