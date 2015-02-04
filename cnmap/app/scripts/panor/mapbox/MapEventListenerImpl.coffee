
$window = window

class MapEventListener extends window.cnmap.IMapEventListener
  addLocationHashListener: (map, callback) ->
    MapListener = (e) ->
      center = e.target.getCenter()
      callback.apply this, [center.lat, center.lng, e.target.getZoom()]
    map.on "moveend", MapListener
    map.on "zoomend", MapListener

  addToolBar: (map) ->

  setCenter: (map, lat, lng) ->
    map.setView [lat, lng]

  setZoom: (map, zoom) ->
    map.setZoom zoom

  setZoomAndCenter: (map, zoom, lat, lng) ->
    map.setView [lat, lng], zoom

  zoomIn: (map) ->
    map.zoomIn()

  zoomOut: (map) ->
    map.zoomOut()

  setBounds: (map, sw, ne) ->
    map.fitBounds [
      [sw.lat, sw.lng]
      [ne.lat, ne.lng]
    ]

  inMapView: (lat, lng, map) ->
    map = map || @opts.map
    map.getBounds().contains [lat, lng]

  pixelToPoint: (map, pixel) ->
    map.containerPointToLatLng L.point(pixel.x, pixel.y)

  pointToPixel: (map, point) ->
    map.latLngToContainerPoint [point.lat, point.lng]

  addMarker: (map, lat, lng) ->
    L.marker([lat, lng], {icon: L.mapbox.marker.icon({
      'marker-size': 'medium'
    })}).addTo(map);

  createDraggableMarker: (map, lat, lng) ->
    L.marker([lat, lng], {draggable: true}).addTo(map);

  activeMarker: (marker) ->
    if marker
      marker.setIcon L.mapbox.marker.icon({
        'marker-size': 'large'
        'marker-symbol': 'camera'
        'marker-color': '#00bcd4'
      })
      marker.setZIndexOffset 2

  deactiveMarker: (marker) ->
    if marker
      marker.setIcon L.mapbox.marker.icon({
        'marker-size': 'medium'
      })
      marker.setZIndexOffset 1

  addMarkerActiveListener: (marker, callback) ->
    ActiveListener = (e) ->
      callback.apply marker, []
    marker.on 'click',    ActiveListener
    marker.on 'dblclick', ActiveListener
    marker.on 'dragend',  ActiveListener

  addDragendListener: (marker, callback) ->
    marker.on 'dragend',  (e) ->
      if e.type == "dragend"
        callback.apply marker, [e.target.getLatLng().lat, e.target.getLatLng().lng]


  removeMarker: (marker, map) ->
    map.removeLayer marker

  addMapClickListener: (map, callback)  ->

#    map.on 'click', (e) ->
#      if e.targetType == "map" and not e.mouseMoved
#        point = new Microsoft.Maps.Point(e.getX(), e.getY())
#        loc = e.target.tryPixelToLocation(point);
#        callback.apply map, [loc.latitude, loc.longitude]

  setPosition: (target, lat, lng) ->
    target.setLatLng [lat, lng]

  setMap: (target, map) ->
    target.addTo map

MapEventListener.factory = () ->
  return new cnmap.MapEventListener

window.cnmap = window.cnmap || {}
window.cnmap.MapEventListener = MapEventListener
