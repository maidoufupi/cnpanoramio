package com.cnpanoramio.geo;

import java.util.HashMap;

import com.drew.lang.Rational;
import com.drew.metadata.exif.GpsDirectory;

public class GpsDirectoryEx extends GpsDirectory {

	public GeoLocation getGeoLocationEx() {

		Rational[] latitudes = getRationalArray(GpsDirectory.TAG_GPS_LATITUDE);
		Rational[] longitudes = getRationalArray(GpsDirectory.TAG_GPS_LONGITUDE);
		Rational[] altitudes = getRationalArray(GpsDirectory.TAG_GPS_ALTITUDE);

		String latitudeRef = getString(GpsDirectory.TAG_GPS_LATITUDE_REF);
		String longitudeRef = getString(GpsDirectory.TAG_GPS_LONGITUDE_REF);
		String altitudeRef = getString(GpsDirectory.TAG_GPS_ALTITUDE_REF);

		// Make sure we have the required values
		if (latitudes == null || latitudes.length != 3)
			return null;
		if (longitudes == null || longitudes.length != 3)
			return null;
		if (latitudeRef == null || longitudeRef == null)
			return null;

		Double lat = GeoLocation.degreesMinutesSecondsToDecimal(latitudes[0],
				latitudes[1], latitudes[2], latitudeRef.equalsIgnoreCase("S"));
		Double lon = GeoLocation.degreesMinutesSecondsToDecimal(longitudes[0],
				longitudes[1], longitudes[2],
				longitudeRef.equalsIgnoreCase("W"));
        
		// This can return null, in cases where the conversion was not possible
		if (lat == null || lon == null)
			return null;

		if (altitudes.length != 0) {
			return new GeoLocation(lat, lon, altitudes[0].doubleValue());
		} else {
			return new GeoLocation(lat, lon);
		}
	}

	@Override
	protected HashMap<Integer, String> getTagNameMap() {
		// TODO Auto-generated method stub
		return super.getTagNameMap();
	}

}
