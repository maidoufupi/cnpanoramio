package com.cnpanoramio.service.lbs;

/**
 * 
 * @author any
 * 
 */
public class Circle {
	// The center of a circle
	private Point p;
	// The radius of a circle
	private double r;
	
	// point on the boundary of the circle.
	private Point b1;
	private Point b2;
	private Point b3;

	// Construct a circle without any specification
	public Circle() {
		p = new Point(0, 0);
		r = 0;
	}

	// Construct a circle with the specified circle
	public Circle(Circle circle) {
		p = new Point(circle.p);
		r = circle.r;
		b1 = new Point(circle.b1);
		b2 = new Point(circle.b2);
		b3 = new Point(circle.b3);
	}

	// Construct a circle with the specified center and radius
	public Circle(Point center, double radius) {
		p = new Point(center);
		r = radius;
	}

	// Construct a circle based on one point
	public Circle(Point center) {
		p = new Point(center);
		r = 0;
	}

	// Construct a circle based on two points
	public Circle(Point p1, Point p2) {
		p = p1.midPoint(p2);
		r = p1.distance(p);
		b1 = new Point(p1);
		b2 = new Point(p2);
	}

	// Construct a circle based on three points
	public Circle(Point p1, Point p2, Point p3) {
		try {
			double x = (p3.getX()
					* p3.getX()
					* (p1.getY() - p2.getY())
					+ (p1.getX() * p1.getX() + (p1.getY() - p2.getY())
							* (p1.getY() - p3.getY()))
					* (p2.getY() - p3.getY()) + p2.getX() * p2.getX()
					* (-p1.getY() + p3.getY()))
					/ (2 * (p3.getX() * (p1.getY() - p2.getY()) + p1.getX()
							* (p2.getY() - p3.getY()) + p2.getX()
							* (-p1.getY() + p3.getY())));
			double y = (p2.getY() + p3.getY()) / 2 - (p3.getX() - p2.getX())
					/ (p3.getY() - p2.getY())
					* (x - (p2.getX() + p3.getX()) / 2);
			p = new Point(x, y);
			r = p.distance(p1);
			b1 = new Point(p1);
			b2 = new Point(p2);
			b3 = new Point(p3);
		} catch (Exception e) {
		}
	}

	// Get the center
	public Point getCenter() {
		return p;
	}

	// Get the radius
	public double getRadius() {
		return r;
	}

	// Set the center
	public void setCenter(Point center) {
		p.translate(center);
	}

	// Set the radius
	public void setRadius(double radius) {
		r = radius;
	}

	// Translate the center of a circle to a specified point
	public void translate(Point newCenter) {
		p.translate(newCenter);
	}

	// Offset a circle along its radius by dr
	public void offset(double dr) {
		r += dr;
	}

	// Scale a circle along its radius by a factor
	public void scale(double factor) {
		r *= factor;
	}

	// Calculate the diameter of a circle
	public double getDiameter() {
		return (2 * r);
	}

	// Calculate the circumference of a circle
	public double getCircumference() {
		return (Math.PI * 2 * r);
	}

	// Calcualte the area of a circle
	public double getArea() {
		return (Math.PI * r * r);
	}
	
	/**
	 * 获取以米为单位圆半径
	 * 
	 * @return
	 */
	public double getRadiusOfMi() {
		return 1000 * Distance.getDistance(p.getX(), p.getY(),
				b1.getX(), b1.getY());
	}

	// Is a point in the circle
	public int contain(Point point) {
		int answer = 0;
		double d = p.distance(point);
		if (d > r) {
			answer = 1; // The point is outside the circle
		} else if (d == r) {
			answer = 0; // The point is on the circumference of the circle
		} else {
			answer = -1; // The point is inside the circle
		}
		return answer;
	}

	// Determine whether two points are equal
	public boolean equals(Circle circle) {
		return p.equals(circle.p) && (r == circle.r);
	}

	// Return a representation of a point as a string
	public String toString() {
		return "Center = (" + p.getX() + ", " + p.getY() + "); " + "Radius = "
				+ r;
	}
}
