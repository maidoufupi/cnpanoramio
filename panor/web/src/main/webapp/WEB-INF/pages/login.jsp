<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
  <link rel="stylesheet" href="bower_components/bootstrap/dist/css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="styles/main.min.css"/>
</head>
<body>
<!-- jQuery plugins-->
<script type="text/javascript" src="scripts/ponmApp.jquery.min.js"></script>
<script type="text/javascript" src="scripts/ponmApp.angular.min.js"></script>

<!-- custom scripts -->
<script type="text/javascript" src="scripts/ponmApp.min.js"></script>
<script type="text/javascript" src="scripts/ponmApp.controllers.min.js"></script>

<script>
    var ctx = "${pageContext.request.contextPath}"; // 设置全局变量：应用的根路径
    window.apirest = ctx + "/api/rest";
</script>

<div ng-app="ponmApp.login">
  <div ui-view="navbar" class="header-navbar">
  </div>
  <div ui-view="alert">
  </div>
    <div style="position: absolute;
                  top: 0;
                  bottom: 0;
                  margin-top: 30px;
                  width: 100%;">
        <div ui-view class="login-view-page view-in-out">
        </div>
    </div>
</div>
</body>
</html>
