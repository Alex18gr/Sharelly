package com.sharelly.alexc.sharelly.JsonModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Track implements Parcelable {

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
    @SerializedName("listeners")
    private String listeners;
    @SerializedName("playcount")
    private String playcount;

    public Track() {
    }

    protected Track(Parcel in) {
        mbid = in.readString();
        name = in.readString();
        url = in.readString();
        duration = in.readString();
        artist = in.readParcelable(Artist.class.getClassLoader());
        wiki = in.readParcelable(Wiki.class.getClassLoader());
    }

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

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
                "mbid='" + mbid + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", duration='" + duration + '\'' +
                ", artist=" + artist +
                ", album=" + album +
                ", wiki=" + wiki +
                ", listeners='" + listeners + '\'' +
                ", playcount='" + playcount + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mbid);
        parcel.writeString(name);
        parcel.writeString(url);
        parcel.writeString(duration);
        parcel.writeParcelable(artist, i);
        parcel.writeParcelable(wiki, i);
    }

    public static class Album {
        @SerializedName("title")
        private String title;
        @SerializedName("url")
        private String url;
        @SerializedName("image")
        private List<Image> images;

        public Album() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "Album{" +
                    "title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", images=" + images +
                    '}';
        }
    }

    public class Wiki implements Parcelable{

        @SerializedName("published")
        @Expose
        private String published;
        @SerializedName("summary")
        @Expose
        private String summary;
        @SerializedName("content")
        @Expose
        private String content;

        protected Wiki(Parcel in) {
            published = in.readString();
            summary = in.readString();
            content = in.readString();
        }

        public final Creator<Wiki> CREATOR = new Creator<Wiki>() {
            @Override
            public Wiki createFromParcel(Parcel in) {
                return new Wiki(in);
            }

            @Override
            public Wiki[] newArray(int size) {
                return new Wiki[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(published);
            parcel.writeString(summary);
            parcel.writeString(content);
        }
    }

    public static class Image implements Parcelable{

        @SerializedName("#text")
        @Expose
        private String text;
        @SerializedName("size")
        @Expose
        private String size;

        public Image() {
        }

        protected Image(Parcel in) {
            text = in.readString();
            size = in.readString();
        }

        public final Creator<Image> CREATOR = new Creator<Image>() {
            @Override
            public Image createFromParcel(Parcel in) {
                return new Image(in);
            }

            @Override
            public Image[] newArray(int size) {
                return new Image[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(text);
            parcel.writeString(size);
        }
    }

    public static class Artist implements Parcelable{
        @SerializedName("name")
        private String name;
        @SerializedName("url")
        private String url;

        public Artist() {
        }

        protected Artist(Parcel in) {
            name = in.readString();
            url = in.readString();
        }

        public static final Creator<Artist> CREATOR = new Creator<Artist>() {
            @Override
            public Artist createFromParcel(Parcel in) {
                return new Artist(in);
            }

            @Override
            public Artist[] newArray(int size) {
                return new Artist[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(name);
            parcel.writeString(url);
        }
    }

    public class TrackInfo implements Parcelable{
        @SerializedName("track")
        private Track track;

        protected TrackInfo(Parcel in) {
            track = in.readParcelable(Track.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(track, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public final Creator<TrackInfo> CREATOR = new Creator<TrackInfo>() {
            @Override
            public TrackInfo createFromParcel(Parcel in) {
                return new TrackInfo(in);
            }

            @Override
            public TrackInfo[] newArray(int size) {
                return new TrackInfo[size];
            }
        };

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
