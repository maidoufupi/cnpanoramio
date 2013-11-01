package com.cnpanoramio.geo;

import java.text.DecimalFormat;

import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;

/**
 * 增加了海拔告诉属性的GeoLocation
 * 
 * @author anypossible.w@gmail.com
 */
public class GeoLocation {
	private final double _latitude; // 纬度
	private final double _longitude; // 经度
	private double _altitude; // 海拔高度

	/**
	 * Instantiates a new instance of {@link GeoLocation}.
	 * 
	 * @param latitude
	 *            the latitude, in degrees
	 * @param longitude
	 *            the longitude, in degrees
	 */
	public GeoLocation(double latitude, double longitude) {
		_latitude = latitude;
		_longitude = longitude;
	}
	
	/**
	 * Instantiates a new instance of {@link GeoLocation}
	 *  with elevation.
	 * @param latitude
	 * @param longitude
	 * @param altitude
	 */
	public GeoLocation(double latitude, double longitude, double altitude) {
		_latitude = latitude;
		_longitude = longitude;
		_altitude = altitude;
	}

	/**
	 * @return the latitudinal angle of this location, in degrees.
	 */
	public double getLatitude() {
		return _latitude;
	}

	/**
	 * @return the longitudinal angle of this location, in degrees.
	 */
	public double getLongitude() {
		return _longitude;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getAltitude() {
		return _altitude;
	}

	/**
	 * @return true, if both latitude and longitude are equal to zero
	 */
	public boolean isZero() {
		return _latitude == 0 && _longitude == 0;
	}

	/**
	 * Converts a decimal degree angle into its corresponding DMS
	 * (degrees-minutes-seconds) representation as a string, of format:
	 * {@code -1° 23' 4.56"}
	 */
	@NotNull
	public static String decimalToDegreesMinutesSecondsString(double decimal) {
		double[] dms = decimalToDegreesMinutesSeconds(decimal);
		DecimalFormat format = new DecimalFormat("0.##");
		return String.format("%s° %s' %s\"", format.format(dms[0]),
				format.format(dms[1]), format.format(dms[2]));
	}

	/**
	 * Converts a decimal degree angle into its corresponding DMS
	 * (degrees-minutes-seconds) component values, as a double array.
	 */
	@NotNull
	public static double[] decimalToDegreesMinutesSeconds(double decimal) {
		int d = (int) decimal;
		double m = Math.abs((decimal % 1) * 60);
		double s = (m % 1) * 60;
		return new double[] { d, (int) m, s };
	}

	/**
	 * Converts DMS (degrees-minutes-seconds) rational values, as given in
	 * {@link com.drew.metadata.exif.GpsDirectory}, into a single value in
	 * degrees, as a double.
	 */
	@Nullable
	public static Double degreesMinutesSecondsToDecimal(
			@NotNull final Rational degs, @NotNull final Rational mins,
			@NotNull final Rational secs, final boolean isNegative) {
		double decimal = Math.abs(degs.doubleValue()) + mins.doubleValue()
				/ 60.0d + secs.doubleValue() / 3600.0d;

		if (Double.isNaN(decimal))
			return null;

		if (isNegative)
			decimal *= -1;

		return decimal;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GeoLocation that = (GeoLocation) o;
		if (Double.compare(that._latitude, _latitude) != 0)
			return false;
		if (Double.compare(that._longitude, _longitude) != 0)
			return false;
		if (Double.compare(that._altitude, _altitude) != 0)
			return false;
		return true;
	}

	@Override
	//TODO
	public int hashCode() {
		int result;
		long temp;
		temp = _latitude != +0.0d ? Double.doubleToLongBits(_latitude) : 0L;
		result = (int) (temp ^ (temp >>> 32));
		temp = _longitude != +0.0d ? Double.doubleToLongBits(_longitude) : 0L;
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * @return a string representation of this location, of format:
	 *         {@code 1.23, 4.56, 120}
	 */
	@Override
	@NotNull
	public String toString() {
		return _latitude + ", " + _longitude + ", " + _altitude;
	}

	/**
	 * @return a string representation of this location, of format:
	 *         {@code -1° 23' 4.56", 54° 32' 1.92", 120}
	 */
	@NotNull
	public String toDMSString() {
		return decimalToDegreesMinutesSecondsString(_latitude) + ", "
				+ decimalToDegreesMinutesSecondsString(_longitude) + ", "
				+ _altitude;
	}
}
