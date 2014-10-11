<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>美丽星球</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">

	<link rel="stylesheet" href="<c:url value="/bower_components/bootstrap-sass-official/assets/stylesheets/bootstrap.css"/>">
	<link rel="stylesheet" href="<c:url value="/bower_components/blueimp-gallery/css/blueimp-gallery.css"/>">
    <%-- <link rel="stylesheet" href="<c:url value="/bower_components/angular-xeditable/dist/css/xeditable.css"/>">
    <link rel="stylesheet" href="<c:url value="/bower_components/blueimp-file-upload/css/jquery.fileupload.css"/>">
	<link rel="stylesheet" href="<c:url value="/bower_components/blueimp-file-upload/css/jquery.fileupload-ui.css"/>">
	<link rel="stylesheet" href='<c:url value="bower_components/Jcrop/css/jquery.Jcrop.css"/>'/> --%>
	
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/main.min.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/styles/vendor.min.css'/>" />
    
</head>
<body>
	<script type="text/javascript" src="<c:url value="/scripts/ponmApp.vendor.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/scripts/blueimp-file-upload.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ponmApp.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ponmApp.controllers.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ponmApp.directives.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/scripts.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/script.js'/>"></script>
<c:choose>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
    <script type="text/javascript" src="<c:url value='/bower_components/angular-ui-map-baidu/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/scripts.baidu.min.js'/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "qq"}'>
    <script type="text/javascript" src="<c:url value='/bower_components/angular-ui-map-qq/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/scripts.qq.min.js'/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "gaode"}'>
    <script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/scripts.gaode.min.js'/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "mapbar"}'>
  
  </c:when>
  <c:otherwise>
    <script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/scripts.gaode.min.js'/>"></script>
  </c:otherwise>
</c:choose>

	<script>
	window.staticCtx = "http://test.photoshows.cn";
	window.corsproxyCtx = "http://www.corsproxy.com/test.photoshows.cn";
	//window.staticCtx = "http://static.photoshows.cn";
	//window.corsproxyCtx = "http://www.corsproxy.com/static.photoshows.cn";
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
    <div ui-view="navbar">
        <i>Some content will load here!</i>
    </div>
    <div ui-view="alert">
        alert
    </div>
    <div ui-view style="position: absolute;
                        bottom: 0;
                        top: 51px;
                        width: 100%;">
        <i>Some content will load here!</i>
    </div>
</div>
</body>
</html>