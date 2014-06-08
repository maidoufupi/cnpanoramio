package com.cnpanoramio.service.lbs;

import java.awt.*;
import java.awt.event.*;

public class SEC extends java.applet.Applet {
	private static final int MAXPOINTS = 1000; // Maximum number of points can
												// be handled by this calss
	private static final int POINTSIZE = 2; // Size of the points drawn by this
											// calss
	private static final int PICKTOLERANCE = POINTSIZE << 1; // Tolerance for
																// picking up a
																// point

	private Point[] p; // points input by the user
	private int n = 0; // Number of points input by the user
	private int pickedPointIndex = -1; // Index of the point being picked up by
										// the user
	private Point[] b; // Points on the boundary of the circle
	private Circle sec; // Smallest Enclosing Circle

	// Initialize the applet
	public void init() {
		// Enable the mouse events
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK
				| AWTEvent.MOUSE_MOTION_EVENT_MASK);

		// Set the applet window size
		resize(500, 500);

		// Set background color to be black
		setBackground(Color.black);

		// Allocate memory
		p = new Point[MAXPOINTS];
		b = new Point[3];
		sec = new Circle();
	}

	// Process the mouse events
	protected void processMouseEvent(MouseEvent e) {
		int eID = e.getID();

		if (eID == MouseEvent.MOUSE_PRESSED) {
			int lastX = e.getX();
			int lastY = e.getY();

			// Check if the user is trying pick up a point
			pickPoint(lastX, lastY);

			// If no point is picked up, add the point defined by the new click
			// to the point set
			if ((pickedPointIndex == -1) && (n < MAXPOINTS)) {
				p[n++] = new Point(lastX, lastY);
				sec = findSec(n, p, 0, b);
				repaint();
			}
		} else if (eID == MouseEvent.MOUSE_RELEASED) {
			pickedPointIndex = -1;
		} else {
			super.processMouseEvent(e);
		}
	}

	// Process the mouse motion events
	protected void processMouseMotionEvent(MouseEvent e) {
		int eID = e.getID();

		if (eID == MouseEvent.MOUSE_DRAGGED) {
			int lastX = e.getX();
			int lastY = e.getY();

			// If the user is dragging a point
			if (pickedPointIndex != -1) {
				p[pickedPointIndex].translate(lastX, lastY);
				sec = findSec(n, p, 0, b);
				repaint();
			}
		} else {
			super.processMouseMotionEvent(e);
		}
	}

	// Determine whether the user is trying to pick up a point
	private void pickPoint(int x, int y) {
		for (int i = 0; i < n; i++) {
			if ((Math.abs(x - p[i].getX()) < PICKTOLERANCE)
					&& (Math.abs(y - p[i].getY()) < PICKTOLERANCE)) {
				pickedPointIndex = i;
				break;
			}
		}
	}

	// Compute the Smallest Enclosing Circle of the n points in p,
	// such that the m points in B lie on the boundary of the circle.
	private Circle findSec(int n, Point[] p, int m, Point[] b) {
		Circle sec = new Circle();

		// Compute the Smallest Enclosing Circle defined by B
		if (m == 1) {
			sec = new Circle(b[0]);
		} else if (m == 2) {
			sec = new Circle(b[0], b[1]);
		} else if (m == 3) {
			return new Circle(b[0], b[1], b[2]);
		}

		// Check if all the points in p are enclosed
		for (int i = 0; i < n; i++) {
			if (sec.contain(p[i]) == 1) {
				// Compute B <--- B union P[i].
				b[m] = new Point(p[i]);
				// Recurse
				sec = findSec(i, p, m + 1, b);
			}
		}

		return sec;
	}

	public void paint(Graphics g) {
		// Draw the points
		for (int i = 0; i < n; i++) {
			g.setColor(Color.green);
			g.drawOval((int) (p[i].getX() - POINTSIZE),
					(int) (p[i].getY() - POINTSIZE), PICKTOLERANCE,
					PICKTOLERANCE);
			g.setColor(Color.red);
			g.drawString(Integer.toString(i + 1),
					(int) (p[i].getX() + PICKTOLERANCE),
					(int) (p[i].getY() + PICKTOLERANCE));
		}

		// Draw the Smallest Enclosing Circle
		g.setColor(Color.yellow);
		g.drawOval((int) (sec.getCenter().getX() - sec.getRadius()), (int) (sec
				.getCenter().getY() - sec.getRadius()), (int) (sec
				.getDiameter()), (int) (sec.getDiameter()));
	}
}
