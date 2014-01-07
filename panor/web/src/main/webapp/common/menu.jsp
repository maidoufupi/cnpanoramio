<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<menu:useMenuDisplayer name="Velocity" config="navbarMenu.vm"
	permissions="rolesAdapter">
	<div class="collapse navbar-collapse">
		<ul class="nav navbar-nav">
			<c:if test="${empty pageContext.request.remoteUser}">
				<li class="active"><a href="<c:url value="/login"/>"><fmt:message
							key="login.title" /></a></li>
			</c:if>
<%-- 			<menu:displayMenu name="MainMenu" />
			<menu:displayMenu name="UserMenu" />
			<menu:displayMenu name="SettingsMenu" /> --%>
			<menu:displayMenu name="AdminMenu" />
			<%-- <menu:displayMenu name="Logout" /> --%>
			<li>
				<div id="navbar-search" class="input-group">
					<div class="input-group-btn">
						<button type="button" class="btn btn-default dropdown-toggle"
							data-toggle="dropdown">
							Action <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li><a href="#">Action</a></li>
							<li><a href="#">Another action</a></li>
							<li><a href="#">Something else here</a></li>
							<li class="divider"></li>
							<li><a href="#">Separated link</a></li>
						</ul>
					</div>
					<!-- /btn-group -->
					<input type="text" class="form-control">
				</div> <!-- /input-group -->
			</li>
		</ul>
		<ul class="nav navbar-nav navbar-right">
	  		<li>
                    <a href="<c:url value='/exploreWorld'/>">
                        <span class="glyphicon glyphicon-plane white"></span>
                    </a>
                </li>
			<li>
               <a href="<c:url value='/photoUpload'/>">
                 <span class="glyphicon glyphicon-camera white"></span>
               </a>
            </li>
			<li class="btn-group">
				<a class="dropdown-toggle" data-toggle="dropdown">
                        <span class="glyphicon glyphicon-th-list white"></span>
                    </a>
                    <ul id="dropdown" class="dropdown dropdown-menu">
                        <li><a href="<c:url value='/userSettings'/>"><span class="glyphicon glyphicon-cog"></span>用户设置</a></li>
                        <li><a href="<c:url value='/logout'/>"><span class="glyphicon glyphicon-log-out"></span>注销</a></li>
                    </ul>
			</li>
		</ul>

	</div>

</menu:useMenuDisplayer>
