
class IMapService

  ## 地图 
  map: undefined

  ##
   # 初始化地图
   #
   # @param map
   # @param callback (Geocoder)
   #/
  init: (map, callback) ->

  ##
   # 根据gps获取地址信息
   #
   # @param lat
   # @param lng
   # @param callback
   #/
  getAddress: (lat, lng, callback) ->

  ##
  # 获取gps点附近的兴趣点
  #
  # @param lat
  # @param lng
  # @param callback
  #/
  getAddrPois: (lat, lng, callback) ->

  ##
  # 根据地址获取gps信息
  #
  # @param address
  # @param callback
  #/
  getLocation:  (address, callback) ->

  ##
  # 根据地址获取gps信息(pois信息)
  #
  # @param address
  # @param callback
  #/
  getLocPois:   (address, callback) ->

  ##
  # 转换gps坐标
  #
  # @param address
  # @param callback
  #/
  translate: (lat, lng, callback) ->

window.cnmap = window.cnmap || {}
window.cnmap.IMapService = IMapService
