<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<html lang="en">
<head>
<title><fmt:message key="menu.explore" /></title>
<meta name="menu" content="AdminMenu" />
<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="UTF-8">
<title>Explore the world</title>
<meta name="description" content="Explore the world">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Custom styles for this template -->
    <link href="<c:url value="/styles/ExploreWorld.css"/>" rel="stylesheet">
    
</head>
<body>
    <script type="text/javascript" src="<c:url value="/bower_components/angular/angular.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-cookies/angular-cookies.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-resource/angular-resource.js"/>"></script>
    
    <script type="text/javascript" src="<c:url value="/bower_components/jquery.rest/dist/jquery.rest.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/fileupload/blueimp/tmpl.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/jquery-bbq/jquery.ba-bbq.js"/>"></script>
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

<script type="text/javascript">
$(document).ready(function () {
    map = $.cnmap.initMap("map-canvas", {
        toolbar: true,
        scrollzoom: true,
        maptype: true,
        overview: true,
        locatecity: true
    });
    setPanoramioLayer(map);

    function setPanoramioLayer(map) {
        var panoramioLayer = new cnmap.PanoramioLayer(
                {suppressInfoWindows: true,
                 mapVendor: "gaode"});
        panoramioLayer.initEnv(ctx);
        panoramioLayer.setMap(map);

        $(panoramioLayer).bind("data_changed", function(e, data) {
            angular.element($('.col-info')).scope().$apply(function(scope) {
                scope.updatePhoto(data);
            });
        })
    }

    $.explore(map);

    $(window).trigger( 'hashchange' );
})
</script> 

<div class="container container-main" ng-app="cnmapApp">
    <div class="col-main">
        <div id="map-canvas"></div>
    </div>
    <div class="col-info" ng-controller="ExploreWorldCtrl">
        <div id="thumbinnerarea">
            <ul id="tabs" class="nav nav-pills">
                <li id="tab_li_1" class="active">
                    <a set="public" order="popularity" tab="1" kml_text="Google 地球中的热门照片"
                       kml_link="/kml/" href="/map/?set=public&amp;order=popularity">热门照片<span style="display:none" class="total_photos"> (2294)</span>
                        <img class="loading" src="images/loading-p.gif" alt="读取中" style="display: none;">
                    </a>
                </li>
                <li id="tab_li_2">
                    <a set="recent" order="popularity" tab="2" kml_text="Google 地球中的最新照片" kml_link="/kml/?recent" href="/map/?set=recent&amp;order=popularity">最新照片<span style="display:none" class="total_photos"></span>
                        <img class="loading hide" src="images/loading-p.gif" alt="读取中">
                    </a>
                </li>
                <li id="tab_li_7">
                    <a set="places" order="popularity" tab="7" kml_text="谷歌地球里这个地方在所有的照片。" kml_link="/kml/?place=place_id" href="/map/?set=places&amp;order=popularity">地点<span style="display:none" class="total_photos"></span>
                        <img class="loading hide" src="images/loading-p.gif" alt="读取中">
                    </a>
                </li>
                <li id="tab_li_8">
                    <a set="indoor" order="popularity" tab="8" kml_text="谷歌地球里的室内照片" kml_link="/kml/?indoor" href="/map/?set=indoor&amp;order=popularity">室内<span style="display:none" class="total_photos"></span>
                        <img class="loading hide" src="images/loading-p.gif" alt="读取中">
                    </a>
                </li>
                <li id="tab_li_4">
                    <a set="6324111" order="upload_date" tab="4" kml_text="您在Google 地球中的照片" kml_link="/kml/?user=6324111" href="/map/?set=6324111&amp;order=upload_date">您的照片<span style="display:none" class="total_photos"></span>
                        <img class="loading hide" src="images/loading-p.gif" alt="读取中">
                    </a>
                </li>
            </ul>
            <div id="place_detail_area" style="display: none;">
                <div id="cover_photo_box">
                    <a id="cover_photo_link" href="">
                        <img alt="Cover photo" id="cover_photo" ng-src="{{ctx}}/images/place_cover_photo.jpg">
                    </a>
                </div>
                <div id="place_description">
                    <span id="place_title" class="place_name"></span><br>
                    <a id="external-place-link" style="display: none;">
                        <span class="icon"></span>
                        Place details in Google Places.</a>
                    <a id="place-kml-link">See photos in Google Earth.</a>
                </div>
                <a id="all_places_link">? Back to all places</a>
                <div id="place_photo_heading">
                    Photos of <span class="place_name"></span>
                </div>
            </div>
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