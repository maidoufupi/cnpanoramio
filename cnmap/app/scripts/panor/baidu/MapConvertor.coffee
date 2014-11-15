load_script = (xyUrl, callback) ->
  head = document.getElementsByTagName('head')[0]
  script = document.createElement('script')
  script.type = 'text/javascript'
  script.src = xyUrl
  ## 借鉴了jQuery的script跨域方法
  script.onload = script.onreadystatechange = () ->
    if((!this.readyState || this.readyState == "loaded" || this.readyState == "complete"))
      callback && callback()
      ## Handle memory leak in IE
      script.onload = script.onreadystatechange = null;
      if ( head && script.parentNode )
        head.removeChild(script)

  ## Use insertBefore instead of appendChild  to circumvent an IE6 bug.
  head.insertBefore(script, head.firstChild);

translate = (point, type, callback) ->
  callbackName = 'cbk_' + Math.round(Math.random() * 10000); ## 随机函数名
  xyUrl = "http://api.map.baidu.com/ag/coord/convert?from=" + type + "&to=4&x=" + point.lng + "&y=" + point.lat + "&callback=BMap.Convertor." + callbackName
  ## 动态创建script标签
  load_script(xyUrl);
  BMap.Convertor[callbackName] = (xyResult) ->
    delete BMap.Convertor[callbackName] ##调用完需要删除改函数
    point = new BMap.Point(xyResult.x, xyResult.y)
    callback && callback(point)

window.BMap = window.BMap || {}
BMap.Convertor = {}
BMap.Convertor.translate = translate
