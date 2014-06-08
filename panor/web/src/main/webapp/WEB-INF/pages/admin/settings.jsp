<%@ include file="/common/taglibs.jsp" %>
<%@ page language="java" pageEncoding="UTF-8"%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<script type="text/javascript" src="<c:url value="/bower_components/jquery.rest/dist/jquery.rest.min.js"/>"></script>
<script type="text/javascript" src="<c:url value='/bower_components/jquery-bs-alerts/build/jquery.bsAlerts.min.js'/>"></script>

    <script type="text/javascript">
    $(document).ready(function() {

        $("#header-alert").hide();
        var client = new $.RestClient(apirest + "/");
        client.add('panoramio');
        client.panoramio.add('latest');
        $("#update-gisindex").click(function() {
            $(this).addClass("disable");
            var that = this;
            client.panoramio.read().done(function() {
                $(document).trigger("add-alerts", [
                    {
                        message: "This is success",
                        priority: "success"
                    }
                ]);
                $(that).removeClass("disable");
            });
            client.panoramio.latest.read().done(function() {
                $(document).trigger("add-alerts", [
                    {
                        message: "This is success",
                        priority: "success"
                    }
                ]);
                $(that).removeClass("disable");
            });
        })

        var restclient = new $.RestClient(ctx + '/api/rest/');
        restclient.add('index');
        $("#index-photo-id").click(function() {
            $(this).addClass("disable");
            var that = this;
            var photoid = $("#photo-id").val();
            restclient.index.update(photoid).done(function() {
                $(document).trigger("add-alerts", [
                    {
                        message: "This is success",
                        priority: "success"
                    }
                ]);
                $(that).removeClass("disable");
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
	
	<div class="panel panel-default">
        <div class="panel-heading">set index photo id</div>
        <div class="panel-body">
            <div class="input-group input-group-sm">
                <input id="photo-id" type="text" class="form-control" placeholder="Photo Id">
                <span class="input-group-btn">
                   <button id="index-photo-id" class="btn btn-primary">set</button>
                </span>
            </div>

        </div>
    </div>
</div>