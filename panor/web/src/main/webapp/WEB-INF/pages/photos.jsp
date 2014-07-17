<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html lang="en">
<head>
<title><fmt:message key="menu.explore" /></title>
<meta name="menu" content="AdminMenu" />
    
</head>
<body>
<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("photosApp"), ['photosApp']);
	})
</script>

<div class="container waypoint-scrollable"
     id="photosApp">
    <ui-view>
        <i>Some content will load here!</i>
    </ui-view>
</div>

</body>
</html>