<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<head>
<title><fmt:message key="user.page.title" /></title>
<meta name="menu" content="AdminMenu" />
</head>

<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("userPageApp"), ['userPageApp']);
	})
</script>

<div id="userPageApp" class="container" data-ng-controller="UserCtrl">

    <div class="photo-col" >

        <div id="user-page_main-header">
            <div class="user-page_main-header_card">
                <img ng-src="{{staticCtx}}/avatar{{userOpenInfo.avatar || 1}}.png" 
                	 width="120" height="120" alt="" class="user_page-profile_img">

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
                        <a href="{{ctx}}/map##userid={{userId}}">
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
            <pagination items-per-page="photo.pageSize"
                        total-items="photo.totalItems"
                        ng-model="photo.currentPage"
                        max-size="photo.maxSize"
                        class="pagination-sm"
                        boundary-links="true"
                        rotate="false"
                        num-pages="photo.numPages"></pagination>
        </div>

        <div photo-fluid-container="photos"
             item-selector=".fluid-brick"
             class="photo-fluid-container"
             fluid-line-max-height="200"
             fluid-line-min-height="100">
            <a ng-repeat="photo in photos"
               ng-click="activePhoto(photo)"
               href=""
               class="fluid-brick ponm-photo"
               ponm-hover
                    >
                <img ng-src="{{photo.oss_key && staticCtx + '/' + photo.oss_key + '@!photo-preview-big'}}">
                <div class="action ponm-photo-footer">
                    <p>{{photo.point.address}}</p>
                    <pre class="description">{{photo.description}}</pre>
                </div>
                <div ng-show="editable"
                     class="action ponm-photo-remove">
                    <span class="glyphicon glyphicon-remove"></span>
                </div>
            </a>
        </div>
        
        <div class="paginator-wrapper" data-ng-show="photo.totalItems > 0">
            <pagination items-per-page="photo.pageSize"
                        total-items="photo.totalItems"
                        ng-model="photo.currentPage"
                        max-size="photo.maxSize"
                        class="pagination-sm"
                        boundary-links="true"
                        rotate="false"
                        num-pages="photo.numPages"></pagination>
        </div>
    </div>
    <div class="info-col">
        <div class="interim-info-card" id="user_page-map_card">
            <div class="user_page-map" id="user_page-map" ui-map="minimap" ui-options="mapOptions">
            </div>
        </div>

        <div class="interim-info-card">
            <h3>标签</h3>
            <ul class="interim-tags">
                <li data-ng-repeat="tag in userOpenInfo.tags">
                    <a href="{{ctx}}/user/{{userId}}#?tag={{tag}}">{{tag}}</a>
                </li>
            </ul>
        </div>

        <div class="interim-info-card">
            <h3>旅行</h3>
            <ul class="interim-tags">
                <li data-ng-repeat="travel in travels">
                    <a href="{{ctx}}/travel#?id={{travel.id}}">{{travel.title}}</a>
                </li>
            </ul>
        </div>
    </div>
</div>