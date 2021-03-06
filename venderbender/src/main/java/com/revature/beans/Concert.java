package com.revature.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Concert {

	@Id
	@Column(name = "CONCERT_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ConcertId")
	@SequenceGenerator(name = "ConcertId", sequenceName = "CONCERT_ID_SEQ")
	private int id;

	@Column(name = "CONCERT_DATE", nullable = false)
	private Date date;

	@Column(name = "CONCERT_LOCATION", nullable = false)
	private String location;

	@Column(name = "CONCERT_BAND", nullable = false)
	private String band;

	@Column(name = "CONCERT_PRICE", nullable = false)
	private double price;
	
	@JsonIgnore
	@OneToMany(mappedBy="concertTicket", cascade = CascadeType.DETACH)
	private List<OrderItem> orderItem;

	public Concert() {
		super();
	}

	public Concert(Date date, String location, String band, double price) {
		super();
		this.date = date;
		this.location = location;
		this.band = band;
		this.price = price;
	}
	
	

	public List<OrderItem> getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(List<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((band == null) ? 0 : band.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Concert other = (Concert) obj;
		if (band == null) {
			if (other.band != null)
				return false;
		} else if (!band.equals(other.band))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Concert [date=" + date + ", location=" + location + ", band=" + band + ", price=" + price + "]";
	}
}
