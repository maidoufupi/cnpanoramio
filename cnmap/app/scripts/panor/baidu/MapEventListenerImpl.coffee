
$window = window
#BMap = $window.BMap
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
    map.addControl(new window.BMap.NavigationControl());
    map.addControl(new window.BMap.ScaleControl());
    map.addControl(new window.BMap.OverviewMapControl());
    map.addControl(new window.BMap.MapTypeControl());

  setCenter: (map, lat, lng) ->
    map.setCenter new BMap.Point lng, lat

  setZoom: (map, zoom) ->
    map.setZoom zoom

  setZoomAndCenter: (map, zoom, lat, lng) ->
    point = new BMap.Point lng, lat
    map.centerAndZoom point, zoom

  zoomIn: (map) ->
    map.zoomIn()

  zoomOut: (map) ->
    map.zoomOut()

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
    marker = new BMap.Marker new BMap.Point lng, lat
    map.addOverlay marker
    marker

  createDraggableMarker: (map, lat, lng) ->
    marker = new BMap.Marker new BMap.Point(lng, lat), {enableDragging: true}
    map.addOverlay marker
    return marker

  activeMarker: (marker) ->
    if marker
      marker.defaultIcon = marker.getIcon()
      marker.setIcon new BMap.Icon "images/marker.png", new BMap.Size(20, 30), {anchor: new BMap.Size(10, 30)}
      marker.setZIndex 2

  deactiveMarker: (marker) ->
    if marker and marker.defaultIcon
      marker.setIcon marker.defaultIcon
      marker.setZIndex 1

  addMarkerActiveListener: (marker, callback) ->
    ActiveListener = (e) ->
      callback.apply marker, []

    marker.addEventListener "click", ActiveListener
    marker.addEventListener "dragend", ActiveListener
    marker.addEventListener "rightclick", ActiveListener


  addDragendListener: (marker, callback) ->
    marker.addEventListener "dragend", (e) ->
      callback.apply marker, [e.point.lat, e.point.lng]

  removeMarker: (marker) ->
    map = marker.getMap()
    if map
      map.removeOverlay marker

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