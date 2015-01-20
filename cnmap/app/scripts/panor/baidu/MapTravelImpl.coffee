class TravelLayer extends window.cnmap.ITravelLayer

#  initMap: (map) ->
#    @map = map if map
#    @calcSpotTime()
#    point = []
#    if @travel
#      for spot in @travel.spots
#        @createMarker photo for photo in spot.photos when !!photo.point
#        point = []
#        point.push @createPoint photo for photo in spot.photos when !!photo.point
#
#        spot.polyline = new BMap.Polyline point, {
#          map: @map
#          strokeWeight: 2
##            strokeStyle: 'dashed'
#        }
#        @map.addOverlay spot.polyline

  createPoint: (photo) ->
    new BMap.Point photo.point.lng, photo.point.lat

  createMarker: (photo) ->
    if !photo.point
      return
    that = this
    marker = new BMap.Marker @createPoint(photo), {
      icon: new BMap.Icon @getMarkerImage(photo), {
        width: 34
        height: 34
      }
      title: photo.title || ''
    }
    marker.photo = photo
    @map.addOverlay marker
    if @opts.clickable
      marker.addEventListener 'click',
        (e) ->
          jQuery(that).trigger("data_clicked", [this.photo.id])
    photo.marker = marker
    marker

  createPolyline: (points) ->
    polyline = new BMap.Polyline points, {
        map: @map
        strokeWeight: 2
#            strokeStyle: 'dashed'
      }
    @map.addOverlay polyline
    polyline

  setPolylinePath: (polyline, points) ->
    polyline.setPath points

  removeMarker: (photo) ->
    if photo.marker
      @map.removeOverlay photo.marker
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
        if that.opts.editable and editable
          marker.enableDragging()
        else
          marker.disableDragging()
        if that.opts.editable and editable
          marker.addEventListener 'dragend',
            (e) ->
              if not this.photo.oPoint
                this.photo.oPoint = $.extend {}, this.photo.point
              this.photo.point.lat = e.point.lat
              this.photo.point.lng = e.point.lng
              that.updateSpotLine spot
              $(that).trigger("spot.edited", [spot.id]);
        else
          marker.removeEventListener 'dragend'
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
#    point = []
#    point.push @createPoint photo for photo in spot.photos
#    if spot.polyline
#      spot.polyline.setPath point
#    else
#      spot.polyline = new BMap.Polyline point, {
#        map: @map
#        strokeStyle: 'dashed'
#      }
#      @map.addOverlay spot.polyline

window.cnmap.TravelLayer = TravelLayer
