package com.cnpanoramio.service.lbs;

public class SmallestCircle {

	// Compute the Smallest Enclosing Circle of the n points in p,
	// such that the m points in B lie on the boundary of the circle.
	public static Circle findSec(int n, Point[] p, int m, Point[] b) {
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
	
	public static Circle cal(Double[][] ps) {
		int n = ps.length;
		Point points[] = new Point[n];
		int num = 0;
		for(Double[] p : ps) {
			points[num++] = new Point(p[0], p[1]);
		}
		Point inCPs[] = new Point[3];		
		Circle circle = findSec(n, points, 0, inCPs);
		return circle;
	}
	
	public static void main(String[] args) {
		
		Circle circle = cal(new Double[][]{{2D, 3D}, {4D, 1D}, {50D, 50D}, {60D, 70D}});
		System.out.println(circle);
		System.out.println(circle.getCenter());
		System.out.println(circle.getRadiusOfMi());
		
		int n = 7;
		Point ps[] = new Point[n];
		ps[0] = new Point(2D, 3D);
		ps[1] = new Point(4D, 1D);
		ps[2] = new Point(50D, 50D);
		ps[3] = new Point(25D, 26D);
		ps[4] = new Point(25D, 26D);
		ps[5] = new Point(25D, 26D);
		ps[6] = new Point(25D, 26D);
		
		Point inCPs[] = new Point[3];
		circle = findSec(n, ps, 0, inCPs);
		System.out.println(circle);
		System.out.println(circle.getCenter());
		System.out.println(circle.getRadiusOfMi());
		System.out.println(inCPs[0]);
		System.out.println(inCPs[1]);
		System.out.println(inCPs[2]);
	}
}
