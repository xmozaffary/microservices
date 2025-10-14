package se.moln.orderservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import se.moln.orderservice.model.MovieResponse;

@Service
public class MovieService {
    private final WebClient webClient;
    private final String apiKey;

    public MovieService(
            WebClient.Builder webClientBuilder,
            @Value("${omdb.api.url}") String apiUrl,
            @Value("${omdb.api.key}") String apiKey
    ) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.apiKey = apiKey;
    }


    public Mono<MovieResponse> searchMovieByTitle(String title) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", apiKey)
                        .queryParam("t", title)
                        .queryParam("plot", "full") // full plot instead of short
                        .build())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        clientResponse -> Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid request to OMDb API"
                                )
                        )
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        clientResponse -> Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.SERVICE_UNAVAILABLE,
                                        "OMDb API is currently unavailable"
                                )
                        )
                )
                .bodyToMono(MovieResponse.class)
                .flatMap(response -> {
                    // OMDb returns "Response": "False" when movie not found
                    if ("False".equalsIgnoreCase(response.response())) {
                        return Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        response.error() != null
                                                ? response.error()
                                                : "Movie not found"
                                )
                        );
                    }
                    return Mono.just(response);
                })
                .doOnSuccess(response ->
                        System.out.println("Successfully fetched movie: " + response.title())
                )
                .doOnError(error ->
                        System.err.println("Error fetching movie: " + error.getMessage())
                );
    }
}
