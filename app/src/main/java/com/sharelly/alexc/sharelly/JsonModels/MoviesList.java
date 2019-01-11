package com.sharelly.alexc.sharelly.JsonModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesList {

    @SerializedName("Search")
    private List<SearchResultMovie> searchResults;

    public List<SearchResultMovie> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<SearchResultMovie> searchResults) {
        this.searchResults = searchResults;
    }

    @Override
    public String toString() {
        return "MoviesList{" +
                "searchResults=" + searchResults +
                '}';
    }

    public class SearchResultMovie {

        @SerializedName("Title")
        private String title;

        @SerializedName("Year")
        private String year;

        @SerializedName("imdbID")
        private String imdbId;

        @SerializedName("Type")
        private String type;

        @SerializedName("Poster")
        private String poster;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getImdbId() {
            return imdbId;
        }

        public void setImdbId(String imdbId) {
            this.imdbId = imdbId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }

        @Override
        public String toString() {
            return "SearchResultMovie{" +
                    "title='" + title + '\'' +
                    ", year='" + year + '\'' +
                    ", imdbId='" + imdbId + '\'' +
                    ", type='" + type + '\'' +
                    ", poster='" + poster + '\'' +
                    '}';
        }
    }

}
