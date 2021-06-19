CREATE TABLE billionaire (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  career VARCHAR(250) DEFAULT NULL
);

INSERT INTO billionaire (first_name, last_name, career) VALUES ('Aliko', 'Dangote', 'Billionaire Industrialist'),
                                                               ('Bill', 'Gates', 'Billionaire Tech Entrepreneur'),
                                                               ('Folrunsho', 'Alakija', 'Billionaire Oil Magnate');
