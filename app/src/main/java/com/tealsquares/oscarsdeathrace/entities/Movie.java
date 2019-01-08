package com.tealsquares.oscarsdeathrace.entities;

public class Movie {

    public String getImdbTag() {
        return imdbTag;
    }

    public void setImdbTag(String imdbTag) {
        this.imdbTag = imdbTag;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public long getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(long movieYear) {
        this.movieYear = movieYear;
    }

    public String getPosterLink() {
        return posterLink;
    }

    public void setPosterLink(String posterLink) {
        this.posterLink = posterLink;
    }

    String imdbTag;
    String movieName;
    long movieYear;
    String posterLink;

}
