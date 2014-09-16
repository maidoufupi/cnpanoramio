
$window = window
BMap = $window.BMap
class MapService extends window.cnmap.IMapService
  constructor: (@map) ->

  init: (map, callback) ->
    @geocoder = new BMap.Geocoder();
    if callback
      callback.apply @geocoder, [@geocoder]

  getAddress: (lat, lng, callback) ->
    if !@geocoder
      @init()
    # 根据坐标得到地址描述
    @geocoder.getLocation new BMap.Point(lng, lat), (result) ->
        if result
          callback.apply undefined, [result.address]

  getAddrPois: (lat, lng, callback) ->
    deferred = jQuery.Deferred()
    @geocoder.getLocation new BMap.Point(lng, lat), (result) ->
      console.log(result)
      addresses = {}
      if result
        $.each result.surroundingPois, (index, poi) ->
            if poi.title
              address = poi.address + " / " + poi.title
            else
              address = poi.address

            addresses[address] = {
              poiweight: 1
              location: poi.point
            };
      deferred.resolve addresses, result.address

    deferred.promise()

  getLocation: (address, callback) ->
    if !@geocoder
      @init()
    @geocoder.getPoint address, (point) ->
      if point
        callback.apply(undefined, [point])

  getLocPois: (address) ->
    deferred = jQuery.Deferred()

    if !@geocoder
      @init()
    @geocoder.getPoint address, (point) ->
      addresses = [];
#      console.log(point)
      if point
        addresses.push {
            address: address
            location: point
            similarity: 1
    #        zoom: levelMap[geocode.level] || 4
          }
      deferred.resolve addresses

    deferred.promise()

MapService.factory = () ->
  return new window.cnmap.MapService

window.cnmap = window.cnmap || {}
window.cnmap.MapService = MapService