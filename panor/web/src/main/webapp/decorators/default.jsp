<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh">
<head>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="<c:url value="/images/favicon.ico"/>"/>
    <title><decorator:title/> | <fmt:message key="webapp.name"/></title>

    <link rel="stylesheet" href="<c:url value="/bower_components/bootstrap-sass-official/assets/stylesheets/bootstrap.css"/>">
    <link rel="stylesheet" href="<c:url value="/bower_components/angular-xeditable/dist/css/xeditable.css"/>">
    <!-- Bootstrap theme -->
    <%-- <link rel="stylesheet" href="<c:url value="/bower_components/sass-bootstrap/dist/css/bootstrap-theme.min.css"/>"> --%>
    
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/main.min.css'/>" />
    <decorator:head/>

    <script type="text/javascript" src="<c:url value='/bower_components/jquery/jquery.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/bower_components/sass-bootstrap/dist/js/bootstrap.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular/angular.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-route/angular-route.js"/>"></script>
    <%-- <script type="text/javascript" src="<c:url value='/bower_components/jquery.cookie/jquery.cookie.js'/>"></script>
    


    <script type="text/javascript" src="<c:url value="/bower_components/angular-resource/angular-resource.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-cookies/angular-cookies.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-sanitize/angular-sanitize.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-bootstrap/ui-bootstrap.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-bootstrap/ui-bootstrap-tpls.js"/>"></script>
    
    <script type="text/javascript" src="<c:url value="/bower_components/angular-xeditable/dist/js/xeditable.js"/>"></script>
    
    <script type="text/javascript" src="<c:url value="/bower_components/angular-ui-utils/event.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/eventie/eventie.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/eventEmitter/EventEmitter.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/imagesloaded/imagesloaded.js"/>"></script>
    
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/jquery.mousewheel.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/jquery.panzoom.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/three.min.js"/>"></script> 
    
    <script type="text/javascript" src="<c:url value="/bower_components/jquery-backstretch/jquery.backstretch.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/jquery-waypoints/waypoints.js"/>"></script>
    --%>
    
    <%--<script type="text/javascript" src="<c:url value="/scripts/ponmApp.vendor.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ponmApp.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ponmApp.controllers.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ponmApp.directives.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/scripts.min.js"/>"></script>--%>

    <script type="text/javascript" src="<c:url value='/scripts/script.js'/>"></script>
    
    <script>
	    var ctx = "${pageContext.request.contextPath}"; // 设置全局变量：应用的根路径
	    window.login = "${not empty pageContext.request.remoteUser}"; // 设置全局变量：用户是否登录
	    window.userId = "${sessionScope.userId}"; // 用户ID
	    window.apirest = ctx + "/api/rest";
	    window.mapVendor = '<c:out value="${sessionScope.mapVendor}"/>' || "gaode";
    </script>
    
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
</head>
<body <decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>
    <c:set var="currentMenu" scope="request"><decorator:getProperty property="meta.menu"/></c:set>

    <div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <%-- For smartphones and smaller screens --%>
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="<c:url value='/'/>">
                    <img src="<c:url value="/images/logo.png"/>" class="navbar-logo"/>
                </a>
                
                <%-- <c:if test='${pageContext.request.locale.language ne "en"}'>
                    <div id="switchLocale"><a href="<c:url value='/?locale=en'/>">
                        <fmt:message key="webapp.name"/> in English</a>
                    </div>
                </c:if> --%>
            </div>
            <%--<%@ include file="/common/menu.jsp" %>--%>
            <%@ include file="/views/ponm.navbar.html" %>
        </div>
    </div>

    <div class="container waypoint-scrollable">
    	
        <%@ include file="/common/messages.jsp" %>
        <div class="row">
            <decorator:body/>
        </div>
    </div>

    <div id="footer">
        <span class="left"><fmt:message key="webapp.version"/>
            <c:if test="${pageContext.request.remoteUser != null}">
            | <fmt:message key="user.status"/> ${pageContext.request.remoteUser}
            </c:if>
        </span>
        <span class="right">
            &copy; <fmt:message key="copyright.year"/> <a href="<fmt:message key="company.url"/>"><fmt:message key="company.name"/></a>
        </span>
    </div>
<%= (request.getAttribute("scripts") != null) ?  request.getAttribute("scripts") : "" %>

<script id="bdshell_js" type="text/javascript"></script>
</body>
</html>
