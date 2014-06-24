<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<html lang="en">
<head>
<title><fmt:message key="menu.explore" /></title>
<meta name="menu" content="AdminMenu" />
    
</head>
<body>
	
<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("aTravelApp"), ['aTravelApp']);
	})
</script>

<div id="aTravelApp" class="container travel" ng-controller="ATravelCtrl">
    
    <div class="container left-map">
        <div class="map-container">
            <div class="map-canvas" ui-map="myMap" ui-options="mapOptions"></div>
            <div class="map-controls"
                 ponm-map-controls
                 ponm-map="myMap"
                 ponm-map-service="mapService"
                 ponm-map-event-listener="mapEventListener">
            </div>
        </div>
    </div>
    <div class="container right-content waypoint-scrollable"
         ng-click="getTravel()">

        <div class="page-header travel-header">
            <div class="media user-info">
                <a class="pull-left" href="{{ctx}}/user#?id={{userOpenInfo.id}}">
                    <img class="media-object img-circle" ng-src="{{staticCtx}}/avatar{{userOpenInfo.avatar || 1}}.png">
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
                    ng-repeat="spot in travel.spots"
                    waypoint="{{spot.id}}"
                    ng-class="{'active': spot.active}"
                    >
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <div class="travel-circle-header">
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
                            <img ng-src="{{photo.oss_key && staticCtx + '/' + photo.oss_key + '@!photo-preview-big'}}">
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
    
    <!-- 一键分享组件 -->
    <div bd-share class="bd-share"
            data-ng-class="{'active': mouseEnter}">
    </div>
</div>

</body>
</html>