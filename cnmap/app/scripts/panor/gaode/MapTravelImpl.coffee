class TravelLayer extends window.cnmap.ITravelLayer

  initMap: (map) ->
    @map = map if map
#    @mapEventListener.setMap @marker, @map

    @calcSpotTime()

    point = []
    if @travel
      for spot in @travel.spots
        @createLabel photo for photo in spot.photos
        point = []
        point.push @createPoint photo for photo in spot.photos
        new AMap.Polyline {
          map: @map
          path: point
          strokeStyle: 'dashed'
        }

  createPoint: (photo) ->
    new AMap.LngLat(photo.point.lng, photo.point.lat)

  createLabel: (photo) ->
    that = this
    label = new AMap.Marker({
      map: @map
      position: new AMap.LngLat(photo.point.lng, photo.point.lat) ##基点位置
      offset: new AMap.Pixel(-15, -15) ##相对于基点的偏移位置
      content: @getLabelContent(photo.oss_key)  ##自定义点标记覆盖物内容
    })
    label.photoId = photo.id
    if @opts.clickable
      AMap.event.addListener(label, 'click',
        () ->
          jQuery(that).trigger("data_clicked", [this.photoId]))

    label.photoId = photo.id
    label.setMap(@map)

  createSpot: (spot) ->
    distance = 0
    for photo in spot.photos
      cdistance = window.cnmap.GPS.distanceInMeter(spot.center_lat, spot.center_lng, photo.point.lat, photo.point.lng)
      if cdistance > distance
        distance = cdistance
    new AMap.Circle {
      map: @map
      center: new AMap.LngLat(spot.center_lng, spot.center_lat)
      radius: distance
      strokeStyle: 'dashed'
      strokeColor: '#0066FF'
      strokeOpacity: 0.2
      fillColor: '#0066FF'
      fillOpacity: 0.2
    }

  createLine: (spots) ->
    path = for spot, i in spots
      do (spot, i) ->
        new AMap.LngLat(spot.center_lng, spot.center_lat)
    new AMap.Polyline {
        map: @map
        path: path
        strokeStyle: 'dashed'
      }

  createMarker: () ->
#    if window.AMap
#      new AMap.Marker {
#        map: @map
#        icon: "images/marker.png"
#        animation: "AMAP_ANIMATION_BOUNCE"
#      }
window.cnmap.TravelLayer = TravelLayer