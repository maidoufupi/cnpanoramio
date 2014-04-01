<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<html lang="en">
<head>
<title><fmt:message key="menu.explore" /></title>
<meta name="menu" content="AdminMenu" />

    <!-- Custom styles for this template -->
    <link href="<c:url value="/styles/ExploreWorld.css"/>" rel="stylesheet">
</head>
<body>
	
    <script type="text/javascript" src="<c:url value="/bower_components/jquery.rest/dist/jquery.rest.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/jquery-bbq/jquery.ba-bbq.js"/>"></script> 
    <script type="text/javascript" src="<c:url value="/bower_components/angular-ui-utils/event.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-ui-mapgaode/src/ui-map.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.comm.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.Panoramio.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/services/main.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/controllers/ExploreWorldCtrl.js"/>"></script>
<c:choose>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.baidu.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.baidu.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/explore/cnmap.explore.baidu.js"/>"></script>
    <script type="text/javascript"
            src="http://api.map.baidu.com/api?v=2.0&ak=41cd06c76f253eebc6f322c863d4baa1"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "qq"}'>
    <script charset="utf-8" src="http://map.qq.com/api/js?v=2.0"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.qq.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.qq.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/explore/cnmap.explore.qq.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "gaode"}'>
    <script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.gaode.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.gaode.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/explore/cnmap.explore.gaode.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "mapbar"}'>
  
  </c:when>
  <c:otherwise>
    <script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.gaode.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.gaode.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/explore/cnmap.explore.gaode.js"/>"></script>
  </c:otherwise>
</c:choose>
<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("exploreWorld"), ['exploreWorldApp']);
	})
</script>
<div id="exploreWorld" class="container container-main" ng-controller="ExploreWorldCtrl">
    <div class="col-main">
        <div id="map-canvas" ui-map="myMap" ui-options="mapOptions"></div>
    </div>
    <div class="col-info">
        <div id="thumbinnerarea">
            <ul id="tabs" class="nav nav-pills">
                <li id="tab_li_1" data-ng-class="{active: tabs.map}">
                    <a data-ng-click="setPanormaioType('map')" tab="1" href="">热门照片<span style="display:none" class="total_photos"> (2294)</span>
                        <img class="loading" src="images/loading-p.gif" alt="读取中" style="display: none;">
                    </a>
                </li>
                <li id="tab_li_2" data-ng-class="{active: tabs.latest}">
                    <a data-ng-click="setPanormaioType('latest')" href="">最新照片<span style="display:none" class="total_photos"></span>
                        <img class="loading hide" src="images/loading-p.gif" alt="读取中">
                    </a>
                </li>
                <li id="tab_li_8" data-ng-class="{active: tabs.favorite}">
                    <a data-ng-click="setPanormaioType('favorite')" href="">收藏<span style="display:none" class="total_photos"></span>
                        <img class="loading hide" src="images/loading-p.gif" alt="读取中">
                    </a>
                </li>
                <li id="tab_li_4" data-ng-class="{active: tabs.user}">
                    <a data-ng-click="setPanormaioType('user')" href="">您的照片<span style="display:none" class="total_photos"></span>
                        <img class="loading hide" src="images/loading-p.gif" alt="读取中">
                    </a>
                </li>
            </ul>
            <div id="preview">
                <div class="preview_thumb_area" ng-repeat="photo in photos" id="pid{{photo.photoId}}" style="height: 112px;">
                    <a href="photo/{{photo.photoId}}">
                        <img title=""
                             id="r{{photo.photoId}}"
                             ng-src="{{apirest}}/photo/{{photo.photoId}}/2">
                    </a>
                </div>
            </div>
            <ul class="pager">
                <li class="previous" ng-hide="photoStart"><a href ng-click="prePhoto()">&larr; Older</a></li>
                <li class="next" ng-hide="photoEnd"><a href ng-click="nextPhoto()">Newer &rarr;</a></li>
            </ul>
        </div>
    </div>
</div>

</body>
</html>