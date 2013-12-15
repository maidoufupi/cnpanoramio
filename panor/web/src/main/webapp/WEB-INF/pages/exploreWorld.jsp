<%@ include file="/common/taglibs.jsp"%>

<html lang="en">
<head>
<title><fmt:message key="upload.title" /></title>
<meta name="menu" content="AdminMenu" />
<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="utf-8">
<title>Explore the world</title>
<meta name="description" content="Explore the world">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Custom styles for this template -->
    <link href="<c:url value="/scripts/panor/css/explore.css"/>" rel="stylesheet">
</head>
<body>
<script type="text/javascript" src="<c:url value="/scripts/fileupload/vendor/jquery.min.1.10.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.baidu.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/cnmap.Explore.baidu.js"/>"></script>
    <script type="text/javascript"
            src="http://api.map.baidu.com/api?v=2.0&ak=41cd06c76f253eebc6f322c863d4baa1"></script>
<script type="text/javascript" src="<c:url value="/scripts/lib/plugins/jquery.rest.min.js"/>"></script>
    <script type="text/javascript">
    $(document).ready(function () {
        $.cnmap.explore.initMap("map-canvas");
        $.cnmap.explore.setPanoramioLayer();
        $("#get_panoramio").click(function() {
        	var client = new $.RestClient('/panor-web/services/api/', {
                stringifyData: true
            });
            client.add('panoramiothumbnail');
            var rs = client.panoramiothumbnail.create({
                    boundNELat: 180,
                    boundNELng: 200,
                    boundSWLat: 1,
                    boundSWLng: 1,
                    sizeX: 100,
                    sizeY:100
            }).done(function(data) {
                console.log(data)
            });
        })
    })
    </script>
<table class="front-root table table-striped">
    <tr>
        <td class="col-xs-8"><div id="map-canvas"></div></td>
        <td class="col-xs-4"><button id="get_panoramio" value="">get panoramio</button> </td>
    </tr>
</table>
</body>
</html>