$window = window
class MapService extends window.cnmap.IMapService
  constructor: (@map) ->

  init: (map, callback) ->
    @geocoder = new $window.google.maps.Geocoder();
    if callback
      callback.apply @geocoder, [@geocoder]

  getAddress: (lat, lng, callback) ->
    if !@geocoder
      @init()
    # 根据坐标得到地址描述
    @geocoder.geocode {
        location: new $window.google.maps.LatLng(lat, lng)
      },
      (result) ->
        if result
          callback.apply undefined, [result.address]

  getAddrPois: (lat, lng, callback) ->
    deferred = jQuery.Deferred()
    @geocoder.geocode {location: new $window.google.maps.LatLng(lat, lng)}, (result) ->
      console.log(result)
      addresses = {}
      if result
        $.each result, (index, poi) ->
          address = poi.formatted_address
          addresses[address] = {
            poiweight: 1
            location: poi.geometry.location
          }
      if result and result[0]
        deferred.resolve addresses, result[0].formatted_address
      else
        deferred.reject "no result"

    deferred.promise()

  getLocation: (address, callback) ->
    if !@geocoder
      @init()
    @geocoder.geocode {address: address}, (point) ->
      if point
        callback.apply(undefined, [point])

  getLocPois: (address) ->
    deferred = jQuery.Deferred()

    if !@geocoder
      @init()
    @geocoder.geocode {address: address}, (ress) ->
      addresses = [];
      if ress
        for res in ress
          address = {
            address: res.formatted_address
            location:{ lat: res.geometry.location.lat(), lng: res.geometry.location.lng()}
            similarity: 1
          }
          if res.geometry.bounds
            address.bounds = { sw : { lat : res.geometry.bounds.getSouthWest().lat()
            , lng : res.geometry.bounds.getSouthWest().lng()
            }
            , ne : { lat : res.geometry.bounds.getNorthEast().lat()
              , lng: res.geometry.bounds.getNorthEast().lng()
              }
            }
          addresses.push address
      deferred.resolve addresses

    deferred.promise()

  translate: (lat, lng, callback) ->


MapService.factory = () ->
  return new window.cnmap.MapService

window.cnmap = window.cnmap || {}
window.cnmap.MapService = MapService
