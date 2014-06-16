
class ITravelLayer
  constructor: (@opts) ->
    {@ctx, @map, @travel} = @opts if @opts
    @mapEventListener = window.cnmap.MapEventListener.factory()
    @marker = @createMarker()

  initMap: () ->

  setMap: (map) ->
    if map
      @map = map
      @initMap()

  setTravel: (travel) ->
    @travel = travel
    @travel.spots.sort((a,b) -> return a.id-b.id)
    for spot in @travel.spots
      spot.photos.sort((a,b) -> return a.date_time-b.date_time)

  createLabel: (photo) ->

  getLabelContent: (photoId) ->
    "<img src='#{@ctx}/api/rest/photo/#{photoId}/3' style='border: 2px solid white; width: 34px; height: 34px;'>";


  activePhoto: (photo) ->

    if not @mapEventListener.inMapView(photo.point.lat, photo.point.lng, @map)
      @mapEventListener.setPosition(@marker, photo.point.lat, photo.point.lng)
      @mapEventListener.setCenter(@map, photo.point.lat, photo.point.lng)
      @mapEventListener.setZoom(@map, 18)
    else
      @mapEventListener.setPosition(@marker, photo.point.lat, photo.point.lng)
      @mapEventListener.setCenter(@map, photo.point.lat, photo.point.lng)

  createMarker: () ->

  activeSpot: (spot) ->
    if spot.photos[0]
      sw = jQuery.extend {}, spot.photos[0].point
      ne = jQuery.extend {}, spot.photos[0].point
#    if spot.photos[1]
#      if spot.photos[1].point.lat < sw.lat or spot.photos[1].point.lng < sw.lng
#        ne = sw
#        sw = jQuery.extend {}, spot.photos[1].point
#      else
#        ne = jQuery.extend {}, spot.photos[1].point

    if sw and ne
      for photo in spot.photos
        if photo.point.lat < sw.lat
          sw.lat = photo.point.lat
        if photo.point.lng < sw.lng
          sw.lng = photo.point.lng
        if photo.point.lat > ne.lat
          ne.lat = photo.point.lat
        if photo.point.lng > ne.lng
          ne.lng = photo.point.lng

      @mapEventListener.setBounds(@map, sw, ne)

  clearMap: () ->
    @mapEventListener.clearMap @map

window.cnmap = window.cnmap ? {}

window.cnmap.ITravelLayer = ITravelLayer