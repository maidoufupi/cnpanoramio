<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<head>
<title><fmt:message key="photo.display.title" /></title>
<meta name="menu" content="AdminMenu" />
<%-- <link href="<c:url value="/styles/PhotoDisplay.css"/>" rel="stylesheet"> --%>
</head>
	<script type="text/javascript" src="<c:url value="/bower_components/jquery.rest/dist/jquery.rest.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/scripts/panor/js/three.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/scripts/panor/js/jquery.panzoom.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/scripts/panor/js/jquery.mousewheel.js"/>"></script>
        
<c:choose>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
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
			angular.bootstrap(document.getElementById("photoApp"), ['photoApp']);
	})
</script>
<div id="photoApp" class="container" ng-controller="PhotoCtrl">

    <div class="photo-col">
        <div id="main-photo-wrapper" data-id="<c:url value='${photo.id}'/>">
            <div ponm-photo-container
                 ponm-photo-src-l1="{{apirest}}/photo/{{photoId}}/{{(!photo.is360 && 1) || 0}}"
                 ponm-photo-width="{{photo.width}}"
                 ponm-photo-height="{{photo.height}}"
                 ponm-photo-color="{{photo.color}}"
                 ponm-photo-is360="{{!!photo.is360}}"
                    ></div>
        </div>
        <div>
            <div class="photo_page-stats_container">
                <div id="counter_snippet" class="photo_page_counter_snippet">
                        <span class="photo-page-stats" id="total-views-sum">
	                        <a href="/photo/{{photoId}}/stats">
	                            {{photo.views}} views
	                        </a>
                        </span>
                        <span class="photo-page-stats" id="favorites-sum">
	                        <a href="/photo/{{photoId}}/stats">
	                            {{photo.fav_count}} favorite
	                        </a>
                        </span>
                </div>
                <a class="icon_sprite icon_flag photo_page-stats" href="/offensive/photo?id={{photoId}}"
                   title="举报内容不当或带有攻击性的照片"></a>
 				<span id="favorite">
                    <a href="#" data-ng-click="favorite()">
                        <span class="glyphicon glyphicon-heart favorite " data-ng-class="{active: photo.favorite}"/>
                    </a>
                </span>
                <!-- <div id="photo-page-prev-next-container">
                    <a href="/photo/64742707"><img ng-src="{{ctx}}/images/prev-uphoto.png" width="21" height="21" alt=""></a>
                    <a href="/photo/64644016"><img ng-src="{{ctx}}/images/next-uphoto.png" width="21" height="21" alt=""></a>
                </div> -->
            </div>
            <div id="photo-title-box">
                <div id="photo-title-icon"></div>
                <h1 id="photo-title"
                    data-ng-click="setEditable('title')"
                    ng-hide="editable == 'title'">
                    {{photo.title}}
                </h1>
                <span class="photo_description">
                    <input ng-show="editable == 'title'"
                           type="text" size="30" name="title" ng-model="photo.title"
                           ng-blur="photo.save()">
                </span>
            </div>
        </div>
        <!-- ******************************************************************************** -->
        <div data-ng-show="false">
            <div style="float: right;">

                <a href="#" onclick="$('#delete').submit();return false" class="photo-page-edit">
                    <span class="icon_sprite icon_trash"></span>
                    <span class="photo-page-edit-label">Delete photo</span>
                </a>

                <a href="#" id="mark_as_best" class="photo-page-edit" style="display: inline;">
                    <span class="photo-page-edit-label">Mark as best</span>
                </a>
                  <span id="marked_as_best">
                    <a id="mark_as_not_best" href="#">Remove from Best</a>
                  </span>

                <form method="post" action="/do/delete_photo/" id="delete" onsubmit="return confirm('您确定要删除这张照片？');">
                    <input type="hidden" id="xsrf_token" name="xsrf_token"
                           value="lzJDOpMCDknjCLS2bGO4sDoxMzg5ODU2Mjk4MDE5MDk1">
                    <input type="hidden" name="id" value="63753785">
                    <input type="hidden" name="token" value="">
                </form>
            </div>

            <div class="photo-share-icons">
                <a class="icon_sprite icon_twitter" target="_blank" title="在Twitter分享"
                   href="http://twitter.com/?status=%E7%A7%8D%E8%92%9C%20-%20http%3A//panoramio.com/photo/63753785"
                   onclick="_gaq.push(['_trackSocial', 'twitter', 'tweet', '/twitter/photo_id=63753785']);"></a>
                <a href="#" id="sendLink" class="icon_sprite icon_mail" title="发送给朋友"></a>
            </div>
        </div>
        <!-- ******************************************************************************** -->

        <div data-ng-show="false">
            <div style="float: right;">
            </div>
            <!--<g:plusone size="medium" annotation="none"></g:plusone>-->
            <div class="photo-share-icons">
                <a class="icon_sprite icon_twitter" target="_blank" title="在Twitter分享"
                   href="http://twitter.com/?status=admire%20the%20culture%20of%20ancient%20Angkor...%20-%20http%3A//panoramio.com/photo/64742548"
                   onclick="_gaq.push(['_trackSocial', 'twitter', 'tweet', '/twitter/photo_id=64742548']);"></a>
            </div>
        </div>
        <div data-ng-show="false">
            <div class="photo-page-earth-status">
                Selected for Google Maps and Google Earth
            </div>
        </div>
        <div class="photo_description" id="photo-description">
            <span class="center" ng-click="setEditable('description')"
                  ng-hide="editable == 'description'">{{photo.description || '添加描述'}}</span>
            <span ng-show="editable == 'description'" class="photo_description">
                <textarea type="text" size="30" name="description" ng-model="photo.description"
                          ng-blur="photo.save()"></textarea>
            </span>
        </div>
        <div id="comments_wrapper">
            <h2 id="users_comments">
             	评论 ({{comment.totalItems}})
            </h2>

            <div class="paginator-wrapper">
            </div>

            <div class="media comment" ng-repeat="comment in comments">
                <a class="pull-left" href="{{ctx}}/user/{{comment.userId}}">
                    <img class="media-object"
                         data-ng-src="{{apirest}}/user/{{comment.userId}}/avatar"
                         width="60" height="60" alt="{{comment.username}}">
                </a>
                <div class="media-body">
                    <h6 class="media-heading">
                        <a href="{{ctx}}/user/{{comment.userId}}">{{comment.username}}</a> 于 {{comment.createTime}}
                    </h6>
                    <p ng-repeat="line in (comment.content | newlines) track by $index">{{line}}</p>
                </div>
            </div>

            <div class="paginator-wrapper" data-ng-show="comment.totalItems > 0">
            	<pagination items-per-page="comment.pageSize" total-items="comment.totalItems" 
            				page="comment.currentPage" max-size="comment.maxSize" class="pagination-sm" 
            				boundary-links="true" rotate="false" num-pages="comment.numPages"></pagination>
            </div>
        </div>

        <form action="" method="post" id="comment" ng-show="user.login" >
            <h3>发送评论 <span>(以 {{user.open_info.name}})</span></h3>
            <textarea data-ng-trim cols="50" rows="3" id="tcomment" name="comment" ng-model="comment.content" class="form-control"></textarea>
            <br>
            <button ng-click="createComment(comment.content)" id="submit_comment"
                   class="btn btn-default">发送评论</button>
            <!--            <a href="/help_format/" id="help_format" rel="help" target="_blank">
                            想用黑体，斜体或链接？
                        </a>-->
            <div style="height: 50px"></div>
        </form>
    </div>
    <div class="info-col">
        <div class="interim-info-card photo-page-card">
       		<h2>用户</h2>
            <div id="profile_pic_info">
                <a href="{{ctx}}/user/{{userOpenInfo.id}}">
                	<img ng-src="{{apirest}}/user/{{userOpenInfo.id || 1}}/avatar"
                        width="60" height="60" alt="{{userOpenInfo.name}}" id="profile_pic_avatar"></a>

                <div id="profile_info">
                    <div id="profile_name">
                        <a href="{{ctx}}/user/{{userOpenInfo.id}}#?with_photo_id={{photoId}}" rel="author">{{userOpenInfo.name}}</a>
                    </div>
                    <div id="profile_location">
                        {{userOpenInfo.location}}
                    </div>
                    <div id="profile-stats">
                        <a href="{{ctx}}/user/{{userOpenInfo.id}}">
                  <span class="profile-stats-text">
                      {{userOpenInfo.photo_count || '0' }} 张图片</span></a>
                    </div>
                </div>
            </div>
            <div class="photo_page-info_card_img_row" id="photo_page-owner_photos">
            </div>
            <div id="wapi_photo_list">
                <div class="panoramio-wapi-photolist-h panoramio-wapi-photolist" style="width: 295px; height: 48px;">
                    <span class="panoramio-wapi-arrowbox panoramio-wapi-arrowbox-prev"
                          style="width: 39px; display: none;"></span>

                    <div class="panoramio-wapi-overlay" style="width: 265px; height: 48px; display: none;"></div>
                    <div class="panoramio-wapi-images" style="width: 265px; height: 48px;">
                        <div class="panoramio-wapi-images" style="width: 265px; height: 48px;">
                            <div data-ng-repeat="photo in user_photos" class="panoramio-wapi-loaded-img-div" style="width: 44px; height: 48px;">
                                <div class="panoramio-wapi-wrapper-div"
                                     style="padding: 0px; border-width: 1px; margin: 4px 2px;">
                                    <div class="panoramio-wapi-crop-div" style="width: 38px; height: 38px;">
                                        <a href="{{ctx}}/photo/{{photo.id}}">
                                            <img data-ng-src="{{apirest}}/photo/{{photo.id}}/3"
                                                 class="panoramio-wapi-img panoramio-wapi-loaded-img" alt="" title="t"
                                                 style="width: 38px; height: 38px; left: 0px; top: 0px;"></a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <span class="panoramio-wapi-arrowbox panoramio-wapi-arrowbox-next"
                          style="width: 39px; display: none;"></span>
                </div>
                <div class="panoramio-wapi-tos">
                </div>
            </div>
            <div class="photo_page-info_card_img_row_footer">

                <h3 id="wapi_photo_h">
                    更多 <a href="{{ctx}}/user/{{userOpenInfo.id}}#?with_photo_id={{photoId}}" rel="author">{{userOpenInfo.name}}</a> 的照片
                </h3>

            </div>
        </div>
        <div class="interim-info-card photo-page-card">
            <h2>地图</h2>
            <div id="minimap1" ui-map="minimap1" ui-options="mapOptions"></div>
            <div id="nearby_photos">
                <a data-ng-repeat="photo in nearby_photos" data-ng-href="{{ctx}}/photo/{{photo.photo_id}}">
                    <img class="nearby-img" data-ng-src="{{apirest}}/photo/{{photo.photo_id}}/3"
                         height="44" width="44" alt="">
                </a>
            </div>
            <div id="nearby">
                <p id="place">
                    照片拍摄于 {{gpsInfo.gps.address}}
                </p>

                <div id="location" class="photo_mapped">
                    <div class="geo">
                        <a title="查看这片区域" href="{{ctx}}/map##lat={{gpsInfo.gps.lat}}&lng={{gpsInfo.gps.lng}}&zoom=17">
                            <abbr class="latitude" title="{{gpsInfo.gps.lat}}">{{gpsInfo.lat}}</abbr>&nbsp;
                            <abbr class="longitude" title="{{gpsInfo.gps.lng}}">{{gpsInfo.lng}}</abbr>
                        </a>
                    </div>
                    <p id="misplaced">
                        <a id="map_photo" href="{{ctx}}/map_photo##id={{photoId}}">
                            放错地点了吗？建议新的位置
                        </a>
                    </p>
                </div>
            </div>
        </div>
        <div class="interim-info-card photo-page-card">
            <h2>标签</h2>
            <ul id="interim-tags">

                <li data-ng-repeat="tag in photo.tags" id="tag_element_{{$index}}">
                    <a  href="{{ctx}}/user/{{userOpenInfo.id}}#?tag={{tag}}">
                        {{tag}}
                    </a>
                </li>
            </ul>
        </div>
        <div class="interim-info-card photo-page-card">
            <h2>相机信息</h2>
            <ul id="details">
                <li>
                    于 {{photo.create_time}} 上传
                </li>
                <li class="license c">
                    © 保留所有权利
                    <br>作者 {{userOpenInfo.name}}
                </li>
                <li id="tech-details">
                    <ul>
                        <li>相机型号：{{cameraInfo.model}}</li>
                        <li>拍摄日期：{{cameraInfo.date_time_original}}</li>
                        <li>曝光时间：{{cameraInfo.exposure_time}}</li>
                        <li>焦距：{{cameraInfo.focal_length}}</li>
                        <li>光圈： {{cameraInfo.fnumber}}</li>
                        <li>ISO：{{cameraInfo.iso}}</li>
                        <li>曝光补偿： {{cameraInfo.exposure_bias}}</li>
                        <li>{{cameraInfo.flash ? ('闪光灯：' + cameraInfo.flash) : '无闪光灯'}}</li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>

