SET foreign_key_checks = 0;

TRUNCATE TABLE billionaire;

SET foreign_key_checks = 1;

INSERT INTO billionaire (first_name, last_name, career) VALUES ('Aliko', 'Dangote', 'Billionaire Industrialist'),
                                                               ('Bill', 'Gates', 'Billionaire Tech Entrepreneur'),
                                                               ('Folrunsho', 'Alakija', 'Billionaire Oil Magnate');
