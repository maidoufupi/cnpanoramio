<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<html lang="en">
<head>
<title><fmt:message key="upload.title" /></title>
<meta name="menu" content="AdminMenu" />
<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="utf-8">
<title>Map Photo</title>
<meta name="description" content="File Upload widget with multiple file selection, drag&amp;drop support, progress bars, validation and preview images, audio and video for AngularJS. Supports cross-domain, chunked and resumable file uploads and client-side image resizing. Works with any server-side platform (PHP, Python, Ruby on Rails, Java, Node.js, Go etc.) that supports standard HTML form file uploads.">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap styles -->
<link rel="stylesheet" href="<c:url value="/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput.css"/>">
</head>
<body>
<script type="text/javascript" src="<c:url value="/bower_components/jquery-bbq/jquery.ba-bbq.js"/>"></script> 
    <script src="<c:url value="/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput.js"/>"></script>
    <script src="<c:url value="/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput-angular.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/jquery.canvas.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.comm.js"/>"></script>
    <!-- The main application script -->
    <script src="<c:url value="/scripts/services/main.js"/>"></script>
    <script src="<c:url value="/scripts/controllers/MapPhotoCtrl.js"/>"></script>
    
<c:choose>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=41cd06c76f253eebc6f322c863d4baa1"></script>
    <script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "qq"}'>
  	<script charset="utf-8" src="http://map.qq.com/api/js?v=2.0"></script>
	<script type="text/javascript" src="<c:url value='/bower_components/angular-ui-map-qq/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/qq/MapEventListenerImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/qq/MapServiceImpl.js'/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "gaode"}'>
  	<script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/gaode/MapEventListenerImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/gaode/MapServiceImpl.js'/>"></script>
  </c:when>
  <c:otherwise>
  	<script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
  	<script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/gaode/MapEventListenerImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/gaode/MapServiceImpl.js'/>"></script>
   </c:otherwise>
</c:choose>
<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("mapPhotoApp"), ['mapPhotoApp']);
	})
</script>
<div id="mapPhotoApp" class="map_panel container" data-ng-controller="MapPhotoCtrl">
	<alert data-ng-repeat="alert in alerts" type="alert.type" close="closeAlert($index)">{{alert.msg}}</alert>
    <div id="selected-photo-editor" class="selected_photo_editor">
        <div class="properties">
            <div id="loc-preview" class="thumbnail">
                <img class="preview" data-ng-src="{{apirest}}/photo/{{photoId}}/2">
            </div>
            <form id="geocoder_form" class="form" ng-controller="TypeaheadCtrl" ng-submit="goLocation(asyncSelected)">
                <div class="col-12">
                    <!--<div class="input-group input-group-sm">
                        <input ng-model="address" type="text" class="form-control">
                                    <span id="location-search-go" class="input-group-btn">
                                       <button type="submit" class="btn btn-default">Go!</button>
                                     </span>
                    </div>-->
                    <!-- /input-group -->
                    <div class="input-group input-group-sm">
                        <input type="text"
                               ng-model="asyncSelected"
                               placeholder="搜索地址"
                               typeahead="address as address.formatted_address for address in getLocation($viewValue) | filter:$viewValue"
                               typeahead-loading="loadingLocations"
                               class="form-control"
                                >
                                <span class="input-group-btn">
                                   <button type="submit" class="btn btn-default">Go!</button>
                                 </span>
                        <i ng-show="loadingLocations" class="glyphicon glyphicon-refresh"></i>
                    </div>
                </div>

            </form>

            <div id="the-place" class="no_place disabled place_search_bar">
                <span class="lat">{{file.mapVendor.latPritty}} {{file.latRef}}</span>
                <span class="comma">{{((file.mapVendor.lat && file.mapVendor.lng) && ", ") || "[GPS地址]"}}</span>
                <span class="lng">{{file.mapVendor.lngPritty}} {{file.lngRef}}</span>
            </div>
            <div class="coder_place">
                <div id="the-address" class="original_place_name">{{file.mapVendor.address || "[解析地址]"}}</div>
            </div>
            <label class="indoors_info"><input type="checkbox">This photo is taken indoors</label>

            <div>
                点击地图设置拍摄地点
            </div>
        </div>
    </div>
    <div class="map-photo-map">
        <div style="width: 100%; height: 100%">
            <div id="map_canvas" ui-map="myMap" ui-options="mapOptions">
            </div>
        </div>
    </div>
    <div class="modal-footer map-photo">
        <button class="btn btn-primary" ng-click="ok()">保存，完成</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
    </div>
</div>
</body>
</html>
