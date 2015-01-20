class TravelLayer extends window.cnmap.ITravelLayer

#  initMap: (map) ->
#    @map = map if map
#    @calcSpotTime()
#
#    if @travel
#      for spot in @travel.spots
#        @createMarker photo for photo in spot.photos
#        points = []
#        points.push @createPoint photo for photo in spot.photos
#
#        spot.polyline = @createPolyline points

  createPoint: (photo) ->
    new Microsoft.Maps.Location photo.point.lat, photo.point.lng

  createMarker: (photo) ->
    that = this
    marker = new Microsoft.Maps.Pushpin @createPoint(photo), {
      icon: @getMarkerImage(photo)
      width: 34
      height: 34
    }
    marker.photo = photo
    @map.entities.push marker
    if @opts.clickable
      Microsoft.Maps.Events.addHandler marker, 'click',
        (e) ->
          if not e.target.getDraggable()
            jQuery(that).trigger("data_clicked", [e.target.photo.id])
    photo.marker = marker
    marker

  createPolyline: (points) ->
    polyline = new Microsoft.Maps.Polyline points, {
      strokeThickness: 2
    }
    @map.entities.push polyline
    polyline

  setPolylinePath: (polyline, points) ->
    polyline.setLocations points

  removeMarker: (photo) ->
    if photo.marker
      @map.entities.remove photo.marker
      delete photo.marker

  toggleSpotLine: (spot, visible) ->
    if spot.polyline
      if visible
        spot.polyline.setOptions {
          visible: true
        }
      else
        spot.polyline.setOptions {
          visible: false
        }

  spotEditable: (spot, editable) ->
    editable = !!editable
    that = this
    editMarker = (marker) ->
      if marker
        if that.opts.editable and editable
          marker.setOptions {
            draggable: true
          }
        else
          marker.setOptions {
            draggable: false
          }
        if that.opts.editable and editable
          marker.dragListener = Microsoft.Maps.Events.addHandler marker, 'dragend',
            (e) ->
              photo = e.entity.photo
              if not photo.oPoint
                photo.oPoint = $.extend {}, photo.point
              loc = e.entity.getLocation()
              photo.point.lat = loc.latitude
              photo.point.lng = loc.longitude
              that.updateSpotLine spot
              $(that).trigger("spot.edited", [spot.id]);
        else
          Microsoft.Maps.Events.removeHandler marker.dragListener
          delete marker.dragListener
    editMarker photo.marker for photo in spot.photos

  cancelSpotEdit: (spot) ->
    that = this
    cancelMarker = (photo) ->
      if photo.oPoint
        photo.point = photo.oPoint
        delete photo.oPoint
      if photo.marker
        photo.marker.setLocation that.createPoint(photo)
    cancelMarker photo for photo in spot.photos
    @updateSpotLine spot

#  updateSpotLine: (spot) ->
#    points = []
#    points.push @createPoint photo for photo in spot.photos
#    if spot.polyline
#      spot.polyline.setLocations points
#    else
#      spot.polyline = @createPolyline points

window.cnmap.TravelLayer = TravelLayer
