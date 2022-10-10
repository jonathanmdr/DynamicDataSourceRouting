SET foreign_key_checks = 0;

TRUNCATE TABLE billionaire;

SET foreign_key_checks = 1;

INSERT INTO billionaire (first_name, last_name, career, idempotency_id) VALUES ('Aliko', 'Dangote', 'Billionaire Industrialist', UUID()),
                                                                               ('Bill', 'Gates', 'Billionaire Tech Entrepreneur', UUID()),
                                                                               ('Folrunsho', 'Alakija', 'Billionaire Oil Magnate', UUID());
