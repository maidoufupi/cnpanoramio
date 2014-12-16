<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<title>美丽星球</title>
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
	<meta charset="utf-8">
	<meta http-equiv="keywords" content="美丽星球,分享,旅行,照片,地图">
    <meta http-equiv="description" content="美丽星球，是一个致力于在地图上展示您的照片的网站，里面有丰富的地图功能，美观的界面设计，您一定会喜欢上它他。">
    <meta name="robots" content="all" />
    <meta name="author" content="http://www.photoshows.cn/#/blog/" />
    <meta name="copyright" content="© http://www.photoshows.cn/#/blog/" />
    <!--<meta http-equiv="refresh" content="5; url=http://127.0.0.1:9000/#/maps/user/3" />-->
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />

	<link rel="stylesheet" href="<c:url value="/bower_components/bootstrap/dist/css/bootstrap.css"/>">
	<link rel="stylesheet" href="<c:url value="/bower_components/blueimp-gallery/css/blueimp-gallery.css"/>">
    <%-- <link rel="stylesheet" href="<c:url value="/bower_components/angular-xeditable/dist/css/xeditable.css"/>">
    <link rel="stylesheet" href="<c:url value="/bower_components/blueimp-file-upload/css/jquery.fileupload.css"/>">
	<link rel="stylesheet" href="<c:url value="/bower_components/blueimp-file-upload/css/jquery.fileupload-ui.css"/>">
	<link rel="stylesheet" href='<c:url value="bower_components/Jcrop/css/jquery.Jcrop.css"/>'/> --%>
	
    <link rel="stylesheet" type="text/css" href="<c:url value='/styles/vendor.min.css'/>" />
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/main.min.css'/>" />
    
</head>
<body>
	<script type="text/javascript" id="bdshare_js" data="type=tools&mini=1" ></script>
	<script id="bdshell_js"></script>
		
	<%--<script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/ponmApp.jquery.min.js"></script>--%>
	<%--<script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/ponmApp.angular.min.js"></script>--%>
	<%--<script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/blueimp-file-upload.min.js"></script>--%>
    <%--<script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/ponmApp.min.js"></script>--%>
    <%--<script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/ponmApp.controllers.min.js"></script>--%>
    <%--<script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/ponmApp.directives.min.js"></script>--%>
    <%--<script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/scripts.min.js"></script>--%>

    <script type="text/javascript" src="scripts/ponmApp.jquery.min.js"></script>
    <script type="text/javascript" src="scripts/ponmApp.angular.min.js"></script>
    <script type="text/javascript" src="scripts/blueimp-file-upload.min.js"></script>
    <script type="text/javascript" src="scripts/ponmApp.min.js"></script>
    <script type="text/javascript" src="scripts/ponmApp.controllers.min.js"></script>
    <script type="text/javascript" src="scripts/panor/scripts.min.js"></script>
<c:choose>
  <c:when test='${sessionScope.mapVendor eq "google"}'>
  	<script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/scripts.google.min.js"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
  	<script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/scripts.baidu.min.js"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "qq"}'>
    <script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/scripts.qq.min.js"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "gaode"}'>
    <script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/scripts.gaode.min.js"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "mapbar"}'>
  
  </c:when>
  <c:otherwise>
    <script type="text/javascript" src="http://panor-static.oss-cn-qingdao.aliyuncs.com/scripts.gaode.min.js"></script>
  </c:otherwise>
</c:choose>

	<script>
	window.staticCtx = "http://test.photoshows.cn";
	window.corsproxyCtx = "http://www.photoshows.cn:9292/test.photoshows.cn";
	//window.staticCtx = "http://static.photoshows.cn";
	//window.corsproxyCtx = "http://www.photoshows.cn:9292/static.photoshows.cn";
	
	    var ctx = "${pageContext.request.contextPath}"; // 设置全局变量：应用的根路径
	    /* window.login = "${not empty pageContext.request.remoteUser}"; // 设置全局变量：用户是否登录 */
	    window.userId = "${sessionScope.userId}"; // 用户ID
	    window.apirest = ctx + "/api/rest";
	    window.mapVendor = '<c:out value="${sessionScope.mapVendor}"/>' || "gaode";
	    window.user = {
	            id: window.userId
	            };
    </script>

    <div ng-app="ponmApp.Index" ui-map-async-load>
        <div ui-view="navbar" class="header-navbar" ng-class="{'header-navbar-fixed': pageNavMode=='fixed'}">
        </div>
        <div ui-view="alert">
        </div>
        <div ui-view class="main-view-page" ng-class="{'header-navbar-fixed': pageNavMode=='fixed'}">
        </div>
    </div>
</body>
</html>