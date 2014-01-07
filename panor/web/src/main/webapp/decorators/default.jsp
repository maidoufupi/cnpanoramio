<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<html lang="en">
<head>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="<c:url value="/images/favicon.ico"/>"/>
    <title><decorator:title/> | <fmt:message key="webapp.name"/></title>

    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/bower_components/bootstrap3/css/bootstrap.min.css'/>" />
    <!-- Bootstrap theme -->
    <link href="<c:url value="/bower_components/bootstrap3/css/bootstrap-theme.min.css"/>" rel="stylesheet">
    
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/style.css'/>" />
    <decorator:head/>

    <script type="text/javascript" src="<c:url value='/bower_components/jquery/jquery.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/bower_components/bootstrap3/js/bootstrap.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/bower_components/jquery/plugins/jquery.cookie.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/bower_components/imgLiquid/imgLiquid.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/script.js'/>"></script>
    <script>var ctx = "${pageContext.request.contextPath}"</script>
</head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>
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
                <a class="navbar-brand" href="<c:url value='/'/>"><img src="<c:url value="/images/logo.png"/>" class="navbar-logo"/></a>
                
                <%-- <c:if test='${pageContext.request.locale.language ne "en"}'>
                    <div id="switchLocale"><a href="<c:url value='/?locale=en'/>">
                        <fmt:message key="webapp.name"/> in English</a>
                    </div>
                </c:if> --%>
            </div>
            <%@ include file="/common/menu.jsp" %>
        </div>
    </div>

    <div class="container">
        <%@ include file="/common/messages.jsp" %>
        <div class="row">
            <decorator:body/>

            <%-- <c:if test="${currentMenu == 'AdminMenu'}">
                <div class="span2">
                <menu:useMenuDisplayer name="Velocity" config="navlistMenu.vm" permissions="rolesAdapter">
                    <menu:displayMenu name="AdminMenu"/>
                </menu:useMenuDisplayer>
                </div>
            </c:if> --%>
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
</body>
</html>
