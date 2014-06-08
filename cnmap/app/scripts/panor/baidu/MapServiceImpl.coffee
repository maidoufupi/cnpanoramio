$window = window
BMap = $window.BMap
class MapService extends window.cnmap.IMapService
  constructor: (@map) ->

  init: (map, callback) ->
    @geocoder = new BMap.Geocoder();
    callback.apply @geocoder, [@geocoder]

  getAddress: (lat, lng, callback) ->
    if !@geocoder
      @init()
    # 根据坐标得到地址描述
    @geocoder.getLocation new BMap.Point(lng, lat), (result) ->
        if result
          callback.apply undefined, [result.address]

  getLocation: (address, callback) ->
    if !@geocoder
      @init()
    @geocoder.getPoint address, (point) ->
      if point
        callback.apply(undefined, [point])