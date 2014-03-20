<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<head>
<title><fmt:message key="photo.display.title" /></title>
<meta name="menu" content="AdminMenu" />
<link href="<c:url value="/styles/PhotoDisplay.css"/>" rel="stylesheet">
</head>
<script type="text/javascript" src="<c:url value="/bower_components/jquery.rest/dist/jquery.rest.min.js"/>"></script>

<!-- angularjs -->
    <script type="text/javascript" src="<c:url value="/bower_components/angular/angular.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-cookies/angular-cookies.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-resource/angular-resource.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-sanitize/angular-sanitize.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-route/angular-route.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-ui-utils/event.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-ui-mapgaode/src/ui-map.js"/>"></script>
    
    <script type="text/javascript" src="<c:url value="/scripts/services/main.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/controllers/PhotoCtrl.js"/>"></script>
    
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/panoramio/cnmap.comm.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/js/cnmap.gaode.js'/>"></script>


<div class="interim-important_notice_wrapper hide">
	<div class="interim-important_notice">
		将您的Google+帐户与Panoramio<a class="interim-important_notice_link"
			href="#" id="gplus_connector">相关联</a>。<a
			class="interim-important_notice_link" href="/help/gplus-faq">了解详情</a>。
	</div>
</div>
<div class="container" ng-app="photoApp" ng-controller="PhotoCtrl">

    <div class="photo-col">
        <div id="main-photo-wrapper" data-id="<c:url value='${photo.id}'/>">
            <a id="main-photo" href="#">
                <img ng-src="{{apirest}}/{{photoId}}/1" alt="{{photo.description}}" id="main-photo_photo"
                     style="max-height: 541px;">
            </a>
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
                <div id="photo-page-prev-next-container">
                    <a href="/photo/64742707"><img ng-src="{{ctx}}/images/prev-uphoto.png" width="21" height="21" alt=""></a>
                    <a href="/photo/64644016"><img ng-src="{{ctx}}/images/next-uphoto.png" width="21" height="21" alt=""></a>
                </div>
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
                Comments ({{comment.count}})
            </h2>

            <div class="paginator-wrapper">
            </div>

            <div class="comment" ng-repeat="comment in comments" id="{{comment.id}}">
                <img class="comment-avatar" width="48"
                     ng-src="{{ctx}}/images/user_avatar.png" alt="">

                <div class="comment-inner">
                    <div class="comment-author">
                        <a href="{{ctx}}/user/{{comment.userId}}">{{comment.username}}</a> 于 {{comment.createTime}}
                    </div>
                    <div id="c49440741" class="photo-comment-text">
                        <p>{{comment.content}}</p>
                    </div>
                    <div class="translated-text" id="{{comment.id}}"></div>
                    <div class="translate-comment"><a class="translate-comment-link" id="t49440741" href="#">翻译</a>
                    </div>
                </div>
            </div>

            <div class="paginator-wrapper">
            </div>
        </div>

        <form action="" method="post" id="comment" ng-show="user.login" >
            <h3>发送评论 <span>(以 {{userOpenInfo.name}})</span></h3>
            <textarea cols="50" rows="3" id="tcomment" name="comment" ng-model="comment.content" class="form-control"></textarea>
            <br>
            <button ng-click="save(comment.content)" id="submit_comment"
                   class="button button-positive">发送评论</button>
            <!--            <a href="/help_format/" id="help_format" rel="help" target="_blank">
                            想用黑体，斜体或链接？
                        </a>-->
        </form>
    </div>
    <div class="info-col">
        <div class="interim-info-card photo-page-card">
            <div id="profile_pic_info">
                <a href="{{ctx}}/user/{{userOpenInfo.id}}">
                	<img ng-src="{{ctx}}/images/user_avatar.png"
                        width="60" height="60" alt="{{userOpenInfo.name}}" id="profile_pic_avatar"></a>

                <div id="profile_info">
                    <div id="profile_name">
                        <a href="{{ctx}}/user/{{userOpenInfo.id}}?with_photo_id={{photoId}}" rel="author">{{userOpenInfo.name}}</a>
                    </div>
                    <div id="profile_location">
                        {{userOpenInfo.location}}
                    </div>
                    <div id="profile-stats">
                        <a href="{{ctx}}/user/{{userOpenInfo.id}}">
                  <span class="profile-stats-text">
                      {{userOpenInfo.photoCount}} photos</span></a>
                    </div>
                </div>
            </div>
            <div class="photo_page-info_card_img_row" id="photo_page-owner_photos">
            </div>
            <div id="wapi_photo_list">
                <div class="panoramio-wapi-photolist-h panoramio-wapi-photolist" style="width: 295px; height: 48px;">
                    <a class="panoramio-wapi-arrow panoramio-wapi-prev" href="#" style="display: none;">
                        <img src="http://www.panoramio.com/img/wapi/photo_list_widget/left_arrow.png" alt="??" width="39"
                             height="38" title="??" style="margin-top: 5px;">
                    </a>
                    <span class="panoramio-wapi-arrowbox panoramio-wapi-arrowbox-prev"
                          style="width: 39px; display: none;"></span>

                    <div class="panoramio-wapi-overlay" style="width: 265px; height: 48px; display: none;"></div>
                    <div class="panoramio-wapi-images" style="width: 265px; height: 48px;">
                        <div class="panoramio-wapi-images" style="width: 265px; height: 48px;">
                            <div class="panoramio-wapi-loaded-img-div" style="width: 44px; height: 48px;">
                                <div class="panoramio-wapi-wrapper-div"
                                     style="padding: 0px; border-width: 1px; margin: 4px 2px;">
                                    <div class="panoramio-wapi-crop-div" style="width: 38px; height: 38px;">
                                        <a href="http://www.panoramio.com/photo/42477493">
                                            <img id="loadedImage7"
                                                 src="http://mw2.google.com/mw-panoramio/photos/square/42477493.jpg"
                                                 class="panoramio-wapi-img panoramio-wapi-loaded-img" alt="" title="t"
                                                 style="width: 38px; height: 38px; left: 0px; top: 0px;"></a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <a class="panoramio-wapi-arrow panoramio-wapi-next" href="#" style="display: none;">
                        <img src="http://www.panoramio.com/img/wapi/photo_list_widget/right_arrow.png" alt="??" width="39"
                             height="38" title="??" style="margin-top: 5px;">
                    </a>
                    <span class="panoramio-wapi-arrowbox panoramio-wapi-arrowbox-next"
                          style="width: 39px; display: none;"></span>
                </div>
                <div class="panoramio-wapi-tos">
                </div>
            </div>
            <div class="photo_page-info_card_img_row_footer">

                <h3 id="wapi_photo_h">
                    更多 <a href="/user/3908287?with_photo_id=41234541" rel="author">{{userOpenInfo.name}}</a> 的照片
                </h3>

            </div>
        </div>
        <div class="interim-info-card photo-page-card">
            <div id="map_info_breadcrumbs">
                <a href="/map/">World</a> •
                <a href="/map/#lt=13.406531&amp;ln=103.872785&amp;z=12&amp;k=2">柬埔寨</a> • <a
                    href="/map/#lt=13.406531&amp;ln=103.872785&amp;z=9&amp;k=2">暹粒省</a>
            </div>
            <div id="map_info_name">
                <a href="/map/#lt=13.406531&amp;ln=103.872785&amp;z=12&amp;k=2">Siem Reap</a>
            </div>
            <div id="minimap1" ui-map="minimap1" ui-options="mapOptions"></div>
            <div id="nearby_photos"><a href="/photo/54671273"><img class="nearby-img"
                                                                   src="http://mw2.google.com/mw-panoramio/photos/square/54671273.jpg"
                                                                   height="44" width="44" alt=""></a><a
                    href="/photo/145928"><img class="nearby-img"
                                              src="http://mw2.google.com/mw-panoramio/photos/square/145928.jpg"
                                              height="44" width="44" alt=""></a><a href="/photo/64643919"><img
                    class="selected-nearby-img" src="http://mw2.google.com/mw-panoramio/photos/square/64643919.jpg"
                    height="44" width="44" alt=""></a>

                <div class="next-photo"></div>
                <div class="next-photo"></div>
            </div>
            <div id="nearby">
                <p id="place">
                    Photo taken in {{gpsInfo.gps.address}}
                </p>

                <div id="location" class="photo_mapped">
                    <div class="geo">
                        <a title="查看这片区域" href="">
                            <abbr class="latitude" title="{{gpsInfo.gps.lat}}">{{gpsInfo.lat}}</abbr>&nbsp;
                            <abbr class="longitude" title="{{gpsInfo.gps.lng}}">{{gpsInfo.lng}}</abbr>
                        </a>
                    </div>
                    <p id="misplaced">
                        <a id="map_photo" href="/map_photo/?id=64643919">
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
                    <a  href="{{ctx}}/user/{{userOpenInfo.id}}/tags/{{tag}}">
                        {{tag}}
                    </a>
                </li>

                <li id="show_all_tags" style="display: none;">
                    <a href="#">
                        显示所有标签 (1)
                    </a>
                </li>
            </ul>
        </div>
        <div class="interim-info-card photo-page-card">
            <h2>Photo details</h2>
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

