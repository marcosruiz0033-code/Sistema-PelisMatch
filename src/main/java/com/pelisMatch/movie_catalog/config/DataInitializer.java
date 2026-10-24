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
            cargarPeliculas(peliculaRepository, categorias);
        };
    }

    private Map<String, Categoria> cargarCategorias(CategoriaRepository repository) {
        return Map.ofEntries(
                entry("Acci├│n", obtenerCategoria(repository, "Acci├│n", "Peleas, persecuciones y escenas de alto impacto")),
                entry("Comedia", obtenerCategoria(repository, "Comedia", "Historias para re├¡r y pasarla bien")),
                entry("Drama", obtenerCategoria(repository, "Drama", "Historias intensas y emocionales")),
                entry("Terror", obtenerCategoria(repository, "Terror", "Historias de miedo y suspense oscuro")),
                entry("Ciencia ficci├│n", obtenerCategoria(repository, "Ciencia ficci├│n", "Futuro, tecnolog├¡a y mundos imaginarios")),
                entry("Animaci├│n", obtenerCategoria(repository, "Animaci├│n", "Animaci├│n para todos los p├║blicos")),
                entry("Suspenso", obtenerCategoria(repository, "Suspenso", "Giros argumentales y misterios")),
                entry("Aventura", obtenerCategoria(repository, "Aventura", "Viajes, descubrimientos y acci├│n"))
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

        String fallbackImg = "https://image.tmdb.org/t/p/w500/wwemzKWzjKYJFfCeiB57q3r4Bcm.png";
        String fallbackTrailer = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

        for (SerieSeed seed : series()) {
            Serie serie = existentes.get(seed.titulo().toLowerCase(Locale.ROOT));
            boolean nueva = serie == null;

            if (nueva) {
                serie = new Serie();
                serie.setTitulo(seed.titulo());
            }

            if (nueva || vacio(serie.getSinopsis())) serie.setSinopsis(seed.sinopsis());
            if (nueva || serie.getAnio() == null) serie.setAnio(seed.anio());
            if (nueva || vacio(serie.getImagen())) serie.setImagen(vacio(seed.imagen()) ? fallbackImg : seed.imagen());
            if (nueva || vacio(serie.getEstado())) serie.setEstado(seed.estado());
            if (nueva || serie.getTemporadas() == null) serie.setTemporadas(seed.temporadas());
            if (nueva || serie.getCategoria() == null) serie.setCategoria(categorias.get(seed.categoria()));
            if (vacio(serie.getUrlTrailer())) serie.setUrlTrailer(vacio(seed.urlTrailer()) ? fallbackTrailer : seed.urlTrailer());
            if (vacio(serie.getUrlNetflix())) serie.setUrlNetflix(seed.urlNetflix());
            if (vacio(serie.getUrlPrimeVideo())) serie.setUrlPrimeVideo(seed.urlPrimeVideo());
            if (vacio(serie.getUrlDisneyPlus())) serie.setUrlDisneyPlus(seed.urlDisneyPlus());
            if (vacio(serie.getUrlMax())) serie.setUrlMax(seed.urlMax());
            if (vacio(serie.getUrlAppleTv())) serie.setUrlAppleTv(seed.urlAppleTv());

            if (vacio(serie.getUrlNetflix()) && vacio(serie.getUrlPrimeVideo()) && vacio(serie.getUrlDisneyPlus())
                    && vacio(serie.getUrlMax()) && vacio(serie.getUrlAppleTv())) {
                serie.setUrlNetflix("https://www.netflix.com/search?q=" + serie.getTitulo().replace(" ", "%20"));
            }

            repository.save(serie);
        }
    }

    private void cargarPeliculas(PeliculaRepository repository, Map<String, Categoria> categorias) {
        Map<String, Pelicula> existentes = repository.findAll().stream()
                .collect(Collectors.toMap(
                        pelicula -> pelicula.getTitulo().toLowerCase(Locale.ROOT),
                        Function.identity(),
                        (primera, segunda) -> primera
                ));

        String fallbackImg = "https://image.tmdb.org/t/p/w500/wwemzKWzjKYJFfCeiB57q3r4Bcm.png";
        String fallbackTrailer = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

        for (PeliculaSeed seed : peliculas()) {
            Pelicula pelicula = existentes.get(seed.titulo().toLowerCase(Locale.ROOT));
            boolean nueva = pelicula == null;

            if (nueva) {
                pelicula = new Pelicula();
                pelicula.setTitulo(seed.titulo());
            }

            if (nueva || vacio(pelicula.getSinopsis())) pelicula.setSinopsis(seed.sinopsis());
            if (nueva || vacio(pelicula.getDirector())) pelicula.setDirector(seed.director());
            if (nueva || pelicula.getAnio() == null || pelicula.getAnio() == 0) pelicula.setAnio(seed.anio());
            if (nueva || pelicula.getCategoria() == null) pelicula.setCategoria(categorias.get(seed.categoria()));
            if (vacio(pelicula.getImagen())) pelicula.setImagen(vacio(seed.imagen()) ? fallbackImg : seed.imagen());
            if (pelicula.getDuracion() == null) pelicula.setDuracion(seed.duracion());
            if (vacio(pelicula.getUrlTrailer())) pelicula.setUrlTrailer(vacio(seed.urlTrailer()) ? fallbackTrailer : seed.urlTrailer());
            if (vacio(pelicula.getUrlNetflix())) pelicula.setUrlNetflix(seed.urlNetflix());
            if (vacio(pelicula.getUrlPrimeVideo())) pelicula.setUrlPrimeVideo(seed.urlPrimeVideo());
            if (vacio(pelicula.getUrlDisneyPlus())) pelicula.setUrlDisneyPlus(seed.urlDisneyPlus());
            if (vacio(pelicula.getUrlMax())) pelicula.setUrlMax(seed.urlMax());
            if (vacio(pelicula.getUrlAppleTv())) pelicula.setUrlAppleTv(seed.urlAppleTv());

            if (vacio(pelicula.getUrlNetflix()) && vacio(pelicula.getUrlPrimeVideo()) && vacio(pelicula.getUrlDisneyPlus())
                    && vacio(pelicula.getUrlMax()) && vacio(pelicula.getUrlAppleTv())) {
                pelicula.setUrlNetflix("https://www.netflix.com/search?q=" + pelicula.getTitulo().replace(" ", "%20"));
            }

            repository.save(pelicula);
        }
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private List<SerieSeed> series() {
        return List.of(
                new SerieSeed("Stranger Things", "Un grupo de ni├▒os descubre secretos sobrenaturales en Hawkins.", 2016, "https://image.tmdb.org/t/p/w500/49WJfeN0moxb9IPfGn8AIqMGskD.jpg", "Finalizada", 5, "Ciencia ficci├│n", "https://www.youtube.com/watch?v=b9EkMc79ZSU", "https://www.netflix.com/title/80057281", null, null, null, null),
                new SerieSeed("Breaking Bad", "Un profesor de qu├¡mica se convierte en fabricante de metanfetamina.", 2008, "https://image.tmdb.org/t/p/w500/ggFHVNu6YYI5L9pCfOacjizRGt.jpg", "Finalizada", 5, "Drama", "https://www.youtube.com/watch?v=HhesaQXLuRY", "https://www.netflix.com/title/70143836", null, null, null, null),
                new SerieSeed("The Last of Us", "Un contrabandista escolta a una ni├▒a inmune en un mundo postapocal├¡ptico.", 2023, "https://image.tmdb.org/t/p/w500/uKvVjHNqB5VmOrdxqAt2F7J78ED.jpg", "En emisi├│n", 2, "Suspenso", "https://www.youtube.com/watch?v=uLtkt8BonwM", null, null, null, "https://www.hbo.com/the-last-of-us", null),
                new SerieSeed("Dark", "Un misterio temporal une varias generaciones en un peque├▒o pueblo alem├ín.", 2017, "https://image.tmdb.org/t/p/w500/apbrbWs8M9lyOpJYU5WXrpFbk1Z.jpg", "Finalizada", 3, "Ciencia ficci├│n", "https://www.youtube.com/watch?v=rrwycJ08PSA", "https://www.netflix.com/title/80100172", null, null, null, null),
                new SerieSeed("The Witcher", "Geralt de Rivia, un cazador de monstruos, busca su destino.", 2019, "https://image.tmdb.org/t/p/w500/7vjaCdMw15FEbXyLQTVa04URsPm.jpg", "En emisi├│n", 4, "Aventura", "https://www.youtube.com/watch?v=ndl1W4ltcmg", "https://www.netflix.com/title/80189685", null, null, null, null),
                new SerieSeed("Wednesday", "Wednesday Addams investiga asesinatos en Nevermore Academy.", 2022, "https://image.tmdb.org/t/p/w500/jeGtaMwGxPmQN5xM4ClnwPQcNQz.jpg", "En emisi├│n", 2, "Comedia", "https://www.youtube.com/watch?v=Di310WS8zLk", "https://www.netflix.com/title/81231974", null, null, null, null),
                new SerieSeed("The Crown", "La vida de la Reina Isabel II y la monarqu├¡a brit├ínica.", 2016, "https://image.tmdb.org/t/p/w500/1M876KPjulVwppEpldhdc8V4o68.jpg", "Finalizada", 6, "Drama", "https://www.youtube.com/watch?v=JWtnJjn6ng0", "https://www.netflix.com/title/80025678", null, null, null, null),
                new SerieSeed("Peaky Blinders", "La familia Shelby controla el crimen organizado en Birmingham.", 2013, "https://image.tmdb.org/t/p/w500/vUUqzWa2LnHIVqkaKVlVGkVcZIW.jpg", "Finalizada", 6, "Acci├│n", "https://www.youtube.com/watch?v=oVzVdvGIC7U", "https://www.netflix.com/title/80002479", null, null, null, null),
                new SerieSeed("Game of Thrones", "Lucha por el Trono de Hierro en los Siete Reinos.", 2011, "https://image.tmdb.org/t/p/w500/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg", "Finalizada", 8, "Aventura", "https://www.youtube.com/watch?v=KPLWWIOCOOQ", null, null, null, "https://www.hbo.com/game-of-thrones", null),
                new SerieSeed("Squid Game", "Juegos mortales con un premio millonario.", 2021, "https://image.tmdb.org/t/p/w500/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg", "Finalizada", 3, "Suspenso", "https://www.youtube.com/watch?v=oqxAJKy0ii4", "https://www.netflix.com/title/81040344", null, null, null, null),
                new SerieSeed("The Boys", "Superh├®roes corruptos son enfrentados por un grupo de vigilantes.", 2019, "https://image.tmdb.org/t/p/w500/stTEycfG9928HYGEISBFaG1ngjM.jpg", "Finalizada", 5, "Acci├│n", "https://www.youtube.com/watch?v=06rueu_fh30", null, "https://www.amazon.com/dp/B07QQQ52B3", null, null, null),
                new SerieSeed("The Mandalorian", "Un cazarrecompensas protege a Grogu en la galaxia.", 2019, "https://image.tmdb.org/t/p/w500/sWgBv7LV2PRoQgkxwlibdGXKz1S.jpg", "En emisi├│n", 3, "Aventura", "https://www.youtube.com/watch?v=aOC8E8z_ifw", null, null, "https://www.disneyplus.com/series/the-mandalorian/3jLIGMDYINqD/", null, null),
                new SerieSeed("Severance", "Separaci├│n entre recuerdos laborales y personales.", 2022, "https://image.tmdb.org/t/p/w500/lFf6LLrQjYldcZItzOkGmMMigP7.jpg", "En emisi├│n", 2, "Ciencia ficci├│n", "https://www.youtube.com/watch?v=xEQP4VVuyrY", null, null, null, null, "https://tv.apple.com/show/severance/umc.cmc.1srk2goyh2q2zdxcx605w8vtx"),
                new SerieSeed("Ted Lasso", "Un entrenador de f├║tbol americano dirige un equipo ingl├®s.", 2020, "https://image.tmdb.org/t/p/w500/5fhZdwP1DVJ0FyVH6vrFdHwpXIn.jpg", "En emisi├│n", 3, "Comedia", "https://www.youtube.com/watch?v=3u7EIiohs6U", null, null, null, null, "https://tv.apple.com/show/ted-lasso/umc.cmc.vtoh0mn0xn7t3c643xqonfzy"),
                new SerieSeed("House of the Dragon", "Guerra civil en la casa Targaryen.", 2022, "https://image.tmdb.org/t/p/w500/z2yahl2uefxDCl0nogcRBstwruJ.jpg", "En emisi├│n", 2, "Aventura", "https://www.youtube.com/watch?v=DotnJ7tTA34", null, null, null, "https://www.hbomax.com/show/c68e69d7-9317-428a-a615-cdf8fe5a2e06/", null),
                new SerieSeed("La Casa de Papel", "El mayor atraco a la F├íbrica Nacional de Moneda y Timbre.", 2017, "https://image.tmdb.org/t/p/w500/reEMJA1uzscCbkpeRJeTT2bjqUp.jpg", "Finalizada", 5, "Acci├│n", "https://www.youtube.com/watch?v=htqXL94Rza4", "https://www.netflix.com/title/80192098", null, null, null, null),
                new SerieSeed("Better Call Saul", "La transformaci├│n de Jimmy McGill en Saul Goodman.", 2015, "https://image.tmdb.org/t/p/w500/fC2HDm5t0kHl7mTm7jxMR31b7by.jpg", "Finalizada", 6, "Drama", "https://www.youtube.com/watch?v=lK_70f7PamE", "https://www.netflix.com/title/80021955", null, null, null, null),
                new SerieSeed("Chernobyl", "La tragedia nuclear de Chernobyl en 1986.", 2019, "https://image.tmdb.org/t/p/w500/hlLXt2tOPT6RRnjiUmoxyG1LTFi.jpg", "Finalizada", 1, "Drama", "https://www.youtube.com/watch?v=s9APLXM9Ei8", null, null, null, "https://www.hbomax.com/", null),
                new SerieSeed("Succession", "La lucha por el control de Waystar RoyCo.", 2018, "https://image.tmdb.org/t/p/w500/7HW47XbkNQ5fiwQFYGWdw9gs144.jpg", "Finalizada", 4, "Drama", "https://www.youtube.com/watch?v=OzYxJV_rmE8", null, null, null, "https://www.hbomax.com/", null),
                new SerieSeed("Sherlock", "Sherlock Holmes moderno en Londres.", 2010, "https://image.tmdb.org/t/p/w500/7WTsnHkbA0FaG6R9twfFde0I9hl.jpg", "Finalizada", 4, "Suspenso", "https://www.youtube.com/watch?v=9UcR9iKArd0", "https://www.netflix.com/title/70202589", null, null, null, null),
                new SerieSeed("Mindhunter", "Agentes del FBI estudian asesinos en serie.", 2017, "https://image.tmdb.org/t/p/w500/fbKE87mojpIETWepSbD5Qt741fp.jpg", "Finalizada", 2, "Suspenso", "https://www.youtube.com/watch?v=7gZCfRD_zWE", "https://www.netflix.com/title/80114855", null, null, null, null),
                new SerieSeed("Ozark", "Una familia lava dinero para un cartel.", 2017, "https://image.tmdb.org/t/p/w500/m73bD8VjibSKuTWg597GQVyVhSb.jpg", "Finalizada", 4, "Drama", "https://www.youtube.com/watch?v=5hAXVqrljbs", "https://www.netflix.com/title/80117552", null, null, null, null),
                new SerieSeed("Narcos", "La historia del cartel de Medell├¡n.", 2015, "https://image.tmdb.org/t/p/w500/rTmal9fDbwh5F0waol2hq35U4ah.jpg", "Finalizada", 3, "Drama", "https://www.youtube.com/watch?v=xl8zdCY-abw", "https://www.netflix.com/title/80025172", null, null, null, null),
                new SerieSeed("Vikings", "Las aventuras de Ragnar Lothbrok.", 2013, "https://image.tmdb.org/t/p/w500/bQLrHIRNEkE3PdIWQrZHynQZazu.jpg", "Finalizada", 6, "Aventura", "https://www.youtube.com/watch?v=9GgxinPwAGc", "https://www.netflix.com/title/70301870", null, null, null, null),
                new SerieSeed("The Expanse", "Pol├¡tica y misterio en el sistema solar.", 2015, "https://image.tmdb.org/t/p/w500/wQgNepp290ziBUUbC0DVYTdA55.jpg", "Finalizada", 6, "Ciencia ficci├│n", "https://www.youtube.com/watch?v=8X5gXIQmY-E", null, null, null, null, null),
                new SerieSeed("Mr. Robot", "Un hacker lucha contra una corporaci├│n global.", 2015, "https://image.tmdb.org/t/p/w500/oKIBhzZzDX07SoE2bOLhq2EE8w3.jpg", "Finalizada", 4, "Drama", "https://www.youtube.com/watch?v=xIBiJ_SzJTA", null, "https://www.amazon.com/", null, null, null),
                new SerieSeed("Daredevil", "Un abogado ciego lucha contra el crimen en Hell's Kitchen.", 2015, "https://image.tmdb.org/t/p/w500/QWbPaDxiB6LW2LjASknzYBvjMj.jpg", "Finalizada", 3, "Acci├│n", "https://www.youtube.com/watch?v=jAy6NJ_D5vU", "https://www.netflix.com/title/80018294", null, null, null, null),
                new SerieSeed("Loki", "El Dios del Enga├▒o viaja por el multiverso.", 2021, "https://image.tmdb.org/t/p/w500/voHUmluYmKyleFkTu3lOXQG702u.jpg", "En emisi├│n", 2, "Aventura", "https://www.youtube.com/watch?v=nW948Va-l10", null, null, "https://www.disneyplus.com/series/loki/6pARMvILBGzF", null, null),
                new SerieSeed("Arcane", "La lucha entre Piltover y Zaun.", 2021, "https://image.tmdb.org/t/p/w500/fqldf2t8ztc9aiwn3k6mlX3tvRT.jpg", "Finalizada", 2, "Animaci├│n", "https://www.youtube.com/watch?v=fXmAurh012s", "https://www.netflix.com/title/81435684", null, null, null, null),
                new SerieSeed("Attack on Titan", "La humanidad lucha contra gigantes devoradores.", 2013, "https://image.tmdb.org/t/p/w500/hTP1DtLGFamjfu8WqjnuQdP1n4i.jpg", "Finalizada", 4, "Animaci├│n", "https://www.youtube.com/watch?v=MGRm4IzK1SQ", "https://www.netflix.com/title/80006144", null, null, null, null)
        );
    }

    private List<PeliculaSeed> peliculas() {
        return List.of(
                new PeliculaSeed("Inception", "Un ladrón roba secretos a través de sueños compartidos.", "Christopher Nolan", 2010, "https://image.tmdb.org/t/p/w500/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg", 148, "Ciencia ficción", "https://www.youtube.com/watch?v=YoHD9XEInc0", "https://www.netflix.com/", null, null, null, null),
                new PeliculaSeed("Interstellar", "Astronautas viajan por un agujero de gusano para salvar la humanidad.", "Christopher Nolan", 2014, "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", 169, "Ciencia ficción", "https://www.youtube.com/watch?v=zSWdZVtXT7E", null, null, null, "https://www.hbomax.com/", null),
                new PeliculaSeed("The Dark Knight", "Batman enfrenta al Joker en Gotham.", "Christopher Nolan", 2008, "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg", 152, "Acción", "https://www.youtube.com/watch?v=EXeTwQWrcwY", "https://www.netflix.com/", null, null, null, null),
                new PeliculaSeed("The Matrix", "Un hacker descubre la verdadera naturaleza de la realidad.", "Lana y Lilly Wachowski", 1999, "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg", 136, "Ciencia ficción", "https://www.youtube.com/watch?v=vKQi3bBA1y8", "https://www.netflix.com/", null, null, null, null),
                new PeliculaSeed("Pulp Fiction", "Historias entrelazadas del crimen en Los Ángeles.", "Quentin Tarantino", 1994, "https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg", 154, "Suspenso", "https://www.youtube.com/watch?v=s7EdQ4FqbhY", null, null, null, null, null),
                new PeliculaSeed("The Godfather", "La saga de la familia Corleone.", "Francis Ford Coppola", 1972, "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg", 175, "Drama", "https://www.youtube.com/watch?v=UaVTIH8mujA", null, null, null, null, null),
                new PeliculaSeed("Fight Club", "Un club secreto de peleas desafía al sistema.", "David Fincher", 1999, "https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg", 139, "Drama", "https://www.youtube.com/watch?v=qtRKdVHc-cE", "https://www.netflix.com/", null, null, null, null),
                new PeliculaSeed("Forrest Gump", "La vida extraordinaria de Forrest Gump.", "Robert Zemeckis", 1994, "https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg", 142, "Drama", "https://www.youtube.com/watch?v=bLvqoHBptjg", "https://www.netflix.com/", null, null, null, null),
                new PeliculaSeed("Gladiator", "Un general romano busca venganza como gladiador.", "Ridley Scott", 2000, "https://image.tmdb.org/t/p/w500/ty8TGRuvJLPUmAR1H1nRIsgwvim.jpg", 155, "Acción", "https://www.youtube.com/watch?v=owK1qxDselE", null, null, null, null, null),
                new PeliculaSeed("Titanic", "Una historia de amor a bordo del Titanic.", "James Cameron", 1997, "https://image.tmdb.org/t/p/w500/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg", 195, "Drama", "https://www.youtube.com/watch?v=kVrqfYjkTdQ", null, null, null, null, null),
                new PeliculaSeed("Avatar", "Un exmarine en el planeta Pandora.", "James Cameron", 2009, "https://image.tmdb.org/t/p/w500/kyeqWdyUXW608qlYkRqosgbbJyK.jpg", 162, "Aventura", "https://www.youtube.com/watch?v=5PSNL1qE6VY", null, null, "https://www.disneyplus.com/", null, null),
                new PeliculaSeed("Joker", "El origen del Joker en Gotham.", "Todd Phillips", 2019, "https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg", 122, "Drama", "https://www.youtube.com/watch?v=zAGVQLHvwOY", "https://www.netflix.com/", null, null, null, null),
                new PeliculaSeed("Oppenheimer", "La historia del padre de la bomba atómica.", "Christopher Nolan", 2023, "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg", 180, "Drama", "https://www.youtube.com/watch?v=uYPbbksJxIg", null, null, null, null, null),
                new PeliculaSeed("Dune", "La lucha por Arrakis y la especia.", "Denis Villeneuve", 2021, "https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg", 155, "Ciencia ficción", "https://www.youtube.com/watch?v=8g18jFHCLXk", null, "https://www.amazon.com/", null, null, null),
                new PeliculaSeed("Spider-Man: No Way Home", "Peter Parker enfrenta el multiverso.", "Jon Watts", 2021, "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg", 148, "Acción", "https://www.youtube.com/watch?v=JfVOs4VSpmA", null, null, "https://www.disneyplus.com/", null, null),
                new PeliculaSeed("Parasite", "Una familia se infiltra en otra familia rica.", "Bong Joon Ho", 2019, "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg", 132, "Suspenso", "https://www.youtube.com/watch?v=5xH0HfJHsaY", "https://www.netflix.com/", null, null, null, null),
                new PeliculaSeed("The Prestige", "Dos magos obsesionados con superar al otro.", "Christopher Nolan", 2006, "https://image.tmdb.org/t/p/w500/5MXyQfz8xUP3dIFPTubhTsbFY6N.jpg", 130, "Suspenso", "https://www.youtube.com/watch?v=o4gHCmTQDVI", null, null, null, null, null),
                new PeliculaSeed("Mad Max: Fury Road", "Una persecución épica en un desierto postapocalíptico.", "George Miller", 2015, "https://image.tmdb.org/t/p/w500/hA2ple9q4qnwxp3hKVNhroipsir.jpg", 120, "Acción", "https://www.youtube.com/watch?v=hEJnMQG9ev8", null, null, null, null, null),
                new PeliculaSeed("Whiplash", "Un joven baterista bajo la presión de un instructor exigente.", "Damien Chazelle", 2014, "https://image.tmdb.org/t/p/w500/7fn624j5lj3xTme2SgiLCeuedmO.jpg", 106, "Drama", "https://www.youtube.com/watch?v=7d_jQycdQGo", "https://www.netflix.com/", null, null, null, null),
                new PeliculaSeed("La La Land", "Un músico y una actriz persiguen sus sueños en Los Ángeles.", "Damien Chazelle", 2016, "https://image.tmdb.org/t/p/w500/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg", 128, "Drama", "https://www.youtube.com/watch?v=0pdqf4P9MB8", null, null, null, null, null),
                new PeliculaSeed("Avengers: Endgame", "Los Vengadores luchan contra Thanos.", "Anthony Russo, Joe Russo", 2019, "https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg", 181, "Acción", "https://www.youtube.com/watch?v=TcMBFSGVi1c", null, null, "https://www.disneyplus.com/", null, null),
                new PeliculaSeed("Coco", "Un niño viaja al mundo de los muertos para descubrir su legado.", "Lee Unkrich", 2017, "https://image.tmdb.org/t/p/w500/gGEsBPAijhVUFoiNpgZXqRVWJt2.jpg", 105, "Animación", "https://www.youtube.com/watch?v=Rvr68u6k5sI", null, null, "https://www.disneyplus.com/", null, null),
                new PeliculaSeed("Toy Story", "Los juguetes cobran vida cuando los humanos no están.", "John Lasseter", 1995, "https://image.tmdb.org/t/p/w500/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg", 81, "Animación", "https://www.youtube.com/watch?v=v-PjgYDrg70", null, null, "https://www.disneyplus.com/", null, null),
                new PeliculaSeed("Up", "Un anciano y un niño exploran Sudamérica en una casa voladora.", "Pete Docter", 2009, "https://image.tmdb.org/t/p/w500/vpbaStTMt8qqXaEgnOR2EE4DNJk.jpg", 96, "Animación", "https://www.youtube.com/watch?v=ORFWdXl_zJ4", null, null, "https://www.disneyplus.com/", null, null),
                new PeliculaSeed("Shrek", "Un ogro acepta rescatar a una princesa.", "Andrew Adamson, Vicky Jenson", 2001, "https://image.tmdb.org/t/p/w500/iB64vpL3dIObOtMZgX3RqdVdQDc.jpg", 90, "Animación", "https://www.youtube.com/watch?v=CwXOrWvPBPk", null, null, null, null, null),
                new PeliculaSeed("The Hangover", "Una despedida de soltero en Las Vegas sale mal.", "Todd Phillips", 2009, "https://image.tmdb.org/t/p/w500/uluhlXubGu1VxU63X9VHCLWDAYP.jpg", 100, "Comedia", "https://www.youtube.com/watch?v=tlize92ffnY", "https://www.netflix.com/", null, null, null, null),
                new PeliculaSeed("Superbad", "Dos amigos intentan conseguir alcohol para una fiesta.", "Greg Mottola", 2007, "https://image.tmdb.org/t/p/w500/ek8e8txUyUwd2BNqj6lFEerJfbq.jpg", 113, "Comedia", "https://www.youtube.com/watch?v=4eaZ_48ZYog", "https://www.netflix.com/", null, null, null, null),
                new PeliculaSeed("The Conjuring", "Investigadores paranormales ayudan a una familia aterrorizada.", "James Wan", 2013, "https://image.tmdb.org/t/p/w500/wVYREutTvI2tmxr6ujrHT704wGF.jpg", 112, "Terror", "https://www.youtube.com/watch?v=k10ETZ41q5o", null, null, null, null, null),
                new PeliculaSeed("It", "Un grupo de niños enfrenta a un payaso malvado.", "Andy Muschietti", 2017, "https://image.tmdb.org/t/p/w500/9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg", 135, "Terror", "https://www.youtube.com/watch?v=FnCdOQsX5kc", null, null, null, null, null),
                new PeliculaSeed("Get Out", "Un joven afroamericano descubre un siniestro secreto.", "Jordan Peele", 2017, "https://image.tmdb.org/t/p/w500/tFXcEccSQMf3lfhfXKSU9iRBpa3.jpg", 104, "Terror", "https://www.youtube.com/watch?v=DzfpyUB60YY", "https://www.netflix.com/", null, null, null, null)
        );
    }

    private record SerieSeed(String titulo,
                             String sinopsis,
                             int anio,
                             String imagen,
                             String estado,
                             int temporadas,
                             String categoria,
                             String urlTrailer,
                             String urlNetflix,
                             String urlPrimeVideo,
                             String urlDisneyPlus,
                             String urlMax,
                             String urlAppleTv) {
    }

    private record PeliculaSeed(String titulo,
                                String sinopsis,
                                String director,
                                int anio,
                                String imagen,
                                int duracion,
                                String categoria,
                                String urlTrailer,
                                String urlNetflix,
                                String urlPrimeVideo,
                                String urlDisneyPlus,
                                String urlMax,
                                String urlAppleTv) {
    }
}
