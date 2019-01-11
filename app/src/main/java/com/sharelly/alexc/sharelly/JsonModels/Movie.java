package com.sharelly.alexc.sharelly.JsonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {
	
	@SerializedName("Title")
	private String title;
	
	@SerializedName("Year")
	private String year;
	
	@SerializedName("Released")
	private String Realised;
	
	@SerializedName("Genre")
	private String genre;
	
	@SerializedName("Director")
	private String director;
	
	@SerializedName("Runtime")
	private String runtime;
	
	@SerializedName("Plot")
	private String plot;
	
	@SerializedName("Poster")
	private String poster;
	
	@SerializedName("imdbRating")
	private String imdbRating;
	
	@SerializedName("imdbID")
	private String imdbId;

	@SerializedName("Ratings")
	@Expose
	private List<Rating> ratings = null;

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

	public String getRealised() {
		return Realised;
	}

	public void setRealised(String realised) {
		Realised = realised;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getPlot() {
		return plot;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	@Override
	public String toString() {
		return "Movie{" +
				"title='" + title + '\'' +
				", year='" + year + '\'' +
				", Realised='" + Realised + '\'' +
				", genre='" + genre + '\'' +
				", director='" + director + '\'' +
				", runtime='" + runtime + '\'' +
				", plot='" + plot + '\'' +
				", poster='" + poster + '\'' +
				", imdbRating='" + imdbRating + '\'' +
				", imdbId='" + imdbId + '\'' +
				", ratings=" + ratings +
				'}';
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public class Rating {

		@SerializedName("Source")
		@Expose
		private String source;
		@SerializedName("Value")
		@Expose
		private String value;

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "Rating{" +
					"source='" + source + '\'' +
					", value='" + value + '\'' +
					'}';
		}
	}
	

}
