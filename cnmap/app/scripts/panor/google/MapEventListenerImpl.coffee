
$window = window
class MapEventListener extends window.cnmap.IMapEventListener
  addLocationHashListener: (map, callback) ->
    listeners = []
    MapListener = (e) ->
      point = this.getCenter()
      if point
        callback.apply this, [point.lat(), point.lng(), this.getZoom()]
    listeners.push google.maps.event.addListener map, "idle", MapListener
    listeners

  addToolBar: (map) ->


  setCenter: (map, lat, lng) ->
    map.setCenter new google.maps.LatLng lat, lng

  setZoom: (map, zoom) ->
    map.setZoom zoom

  setZoomAndCenter: (map, zoom, lat, lng) ->
    @setCenter map, lat, lng
    @setZoom map, Number(zoom)

  zoomIn: (map) ->
    @setZoom map, map.getZoon()+1

  zoomOut: (map) ->
    @setZoom map, map.getZoon()-1

  setBounds: (map, sw, ne) ->
    map.fitBounds new google.maps.LatLngBounds new google.maps.LatLng(sw.lat, sw.lng), new google.maps.LatLng(ne.lat, ne.lng)

  inMapView: (lat, lng, map) ->
    map = map || @opts.map
    map.getBounds().contains(new google.maps.LatLng(lat, lng))

  pixelToPoint: (map, pixel) ->
    map.getProjection().fromPointToLatLng new google.maps.Point(pixel.x, pixel.y)

  pointToPixel: (map, point) ->
    map.getProjection().fromLatLngToPoint new google.maps.LatLng(point.lat, point.lng)

  addMarker: (map, lat, lng) ->
    new google.maps.Marker({
      map: map,
      position: new google.maps.LatLng(lat, lng)
    });

  createDraggableMarker: (map, lat, lng) ->
    new google.maps.Marker({
      map: map,
      position: new google.maps.LatLng(lat, lng)
      draggable: true
    });

  activeMarker: (marker) ->
    if marker
#      marker.defaultIcon = marker.getIcon()

      marker.setIcon {
        url: "images/marker.png",
        size: new google.maps.Size(20, 30)
        anchor: new google.maps.Point(10, 30)
      }
      marker.setZIndex 2

  deactiveMarker: (marker) ->
    if marker
#    and marker.defaultIcon
      marker.setIcon null ##marker.defaultIcon
      marker.setZIndex 1

  addMarkerActiveListener: (marker, callback) ->
    ActiveListener = (e) ->
      callback.apply marker, []

    google.maps.event.addListener marker, "click", ActiveListener
    google.maps.event.addListener marker, "dragend", ActiveListener
    google.maps.event.addListener marker, "rightclick", ActiveListener

  addDragendListener: (marker, callback) ->
    google.maps.event.addListener marker, "dragend", (e) ->
      callback.apply marker, [e.latLng.lat(), e.latLng.lng()]

  removeMarker: (marker) ->
    marker.setMap null

  addMapClickListener: (map, callback)  ->
    google.maps.event.addListener map, "click", (e) ->
      callback.apply map, [e.latLng.lat(), e.latLng.lng()]

  setPosition: (target, lat, lng) ->
    target.setPosition new google.maps.LatLng(lat, lng)

  setMap: (target, map) ->
    target.setMap map

MapEventListener.factory = () ->
  return new cnmap.MapEventListener

window.cnmap = window.cnmap || {}
window.cnmap.MapEventListener = MapEventListener