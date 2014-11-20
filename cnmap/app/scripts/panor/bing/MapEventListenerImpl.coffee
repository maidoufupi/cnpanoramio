
$window = window

class MapEventListener extends window.cnmap.IMapEventListener
  addLocationHashListener: (map, callback) ->
    listeners = []
    MapListener = (e) ->
      if this.target
        point = this.target.getCenter()
        callback.apply this, [point.latitude, point.longitude, this.target.getZoom()]
    listeners.push Microsoft.Maps.Events.addHandler map, "viewchangeend", MapListener
    listeners

  addToolBar: (map) ->

  setCenter: (map, lat, lng) ->
    map.setView {center: new Microsoft.Maps.Location lat, lng};

  setZoom: (map, zoom) ->
    map.setView {zoom: zoom};

  setZoomAndCenter: (map, zoom, lat, lng) ->
    map.setView {
      center: new Microsoft.Maps.Location lat, lng
      zoom: zoom
    }

  zoomIn: (map) ->
    @setZoom map, map.getZoom()+1

  zoomOut: (map) ->
    @setZoom map, map.getZoom()-1

  setBounds: (map, sw, ne) ->
    viewRect = Microsoft.Maps.LocationRect.fromCorners(
      new Microsoft.Maps.Location(ne.lat, sw.lng),
      new Microsoft.Maps.Location(sw.lat, ne.lng)
    );
    map.setView({bounds: viewRect});

  inMapView: (lat, lng, map) ->
    map = map || @opts.map
    map.getBounds().contains(new Microsoft.Maps.Location(lat, lng))

  pixelToPoint: (map, pixel) ->
    loc = map.tryPixelToLocation new Microsoft.Maps.Point(pixel.x, pixel.y), Microsoft.Maps.PixelReference.control
    {
      lat: loc.latitude
      lng: loc.longitude
    }

  pointToPixel: (map, point) ->
    map.tryLocationToPixel new Microsoft.Maps.Location(point.lat, point.lng), Microsoft.Maps.PixelReference.control

  addMarker: (map, lat, lng) ->
    marker = new Microsoft.Maps.Pushpin new Microsoft.Maps.Location(lat, lng)
    map.entities.push(marker)
    marker

  createDraggableMarker: (map, lat, lng) ->
    marker = new Microsoft.Maps.Pushpin new Microsoft.Maps.Location(lat, lng), {draggable: true}
    map.entities.push(marker)
    marker

  activeMarker: (marker) ->
    if marker
#      marker.defaultIcon = marker.getIcon()
      marker.setOptions {icon: "images/marker.png", zIndex: 2}

  deactiveMarker: (marker) ->
    if marker
      marker.setOptions {icon: "", zIndex: 1}

  addMarkerActiveListener: (marker, callback) ->
    ActiveListener = (e) ->
      callback.apply marker, []

    Microsoft.Maps.Events.addHandler marker, 'click', ActiveListener
    Microsoft.Maps.Events.addHandler marker, 'dblclick', ActiveListener
    Microsoft.Maps.Events.addHandler marker, 'dragend', ActiveListener
    Microsoft.Maps.Events.addHandler marker, 'rightclick', ActiveListener

  addDragendListener: (marker, callback) ->
    Microsoft.Maps.Events.addHandler marker, 'dragend', (e) ->
      loc = e.entity.getLocation()
      callback.apply marker, [loc.latitude, loc.longitude]

  removeMarker: (marker, map) ->
    map.entities.remove(marker)

  addMapClickListener: (map, callback)  ->
    Microsoft.Maps.Events.addHandler map, 'click', (e) ->
      if e.targetType == "map" and not e.mouseMoved
        point = new Microsoft.Maps.Point(e.getX(), e.getY())
        loc = e.target.tryPixelToLocation(point);
        callback.apply map, [loc.latitude, loc.longitude]

  setPosition: (target, lat, lng) ->
    target.setLocation new Microsoft.Maps.Location(lat, lng)
  setMap: (target, map) ->

MapEventListener.factory = () ->
  return new cnmap.MapEventListener

window.cnmap = window.cnmap || {}
window.cnmap.MapEventListener = MapEventListener
