package com.example.peliculas.objects;

public class Creditos {
    private String name;
    private String original_name;
    private String profile_path;

    public Creditos(String name, String original_name, String profile_path) {
        this.name = name;
        this.original_name=original_name;
        this.profile_path = profile_path;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }
}
