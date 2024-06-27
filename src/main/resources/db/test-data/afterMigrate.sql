SET foreign_key_checks = 0;

TRUNCATE TABLE billionaires;

SET foreign_key_checks = 1;

INSERT INTO billionaires (first_name, last_name, career, idempotency_id) VALUES ('Aliko', 'Dangote', 'Billionaire Industrialist', UUID()),
                                                                                ('Bill', 'Gates', 'Billionaire Tech Entrepreneur', UUID()),
                                                                                ('Folrunsho', 'Alakija', 'Billionaire Oil Magnate', UUID());
