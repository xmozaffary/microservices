package se.moln.orderservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import se.moln.orderservice.model.MovieResponse;
import se.moln.orderservice.dto.MovieSearchRequest;
import se.moln.orderservice.service.JwtService;
import se.moln.orderservice.service.MovieService;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MovieController {

    private final MovieService movieService;
    private final JwtService jwtService;

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Sök film via OMDb API",
            description = "Hämtar detaljerad information om en film från OMDb (Open Movie Database). Kräver JWT-autentisering."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Film hittades",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MovieResponse.class),
                            examples = @ExampleObject(
                                    name = "Inception",
                                    value = """
                                    {
                                      "title": "Inception",
                                      "year": "2010",
                                      "rated": "PG-13",
                                      "released": "16 Jul 2010",
                                      "runtime": "148 min",
                                      "genre": "Action, Sci-Fi, Thriller",
                                      "director": "Christopher Nolan",
                                      "actors": "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
                                      "plot": "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.",
                                      "language": "English, Japanese, French",
                                      "country": "United States, United Kingdom",
                                      "awards": "Won 4 Oscars. 159 wins & 220 nominations total",
                                      "poster": "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg",
                                      "imdbRating": "8.8",
                                      "imdbVotes": "2,400,000",
                                      "imdbId": "tt1375666",
                                      "type": "movie",
                                      "boxOffice": "$292,576,195",
                                      "response": "True"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ogiltig förfrågan",
                    content = @Content(
                            mediaType = "application/problem+json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Bad Request",
                                      "status": 400,
                                      "detail": "Movie title is required"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Otillåten - saknar eller ogiltig JWT-token",
                    content = @Content(
                            mediaType = "application/problem+json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Unauthorized",
                                      "status": 401,
                                      "detail": "Missing or invalid JWT token"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Film hittades inte",
                    content = @Content(
                            mediaType = "application/problem+json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Not Found",
                                      "status": 404,
                                      "detail": "Movie not found!"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "OMDb API är inte tillgänglig",
                    content = @Content(
                            mediaType = "application/problem+json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "about:blank",
                                      "title": "Service Unavailable",
                                      "status": 503,
                                      "detail": "OMDb API is currently unavailable"
                                    }
                                    """
                            )
                    )
            )
    })
    public Mono<ResponseEntity<MovieResponse>> searchMovie(
            @Parameter(description = "Bearer-token i formatet 'Bearer <JWT>'", required = true)
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authHeader,

            @Parameter(description = "Filmtitel att söka efter", required = true)
            @Valid @RequestBody MovieSearchRequest request
    ) {
        // Validate JWT token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(
                    new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Missing Bearer token"
                    )
            );
        }

        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            return Mono.error(
                    new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Invalid or expired token"
                    )
            );
        }

        // Search for movie
        return movieService.searchMovieByTitle(request.title())
                .map(ResponseEntity::ok);
    }

    @GetMapping(path = "/search/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Sök film via URL-parameter (alternativ)",
            description = "Alternativ endpoint för att söka film via URL-parameter istället för request body."
    )
    public Mono<ResponseEntity<MovieResponse>> searchMovieByParam(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @PathVariable String title
    ) {
        // Validate JWT token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(
                    new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Missing Bearer token"
                    )
            );
        }

        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            return Mono.error(
                    new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Invalid or expired token"
                    )
            );
        }

        return movieService.searchMovieByTitle(title)
                .map(ResponseEntity::ok);
    }
}