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
            <div class="panel panel-default travel-spot"
                 ng-animate="'animate'"
                    ng-repeat="spot in travel.spots"
                    waypoint="{{spot.id}}"
                    ng-class="{'active': spot.active}"
                    ng-controller="spotCtrl"
                    ponm-hover>
                <div data-ng-if="travelEnedit"
                     class="action spot-remove">
                    <a href
                       class="icon-action-danger"
                       ng-click="deleteSpot(spot)">
                        <span class="glyphicon glyphicon-remove"></span>
                    </a>
                </div>
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <div class="travel-circle-header">
                        <a href="" data-ng-click="activeSpot(spot)">
                            <div class="spot-date">
                                <span class="spot-date-txt ">{{spot.day || '某'}}</span>
                                <span class="spot-date-flag">DAY</span>
                                <div ng-switch="travelEnedit">
                                    <div ng-switch-when="true">
                                        <a href
                                           class="dropdown-toggle"
                                           data-ng-click="datepickerOpened=!datepickerOpened"
                                           datepicker-popup="yyyy/MM/dd"
                                           ng-model="spot.timeStart"
                                           is-open="datepickerOpened"
                                           min-date="minDate"
                                           max-date="'2015-06-22'"
                                           datepicker-options="dateOptions"
                                           date-disabled="datepickerDisabled(date, mode)"
                                           ng-required="true"
                                           close-text="Close">{{((spot.timeStart || spot.time_start) | date:'yyyy/MM/dd') || '选择日期'}}</a>
                                    </div>
                                    <span ng-switch-default
                                          class="spot-date-flag"
                                            >{{spot.time_start | date:'yyyy/MM/dd'}}</span>
                                </div>
                            </div>
                        </a>
                        <div>
                            <div ng-switch="travelEnedit">
                                <a ng-switch-when="true"
                                   href="#" editable-text="spot.title"
                                   onbeforesave="updateSpot(spot, 'title', $data)">{{ spot.title || '添加标题' }}</a>
                                <h4 ng-switch-default>{{spot.title}}</h4>
                            </div>
                        </div>
                        <div class="info" >
                            <div ng-switch="travelEnedit" class="editable">
                                <a href=""
                                   ng-switch-when="true"
                                   editable-select="spot.address" e-ng-options="addr as addr for (addr, point) in spot.addresses"
                                   buttons="no"
                                   onbeforesave="updateSpot(spot, 'address', $data)">
                                    {{ showSpotAddress(spot) }}
                                </a>
                                <address ng-switch-default>{{ spot.address }}</address>
                            </div>
                        </div>
                        <div ng-switch="travelEnedit">
                            <div class="editable">
                                <a ng-switch-when="true"
                                   href="#" editable-textarea="spot.description" e-rows="4" e-cols="40"
                                   onbeforesave="updateSpot(spot, 'description', $data)">
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
                        <div ng-repeat="photo in spot.photos"
                             class="fluid-brick ponm-photo"

                                ponm-hover>
                            <a ng-click="activePhoto(photo)"
                               href="">
                                <img ng-src="{{photo.oss_key && staticCtx + '/' + photo.oss_key + '@!photo-preview-big'}}">
                            </a>
                            <div class="action ponm-photo-footer">
                                <p>{{photo.point.address}}</p>
                                <pre class="description">{{photo.description}}</pre>
                            </div>
                            <div ng-show="travelEnedit"
                                 class="action ponm-photo-remove">
                                <a href
                                   class="icon-action-danger"
                                   ng-click="removePhoto(photo)">
                                    <span class="glyphicon glyphicon-remove"></span>
                                </a>
                            </div>
                            <div data-ng-if="travelEnedit" class="action ponm-photo-option">
                                <!-- Single button -->
                                <div class="btn-group" dropdown is-open="isopen">
                                    <button type="button" class="btn dropdown-toggle" ng-disabled="disabled">
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="" ng-click="createSpot(photo)">创建新景点</a></li>
                                        <li ng-repeat="spot in travel.spots">
                                            <a href="" ng-click="addSpotPhoto(photo, spot)">移动到第{{spot.day}}天{{spot.title}}</a>
                                        </li>
                                        <li class="divider"></li>
                                        <li><a href="" ng-click="removePhoto(photo)">从旅行中删除</a></li>
                                    </ul>
                                </div>
                            </div>

                        </div>
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