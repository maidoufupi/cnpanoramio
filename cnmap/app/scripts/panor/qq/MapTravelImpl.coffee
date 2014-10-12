class TravelLayer extends window.cnmap.ITravelLayer

  initMap: (map) ->
    @map = map if map
    @calcSpotTime()
    @labels = []
    point = []
    if @travel
      for spot in @travel.spots
        @createMarker photo for photo in spot.photos
        point = []
        point.push @createPoint photo for photo in spot.photos
        spot.polyline = new qq.maps.Polyline {
          map: @map
          path: point
#          strokeDashStyle: 'dash'
          strokeWeight: 2
        }
        @labels.push spot.polyline

  createPoint: (photo) ->
    new qq.maps.LatLng(photo.point.lat, photo.point.lng)

  createMarker: (photo) ->
    that = this
    marker = new qq.maps.Marker({
      icon: new qq.maps.MarkerImage("#{@staticCtx}/#{photo.oss_key}@!panor-lg")
      title: photo.title||''
      map: @map
      position: new qq.maps.LatLng(photo.point.lat, photo.point.lng)
    })
    marker.photo = photo
    if @opts.clickable
      qq.maps.event.addListener(marker, 'click',
        () ->
          jQuery(that).trigger("data_clicked", [this.photo.id]))
    photo.marker = marker
    @labels.push marker
    marker

  removeMarker: (photo) ->
    if photo.marker
      photo.marker.setMap null
      delete photo.marker

  toggleSpotLine: (spot, visible) ->
    if spot.polyline
      spot.polyline.setVisible visible

  spotEditable: (spot, editable) ->
    editable = !!editable
    that = this
    editMarker = (marker) ->
      if marker
        marker.setDraggable(that.opts.editable and editable)
        if that.opts.editable and editable
          marker.dragListener = qq.maps.event.addListener marker, 'dragend',
            (e) ->
              if not this.photo.oPoint
                this.photo.oPoint = $.extend {}, this.photo.point
              this.photo.point.lat = e.latLng.lat
              this.photo.point.lng = e.latLng.lng
              that.updateSpotLine spot
              $(that).trigger("spot.edited", [spot.id]);
        else if marker.dragListener
          qq.maps.event.removeListener marker.dragListener
          marker.dragListener = null
    editMarker photo.marker for photo in spot.photos

  cancelSpotEdit: (spot) ->
    cancelMarker = (photo) ->
      if photo.oPoint
        photo.point = photo.oPoint
        delete photo.oPoint
      if photo.marker
        photo.marker.setPosition new qq.maps.LatLng(photo.point.lat, photo.point.lng)
    cancelMarker photo for photo in spot.photos
    @updateSpotLine spot

  updateSpotLine: (spot) ->
    point = []
    point.push @createPoint photo for photo in spot.photos
    if spot.polyline
      spot.polyline.setPath point
    else
      spot.polyline = new qq.maps.Polyline {
          map: @map
          path: point
#          strokeDashStyle: 'dash'
          strokeWeight: 2
        }
      @labels.push spot.polyline

  clearMap: () ->
    for overlay in @labels
      overlay.setMap null
      overlay.setVisible false
    @labels = [];

window.cnmap.TravelLayer = TravelLayer