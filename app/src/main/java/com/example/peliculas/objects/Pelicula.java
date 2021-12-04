package com.example.peliculas.objects;

public class Pelicula {
    private String title;
    private String backdrop_path;
    private String poster_path;
    private String overview;
    private String release_date;

    public Pelicula(String title, String backdrop_path, String poster_path, String overview, String release_date) {
        this.title = title;
        this.backdrop_path=backdrop_path;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date=release_date;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }
}
