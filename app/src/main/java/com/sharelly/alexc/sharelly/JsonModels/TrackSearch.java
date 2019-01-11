package com.sharelly.alexc.sharelly.JsonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackSearch {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("listeners")
    @Expose
    private String listeners;
    @SerializedName("image")
    @Expose
    private List<Image> image = null;
    @SerializedName("mbid")
    @Expose
    private String mbid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    @Override
    public String toString() {
        return "TrackSearch{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", url='" + url + '\'' +
                ", listeners='" + listeners + '\'' +
                ", image=" + image +
                ", mbid='" + mbid + '\'' +
                '}';
    }

    public class Trackmatches {

        @SerializedName("track")
        @Expose
        private List<TrackSearch> trackList = null;

        public List<TrackSearch> getTrackList() {
            return trackList;
        }

        public void setTrackList(List<TrackSearch> trackList) {
            this.trackList = trackList;
        }

        @Override
        public String toString() {
            return "Trackmatches{" +
                    "track=" + trackList +
                    '}';
        }
    }

    public class Results {

        @SerializedName("results")
        @Expose
        private TrackResults trackResults;

        public TrackResults getTrackResults() {
            return trackResults;
        }

        public void setTrackResults(TrackResults trackResults) {
            this.trackResults = trackResults;
        }


    }

    public class TrackResults {
        @SerializedName("trackmatches")
        @Expose
        private Trackmatches trackmatches;

        public Trackmatches getTrackmatches() {
            return trackmatches;
        }

        public void setTrackmatches(Trackmatches trackmatches) {
            this.trackmatches = trackmatches;
        }

        @Override
        public String toString() {
            return "Results{" +
                    "trackmatches=" + trackmatches +
                    '}';
        }
    }

    public class Image {

        @SerializedName("#text")
        @Expose
        private String text;
        @SerializedName("size")
        @Expose
        private String size;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "Image{" +
                    "text='" + text + '\'' +
                    ", size='" + size + '\'' +
                    '}';
        }
    }
}
