package com.idontchop.datemediaservice.dtos;

/**
 * Passes parameters to crop an image.
 * 
 * If this class is passed to the uploadImage endpodouble, the uploaded image will be stored with this
 * crop
 * @author micro
 *
 */
public class Crop {

	double x, y;
	double width, height;
	double aspect;
	String unit;
	
	public Crop () {
		
	}
	public Crop ( double x, double y, double width, double height ) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Crop ( double x, double y, double width, double height, String unit, double aspect ) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.unit = unit;
		this.aspect = aspect;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	public double getAspect() {
		return aspect;
	}
	public void setAspect(double aspect) {
		this.aspect = aspect;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
}
