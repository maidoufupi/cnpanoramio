class TravelLayer extends window.cnmap.ITravelLayer

  initMap: (map) ->
    @map = map if map
#    @mapEventListener.setMap @marker, @map

    @calcSpotTime()
    @labels = []

    point = []
    if @travel
      for spot in @travel.spots
        @labels.push @createLabel photo for photo in spot.photos
        point = []
        point.push @createPoint photo for photo in spot.photos
        spot.polyline = new qq.maps.Polyline {
          map: @map
          path: point
          strokeDashStyle: 'dash'
          strokeWeight: 5
        }
        @labels.push spot.polyline


  createPoint: (photo) ->
    new qq.maps.LatLng(photo.point.lat, photo.point.lng)

  createLabel: (photo) ->
    that = this
    label = new qq.maps.Label({
      map: @map
      position: new qq.maps.LatLng(photo.point.lat, photo.point.lng) ##基点位置
      content: @getLabelContent(photo.oss_key)  ##自定义点标记覆盖物内容
      style: {
        padding: 0
        border: 0
      }
    })
    label.photoId = photo.id
    if @opts.clickable
      qq.maps.event.addListener(label, 'click',
        () ->
          jQuery(that).trigger("data_clicked", [this.photoId]))

    label.photoId = photo.id
    label.setMap(@map)
    return label

  toggleSpotLine: (spot, visible) ->
    if spot.polyline
      spot.polyline.setVisible visible

  updateSpotLine: (spot) ->
    point = []
    point.push @createPoint photo for photo in spot.photos
    if spot.polyline
      spot.polyline.setPath point
    else
      spot.polyline = new qq.maps.Polyline {
          map: @map
          path: point
          strokeDashStyle: 'dash'
          strokeWeight: 5
        }
      @labels.push spot.polyline

#  createSpot: (spot) ->
#    distance = 0
#    for photo in spot.photos
#      cdistance = window.cnmap.GPS.distanceInMeter(spot.center_lat, spot.center_lng, photo.point.lat, photo.point.lng)
#      if cdistance > distance
#        distance = cdistance
#    new AMap.Circle {
#      map: @map
#      center: new AMap.LngLat(spot.center_lng, spot.center_lat)
#      radius: distance
#      strokeStyle: 'dashed'
#      strokeColor: '#0066FF'
#      strokeOpacity: 0.2
#      fillColor: '#0066FF'
#      fillOpacity: 0.2
#    }

#  createLine: (spots) ->
#    path = for spot, i in spots
#      do (spot, i) ->
#        new AMap.LngLat(spot.center_lng, spot.center_lat)
#    new AMap.Polyline {
#        map: @map
#        path: path
#        strokeStyle: 'dashed'
#      }

#  createMarker: () ->
#    new AMap.Marker {
#      map: @map
#      icon: "images/marker.png"
#      animation: "AMAP_ANIMATION_BOUNCE"
#    }

  clearMap: () ->
    $.each @labels, (index, label) ->
      label.setMap null
      label.setVisible false
    @labels = [];
window.cnmap.TravelLayer = TravelLayer