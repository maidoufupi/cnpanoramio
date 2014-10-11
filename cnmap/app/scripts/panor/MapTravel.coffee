
class ITravelLayer
  constructor: (@opts) ->
    {@ctx, @staticCtx, @map, @travel} = @opts if @opts
    @mapEventListener = window.cnmap.MapEventListener.factory()

  initMap: () ->

  ## 计算spot的时间
  calcSpotTime: () ->
    if @travel
      ## 计算出游览景点开始时间的最小值, 为后面计算第几天
      for spot in @travel.spots
        # 图片排序，按图片拍摄时间
        spot.photos.sort((a,b) -> return a.date_time-b.date_time)
        if spot.time_start
          spot.spotDate = new Date spot.time_start
        if spot.spotDate
          if !spotMinDate or spotMinDate > spot.spotDate
            spotMinDate = spot.spotDate

      # travel的开始时间
      @travel.time_start = spotMinDate

      for spot in @travel.spots
        ## 计算此景点是第几天游览
        if spot.spotDate
          spot.day = Math.ceil((spot.spotDate - spotMinDate) / (1000 * 60 * 60 * 24)) + 1

      # 按时间先后排序spot
      @travel.spots.sort((a,b) -> return a.day-b.day)

      # travel的结束时间
      if @travel.spots.length
        @travel.time_end = @travel.spots[@travel.spots.length-1].time_start

  setMap: (map) ->
    if map
      @map = map
      @initMap()

  setTravel: (travel) ->
    @travel = travel
    @travel.spots.sort((a,b) -> return a.id-b.id)
    for spot in @travel.spots
      spot.photos.sort((a,b) -> return a.date_time-b.date_time)

  ## 在地图上创建marker
  createMarker: (photo) ->
    ## TO IMPL
  ## 从地图上移除marker
  removeMarker: (photo) ->
    ## TO IMPL

  getLabelContent: (photoOssKey) ->
    "<img src='#{@staticCtx}/#{photoOssKey}@!panor-lg' style='border: 2px solid white; width: 34px; height: 34px;'>";

  activePhoto: (photo) ->

    if not @mapEventListener.inMapView(photo.point.lat, photo.point.lng, @map)
#      @mapEventListener.setPosition(@marker, photo.point.lat, photo.point.lng)
      @mapEventListener.setCenter(@map, photo.point.lat, photo.point.lng)
      @mapEventListener.setZoom(@map, 18)
    else
#      @mapEventListener.setPosition(@marker, photo.point.lat, photo.point.lng)
      @mapEventListener.setCenter(@map, photo.point.lat, photo.point.lng)

  addSpot: (spot) ->
    @travel.spots.push spot

  addPhoto: (spot, photo) ->
    spot.photos.push photo
    @calcSpotTime()
    @updateSpotLine(spot)

  removePhoto: (photo, spot) ->
    if spot
      for p in spot.photos when p.id == photo.id
        @removeMarker p
      spot.photos = (p for p in spot.photos when p.id != photo.id )
      @updateSpotLine(spot)
    else
      for spot in @travel.spots
        for p in spot.photos when p.id is photo.id
          @removeMarker p
        spot.photos = (p for p in spot.photos when p.id != photo.id )
        @updateSpotLine(spot)

    @calcSpotTime()

  removeSpot: (spot) ->
    @travel.spots = (sp for sp in @travel.spots when sp.id != spot.id )
    @calcSpotTime()
    @updateSpotLine(spot)

  activeSpot: (spotid) ->
    if not angular.isObject spotid
      for ispot in @travel.spots
        if parseInt(ispot.id, 10) == parseInt(spotid, 10)
          spot = ispot
          break
    else
      spot = spotid

    if spot.photos[0]
      sw = jQuery.extend {}, spot.photos[0].point
      ne = jQuery.extend {}, spot.photos[0].point

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

  ## 旅行是否可编辑
  setEditable: (editable) ->
    if not editable and @travel
      for spot in @travel.spots
        @setSpotEditable spot, false
    @opts.editable = editable
    if editable and @travel
      for spot in @travel.spots
        @setSpotEditable spot, true

  getEditable: () ->
    @opts.editable

  getMarkerImage: (photo) ->
    "#{@staticCtx}/#{photo.oss_key}@!panor-lg"

window.cnmap = window.cnmap ? {}

window.cnmap.ITravelLayer = ITravelLayer