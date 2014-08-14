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

        @map.addOverlay new BMap.Polyline point, {
            map: @map
            strokeStyle: 'dashed'
          }

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

window.cnmap.TravelLayer = TravelLayer