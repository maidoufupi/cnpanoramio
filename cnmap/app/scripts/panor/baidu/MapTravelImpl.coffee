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

        spot.polyline = new BMap.Polyline point, {
            map: @map
            strokeStyle: 'dashed'
          }

        @map.addOverlay spot.polyline

  createPoint: (photo) ->
    new BMap.Point photo.point.lng, photo.point.lat

  createLabel: (photo) ->
    that = this
    label = new BMap.Label
    label.setContent @getLabelContent(photo.oss_key)
    label.setPosition @createPoint photo ##基点位置
    label.setStyle {
        border: 0,
        padding: 0
      }
    label.photoId = photo.id
    @map.addOverlay label

    if @opts.clickable
      label.addEventListener 'click',
        () ->
          jQuery(that).trigger("data_clicked", [this.photoId])

  toggleSpotLine: (spot, visible) ->
    if spot.polyline
      if visible
        spot.polyline.show()
      else
        spot.polyline.hide()

  updateSpotLine: (spot) ->
    point = []
    point.push @createPoint photo for photo in spot.photos
    if spot.polyline
      spot.polyline.setPath point
    else
      spot.polyline = new BMap.Polyline point, {
          map: @map
          strokeStyle: 'dashed'
        }
      @map.addOverlay spot.polyline

window.cnmap.TravelLayer = TravelLayer