$window = window

class MapService extends window.cnmap.IMapService
  constructor: (@map) ->
#    @geocoder = L.mapbox.geocoder('mapbox.places')

  init: (map, callback) ->
    @geocoder = L.mapbox.geocoder('mapbox.places')

  getAddress: (lat, lng, callback) ->
    if !@geocoder
      @init()
    # 根据坐标得到地址描述
    @geocoder.getLocation new window.BMap.Point(lng, lat), (result) ->
      if result
        callback.apply undefined, [result.address]

  getAddrPois: (lat, lng, callback) ->
    deferred = jQuery.Deferred()

    @geocoder.reverseQuery {lat: lat, lng: lng}, (err, data) ->
      console.log data
      addresses = {}
      for feature in data.features
        address = feature.place_name
        addresses[address] = {
          poiweight: feature.relevance
          location: {lat: feature.center[1], lng: feature.center[0]}
        }
      if data.features[0]
        deferred.resolve addresses, data.features[0].place_name


#    reverseGeocodeCallback = (result, userData) ->
#      addresses = {}
#      if result
#        address = result.name
#        addresses[address] = {
#          poiweight: result.matchConfidence
#          location: {lat: result.location.latitude, lng: result.location.longitude}
#        }
#        deferred.resolve addresses, result.name
#
#    errCallback = (request) ->
#      console.log('reverseGeocode error')
#    reverseGeocodeRequest = {
#      location: new Microsoft.Maps.Location(lat, lng)
#      count:10
#      callback:reverseGeocodeCallback
#      errorCallback:errCallback
#    }
#
#    @searchManager.reverseGeocode(reverseGeocodeRequest);
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

    @geocoder.query address, (err, data) ->
      console.log data.results
      addresses = []
      for feature in data.results.features
        address = {
          address: feature.place_name
          location: {
            lat: feature.center[1]
            lng: feature.center[0]
          }
          bounds: {
            sw: {
              lat: feature.bbox[1]
              lng: feature.bbox[0]
            }
            ne: {
              lat: feature.bbox[3]
              lng: feature.bbox[2]
            }
          }
          similarity: feature.relevance
        }
        addresses.push address
      deferred.resolve addresses


#    geocodeCallback = (geocodeResult, userData) ->
#      console.log(geocodeResult)
#      console.log(userData)
#      addresses = []
#      for result in geocodeResult.results
#        address = {
#          address: result.name
#          location: {
#            lat: result.location.latitude
#            lng: result.location.longitude
#          }
#          similarity: result.matchConfidence
#        }
#        if result.bestView
#          sw = {
#            lat: result.bestView.center.latitude - result.bestView.height/2
#          }
#          lng = result.bestView.center.longitude - result.bestView.width/2
#          if lng < -179
#            sw.lng = 180 + lng + 180
#          else
#            sw.lng = lng
#          ne = {
#            lat: result.bestView.center.latitude + result.bestView.height/2
#          }
#          lng = result.bestView.center.longitude + result.bestView.width/2
#          if lng > 180
#            ne.lng = lng - 180 - 180
#          else
#            ne.lng = lng
#
#          address.bounds = {
#            sw: sw
#            ne: ne
#          }
#        addresses.push address
#      deferred.resolve addresses
#    errCallback = () ->
#
#    geocodeRequest = {where: address, count:10, callback:geocodeCallback, errorCallback:errCallback};
#    @searchManager.geocode geocodeRequest

    deferred.promise()

instance = undefined
MapService.factory = () ->
  if !instance
    instance = new window.cnmap.MapService
  instance

window.cnmap = window.cnmap || {}
window.cnmap.MapService = MapService
