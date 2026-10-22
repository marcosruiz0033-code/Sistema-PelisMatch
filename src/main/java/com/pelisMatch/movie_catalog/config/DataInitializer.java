package com.pelisMatch.movie_catalog.config;

import com.pelisMatch.movie_catalog.model.Categoria;
import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.repository.CategoriaRepository;
import com.pelisMatch.movie_catalog.repository.PeliculaRepository;
import com.pelisMatch.movie_catalog.repository.SerieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.entry;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatos(CategoriaRepository categoriaRepository,
                                  SerieRepository serieRepository,
                                  PeliculaRepository peliculaRepository) {
        return args -> {
            Map<String, Categoria> categorias = cargarCategorias(categoriaRepository);
            cargarSeries(serieRepository, categorias);
            cargarPeliculas(peliculaRepository);
        };
    }

    private Map<String, Categoria> cargarCategorias(CategoriaRepository repository) {
        return Map.ofEntries(
                entry("Acción", obtenerCategoria(repository, "Acción", "Peleas, persecuciones y escenas de alto impacto")),
                entry("Comedia", obtenerCategoria(repository, "Comedia", "Historias para reír y pasarla bien")),
                entry("Drama", obtenerCategoria(repository, "Drama", "Historias intensas y emocionales")),
                entry("Terror", obtenerCategoria(repository, "Terror", "Historias de miedo y suspense oscuro")),
                entry("Ciencia ficción", obtenerCategoria(repository, "Ciencia ficción", "Futuro, tecnología y mundos imaginarios")),
                entry("Animación", obtenerCategoria(repository, "Animación", "Series animadas para todos los públicos")),
                entry("Suspenso", obtenerCategoria(repository, "Suspenso", "Giros argumentales y misterios")),
                entry("Aventura", obtenerCategoria(repository, "Aventura", "Viajes, descubrimientos y acción"))
        );
    }

    private Categoria obtenerCategoria(CategoriaRepository repository, String nombre, String descripcion) {
        return repository.findByNombre(nombre).orElseGet(() -> {
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            return repository.save(categoria);
        });
    }

    private void cargarSeries(SerieRepository repository, Map<String, Categoria> categorias) {
        Map<String, Serie> existentes = repository.findAll().stream()
                .collect(Collectors.toMap(
                        serie -> serie.getTitulo().toLowerCase(Locale.ROOT),
                        Function.identity(),
                        (primera, segunda) -> primera
                ));

        for (SerieSeed seed : series()) {
            Serie serie = existentes.get(seed.titulo().toLowerCase(Locale.ROOT));
            boolean nueva = serie == null;

            if (nueva) {
                serie = new Serie();
                serie.setTitulo(seed.titulo());
            }

            if (nueva || vacio(serie.getSinopsis())) {
                serie.setSinopsis(seed.sinopsis());
            }
            if (nueva || serie.getAnio() == null) {
                serie.setAnio(seed.anio());
            }
            if (nueva || vacio(serie.getImagen())) {
                serie.setImagen(seed.imagen());
            }
            if (nueva || vacio(serie.getEstado())) {
                serie.setEstado(seed.estado());
            }
            if (nueva || serie.getTemporadas() == null) {
                serie.setTemporadas(seed.temporadas());
            }
            if (nueva || serie.getCategoria() == null) {
                serie.setCategoria(categorias.get(seed.categoria()));
            }
            if (nueva || vacio(serie.getUrlNetflix())) {
                serie.setUrlNetflix(seed.urlNetflix());
            }
            if (nueva || vacio(serie.getUrlPrimeVideo())) {
                serie.setUrlPrimeVideo(seed.urlPrimeVideo());
            }
            if (nueva || vacio(serie.getUrlDisneyPlus())) {
                serie.setUrlDisneyPlus(seed.urlDisneyPlus());
            }
            if (nueva || vacio(serie.getUrlMax())) {
                serie.setUrlMax(seed.urlMax());
            }
            if (nueva || vacio(serie.getUrlAppleTv())) {
                serie.setUrlAppleTv(seed.urlAppleTv());
            }

            repository.save(serie);
        }
    }

    private void cargarPeliculas(PeliculaRepository repository) {
        Map<String, Pelicula> existentes = repository.findAll().stream()
                .collect(Collectors.toMap(
                        pelicula -> pelicula.getTitulo().toLowerCase(Locale.ROOT),
                        Function.identity(),
                        (primera, segunda) -> primera
                ));

        for (PeliculaSeed seed : peliculas()) {
            Pelicula pelicula = existentes.get(seed.titulo().toLowerCase(Locale.ROOT));
            boolean nueva = pelicula == null;

            if (nueva) {
                pelicula = new Pelicula();
                pelicula.setTitulo(seed.titulo());
            }

            if (nueva || vacio(pelicula.getGenero())) {
                pelicula.setGenero(seed.genero());
            }
            if (nueva || vacio(pelicula.getDirector())) {
                pelicula.setDirector(seed.director());
            }
            if (nueva || pelicula.getAnio() == 0) {
                pelicula.setAnio(seed.anio());
            }
            if (nueva || vacio(pelicula.getDescripcion())) {
                pelicula.setDescripcion(seed.descripcion());
            }

            repository.save(pelicula);
        }
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private List<SerieSeed> series() {
        return List.of(
                new SerieSeed(
                        "Stranger Things",
                        "Un grupo de niños de un pequeño pueblo descubre secretos experimentos y fuerzas sobrenaturales.",
                        2016,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/595/1489169.jpg",
                        "Finalizada",
                        5,
                        "Ciencia ficción",
                        "https://www.netflix.com/title/80057281",
                        null,
                        null,
                        null,
                        null
                ),
                new SerieSeed(
                        "Breaking Bad",
                        "Un profesor de química diagnosticado con cáncer se convierte en fabricante de metanfetamina.",
                        2008,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/501/1253519.jpg",
                        "Finalizada",
                        5,
                        "Drama",
                        "https://www.netflix.com/title/70143836",
                        null,
                        null,
                        null,
                        null
                ),
                new SerieSeed(
                        "The Last of Us",
                        "En un mundo devastado por un hongo, un contrabandista debe escoltar a una niña con inmunidad.",
                        2023,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/563/1409008.jpg",
                        "En emisión",
                        2,
                        "Suspenso",
                        null,
                        null,
                        null,
                        "https://www.hbo.com/the-last-of-us",
                        null
                ),
                new SerieSeed(
                        "Dark",
                        "La desaparición de un niño en un pueblo alemán revela un misterio que abarca varias generaciones.",
                        2017,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/504/1262352.jpg",
                        "Finalizada",
                        3,
                        "Ciencia ficción",
                        "https://www.netflix.com/title/80100172",
                        null,
                        null,
                        null,
                        null
                ),
                new SerieSeed(
                        "The Witcher",
                        "Un cazador de monstruos de cabello blanco recorre un continente peligroso en busca de su destino.",
                        2019,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/594/1486674.jpg",
                        "En emisión",
                        4,
                        "Aventura",
                        "https://www.netflix.com/title/80189685",
                        null,
                        null,
                        null,
                        null
                ),
                new SerieSeed(
                        "Wednesday",
                        "La hija de la familia Addams investiga una ola de asesinatos mientras sobrevive en su escuela.",
                        2022,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/586/1466410.jpg",
                        "En emisión",
                        2,
                        "Comedia",
                        "https://www.netflix.com/title/81231974",
                        null,
                        null,
                        null,
                        null
                ),
                new SerieSeed(
                        "The Crown",
                        "El reinado y los desafíos políticos y personales de la Reina Isabel II de Inglaterra.",
                        2016,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/632/1580063.jpg",
                        "Finalizada",
                        6,
                        "Drama",
                        "https://www.netflix.com/title/80025678",
                        null,
                        null,
                        null,
                        null
                ),
                new SerieSeed(
                        "Peaky Blinders",
                        "Una familia de gánsteres en la Inglaterra de posguerra lucha por controlar la ciudad de Birmingham.",
                        2013,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/48/122213.jpg",
                        "Finalizada",
                        6,
                        "Acción",
                        "https://www.netflix.com/title/80002479",
                        null,
                        null,
                        null,
                        null
                ),
                new SerieSeed(
                        "Game of Thrones",
                        "Nueve familias nobles luchan por el control de los Siete Reinos mientras una antigua amenaza despierta en el norte.",
                        2011,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/498/1245274.jpg",
                        "Finalizada",
                        8,
                        "Aventura",
                        null,
                        null,
                        null,
                        "https://www.hbo.com/game-of-thrones",
                        null
                ),
                new SerieSeed(
                        "Squid Game",
                        "Cientos de personas con problemas económicos aceptan una invitación para competir en juegos infantiles mortales por un premio millonario.",
                        2021,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/576/1440521.jpg",
                        "Finalizada",
                        3,
                        "Suspenso",
                        "https://www.netflix.com/title/81040344",
                        null,
                        null,
                        null,
                        null
                ),
                new SerieSeed(
                        "The Boys",
                        "Un grupo de vigilantes decide enfrentar a los superhéroes corruptos que abusan de su poder bajo la protección de una gran corporación.",
                        2019,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/619/1547768.jpg",
                        "Finalizada",
                        5,
                        "Acción",
                        null,
                        "https://www.amazon.com/dp/B07QQQ52B3",
                        null,
                        null,
                        null
                ),
                new SerieSeed(
                        "The Mandalorian",
                        "Un cazarrecompensas solitario recorre la galaxia mientras protege a un niño con una conexión especial con la Fuerza.",
                        2019,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/501/1253498.jpg",
                        "En emisión",
                        3,
                        "Aventura",
                        null,
                        null,
                        "https://www.disneyplus.com/series/the-mandalorian/3jLIGMDYINqD/",
                        null,
                        null
                ),
                new SerieSeed(
                        "Severance",
                        "Los empleados de una empresa se someten a un procedimiento que separa sus recuerdos laborales de los personales, hasta que todo empieza a fallar.",
                        2022,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/548/1371406.jpg",
                        "En emisión",
                        2,
                        "Ciencia ficción",
                        null,
                        null,
                        null,
                        null,
                        "https://tv.apple.com/show/severance/umc.cmc.1srk2goyh2q2zdxcx605w8vtx"
                ),
                new SerieSeed(
                        "Ted Lasso",
                        "Un entrenador de fútbol americano es contratado para dirigir un equipo de fútbol inglés, a pesar de no tener experiencia en el deporte.",
                        2020,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/634/1585930.jpg",
                        "En emisión",
                        3,
                        "Comedia",
                        null,
                        null,
                        null,
                        null,
                        "https://tv.apple.com/show/ted-lasso/umc.cmc.vtoh0mn0xn7t3c643xqonfzy"
                ),
                new SerieSeed(
                        "House of the Dragon",
                        "Dos ramas de la casa Targaryen se enfrentan por el trono de hierro en una guerra civil que cambiará Poniente para siempre.",
                        2022,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/627/1568449.jpg",
                        "En emisión",
                        2,
                        "Aventura",
                        null,
                        null,
                        null,
                        "https://www.hbomax.com/show/c68e69d7-9317-428a-a615-cdf8fe5a2e06/",
                        null
                ),
                new SerieSeed(
                        "La Casa de Papel",
                        "Un misterioso hombre conocido como El Profesor planea el mayor atraco de la historia con un grupo de ladrones sin nada que perder.",
                        2017,
                        "https://static.tvmaze.com/uploads/images/medium_portrait/376/940830.jpg",
                        "Finalizada",
                        5,
                        "Acción",
                        "https://www.netflix.com/title/80192098",
                        null,
                        null,
                        null,
                        null
                )
        );
    }

    private List<PeliculaSeed> peliculas() {
        return List.of(
                new PeliculaSeed(
                        "Inception",
                        "Ciencia ficción",
                        "Christopher Nolan",
                        2010,
                        "Un ladrón especializado en robar secretos a través de los sueños recibe la misión de implantar una idea en la mente de un empresario."
                ),
                new PeliculaSeed(
                        "Interstellar",
                        "Ciencia ficción",
                        "Christopher Nolan",
                        2014,
                        "Un grupo de astronautas viaja a través de un agujero de gusano para encontrar un nuevo hogar para la humanidad."
                ),
                new PeliculaSeed(
                        "The Dark Knight",
                        "Acción",
                        "Christopher Nolan",
                        2008,
                        "Batman se enfrenta al Joker, un criminal caótico decidido a sumir Gotham en el terror y demostrar que la ciudad está corrupta."
                ),
                new PeliculaSeed(
                        "The Matrix",
                        "Ciencia ficción",
                        "Lana y Lilly Wachowski",
                        1999,
                        "Un hacker descubre que la realidad es una simulación creada por máquinas y se une a la resistencia para liberar a la humanidad."
                ),
                new PeliculaSeed(
                        "Pulp Fiction",
                        "Suspenso",
                        "Quentin Tarantino",
                        1994,
                        "Las vidas de varios personajes del crimen de Los Ángeles se entrelazan en una serie de historias violentas y llenas de humor negro."
                ),
                new PeliculaSeed(
                        "The Godfather",
                        "Drama",
                        "Francis Ford Coppola",
                        1972,
                        "El patriarca de una poderosa familia mafiosa comienza a transferir el control de su imperio a su hijo menor."
                ),
                new PeliculaSeed(
                        "Fight Club",
                        "Drama",
                        "David Fincher",
                        1999,
                        "Un empleado insatisfecho con su vida forma un club clandestino de peleas que pronto se convierte en una rebelión contra el sistema."
                ),
                new PeliculaSeed(
                        "Forrest Gump",
                        "Drama",
                        "Robert Zemeckis",
                        1994,
                        "Un hombre de buen corazón recorre momentos clave de la historia de Estados Unidos mientras persigue el amor de su vida."
                ),
                new PeliculaSeed(
                        "Gladiator",
                        "Acción",
                        "Ridley Scott",
                        2000,
                        "Un general romano traicionado se convierte en gladiador y busca vengar la muerte de su familia frente al emperador."
                ),
                new PeliculaSeed(
                        "Titanic",
                        "Drama",
                        "James Cameron",
                        1997,
                        "Un joven y una joven de distintas clases sociales se enamoran a bordo del transatlántico condenado al desastre."
                ),
                new PeliculaSeed(
                        "Avatar",
                        "Aventura",
                        "James Cameron",
                        2009,
                        "Un exmarine se adentra en el planeta Pandora y se debate entre cumplir su misión o proteger un mundo y una cultura que aprende a amar."
                ),
                new PeliculaSeed(
                        "Joker",
                        "Drama",
                        "Todd Phillips",
                        2019,
                        "Un comediante fracasado y marginado encuentra en la violencia una forma de convertirse en un símbolo del caos en Gotham."
                ),
                new PeliculaSeed(
                        "Oppenheimer",
                        "Drama",
                        "Christopher Nolan",
                        2023,
                        "La historia del científico J. Robert Oppenheimer y su papel en el desarrollo de la bomba atómica durante la Segunda Guerra Mundial."
                ),
                new PeliculaSeed(
                        "Dune",
                        "Ciencia ficción",
                        "Denis Villeneuve",
                        2021,
                        "El heredero de una noble familia llega a un planeta desértico y descubre que su destino está ligado a una guerra por el recurso más valioso del universo."
                ),
                new PeliculaSeed(
                        "Spider-Man: No Way Home",
                        "Acción",
                        "Jon Watts",
                        2021,
                        "Peter Parker abre una grieta en el multiverso y debe enfrentar a villanos de otras realidades para restaurar el equilibrio."
                )
        );
    }

    private record SerieSeed(String titulo,
                             String sinopsis,
                             int anio,
                             String imagen,
                             String estado,
                             int temporadas,
                             String categoria,
                             String urlNetflix,
                             String urlPrimeVideo,
                             String urlDisneyPlus,
                             String urlMax,
                             String urlAppleTv) {
    }

    private record PeliculaSeed(String titulo,
                                String genero,
                                String director,
                                int anio,
                                String descripcion) {
    }
}
