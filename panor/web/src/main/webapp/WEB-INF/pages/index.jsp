<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<title>My panor</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/index.css'/>" />

<%-- <script type="text/javascript" src="<c:url value='/bower_components/jquery1.8/jquery-1.8.2.min.js'/>"></script> --%>
<script type="text/javascript" src="<c:url value='/bower_components/imgLiquid/imgLiquid.js'/>"></script>

</head>
<body>

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
    function initialize() {
        $("div.imgLiquidFill").imgLiquid({
            fill : true
        });
        var lat = <c:url value="${photo.gpsPoint.lat}"/>;
        var lng = <c:url value="${photo.gpsPoint.lng}"/>;
//        var width = $(window).width(),
//            height = $(window).height();
//        $("#map-canvas").attr("width", width).attr("height", height);

        var map = $.cnmap.initMap("map-canvas", {
            //toolbar: true,
//            scrollzoom: true,
            overview: true,
//            locatecity: true
        });

        var oldWidth;
        $(window).bind('resizeEnd', function() {
            //do something, window hasn't changed size in 500ms
            var windowW = $(window).width();
            var panX = windowW - oldWidth;
            oldWidth = windowW;
            $.cnmap.panBy(panX*3/4, 0);
        });
        $(window).resize(function() {
            if(this.resizeTO) {
                clearTimeout(this.resizeTO);
            }
            this.resizeTO = setTimeout(function() {
                $(this).trigger('resizeEnd');
            }, 500);
        });
        $(window).on('load', windowresize);
        function windowresize() {
            $.cnmap.setCenter(lat, lng);
            $.cnmap.addMarkerInCenter();

            var windowW = $(window).width();
            oldWidth = windowW;
            windowW = windowW/4;
            $.cnmap.panBy(windowW, 0);
//            $("body > .container").css("height", ($(window).height() - 84 ));
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