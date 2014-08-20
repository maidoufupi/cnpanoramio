$window = window
BMap = $window.BMap
class MapEventListener extends window.cnmap.IMapEventListener
  addLocationHashListener: (map, callback) ->
    listeners = []
    MapListener = (e) ->
      point = this.getCenter()
      callback.apply this, [point.lat, point.lng, this.getZoom()]
    listeners.push map.addEventListener "moveend", MapListener
    listeners.push map.addEventListener "zoomend", MapListener
    listeners.push map.addEventListener "dragend", MapListener
    listeners

  addToolBar: (map) ->
    map.addControl(new BMap.NavigationControl());
    map.addControl(new BMap.ScaleControl());
    map.addControl(new BMap.OverviewMapControl());
    map.addControl(new BMap.MapTypeControl());

  setCenter: (map, lat, lng) ->
    map.setCenter new BMap.Point lng, lat

  setZoom: (map, zoom) ->
    map.setZoom zoom

  setZoomAndCenter: (map, zoom, lat, lng) ->
    point = new BMap.Point lng, lat
    map.centerAndZoom point, zoom

  setBounds: (map, sw, ne) ->
    map.setViewport [new BMap.Point(sw.lng, sw.lat), new BMap.Point(ne.lng, ne.lat)]

  inMapView: (lat, lng, map) ->
    map = map || @opts.map
    map.getBounds().containsPoint(new BMap.Point(lng, lat))

  pixelToPoint: (map, pixel) ->
    map.pixelToPoint new BMap.Pixel(pixel.x, pixel.y)

  pointToPixel: (map, point) ->
    map.pointToPixel new BMap.Point(point.lng, pixel.lat)

  addMarker: (map, lat, lng) ->
    map.addOverlay new BMap.Marker new BMap.Point lng, lat

  createDraggableMarker: (map, lat, lng) ->
    marker = new BMap.Marker new BMap.Point(lng, lat), {enableDragging: true}
    map.addOverlay marker
    return marker

  activeMarker: (marker) ->
    if marker
      marker.setIcon new BMap.Icon "images/marker.png", new BMap.Size(20, 30)

  deactiveMarker: (marker) ->
    if marker
      marker.setIcon ""

  addMarkerActiveListener: (marker, callback) ->
    marker.addEventListener "click", ActiveListener
    marker.addEventListener "dragend", ActiveListener
    marker.addEventListener "rightclick", ActiveListener
    ActiveListener = (e) ->
      callback.apply marker, []

  addDragendListener: (marker, callback) ->
    marker.addEventListener "dragend", (e) ->
      callback.apply marker, [e.point.lat, e.point.lng]

  addMapClickListener: (map, callback)  ->
    map.addEventListener "click", (e) ->
      callback.apply map, [e.point.lat, e.point.lng]

  setPosition: (target, lat, lng) ->
    target.setPosition new BMap.Point(lng, lat)

  setMap: (target, map) ->
    map.addOverlay target

MapEventListener.factory = () ->
  return new cnmap.MapEventListener

window.cnmap = window.cnmap || {}
window.cnmap.MapEventListener = MapEventListener