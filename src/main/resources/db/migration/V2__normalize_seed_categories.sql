UPDATE peliculas
SET id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Acción')
WHERE id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Acci├│n')
  AND EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Acción');

UPDATE serie
SET id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Acción')
WHERE id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Acci├│n')
  AND EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Acción');

DELETE FROM categoria
WHERE nombre = 'Acci├│n'
  AND EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Acción');

UPDATE categoria
SET nombre = 'Acción',
    descripcion = 'Peleas, persecuciones y escenas de alto impacto'
WHERE nombre = 'Acci├│n';

UPDATE peliculas
SET id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Ciencia ficción')
WHERE id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Ciencia ficci├│n')
  AND EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Ciencia ficción');

UPDATE serie
SET id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Ciencia ficción')
WHERE id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Ciencia ficci├│n')
  AND EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Ciencia ficción');

DELETE FROM categoria
WHERE nombre = 'Ciencia ficci├│n'
  AND EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Ciencia ficción');

UPDATE categoria
SET nombre = 'Ciencia ficción',
    descripcion = 'Futuro, tecnología y mundos imaginarios'
WHERE nombre = 'Ciencia ficci├│n';

UPDATE peliculas
SET id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Animación')
WHERE id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Animaci├│n')
  AND EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Animación');

UPDATE serie
SET id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Animación')
WHERE id_categoria = (SELECT id_categoria FROM categoria WHERE nombre = 'Animaci├│n')
  AND EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Animación');

DELETE FROM categoria
WHERE nombre = 'Animaci├│n'
  AND EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Animación');

UPDATE categoria
SET nombre = 'Animación',
    descripcion = 'Animación para todos los públicos'
WHERE nombre = 'Animaci├│n';
