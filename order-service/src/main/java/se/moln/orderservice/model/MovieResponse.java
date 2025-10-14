package se.moln.orderservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieResponse(
        @JsonProperty("Title")
        String title,

        @JsonProperty("Year")
        String year,

        @JsonProperty("Rated")
        String rated,

        @JsonProperty("Released")
        String released,

        @JsonProperty("Runtime")
        String runtime,

        @JsonProperty("Genre")
        String genre,

        @JsonProperty("Director")
        String director,

        @JsonProperty("Actors")
        String actors,

        @JsonProperty("Plot")
        String plot,

        @JsonProperty("Language")
        String language,

        @JsonProperty("Country")
        String country,

        @JsonProperty("Awards")
        String awards,

        @JsonProperty("Poster")
        String poster,

        @JsonProperty("imdbRating")
        String imdbRating,

        @JsonProperty("imdbVotes")
        String imdbVotes,

        @JsonProperty("imdbID")
        String imdbId,

        @JsonProperty("Type")
        String type,

        @JsonProperty("BoxOffice")
        String boxOffice,

        @JsonProperty("Response")
        String response,

        @JsonProperty("Error")
        String error
) {}