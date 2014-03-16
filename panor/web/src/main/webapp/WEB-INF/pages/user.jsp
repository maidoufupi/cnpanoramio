<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<head>
<title><fmt:message key="user.page.title" /></title>
<meta name="menu" content="AdminMenu" />
<link href="<c:url value="/styles/user.css"/>" rel="stylesheet">
</head>

<script type="text/javascript" src="<c:url value="/bower_components/jquery.rest/dist/jquery.rest.min.js"/>"></script>

<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/panoramio/cnmap.comm.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/panoramio/cnmap.Panoramio.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/js/cnmap.gaode.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/panoramio/cnmap.gaode.js'/>"></script>

	<script type="text/javascript" src="<c:url value="/bower_components/angular/angular.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-cookies/angular-cookies.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-resource/angular-resource.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-ui-utils/event.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-ui-mapgaode/src/ui-map.js"/>"></script>

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

<script>
    docReady(function () {

        var map = $.cnmap.initMap("user_page-map", {
            toolbar: true,
            ruler: false,
            maptype: true
        })
        $.cnmap.setZoom(1);

        var panoramioLayer = new cnmap.PanoramioLayer({suppressInfoWindows: false, mapVendor: window.mapVendor});
        panoramioLayer.initEnv(ctx);
        panoramioLayer.setMap(map);
    });
</script>

<div id="basic" class="container" data-ng-app="userPageApp">

    <div class="photo-col" data-ng-controller="UserCtrl">

        <div id="user-page_main-header">
            <div class="user-page_main-header_card">
                <img ng-src="{{ctx}}/images/user_avatar.png" width="120" height="120" alt="" class="user_page-profile_img">

                <div id="user_profile_info">
                    <div class="user-page_profile_info user-page_main-lede">
                        ${userSettings.name}
                    </div>

                    <div id="profile_icons_user">
                        <a class="icon_sprite icon_link" href="http://" title=""></a>
                    </div>

                    <div id="user_header" style="display: block;">
                        <div class="user_header-best-or-all">
                            <a class="user-page-best-enabled" href="/user/6324111?show=best">Best photos</a>
                            <span class="user-page-best-disabled">All photos</span>
                        </div>
                        <a href="/map/?user=6324111">
                            <img id="user_header-icon"
                                 ng-src="{{ctx}}/images/marker.png"
                                 height="16" alt="">
                            <span id="user_header-map">view on map</span>
                        </a>

                    </div>

                </div>
            </div>

            <div class="user-page_main-header_card_stats user-page_main-header_card">

                <div id="top-line-stats">
                    <div class="user-page_top-line-stat">
                        <div class="user-page_top-line-stat-value">
                            <a href="/user/6324111/stats">30</a>
                        </div>
                        <div class="user-page_top-line-stat-label">
                            photos
                        </div>
                    </div>
                    <div class="user-page_top-line-stat">
                        <div class="user-page_top-line-stat-value">
                            <a href="/user/6324111/stats">24</a>
                        </div>
                        <div class="user-page_top-line-stat-label">
                            on Google Maps
                        </div>
                    </div>

                    <div class="user-page_top-line-stat">
                        <div class="user-page_top-line-stat-value">
                            <a href="/user/6324111/stats">1539</a>
                        </div>
                        <div class="user-page_top-line-stat-label">
                            views
                        </div>
                    </div>
                </div>
            </div>
            <div style="clear: both;"></div>
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
    </div>
    <div class="info-col">
        <div class="interim-info-card" id="user_page-map_card">
            <div class="user_page-map" id="user_page-map">
            </div>
        </div>

        <div class="interim-info-card">
            <h3>标签</h3>
            <ul id="list-inline">

                <li id="tag_element_0">
                    <a href="/user/5851975/tags/Antrim">Antrim</a>
                </li>

                <li id="tag_element_10" style="display: inline;">
                    <a href="/user/5851975/tags/Benone">Benone</a>
                </li>

                <li id="show_all_tags" style="display: none;">
                    <a href="#">
                        Show all tags (73)
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>