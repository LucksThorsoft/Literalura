package com.domain.literalura.main;

import com.domain.literalura.model.*;
import com.domain.literalura.repository.*;
import com.domain.literalura.service.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Main {
    private final Scanner keyboard = new Scanner(System.in);
    private final ApiConsulter apiConsulter = new ApiConsulter();
    private final DataConverter dataConverter = new DataConverter();
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void start() {
        var option = -1;

        while (option != 0) {
            var menu = """
                    \n
                    ======================================
                                  LiterAlura
                    ======================================
                    \n
                    --- Selecciona una opción ---
                    
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un año dado
                    5 - Listar libros por idioma
                    6 - Listar los 10 libros más descargados
                    7 - Mostrar estadísticas de la base de datos
                                        
                    0 - Salir
                    """;

            System.out.println(menu);

            if (keyboard.hasNextInt()) {
                option = keyboard.nextInt();
                keyboard.nextLine();

                switch (option) {
                    case 1:
                        searchBookByTitle();
                        break;
                    case 2:
                        listRegisteredBooks();
                        break;
                    case 3:
                        listRegisteredAuthors();
                        break;
                    case 4:
                        ListAuthorsAliveInAGivenYear();
                        break;
                    case 5:
                        listBooksByLanguage();
                        break;
                    case 6:
                        listTop10();
                        break;
                    case 7:
                        showDbStatistics();
                        break;
                    case 0:
                        System.out.println("\nCerrando la aplicación...");
                        break;
                    default:
                        System.out.println("\nOpción inválida");
                }

            } else {
                System.out.println("\nEntrada inválida");
                keyboard.next();
            }
        }
    }

    @Transactional
    private void searchBookByTitle() {
        String BASE_URL = "https://gutendex.com/books/?search=";
        System.out.println("\nIntroduce el nombre del libro que deseas buscar:");
        var title = keyboard.nextLine();

        if (!title.isBlank() && !isANumber(title)) {

            var json = apiConsulter.obtainData(BASE_URL + title.replace(" ", "%20"));
            var data = dataConverter.obtainData(json, Data.class);
            Optional<BookData> searchBook = data.results()
                    .stream()
                    .filter(b -> b.title().toLowerCase().contains(title.toLowerCase()))
                    .findFirst();

            if (searchBook.isPresent()) {
                BookData bookData = searchBook.get();

                if (!verifiedBookExistence(bookData)) {
                    Book book = new Book(bookData);
                    AuthorData authorData = bookData.author().get(0);
                    Optional<Author> optionalAuthor = authorRepository.findByName(authorData.name());

                    if (optionalAuthor.isPresent()) {
                        Author existingAuthor = optionalAuthor.get();
                        book.setAuthor(existingAuthor);
                        existingAuthor.getBooks().add(book);
                        authorRepository.save(existingAuthor);
                    } else {
                        Author newAuthor = new Author(authorData);
                        book.setAuthor(newAuthor);
                        newAuthor.getBooks().add(book);
                        authorRepository.save(newAuthor);
                    }

                    bookRepository.save(book);

                } else {
                    System.out.println("\nEl libro ya está agregado en la base de datos");
                }

            } else {
                System.out.println("\nLibro no encontrado");
            }

        } else {
            System.out.println("\nEntrada inválida");
        }

    }

    private void listRegisteredBooks() {
        List<Book> books = bookRepository.findAll();

        if(!books.isEmpty()) {
            System.out.println("\n----- Libros registrados -----");
            books.forEach(System.out::println);
        } else {
            System.out.println("\nNada por aquí, aún");
        }

    }

    private void listRegisteredAuthors() {
        List<Author> authors = authorRepository.findAll();

        if(!authors.isEmpty()) {
            System.out.println("\n----- Autores registrados -----");
            authors.forEach(System.out::println);
        } else {
            System.out.println("\nNada por aquí, aún");
        }

    }

    private boolean verifiedBookExistence(BookData bookData) {
        Book book = new Book(bookData);
        return bookRepository.verifiedBDExistence(book.getTitle());
    }

    private void ListAuthorsAliveInAGivenYear() {
        System.out.println("\nIntroduce el año que deseas consultar: ");

        if (keyboard.hasNextInt()) {
            var year = keyboard.nextInt();
            List<Author> authors = authorRepository.findAuthorsAlive(year);

            if (!authors.isEmpty()) {
                System.out.println("\n----- Autores registrados vivos en " + year + " -----");
                authors.forEach(System.out::println);
            } else {
                System.out.println("\nSin resultados, ingresa otro año");
            }

        } else {
            System.out.println("\nEntrada inválida");
            keyboard.next();
        }

    }

    private void listBooksByLanguage() {
        var option = -1;
        String language = "";

        System.out.println("\nSelecciona el idioma que deseas consultar");
        var languagesMenu = """
               \n
               1 - Inglés
               2 - Francés
               3 - Alemán
               4 - Portugués
               5 - Español
               """;

        System.out.println(languagesMenu);

        if (keyboard.hasNextInt()) {
            option = keyboard.nextInt();

            switch (option) {
                case 1:
                    language = "en";
                    break;
                case 2:
                    language = "fr";
                    break;
                case 3:
                    language = "de";
                    break;
                case 4:
                    language = "pt";
                    break;
                case 5:
                    language = "es";
                    break;
                default:
                    System.out.println("\nOpción inválida");
            }

            System.out.println("\nLibros registrados:");
            List<Book> books = bookRepository.findBooksByLanguage(language);

            if (!books.isEmpty()) {
                books.forEach(System.out::println);
            } else {
                System.out.println("\nSin resultados, selecciona otro idioma");
            }

        } else {
            System.out.println("\nEntrada inválida");
            keyboard.next();
        }

    }

    private boolean isANumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void listTop10() {
        List<Book> books = bookRepository.findTop10();

        if (!books.isEmpty()) {
            System.out.println("\n----- Top 10 libros más descargados -----");
            books.forEach(b -> System.out.println(b.getTitle()));
        } else {
            System.out.println("\nNada por aquí, aún");
        }

    }

    private void showDbStatistics() {
        List<Book> books = bookRepository.findAll();

        if (!books.isEmpty()) {
            IntSummaryStatistics sta = books.stream()
                    .filter(b -> b.getDownloads() > 0)
                    .collect(Collectors.summarizingInt(Book::getDownloads));

            System.out.println("\n----- Estadísticas de la base de datos -----");
            System.out.println("Promedio de descargas: " + sta.getAverage());
            System.out.println("Máximo de descargas: " + sta.getMax());
            System.out.println("Mínimo de descargas: " + sta.getMin());
            System.out.println("Libros registrados: " + sta.getCount());
        } else {
            System.out.println("\nNada por aquí, aún");
        }

    }

}
