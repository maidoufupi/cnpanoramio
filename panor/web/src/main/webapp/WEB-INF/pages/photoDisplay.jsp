<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="display.title" /></title>
<meta name="menu" content="AdminMenu" />
</head>

<div>
	<img src="<c:url value="/services/api/photos/${photoId}"/>"
		alt="" id="main-photo_photo" style="max-height: 689px;"/>
</div>