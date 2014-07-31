<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html lang="en">
<head>
<title><fmt:message key="menu.explore" /></title>
<meta name="menu" content="AdminMenu" />
    <link rel="stylesheet" href="<c:url value="/bower_components/blueimp-file-upload/css/jquery.fileupload.css"/>">
	<link rel="stylesheet" href="<c:url value="/bower_components/blueimp-file-upload/css/jquery.fileupload-ui.css"/>">
</head>
<body>

<script type="text/javascript" src="<c:url value="/scripts/blueimp-file-upload.min.js"/>"></script>

<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("mapsApp"), ['mapsApp']);
	})
</script>

<div class="container"
     id="mapsApp">
    <ui-view>
        <i>Some content will load here!</i>
    </ui-view>
</div>

</body>
</html>