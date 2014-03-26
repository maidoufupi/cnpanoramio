<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<menu:useMenuDisplayer name="Velocity" config="navbarMenu.vm" permissions="rolesAdapter">
	<div id="ponm-navbar" class="collapse navbar-collapse" ng-app="ponm.Navbar">
		<ul class="nav navbar-nav">
			<menu:displayMenu name="AdminMenu" />
			<%-- <menu:displayMenu name="Logout" /> --%>
			<li>
				<div id="nav-search-form">
                    <form class="navbar-form navbar-left" novalidate>
                        <input type="text" placeholder="搜索地址" class="form-control">
                        <i ng-show="loadingLocations" class="glyphicon glyphicon-refresh"></i>
                    </form>
                </div>
			</li>
		</ul>
		<ul class="nav navbar-nav navbar-right">
	  		<li>
                <a href="<c:url value="/map"/>" >
                    <span class="glyphicon glyphicon-plane white"></span>
                </a>
            </li>
            <li>
                <a href="<c:url value='/upload'/>" >
                    <span class="glyphicon glyphicon-camera white"></span>
                </a>
            </li>
            <c:choose>
            	<c:when test="${not empty pageContext.request.remoteUser}">
	            	<li>
		            	<a href="<c:url value='/user/${sessionScope.userId}'/>">
		                	<span>${pageContext.request.remoteUser}</span>
                        	<span class="glyphicon glyphicon-user white"></span>
		                </a>
        		    </li>
  				</c:when>
			    <c:otherwise>
			    	<li class="active">
						<a href="<c:url value="/login"/>"><fmt:message key="login.title" /></a>
					</li>
			    </c:otherwise>
			</c:choose>
            
			<li class="btn-group">
				<a class="dropdown-toggle" data-toggle="dropdown">
                        <span class="glyphicon glyphicon-th-list white"></span>
                    </a>
                    <ul id="dropdown" class="dropdown dropdown-menu">
                        <li><a href="<c:url value='/user_settings'/>"><span class="glyphicon glyphicon-cog"></span>用户设置</a></li>
                        <li><a href="<c:url value='/logout'/>"><span class="glyphicon glyphicon-log-out"></span>注销</a></li>
                    </ul>
			</li>
		</ul>

	</div>

</menu:useMenuDisplayer>
