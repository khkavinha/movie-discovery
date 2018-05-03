package com.example.kh870h.moviediscovery;

/**
 * Created by kh870h on 11/8/2017.
 */

public class MovieItem {

    private String id;
    private String title;
    private String overView;
    private String releaseDate;
    private String rating;
    private String posterPath;
    private String url;

    public MovieItem(String id, String title, String overView, String releaseDate, String rating, String posterPath, String url) {
        this.id = id;
        this.title = title;
        this.overView = overView;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.posterPath = posterPath;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

}
