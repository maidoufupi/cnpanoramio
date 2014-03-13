<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<title>My panor</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/index.css'/>" />

</head>
<body>
<script type="text/javascript" src="<c:url value='/bower_components/jquery/jquery.js'/>"></script>
<script type="text/javascript" src="<c:url value='/bower_components/imgLiquid/js/imgLiquid.js'/>"></script>
<script type="text/javascript" src="<c:url value='/bower_components/jquery.rest/dist/jquery.rest.js'/>"></script>
<c:choose>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=41cd06c76f253eebc6f322c863d4baa1"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.baidu.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "qq"}'>
    <script charset="utf-8" src="http://map.qq.com/api/js?v=2.0"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.qq.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "gaode"}'>
    <script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.gaode.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "mapbar"}'>
  
  </c:when>
  <c:otherwise>
    <script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.gaode.js"/>"></script>
  </c:otherwise>
</c:choose>

<script>
    var map;
    var ctx = "http://127.0.0.1:8080/panor-web";

    function initialize() {
        $("div.imgLiquidFill").imgLiquid({
            fill : true
        });

        var photos, photo_index;

        var restclient = new $.RestClient(ctx + '/api/rest/');
        restclient.add('index');
        
        function setPhoto(photo) {
            $(".front-photo_sizer img").attr("src", ctx + "/api/rest/photo/" + photo.id + "/1");
            
            $.cnmap.setCenter(photo.lat, photo.lng);
            if(!photo.mark) {
            	photo.mark = true;
            	$.cnmap.addMarkerInCenter();
            }
            
            $("div.imgLiquidFill").imgLiquid({
                fill : true
            });

            setTimeout(function() {
                setPhoto(photos[photo_index]);
                photo_index = (photo_index + 1) % photos.length;
            }, 8000);
        }

        var map = $.cnmap.initMap("map-canvas", {
            //toolbar: true,
//            scrollzoom: true,
            overview: true,
//            locatecity: true
        });

        $(window).on('load', windowresize);
        function windowresize() {
            var windowW = $(window).width();
            oldWidth = windowW;
            windowW = windowW/4;
            $.cnmap.panBy(windowW, 0);
            
            restclient.index.read('photo').done(function(data) {
                photos = data;
                photo_index = 0;
                setPhoto(photos[photo_index]);
                photo_index = (photo_index + 1) % photos.length;
            })
        }
    }
    $(document).ready(initialize);
</script>

	<div class="front-root">
		<div id="front-photo_stack" class="front-photo_stack">
			<div class="imgLiquidFill front-photo_sizer"
				style="z-index: 2; visibility: visible; opacity: 1;">
				<a href="<c:url value='/photo/${photo.id}'/>"
					class="front-photo_link"><img src="<c:url value="/services/api/photos/${photo.id}/1"/>"
					class="front-photo_img"
					style="position: relative; left: 0px; top: 0.5px;">
				</a>
			</div>
		</div>

		<div class="front-main_map">
			<div id="map-canvas"></div>
		</div>
	</div>
</body>
</html>