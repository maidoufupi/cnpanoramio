distanceInMeter = (lat1, lng1, lat2, lng2) ->
  e = Math.PI * lat1 / 180;
  f = Math.PI * lng1 / 180;
  g = Math.PI * lat2 / 180;
  h = Math.PI * lng2 / 180;
  1000 * 6371 * Math.acos(Math.min(Math.cos(e) * Math.cos(g) * Math.cos(f) * Math.cos(h) + Math.cos(e) * Math.sin(f) * Math.cos(g) * Math.sin(h) + Math.sin(e) * Math.sin(g), 1));

window.cnmap = window.cnmap ? {}
window.cnmap.GPS = window.cnmap.GPS  ? {}
window.cnmap.GPS.distanceInMeter = distanceInMeter