<%@ include file="/common/taglibs.jsp"%>

<%-- <c:redirect url="/mainMenu"/> --%>

<!DOCTYPE html>
<html>
<head>
<title>My panor</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/index.css'/>" />

<!--
    Include the maps javascript with sensor=true because this code is using a
    sensor (a GPS locator) to determine the user's location.
    See: https://developers.google.com/maps/documentation/javascript/tutorial#Loading_the_Maps_API
    -->
<script type="text/javascript" src="<c:url value='/scripts/lib/jquery-1.8.2.min.js'/>"></script>
<!-- <script src="../imgLiquid.js"></script> -->
<script type="text/javascript"
	src="<c:url value='/scripts/imgLiquid/imgLiquid.js'/>"></script>

<!-- <script src="./Panor.js"></script> -->
<script type="text/javascript"
	src="<c:url value='/scripts/panor/Panor.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/scripts/panor/Panor-init.js'/>"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=41cd06c76f253eebc6f322c863d4baa1"></script>

<script>
	var map;
	function initialize() {
		$("div.imgLiquidFill").imgLiquid({
			fill : true
		});
//		map = new jQuery.panorMap("soso", "map-canvas");
        map = new jQuery.panorMap("baidu", "map-canvas");
		jQuery.initIndexMap(map);
	}
//	$(document).ready(initialize);
	$(window).load(initialize);
</script>
</head>
<body>

	<div class="front-root">

		<div id="front-photo_stack" class="front-photo_stack">
			<div class="imgLiquidFill front-photo_sizer"
				style="z-index: 2; visibility: visible; opacity: 1;">
				<a href="<c:url value="/photo/1"/>"
					class="front-photo_link"><img src="<c:url value="/services/api/photos/1"/>"
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