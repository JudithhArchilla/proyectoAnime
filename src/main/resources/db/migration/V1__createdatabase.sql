DROP TABLE IF EXISTS anime CASCADE;

CREATE TABLE anime (
    id uuid NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    name text,
    description text,
    type text,
    year text,
    image text);

-- afegim dades de prova
INSERT INTO anime(name, description, type, year, image) VALUES
    ('Anime I','Anime I description', 'TV', '2021', 'image1.jpg'),
    ('Anime II','Anime II description', 'Film', '2020', 'image2.jpg');



  --('Anime III','Anime III description', 'TV', '2019','image3.jpg');
