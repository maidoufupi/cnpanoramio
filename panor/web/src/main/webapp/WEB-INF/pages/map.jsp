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
			angular.bootstrap(document.getElementById("exploreWorld"), ['exploreWorldApp']);
	})
</script>
<div id="exploreWorld" class="container-explore-map" ng-controller="ExploreWorldCtrl">
    <div class="col-main">
        <div class="map-container">
            <div id="map-canvas" ui-map="myMap" ui-options="mapOptions"></div>
            <div class="map-controls"
                 ponm-map-controls
                 ponm-map="myMap"
                 ponm-map-service="mapService"
                 ponm-map-event-listener="mapEventListener">
            </div>
        </div>
    </div>
    <div class="col-info" >
        <div id="thumbinnerarea">
            <ul id="tabs" class="nav nav-pills">
                <li id="tab_li_1" data-ng-class="{active: tabs.map}">
                    <a data-ng-click="setPanormaioType('map')" href="">热门照片<span style="display:none" class="total_photos"> (2294)</span>
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
                <li id="tab_li_4" data-ng-class="{active: tabs.user}"
                        data-ng-show="user.id">
                    <a data-ng-click="setPanormaioType('user')" href="">{{user.name}}<span class="footnote">的照片</span><span style="display:none" class="total_photos"></span>
                        <img class="loading hide" src="images/loading-p.gif" alt="读取中">
                    </a>
                </li>
            </ul>

            <div id="preview">
                <div class="preview_thumb_area" ng-repeat="photo in photos" id="pid{{photo.photoId}}" style="height: 112px;">
                    <a href=""
                       data-ng-click="displayPhoto(photo.photo_id)">
                        <img title=""
                             id="r{{photo.photo_id}}"
                             ng-src="{{staticCtx}}/{{photo.oss_key}}@!album-thumbnail">
                    </a>
                </div>
            </div>
            <ul class="pager">
                <li class="previous" ng-hide="photoStart"><a href ng-click="prePhoto()">&larr; Older</a></li>
                <li class="next" ng-hide="photoEnd"><a href ng-click="nextPhoto()">Newer &rarr;</a></li>
            </ul>
        </div>
    </div>
    
    <!-- 一键分享组件 -->
    <div bd-share 
    	 class="bd-share"
            data-ng-class="{'active': mouseEnter}">
    </div>
</div>

</body>
</html>