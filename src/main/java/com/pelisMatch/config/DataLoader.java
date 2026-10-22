package com.pelisMatch.movie_catalog.config;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.repository.PeliculaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/** Carga películas de ejemplo solo si la tabla está vacía. */
@Component
public class DataLoader implements CommandLineRunner {

    private final PeliculaRepository repository;

    public DataLoader(PeliculaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
            peli("Inception", "Ciencia ficción", "Christopher Nolan", 2010, 148, 8.8,
                "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
                "Un ladrón que roba secretos desde los sueños recibe una misión distinta: implantar una idea en la mente de un heredero."),
            peli("The Dark Knight", "Acción", "Christopher Nolan", 2008, 152, 9.0,
                "Christian Bale, Heath Ledger, Aaron Eckhart",
                "Batman enfrenta al Joker, un criminal que sume Gotham en el caos y obliga al héroe a cruzar sus propios límites."),
            peli("Interstellar", "Ciencia ficción", "Christopher Nolan", 2014, 169, 8.7,
                "Matthew McConaughey, Anne Hathaway, Jessica Chastain",
                "Con la Tierra en declive, un grupo de astronautas viaja por un agujero de gusano en busca de un nuevo hogar para la humanidad."),
            peli("Parasite", "Drama", "Bong Joon-ho", 2019, 132, 8.5,
                "Song Kang-ho, Choi Woo-shik, Park So-dam",
                "Una familia sin recursos se infiltra poco a poco en la vida de una familia adinerada, con consecuencias que nadie espera."),
            peli("Coco", "Animación", "Lee Unkrich", 2017, 105, 8.4,
                "Anthony Gonzalez, Gael García Bernal, Benjamin Bratt",
                "Miguel sueña con ser músico en una familia que lo prohíbe y, durante el Día de Muertos, llega a la Tierra de los Muertos."),
            peli("Spider-Man: Un nuevo universo", "Animación", "Bob Persichetti, Peter Ramsey, Rodney Rothman", 2018, 117, 8.4,
                "Shameik Moore, Hailee Steinfeld, Mahershala Ali",
                "Miles Morales descubre que no es el único Spider-Man cuando varios héroes de otras dimensiones llegan a su ciudad."),
            peli("Gladiador", "Acción", "Ridley Scott", 2000, 155, 8.5,
                "Russell Crowe, Joaquin Phoenix, Connie Nielsen",
                "Un general romano traicionado es llevado a la arena como gladiador y busca vengar a su familia y al emperador."),
            peli("Matrix", "Ciencia ficción", "Lana y Lilly Wachowski", 1999, 136, 8.7,
                "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
                "Un programador descubre que el mundo que conoce es una simulación y se une a una rebelión contra las máquinas."),
            peli("Toy Story", "Animación", "John Lasseter", 1995, 81, 8.3,
                "Tom Hanks, Tim Allen, Don Rickles",
                "Woody, el juguete favorito de Andy, ve peligrar su lugar cuando llega Buzz Lightyear, un astronauta de juguete."),
            peli("Titanic", "Romance", "James Cameron", 1997, 195, 7.9,
                "Leonardo DiCaprio, Kate Winslet, Billy Zane",
                "Jack y Rose, de clases sociales opuestas, se enamoran a bordo del Titanic durante su único viaje."),
            peli("Avatar", "Aventura", "James Cameron", 2009, 162, 7.9,
                "Sam Worthington, Zoe Saldaña, Sigourney Weaver",
                "Un exmarine paralítico es enviado a Pandora, donde se une al pueblo na'vi y debe elegir de qué lado está."),
            peli("Joker", "Drama", "Todd Phillips", 2019, 122, 8.4,
                "Joaquin Phoenix, Robert De Niro, Zazie Beetz",
                "Arthur Fleck, un comediante fracasado y marginado por la sociedad, se transforma poco a poco en el Joker.")
        ));
    }

    private Pelicula peli(String titulo, String genero, String director, int anio,
                          int duracion, double puntuacion, String reparto, String descripcion) {
        Pelicula p = new Pelicula();
        p.setTitulo(titulo);
        p.setGenero(genero);
        p.setDirector(director);
        p.setAnio(anio);
        p.setDuracion(duracion);
        p.setPuntuacion(puntuacion);
        p.setReparto(reparto);
        p.setDescripcion(descripcion);
        return p;
    }
}
