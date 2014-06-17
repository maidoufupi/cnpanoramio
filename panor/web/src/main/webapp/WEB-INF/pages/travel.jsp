<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<html lang="en">
<head>
<title><fmt:message key="menu.explore" /></title>
<meta name="menu" content="AdminMenu" />
    
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
<!--             <div class="map-plugins">

            </div> -->
        </div>
    </div>
    <div class="container right-content" ng-click="getTravel()"
         ui-event="{ scroll : 'scrollCallback($event)' }">

        <div class="page-header travel-header">
            <div class="media user-info">
                <a class="pull-left" href="{{ctx}}/user#?id={{travel.user_id}}">
                    <img class="media-object img-circle" ng-src="{{apirest}}/user/{{travel.user_id || 1}}/avatar">
                </a>

                <div class="media-body">
                    <h4 class="media-heading">{{travel.username}}</h4>
                    <div>
                        <div class="title">{{travel.title}}</div>
                        <span class="footnotes">{{'  ' + (travel.time_start | date:'yyyy/MM/dd') + ' - ' + (travel.time_end | date:'yyyy/MM/dd') }}</span>
                    </div>
                </div>
            </div>

            <div class="travel-desc">
                <div ng-switch="travelEnedit">
                    <a ng-switch-when="true"
                       href="#"
                       class="editable"
                       editable-textarea="travel.description" e-rows="4" e-cols="40"
                       onbeforesave="updateTravel(travel, $data)">
                        <pre class="description">{{ travel.description || '添加描述' }}</pre>
                    </a>
                    <pre class="description"
                         ng-switch-default>{{travel.description}}</pre>
                </div>
            </div>
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
                                <span class="spot-date-flag">{{spot.time_start | date:'yyyy/MM/dd'}}</span>
                            </div>
                        </a>
                        <div>
                            <div ng-switch="travelEnedit">
                                <a ng-switch-when="true"
                                   href="#" editable-text="spot.title"
                                   onbeforesave="updateSpot(travel, spot, 'title', $data)">{{ spot.title || '添加标题' }}</a>
                                <h4 ng-switch-default>{{spot.title}}</h4>
                            </div>
                        </div>
                        <div class="info" >
                            <div ng-switch="travelEnedit" class="editable">
                                <a href=""
                                   ng-switch-when="true"
                                   editable-select="spot.address" e-ng-options="addr as addr for (addr, point) in spot.addresses"
                                   buttons="no"
                                   onbeforesave="updateSpot(travel, spot, 'address', $data)">
                                    {{ showSpotAddress(spot) }}
                                </a>
                                <address ng-switch-default>{{ spot.address }}</address>
                            </div>
                        </div>
                        <div ng-switch="travelEnedit">
                            <div class="editable">
                                <a ng-switch-when="true"
                                   href="#" editable-textarea="spot.description" e-rows="4" e-cols="40"
                                   onbeforesave="updateSpot(travel, spot, 'description', $data)">
                                    <pre>{{ spot.description || '添加描述' }}</pre>
                                </a>
                            </div>

                            <pre class="description" ng-switch-default>{{ spot.description }}</pre>
                        </div>
                    </div>

                </div>
                <div class="panel-body">
                    <div photo-fluid-container
                         class="photo-fluid-container">
                        <a ng-repeat="photo in spot.photos"
                           ng-click="activePhoto(photo)"
                           href=""
                           class="fluid-brick ponm-photo"
                           ponm-photo="photo"
                                >
                            <img ng-src="{{apirest}}/photo/{{photo.id}}/2">
                            <div class="action ponm-photo-footer">
                                <p>{{photo.point.address}}</p>
                                <pre class="description">{{photo.description}}</pre>
                            </div>
                            <div ng-show="travelEnedit"
                                 class="action ponm-photo-remove">
                                <span class="glyphicon glyphicon-remove"></span>
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