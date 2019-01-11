package com.sharelly.alexc.sharelly.JsonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Track {

    @SerializedName("mbid")
    private String mbid;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("duration")
    private String duration;
    @SerializedName("artist")
    private Artist artist;
    @SerializedName("album")
    private Album album;
    @SerializedName("wiki")
    private Wiki wiki;

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Wiki getWiki() {
        return wiki;
    }

    public void setWiki(Wiki wiki) {
        this.wiki = wiki;
    }

    @Override
    public String toString() {
        return "Track{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", duration='" + duration + '\'' +
                ", artist=" + artist +
                ", album=" + album +
                ", wiki=" + wiki +
                '}';
    }

    public class Album {
        @SerializedName("title")
        private String title;
        @SerializedName("url")
        private String url;
        @SerializedName("image")
        private List<Image> images;

        @Override
        public String toString() {
            return "Album{" +
                    "title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", images=" + images +
                    '}';
        }
    }

    public class Wiki {

        @SerializedName("published")
        @Expose
        private String published;
        @SerializedName("summary")
        @Expose
        private String summary;
        @SerializedName("content")
        @Expose
        private String content;

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "Wiki{" +
                    "published='" + published + '\'' +
                    ", summary='" + summary + '\'' +
                    ", content='" + content + '\'' +
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

    public class Artist {
        @SerializedName("name")
        private String name;
        @SerializedName("url")
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Artist{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public class TrackInfo {
        @SerializedName("track")
        private Track track;

        public Track getTrack() {
            return track;
        }

        public void setTrack(Track track) {
            this.track = track;
        }

        @Override
        public String toString() {
            return "TrackInfo{" +
                    "track=" + track +
                    '}';
        }
    }
}
