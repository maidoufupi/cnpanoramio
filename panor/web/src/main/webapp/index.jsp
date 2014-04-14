<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>美丽星球</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/index.css'/>" />

</head>
<body>
<script type="text/javascript" src="<c:url value='/bower_components/jquery/jquery.js'/>"></script>
<script type="text/javascript" src="<c:url value='/bower_components/imgLiquid/js/imgLiquid.js'/>"></script>
<script type="text/javascript" src="<c:url value='/bower_components/jquery.rest/dist/jquery.rest.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/services/main.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/controllers/main.js'/>"></script>

<c:choose>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=41cd06c76f253eebc6f322c863d4baa1"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.baidu.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "qq"}'>
    <script charset="utf-8" src="http://map.qq.com/api/js?v=2.0&key=ZYZBZ-WCCHU-ETAVP-4UZUB-RGLDJ-QDF57"></script>
    <script type="text/javascript" src="<c:url value='/bower_components/angular-ui-map-qq/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/qq/MapEventListenerImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.qq.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "gaode"}'>
    <script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
    <script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/gaode/MapEventListenerImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.gaode.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "mapbar"}'>
  
  </c:when>
  <c:otherwise>
    <script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
    <script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/gaode/MapEventListenerImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.gaode.js"/>"></script>
  </c:otherwise>
</c:choose>
<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("indexApp"), ['indexApp']);
	})
</script>
	<div id="indexApp" class="front-root" data-ng-app="indexApp" data-ng-controller="IndexCtrl">
		<div id="front-photo_stack" class="front-photo_stack">
			<div class="imgLiquidFill front-photo_sizer"
				style="z-index: 2; visibility: visible; opacity: 1;">
				<a href="<c:url value='/photo/1'/>"	class="front-photo_link">
					<img src=""	class="front-photo_img"	style="position: relative; left: 0px; top: 0.5px;">
				</a>
			</div>
		</div>

		<div class="front-main_map">
			<div id="map_canvas" ui-map="map" ui-options="mapOptions"></div>
		</div>
	</div>
</body>
</html>