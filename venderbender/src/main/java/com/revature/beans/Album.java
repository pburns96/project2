/**
 * 
 */
package com.revature.beans;

import java.util.HashSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Russ Barnes
 * Album bean. Holds all the standard information needed for an album.
 */
@Entity
@Table
public class Album {
	@Id
	@Column(name="ALBUM_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="album_gen")
	@SequenceGenerator(name="album_gen", sequenceName="ALBUM_SEQ")
	private int id;
	@Column(name="ALBUM_TITLE", nullable=false)
	private String title;
	@Column(name="ALBUM_ARTIST", nullable=false)
	private String artist;
	@Column(name="ALBUM_TRACKS")
	private List<String> tracks;
	@Column(name="ALBUM_YEAR", nullable=false)
	private short year;
	@Column(name="ALBUM_PRICE")
	private double price;
	private String imagePath;
	
	@OneToMany(mappedBy ="album")
	private HashSet<OrderItem> orderItems;
	
	public Album() {
		super();
	}


	public Album(String title, String artist, List<String> tracks, short year, double price, String imagePath,
			HashSet<OrderItem> orderItems) {
		super();
		this.title = title;
		this.artist = artist;
		this.tracks = tracks;
		this.year = year;
		this.price = price;
		this.imagePath = imagePath;
		this.orderItems = orderItems;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public List<String> getTracks() {
		return tracks;
	}

	public void setTracks(List<String> tracks) {
		this.tracks = tracks;
	}

	public short getYear() {
		return year;
	}

	public void setYear(short year) {
		this.year = year;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	

	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public HashSet<OrderItem> getOrderItems() {
		return orderItems;
	}


	public void setOrderItems(HashSet<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}


	@Override
	public String toString() {
		return "Album [id=" + id + ", title=" + title + ", artist=" + artist + ", year=" + year + ", price=" + price
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		if (artist == null) {
			if (other.artist != null)
				return false;
		} else if (!artist.equals(other.artist))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

}