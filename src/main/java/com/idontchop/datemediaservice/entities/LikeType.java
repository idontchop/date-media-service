package com.idontchop.datemediaservice.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LikeType {
	
	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	private double costIncoming;
	private double costOutgoing;	
	
	private Date created = new Date();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public double getCostIncoming() {
		return costIncoming;
	}

	public void setCostIncoming(double costIncoming) {
		this.costIncoming = costIncoming;
	}

	public double getCostOutgoing() {
		return costOutgoing;
	}

	public void setCostOutgoing(double costOutgoing) {
		this.costOutgoing = costOutgoing;
	}



}
