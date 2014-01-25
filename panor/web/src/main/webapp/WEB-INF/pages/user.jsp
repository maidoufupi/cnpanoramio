<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<head>
<title><fmt:message key="display.title" /></title>
<meta name="menu" content="AdminMenu" />
<link href="<c:url value="/styles/user.css"/>" rel="stylesheet">
</head>

<script type="text/javascript" src="<c:url value="/bower_components/jquery/plugins/jquery.rest.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/bower_components/bootstrap3/js/bootstrap-paginator.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/bower_components/fileupload/blueimp/tmpl.js"/>"></script>
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/panoramio/cnmap.comm.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/js/cnmap.gaode.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/panoramio/cnmap.gaode.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/panor/panoramio/cnmap.Panoramio.js'/>"></script>

<script src="<c:url value="/bower_components/eventEmitter/EventEmitter.js"/>"></script>
<script src="<c:url value="/bower_components/eventie/eventie.js"/>"></script>
<script src="<c:url value="/bower_components/doc-ready/doc-ready.js"/>"></script>
<script src="<c:url value="/bower_components/get-style-property/get-style-property.js"/>"></script>
<script src="<c:url value="/bower_components/get-size/get-size.js"/>"></script>
<script src="<c:url value="/bower_components/jquery-bridget/jquery.bridget.js"/>"></script>
<script src="<c:url value="/bower_components/matches-selector/matches-selector.js"/>"></script>
<script src="<c:url value="/bower_components/outlayer/item.js"/>"></script>
<script src="<c:url value="/bower_components/outlayer/outlayer.js"/>"></script>
<script src="<c:url value="/bower_components/masonry/masonry.js"/>"></script>

<script>
    docReady(function () {
        var container = document.querySelector('.masonry-container');
        var msnry = new Masonry(container, {
            columnWidth: 60
        });

        var map = $.cnmap.initMap("user_page-map", {
            toolbar: true,
            ruler: false,
            maptype: true
        })
        $.cnmap.setZoom(1);

        var panoramioLayer = new $.cnmap.PanoramioLayer({suppressInfoWindows: true});
        panoramioLayer.setMap(map);
    });
</script>

<div id="basic" class="container">
    <div class="photo-col">

        <div id="user-page_main-header">
            <div class="user-page_main-header_card">
                <img src="<c:url value='/images/user_avatar.png'/>" width="120" height="120" alt="" class="user_page-profile_img">

                <div id="user_profile_info">
                    <div class="user-page_profile_info user-page_main-lede">
                        ${userSettings.name}
                    </div>
                    <div id="profile_icons_user">
                        <a class="icon_sprite icon_link" href="http://" title=""></a>
                    </div>
                    <div id="user_header" style="display: block;">
                        <div class="user_header-best-or-all">
                            <a class="user-page-best-enabled" href="<c:url value='/user/${user.id}?show=best'/>">Best photos</a>
                            <span class="user-page-best-disabled">All photos</span>
                        </div>

                        <a href="<c:url value='/map/?user=${user.id}'/>">
                        	<img id="user_header-icon" src="/img/marker.png" height="16"
                                                          alt="">
                            <span id="user_header-map">view on map</span></a>

                    </div>

                </div>
            </div>

            <div class="user-page_main-header_card_stats user-page_main-header_card">

                <div id="top-line-stats">
                    <div class="user-page_top-line-stat">
                        <div class="user-page_top-line-stat-value">
                            <a href="<c:url value='/user/${user.id}/stats'/>">30</a>
                        </div>
                        <div class="user-page_top-line-stat-label">
                            photos
                        </div>
                    </div>
                    <div class="user-page_top-line-stat">
                        <div class="user-page_top-line-stat-value">
                            <a href="<c:url value='/user/${user.id}/stats'/>">24</a>
                        </div>
                        <div class="user-page_top-line-stat-label">
                            on Google Maps
                        </div>
                    </div>

                    <div class="user-page_top-line-stat">
                        <div class="user-page_top-line-stat-value">
                            <a href="<c:url value='/user/${user.id}/stats'/>">1539</a>
                        </div>
                        <div class="user-page_top-line-stat-label">
                            views
                        </div>
                    </div>
                </div>
            </div>
            <div style="clear: both;"></div>
        </div>
        <div class="masonry-container">
        <c:forEach var='photo' items='${photos}' varStatus='status'>
            <div class="masonry-item">
                <a href="<c:url value='/photo/${photo.id}'/>">
                	<img src="<c:url value='/services/api/photos/${photo.id}/1'/>" class="item-img">
                </a>
                <div class="thumb-overlay">
                    <div class="thumb-overlay-text" style="max-width: 189px">
                        <a href="<c:url value='/photo/${photo.id}'/>">The Dark Hedges</a></div>
                    <div class="thumb-overlay-icons" style="max-width: 189px">
                        <div class="thumb-overlay-icon">
                            <img src="<c:url value='/images/panoramio-marker.png'/>"
                                 title="Selected for Google Maps and Google Earth"
                                 width="16" height="16" alt="">
                        </div>
                        <div class="thumb-overlay-icon">
                            <a href="<c:url value='/photo/${photo.id}'/>"><span id="counter_52034419">274073 次查看</span></a>
                        </div>
                    </div>
                </div>
            </div>
          </c:forEach>
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