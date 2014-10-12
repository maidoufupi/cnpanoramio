class TravelLayer extends window.cnmap.ITravelLayer

  initMap: (map) ->
    @map = map if map
    @calcSpotTime()
    point = []
    if @travel
      for spot in @travel.spots
        @createMarker photo for photo in spot.photos
        point = []
        point.push @createPoint photo for photo in spot.photos
        spot.polyline = new AMap.Polyline {
            map: @map
            path: point
            strokeWeight: 2
#            strokeStyle: 'dashed'
          }

  createPoint: (photo) ->
    new AMap.LngLat(photo.point.lng, photo.point.lat)

  createMarker: (photo) ->
    that = this
    marker = new AMap.Marker({
      map: @map
      position: new AMap.LngLat(photo.point.lng, photo.point.lat) ##基点位置
      offset: new AMap.Pixel(-15, -15) ##相对于基点的偏移位置
      content: @getLabelContent(photo.oss_key)  ##自定义点标记覆盖物内容
      topWhenClick: true
    })
    marker.photo = photo
    if @opts.clickable
      AMap.event.addListener(marker, 'click',
        () ->
          jQuery(that).trigger("data_clicked", [this.photo.id]))
    photo.marker = marker
    marker

  removeMarker: (photo) ->
    if photo.marker
      photo.marker.setMap null
      delete photo.marker

  toggleSpotLine: (spot, visible) ->
    if spot.polyline
      if visible
        spot.polyline.show()
      else
        spot.polyline.hide()

  spotEditable: (spot, editable) ->
    editable = !!editable
    that = this
    editMarker = (marker) ->
      if marker
        marker.setDraggable(that.opts.editable and editable)
        if that.opts.editable and editable
          marker.dragListener = AMap.event.addListener marker, 'dragend',
            (e) ->
              if not this.photo.oPoint
                this.photo.oPoint = $.extend {}, this.photo.point
              this.photo.point.lat = e.lnglat.getLat()
              this.photo.point.lng = e.lnglat.getLng()
              that.updateSpotLine spot
              $(that).trigger("spot.edited", [spot.id]);
        else if marker.dragListener
          AMap.event.removeListener marker.dragListener
          marker.dragListener = null
    editMarker photo.marker for photo in spot.photos

  cancelSpotEdit: (spot) ->
    cancelMarker = (photo) ->
      if photo.oPoint
        photo.point = photo.oPoint
        delete photo.oPoint
      if photo.marker
        photo.marker.setPosition new AMap.LngLat(photo.point.lng, photo.point.lat)
    cancelMarker photo for photo in spot.photos
    @updateSpotLine spot

  updateSpotLine: (spot) ->
    point = []
    point.push @createPoint photo for photo in spot.photos
    if spot.polyline
      spot.polyline.setPath point
    else
      spot.polyline = new AMap.Polyline {
          map: @map
          path: point
          strokeWeight: 2
#          strokeStyle: 'dashed'
        }

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

#  createLine: (spots) ->
#    path = for spot, i in spots
#      do (spot, i) ->
#        new AMap.LngLat(spot.center_lng, spot.center_lat)
#    new AMap.Polyline {
#        map: @map
#        path: path
#        strokeStyle: 'dashed'
#      }

window.cnmap.TravelLayer = TravelLayer