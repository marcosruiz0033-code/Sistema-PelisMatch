-- ===== Datos de prueba — categorías (solo si no existen) =====

INSERT INTO categoria (nombre, descripcion)
SELECT 'Acción', 'Peleas, persecuciones y escenas de alto impacto'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Acción');

INSERT INTO categoria (nombre, descripcion)
SELECT 'Comedia', 'Historias para reír y pasarla bien'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Comedia');

INSERT INTO categoria (nombre, descripcion)
SELECT 'Drama', 'Historias intensas y emocionales'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Drama');

INSERT INTO categoria (nombre, descripcion)
SELECT 'Terror', 'Historias de miedo y suspense oscuro'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Terror');

INSERT INTO categoria (nombre, descripcion)
SELECT 'Ciencia ficción', 'Futuro, tecnología y mundos imaginarios'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Ciencia ficción');

INSERT INTO categoria (nombre, descripcion)
SELECT 'Animación', 'Series animadas para todos los públicos'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Animación');

INSERT INTO categoria (nombre, descripcion)
SELECT 'Suspenso', 'Giros argumentales y misterios'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Suspenso');

INSERT INTO categoria (nombre, descripcion)
SELECT 'Aventura', 'Viajes, descubrimientos y acción'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Aventura');

-- ===== Datos de prueba — series (solo si no existen) =====

INSERT INTO serie (titulo, sinopsis, anio, imagen, estado, temporadas, id_categoria)
SELECT 'Stranger Things',
       'Un grupo de niños de un pequeño pueblo descubre secretos experimentos y fuerzas sobrenaturales.',
       2016, NULL, 'En emisión', 4,
       (SELECT id_categoria FROM categoria WHERE nombre = 'Ciencia ficción')
WHERE NOT EXISTS (SELECT 1 FROM serie WHERE titulo = 'Stranger Things');

INSERT INTO serie (titulo, sinopsis, anio, imagen, estado, temporadas, id_categoria)
SELECT 'Breaking Bad',
       'Un profesor de química diagnosticado con cáncer se convierte en fabricante de metanfetamina.',
       2008, NULL, 'Finalizada', 5,
       (SELECT id_categoria FROM categoria WHERE nombre = 'Drama')
WHERE NOT EXISTS (SELECT 1 FROM serie WHERE titulo = 'Breaking Bad');

INSERT INTO serie (titulo, sinopsis, anio, imagen, estado, temporadas, id_categoria)
SELECT 'The Last of Us',
       'En un mundo devastado por un hongo, un contrabandista debe escoltar a una niña con inmunidad.',
       2023, NULL, 'En emisión', 2,
       (SELECT id_categoria FROM categoria WHERE nombre = 'Suspenso')
WHERE NOT EXISTS (SELECT 1 FROM serie WHERE titulo = 'The Last of Us');

INSERT INTO serie (titulo, sinopsis, anio, imagen, estado, temporadas, id_categoria)
SELECT 'Dark',
       'La desaparición de un niño en un pueblo alemán revela un misterio que abarca varias generaciones.',
       2017, NULL, 'Finalizada', 3,
       (SELECT id_categoria FROM categoria WHERE nombre = 'Ciencia ficción')
WHERE NOT EXISTS (SELECT 1 FROM serie WHERE titulo = 'Dark');

INSERT INTO serie (titulo, sinopsis, anio, imagen, estado, temporadas, id_categoria)
SELECT 'The Witcher',
       'Un cazador de monstruos de cabello blanco recorre un continente peligroso en busca de su destino.',
       2019, NULL, 'En emisión', 3,
       (SELECT id_categoria FROM categoria WHERE nombre = 'Aventura')
WHERE NOT EXISTS (SELECT 1 FROM serie WHERE titulo = 'The Witcher');

INSERT INTO serie (titulo, sinopsis, anio, imagen, estado, temporadas, id_categoria)
SELECT 'Wednesday',
       'La hija de la familia Addams investiga una ola de asesinatos mientras sobrevive en su escuela.',
       2022, NULL, 'En emisión', 1,
       (SELECT id_categoria FROM categoria WHERE nombre = 'Comedia')
WHERE NOT EXISTS (SELECT 1 FROM serie WHERE titulo = 'Wednesday');

INSERT INTO serie (titulo, sinopsis, anio, imagen, estado, temporadas, id_categoria)
SELECT 'The Crown',
       'El reinado y los desafíos políticos y personales de la Reina Isabel II de Inglaterra.',
       2016, NULL, 'Finalizada', 6,
       (SELECT id_categoria FROM categoria WHERE nombre = 'Drama')
WHERE NOT EXISTS (SELECT 1 FROM serie WHERE titulo = 'The Crown');

INSERT INTO serie (titulo, sinopsis, anio, imagen, estado, temporadas, id_categoria)
SELECT 'Peaky Blinders',
       'Una familia de gánsteres en la Inglaterra de posguerra lucha por controlar la ciudad de Birmingham.',
       2013, NULL, 'Finalizada', 6,
       (SELECT id_categoria FROM categoria WHERE nombre = 'Acción')
WHERE NOT EXISTS (SELECT 1 FROM serie WHERE titulo = 'Peaky Blinders');
