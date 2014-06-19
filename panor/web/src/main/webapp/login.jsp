<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<head>
<title><fmt:message key="login.title" /></title>
<meta name="menu" content="Login" />
</head>

<body>

<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("ponmAppLogin"), ['ponmApp.login']);
	})
</script>
<div id="ponmAppLogin" class="login-container" ng-controller="LoginCtrl">
    <div class="login-form">
    <div class="animated bounceIn">
        <div class="form-header">

        </div>
        <!--  -->
        <div class="form-main">
            <form method="post" action="/panor-web/j_security_check" name="form"
                  novalidate>
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="用户名"
                           name="j_username"
                           ng-model="credentials.username" required>
                    <input type="password" class="form-control" placeholder="密码"
                           name="j_password"
                           ng-model="credentials.password" required>
                </div>
                <button type="submit" class="btn btn-block signin" data-ng-click="login($event, credentials)">登录</button>
            </form>
        </div>
        <div class="form-footer">
            <div class="row">
                <div class="col-xs-7">
                    <i class="glyphicon glyphicon-lock"></i>
                    <a href="" data-ng-click="passwordHint()">忘记密码?</a>
                </div>
                <div class="col-xs-5">
                    <i class="glyphicon glyphicon-user"></i>
                    <a ng-href="{{ctx}}/signup">新建用户</a>
                </div>
            </div>
        </div>
    </div>
    </div>
</div>
</body>