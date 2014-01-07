<%@ include file="/common/taglibs.jsp" %>
<%@ page language="java" pageEncoding="UTF-8"%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<%-- <script type="text/javascript" src="<c:url value='/bower_components/jquery/jquery.js'/>"></script> --%>
<script type="text/javascript" src="<c:url value='/bower_components/jquery/plugins/jquery.rest.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/bower_components/jquery/plugins/jquery.bsAlerts.min.js'/>"></script>

    <script type="text/javascript">
        $(document).ready(function() {

            var client = new $.RestClient(ctx + '/services/api/');
            client.add('gisindex');
            $("#update-gisindex").click(function() {
                $(this).addClass("disabled");
                var that = this;
                client.gisindex.read().done(function() {
                	$(document).trigger("add-alerts", [
                	                                   {
                	                                       message: "更新成功",
                	                                       priority: "success"
                	                                   }
                	                               ]);
                    $(that).removeClass("disabled");
                });
            })
        })
    </script>

<div class="container">
	<div class="panel panel-default">
	  <div class="panel-heading">trigger update photo gis index table</div>
	  <div class="panel-body">
	    <button id="update-gisindex" class="btn btn-primary">trigger</button>
	  </div>
	</div>
</div>