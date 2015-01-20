class TravelLayer extends window.cnmap.ITravelLayer

#  initMap: (map) ->
#    @map = map if map
#    @calcSpotTime()
#    @labels = []
#    point = []
#    if @travel
#      for spot in @travel.spots
#        @createMarker photo for photo in spot.photos
#        point = []
#        point.push @createPoint photo for photo in spot.photos
#        spot.polyline = new google.maps.Polyline {
#          map: @map
#          path: point
##          strokeDashStyle: 'dash'
#          strokeWeight: 2
#        }
#        @labels.push spot.polyline

  createPoint: (photo) ->
    new google.maps.LatLng(photo.point.lat, photo.point.lng)

  createMarker: (photo) ->
    that = this
    marker = new google.maps.Marker({
      map: @map
      position: @createPoint photo ##基点位置
      icon: @getMarkerImage photo
    })
    marker.photo = photo
    if @opts.clickable
      google.maps.event.addListener(marker, 'click',
        (e) ->
          jQuery(that).trigger("data_clicked", [this.photo.id]))
    photo.marker = marker
    @labels.push marker
    marker

  createPolyline: (points) ->
    new google.maps.Polyline {
      map: @map
      path: point
#          strokeDashStyle: 'dash'
      strokeWeight: 2
    }

  setPolylinePath: (polyline, points) ->
    polyline.setPath points

  removeMarker: (photo) ->
    if photo.marker
      photo.marker.setMap null
      delete photo.marker

  toggleSpotLine: (spot, visible) ->
    if spot.polyline
      spot.polyline.setVisible visible

  spotEditable: (spot, editable) ->
    that = this
    editMarker = (marker) ->
      if marker
        marker.setDraggable(that.opts.editable and editable)
        if that.opts.editable and editable
          marker.dragListener = google.maps.event.addListener marker, 'dragend',
          (e) ->
            if not this.photo.oPoint
              this.photo.oPoint = $.extend {}, this.photo.point
            this.photo.point.lat = e.latLng.lat()
            this.photo.point.lng = e.latLng.lng()
            that.updateSpotLine spot
            $(that).trigger("spot.edited", [spot.id]);
        else if marker.dragListener
          google.maps.event.removeListener marker.dragListener
          marker.dragListener = null
    editMarker photo.marker for photo in spot.photos

  cancelSpotEdit: (spot) ->
    that = this
    cancelMarker = (photo) ->
      if photo.oPoint
        photo.point = photo.oPoint
        delete photo.oPoint
      if photo.marker
        photo.marker.setPosition that.createPoint(photo)
    cancelMarker photo for photo in spot.photos
    @updateSpotLine spot

#  updateSpotLine: (spot) ->
#    points = []
#    points.push @createPoint photo for photo in spot.photos
#    if spot.polyline
#      spot.polyline.setPath points
#    else
#      spot.polyline = new google.maps.Polyline {
#        map: @map
#        path: points
##        strokeDashStyle: 'dash'
#        strokeWeight: 2
#      }
#      @labels.push spot.polyline

  clearMap: () ->
    $.each @labels, (index, label) ->
      label.setMap null
      label.setVisible false
    @labels = [];

window.cnmap.TravelLayer = TravelLayer
