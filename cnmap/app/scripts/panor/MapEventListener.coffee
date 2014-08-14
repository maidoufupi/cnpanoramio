 ##
 ## Created by any on 14-4-12.
 ##

class IMapEventListener
  opts: {map: null}

  ##
   # 添加地图获取location hash值的监听接口
   #
   # @param map
   # @param callback (lat, lng, zoom)
   #/
  addLocationHashListener: (map, callback) ->

  ##
  #
  # @param listeners : listener Array
  removeListener: (listeners) ->

  ##
   # 为地图添加ToolBar控件
   #
   # @param map
   #/
  addToolBar: (map) ->

  ##
   # 设置地图中心坐标
   #
   # @param map
   # @param lat
   # @param lng
   #/
  setCenter: (map, lat, lng) ->

  ##
   # 设置地图的缩放级别
   #
   # @param map
   # @param zoom
   #/
  setZoom: (map, zoom) ->

  ##
    # 设置地图的缩放级别
    #
    # @param map
    # @param zoom
    #/
  setZoomAndCenter: (map, zoom, lat, lng) ->

  ##
   # 设置地图边界
   #
   # @param map
   # @param sw
   # @param ne
   #/
  setBounds: (map, sw, ne) ->

  ##
  # 删除地图上所有的覆盖物
  #
  # @param map
  #/
  clearMap: (map) ->

  ##
   # 判断gps坐标是否在地图视野内
   #
   # @param lat
   # @param lng
   # @param map
   #/
  inMapView: (lat, lng, map) ->

  ##
   # 添加标记
   #
   # @param map
   # @param lat
   # @param lng
   #/
  addMarker: (map, lat, lng) ->

  ##
   # 创建可移动的marker
   #
   # @param map
   # @param lat
   # @param lng
   #/
  createDraggableMarker: (map, lat, lng) ->

  ##
   # 激活marker
   #
   # @param marker
   #/
  activeMarker: (marker) ->

  ##
   # 取消激活marker
   #
   # @param marker
   #/
  deactiveMarker: (marker) ->

  ##
   # 为marker添加激活状态的监听接口
   #
   # @param marker
   # @param callback ()
   #/
  addMarkerActiveListener: (marker, callback) ->

  ##
   # 为marker添加移动结束后的监听接口
   #
   # @param marker
   # @param callback (lat, lng)
   #/
  addDragendListener: (marker, callback) ->

  ##
  # 从地图上移除marker
  #
  # @param marker
  # @param callback (lat, lng)
  #/
  removeMarker: (marker) ->

  ##
   # 为地图添加点击监听
   #
   # @param map
   # @param callback (lat, lng)
   #/
  addMapClickListener: (map, callback)  ->

  ##
   # 为目标对象设置位置
   #
   # @param target
   # @param lat
   # @param lng
   #/
  setPosition: (target, lat, lng) ->

  ##
   # 为目标对象设置地图并show
   #
   # @param target
   # @param map
   #/
  setMap: (target, map) ->

window.cnmap = window.cnmap || {}
window.cnmap.IMapEventListener = IMapEventListener