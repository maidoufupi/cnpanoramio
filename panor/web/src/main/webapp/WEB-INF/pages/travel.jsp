<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<html lang="en">
<head>
<title><fmt:message key="menu.explore" /></title>
<meta name="menu" content="AdminMenu" />

	<script src="<c:url value="/bower_components/eventie/eventie.js"/>"></script>
    <script src="<c:url value="/bower_components/eventEmitter/EventEmitter.js"/>"></script>
    <script src="<c:url value="/bower_components/imagesloaded/imagesloaded.js"/>"></script>
    
</head>
<body>
	
<c:choose>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
    <script type="text/javascript" src="<c:url value="/scripts/panor/script.baidu.min.js"/>"></script>
    <script type="text/javascript"
            src="http://api.map.baidu.com/api?v=2.0&ak=41cd06c76f253eebc6f322c863d4baa1"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "qq"}'>
    <script charset="utf-8" src="http://map.qq.com/api/js?v=2.0&key=ZYZBZ-WCCHU-ETAVP-4UZUB-RGLDJ-QDF57"></script>
    <script type="text/javascript" src="<c:url value='/bower_components/angular-ui-map-qq/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/scripts.qq.min.js'/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "gaode"}'>
    <script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
    <script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/scripts.gaode.min.js'/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "mapbar"}'>
  
  </c:when>
  <c:otherwise>
    <script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
    <script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/scripts.gaode.min.js'/>"></script>
  </c:otherwise>
</c:choose>
<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("aTravelApp"), ['aTravelApp']);
	})
</script>

<div id="aTravelApp" class="container travel" ng-controller="ATravelCtrl">
    <div class="container left-map">
        <div class="map-container">
            <div class="map-canvas" ui-map="myMap" ui-options="mapOptions"></div>
            <div class="map-plugins">

            </div>
        </div>
    </div>
    <div class="container right-content" ng-click="getTravel()"
         ui-event="{ scroll : 'scrollCallback($event)' }">

        <div class="page-header travel-header">
            <h2>{{travel.title}}</h2>
            <h4>{{travel.create_time | date:'yyyy-MM-dd'}}</h4>
            <small>{{travel.description}}</small>
        </div>

        <div class="container travel-content">
            <div class="panel panel-default"
                    ng-repeat="spot in travel.spots">
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <div class="travel-circle-header" >
                        <a href="" data-ng-click="activeSpot(spot)">
                            <div class="spot-date">
                                <span class="spot-date-txt ">{{spot.day}}</span>
                                <span class="spot-date-flag">DAY</span>
                            </div>
                        </a>
                        <div class="info">
                            <div contentEditable="true"
                                 data-ng-model="travel.spots[$index].address"
                                 data-place-holder="添加地点"
                                 editable="true"
                                 multiple-line="3"
                                 title="Click to edit"></div>
                        </div>
                        <div>
                            {{spot.address}}
                        </div>
                        <div>
                            {{spot.time}}
                        </div>
                    </div>

                </div>
                <div class="panel-body">
                    <div photo-fluid-container class="photo-fluid-container">
                        <a ng-repeat="photo in spot.photos"
                           ng-click="activePhoto(photo)"
                           class="fluid-brick"
                           href=""
                           ponm-photo>
                            <img class="item-img" ng-src="{{apirest}}/photo/{{photo.id}}/2">
                            <div class="ponm-photo-footer">
                                <p>{{photo.point.address}}</p>
                                <p>{{photo.description | newlines}}</p>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>