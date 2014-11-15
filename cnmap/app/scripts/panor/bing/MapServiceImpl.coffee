$window = window

class MapService extends window.cnmap.IMapService
  constructor: (@map) ->

  init: (map, callback) ->
    that = @
    createSearch = () ->
      that.searchManager  = new Microsoft.Maps.Search.SearchManager map
      if callback
        callback.apply @searchManager, [@searchManager]
    searchModuleLoaded = () ->
      createSearch()
    if not Microsoft.Maps.Search
      Microsoft.Maps.loadModule('Microsoft.Maps.Search', { callback: searchModuleLoaded });
    else
      createSearch()
  getAddress: (lat, lng, callback) ->
    if !@geocoder
      @init()
    # 根据坐标得到地址描述
    @geocoder.getLocation new window.BMap.Point(lng, lat), (result) ->
        if result
          callback.apply undefined, [result.address]

  getAddrPois: (lat, lng, callback) ->
    deferred = jQuery.Deferred()
    reverseGeocodeCallback = (result, userData) ->
      addresses = {}
      if result
        address = result.name
        addresses[address] = {
          poiweight: result.matchConfidence
          location: {lat: result.location.latitude, lng: result.location.longitude}
          }
        deferred.resolve addresses, result.name

    errCallback = (request) ->
      console.log('reverseGeocode error')
    reverseGeocodeRequest = {
      location: new Microsoft.Maps.Location(lat, lng)
      count:10
      callback:reverseGeocodeCallback
      errorCallback:errCallback
    }

    @searchManager.reverseGeocode(reverseGeocodeRequest);
#    @searchManager.getLocation new window.BMap.Point(lng, lat), (result) ->
#      console.log(result)
#      addresses = {}
#      if result
#        $.each result.surroundingPois, (index, poi) ->
#            if poi.title
#              address = poi.address + " " + poi.title
#            else
#              address = poi.address
#
#            addresses[address] = {
#              poiweight: 1
#              location: poi.point
#            };
#      deferred.resolve addresses, result.address

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

  translate: (lat, lng, callback) ->
instance = undefined
MapService.factory = () ->
  if !instance
    instance = new window.cnmap.MapService
  instance

window.cnmap = window.cnmap || {}
window.cnmap.MapService = MapService
