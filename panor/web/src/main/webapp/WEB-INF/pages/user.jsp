<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<head>
<title><fmt:message key="user.page.title" /></title>
<meta name="menu" content="AdminMenu" />
<link href="<c:url value="/styles/user.css"/>" rel="stylesheet">
</head>
<script type="text/javascript" src="<c:url value="/bower_components/jquery.rest/dist/jquery.rest.min.js"/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/panoramio/cnmap.comm.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/panoramio/cnmap.Panoramio.js'/>"></script>

    <script src="<c:url value="/bower_components/get-style-property/get-style-property.js"/>"></script>
    <script src="<c:url value="/bower_components/get-size/get-size.js"/>"></script>
    <script src="<c:url value="/bower_components/eventie/eventie.js"/>"></script>
    <script src="<c:url value="/bower_components/doc-ready/doc-ready.js"/>"></script>
    <script src="<c:url value="/bower_components/eventEmitter/EventEmitter.js"/>"></script>
    <script src="<c:url value="/bower_components/jquery-bridget/jquery.bridget.js"/>"></script>
    <script src="<c:url value="/bower_components/matches-selector/matches-selector.js"/>"></script>
    <script src="<c:url value="/bower_components/outlayer/item.js"/>"></script>
    <script src="<c:url value="/bower_components/outlayer/outlayer.js"/>"></script>
    <script src="<c:url value="/bower_components/masonry/masonry.js"/>"></script>
    <script src="<c:url value="/bower_components/imagesloaded/imagesloaded.js"/>"></script>
    <script src="<c:url value="/bower_components/angular-masonry/angular-masonry.js"/>"></script>

    <script type="text/javascript" src="<c:url value="/scripts/services/main.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/controllers/UserPageCtrl.js"/>"></script>
    
<c:choose>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=41cd06c76f253eebc6f322c863d4baa1"></script>
    <script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
	<script type="text/javascript" src="<c:url value="/scripts/panor/js/modal/cnmap.Modal.baidu.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "qq"}'>
  	<script charset="utf-8" src="http://map.qq.com/api/js?v=2.0"></script>
	<script type="text/javascript" src="<c:url value='/bower_components/angular-ui-map-qq/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/qq/MapEventListenerImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/qq/MapServiceImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.qq.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.qq.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "gaode"}'>
  	<script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/gaode/MapEventListenerImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/gaode/MapServiceImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.gaode.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.gaode.js"/>"></script>
  </c:when>
  <c:otherwise>
  	<script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
  	<script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/gaode/MapEventListenerImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/gaode/MapServiceImpl.js'/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.gaode.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/cnmap.gaode.js"/>"></script>
   </c:otherwise>
</c:choose>
<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("userPageApp"), ['userPageApp']);
	})
</script>

<div id="userPageApp" class="container" data-ng-controller="UserCtrl">

    <div class="photo-col" >

        <div id="user-page_main-header">
            <div class="user-page_main-header_card">
                <img ng-src="{{apirest}}/user/{{userId}}/avatar" width="120" height="120" alt="" class="user_page-profile_img">

                <div id="user_profile_info">
                    <div class="user-page_profile_info user-page_main-lede">
                        {{userOpenInfo.name}}
                    </div>

                    <div id="profile_icons_user">
                        <a class="icon_sprite icon_link" href="http://" title=""></a>
                    </div>

                    <div id="user_header" style="display: block;">
                        <div class="user_header-best-or-all">
                            <a class="user-page-best-enabled" href="{{ctx}}/map##userid={{userId}}&favorite=true">在地图上查看收藏的照片</a>
                        </div>
                        <a href="{{ctx}}/map/##userid={{userId}}">
                            <img id="user_header-icon"
                                 ng-src="{{ctx}}/images/marker.png"
                                 height="16" alt="">
                            <span id="user_header-map">在地图上查看照片</span>
                        </a>

                    </div>

                </div>
            </div>

            <div class="user-page_main-header_card_stats user-page_main-header_card">

               <div id="top-line-stats">
                    <div class="user-page_top-line-stat">
                        <div class="user-page_top-line-stat-value">
                            <a href="/user/{{userId}}/stats">{{userOpenInfo.photo_count}}</a>
                        </div>
                        <div class="user-page_top-line-stat-label">
                            张图片
                        </div>
                    </div>
                    <div class="user-page_top-line-stat">
                        <div class="user-page_top-line-stat-value">
                            <a href="/user/{{userId}}/stats">{{userOpenInfo.photo_favorites}}</a>
                        </div>
                        <div class="user-page_top-line-stat-label">
                            张被收藏
                        </div>
                    </div>

                    <div class="user-page_top-line-stat">
                        <div class="user-page_top-line-stat-value">
                            <a href="/user/{{userId}}/stats">{{userOpenInfo.photo_views}}</a>
                        </div>
                        <div class="user-page_top-line-stat-label">
                            次被查看
                        </div>
                    </div>
                </div>
            </div>
            <div style="clear: both;"></div>
        </div>
        <div class="paginator-wrapper" data-ng-show="photo.totalItems > 0">
            <pagination items-per-page="photo.pageSize" total-items="photo.totalItems" page="photo.currentPage" max-size="photo.maxSize" class="pagination-sm" boundary-links="true" rotate="false" num-pages="photo.numPages"></pagination>
        </div>

        <div class="masonry-container" masonry preserve-order>
            <div id="masonry-{{photo.id}}" class="masonry-brick" ng-repeat="photo in photos" masonry-brick>
                <a href="{{ctx}}/photo/{{photo.id}}">
                    <img ng-src="{{apirest}}/photo/{{photo.id}}/2" class="item-img" alt="{{photo.title}}"></a>

                <div data-ng-hide="true" class="thumb-overlay">
                    <div class="thumb-overlay-text" style="max-width: 189.2885400512707px">
                        <a href="/photo/52034419">{{photo.title}}</a></div>
                    <div class="thumb-overlay-icons" style="max-width: 189.2885400512707px">
                        <div class="thumb-overlay-icon">
                            <img ng-src="{{ctx}}/images/marker.png"
                                 title="Selected for Google Maps and Google Earth"
                                 width="16" height="16" alt="">
                        </div>
                        <div class="thumb-overlay-icon">
                            <a href="/photo/52034419/stats"><span id="counter_5203441">274073 次查看</span></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="paginator-wrapper" data-ng-show="photo.totalItems > 0">
            <pagination items-per-page="photo.pageSize" total-items="photo.totalItems" page="photo.currentPage" max-size="photo.maxSize" class="pagination-sm" boundary-links="true" rotate="false" num-pages="photo.numPages"></pagination>
        </div>
    </div>
    <div class="info-col">
        <div class="interim-info-card" id="user_page-map_card">
            <div class="user_page-map" id="user_page-map" ui-map="minimap" ui-options="mapOptions">
            </div>
        </div>

        <div class="interim-info-card">
            <h3>标签</h3>
            <ul id="interim-tags">
         	    <li data-ng-repeat="tag in userOpenInfo.tags">
                    <a href="{{ctx}}/user/{{userId}}#?tag={{tag}}">{{tag}}</a>
                </li>
            </ul>
        </div>
    </div>
</div>