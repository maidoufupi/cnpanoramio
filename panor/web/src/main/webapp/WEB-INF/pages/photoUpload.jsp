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
<title>Photo Upload</title>
<meta name="description" content="File Upload widget with multiple file selection, drag&amp;drop support, progress bars, validation and preview images, audio and video for jQuery. Supports cross-domain, chunked and resumable file uploads and client-side image resizing. Works with any server-side platform (PHP, Python, Ruby on Rails, Java, Node.js, Go etc.) that supports standard HTML form file uploads.">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap styles -->
<!-- <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"> -->
<%-- <link rel="stylesheet" href="<c:url value="/styles/fileupload/bootstrap.min.css"/>"> --%>
<!-- Generic page styles -->
<link rel="stylesheet" href="<c:url value="/styles/fileupload/style.css"/>">
<!-- blueimp Gallery styles -->
<link rel="stylesheet" href="http://blueimp.github.io/Gallery/css/blueimp-gallery.min.css">
<%-- <link rel="stylesheet" href="<c:url value="/styles/fileupload/blueimp-gallery.min.css"/>"> --%>
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="<c:url value="/styles/fileupload/jquery.fileupload.css"/>">
<link rel="stylesheet" href="<c:url value="/styles/fileupload/jquery.fileupload-ui.css"/>">
<!-- CSS adjustments for browsers with JavaScript disabled -->
<noscript><link rel="stylesheet" href="<c:url value="/styles/fileupload/jquery.fileupload-noscript.css"/>"></noscript>
<noscript><link rel="stylesheet" href="<c:url value="/styles/fileupload/jquery.fileupload-ui-noscript.css"/>"></noscript>
<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script> -->
<script type="text/javascript" src="<c:url value="/scripts/fileupload/vendor/jquery.min.1.10.2.js"/>"></script>

    <script type="text/javascript"
            src="http://api.map.baidu.com/api?v=1.5&ak=41cd06c76f253eebc6f322c863d4baa1"></script>

    <!-- Custom styles for this template -->
    <link href="<c:url value="/styles/fileupload/modal.css"/>" rel="stylesheet">

    <script type="text/javascript">
        $(document).ready(function () {
            $('#myModal').modal({show: false});
            $.cnmap.modal.initMap("upload-map");
            $.cnmap.modal.initGeocoder();
        })

    </script>
</head>

<body>
<script type="text/javascript" src="<c:url value="/scripts/panor/cnmap.Modal.baidu.js"/>"></script>
<div class="span3">
	<h2>
		<fmt:message key="upload.heading" />
	</h2>
	<p>
		<fmt:message key="upload.message" />
	</p>
</div>

<div class="container">
    <h1>jQuery File Upload Demo</h1>
    <br>
    <!-- The file upload form used as target for the file upload widget -->
    <form id="fileupload" action="<c:url value="/services/api/photos"/>" method="POST" enctype="multipart/form-data">
        <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
        <div class="row fileupload-buttonbar">
            <div class="col-lg-7">
                <!-- The fileinput-button span is used to style the file input field as button -->
                <span class="btn btn-success fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>Add files...</span>
                    <input type="file" name="image" multiple>
                </span>
                <button type="submit" class="btn btn-primary start">
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>Start upload</span>
                </button>
                <button type="reset" class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>Cancel upload</span>
                </button>
                <button type="button" class="btn btn-danger delete">
                    <i class="glyphicon glyphicon-trash"></i>
                    <span>Delete</span>
                </button>
                <input type="checkbox" class="toggle">
                <!-- The loading indicator is shown during file processing -->
                <span class="fileupload-loading"></span>
            </div>
            <!-- The global progress information -->
            <div class="col-lg-5 fileupload-progress fade">
                <!-- The global progress bar -->
                <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                    <div class="progress-bar progress-bar-success" style="width:0%;"></div>
                </div>
                <!-- The extended global progress information -->
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>
        <!-- The table listing the files available for upload/download -->
        <table role="presentation" class="table table-striped"><tbody class="files"></tbody></table>
    </form>

</div>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel"><span>Map</span> your photos</h4>
            </div>
            <div class="modal-body">
                <span class="mapping_info">
                  Drag a photo or click on the map to select the photographer position.
                    Please place the photo where the camera was, then select a place as the subject of the photo if applicable.
                </span>

                <div class="photo_list_and_map">
                    <div class="photo_list_panel">
                        <div id="map-photo-list" class="list-group map_photo_list">
                        </div>
                        <button id="button-save-complete" type="button" class="btn btn-primary"><fmt:message key="upload.save" /></button>
                    </div>
                    <div class="map_panel">
                        <div id="selected-photo-editor" class="selected_photo_editor">
                            <div class="properties">
                                <form id="geocoder_form" class="form">
                                    <div class="col-12">
                                        <div class="input-group input-group-sm">
                                            <input id="location-search-input" type="text" class="form-control">
                                            <span id="location-search-go" class="input-group-btn">
                                               <button type="submit" class="btn btn-default">Go!</button>
                                             </span>
                                        </div>
                                        <!-- /input-group -->
                                    </div>
                                </form>
                                <div id="the-place" class="no_place disabled place_search_bar">
                                    <span class="lat"></span>
                                    <span class="comma"></span>
                                    <span class="alt"><fmt:message key="upload.place" /></span>
                                    <span class="lng"></span>
                                </div>
                                <div class="coder_place"><span class="alt"><fmt:message key="upload.address" /></span><div id="the-address" class="original_place_name"></div></div>
                                
                                <label class="indoors_info"><input type="checkbox">This photo is taken indoors</label>

                                <div class="original_category_name">Original value: no</div>
                                <div>
                                    <button id="button-set-place" type="button" class="btn btn-primary"><fmt:message key="upload.setPlace" /></button>
                                </div>
                            </div>
                        </div>
                        <div id="upload-map" class="map"></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
    {% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td id="preview">
            <span class="preview"></span>
        </td>
        <td>
            <p class="name">{%=file.name%}</p>
            <strong class="error text-danger"></strong>
        </td>
        <td id="progress">
            <p class="size">Processing...</p>

            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100"
                 aria-valuenow="0">
                <div class="progress-bar progress-bar-success" style="width:0%;"></div>
            </div>
        </td>
        <td id="uploadButton" class="col-xs-3">
            {% if (!i && !o.options.autoUpload) { %}
            <button class="btn btn-primary start" disabled>
                <i class="glyphicon glyphicon-upload"></i>
                <span>Start</span>
            </button>
            {% } %}
            {% if (!i) { %}
            <button class="btn btn-warning cancel">
                <i class="glyphicon glyphicon-ban-circle"></i>
                <span>Cancel</span>
            </button>
            {% } %}
        </td>
        <td class="col-xs-3">
            <a class="a-change-location" href="#">change the location</a>
            <div class="location-display-place">
                <span class="lat"></span>
                <span class="comma"></span>
                <span class="lng"></span>
            </div>
            <div class="location-display-address"></div>
        </td>
    </tr>
    {% } %}
</script>

<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
    {% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <span class="preview">
                {% if (file.thumbnailUrl) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img
                            src="{%=file.thumbnailUrl%}"></a>
                {% } %}
            </span>
        </td>
        <td>
            <p class="name">
                {% if (file.url) { %}
                <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}"
                {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                {% } else { %}
                <span>{%=file.name%}</span>
                {% } %}
            </p>
            {% if (file.error) { %}
            <div><span class="label label-danger">Error</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            {% if (file.deleteUrl) { %}
            <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"
            {% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
            <i class="glyphicon glyphicon-trash"></i>
            <span>Delete</span>
            </button>
            <input type="checkbox" name="delete" value="1" class="toggle">
            {% } else { %}
            <button class="btn btn-warning cancel">
                <i class="glyphicon glyphicon-ban-circle"></i>
                <span>Cancel</span>
            </button>
            {% } %}
        </td>
    </tr>
    {% } %}
</script>
<script id="template-mapPhotoThumb" type="text/x-tmpl">
    {% for (var i=0, item; item=o.items[i]; i++) { %}
    <a href="#" class="list-group-item map_photo_cell active">
        <p class="map_photo_thumbnail">{%=item.src%}</p>
        <span class="list-group-item-text map_photo_title">{%=item.name%}</span>
    </a>
    {% } %}
</script>

<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="<c:url value="/scripts/fileupload/vendor/jquery.ui.widget.js"/>"></script>
<!-- The Templates plugin is included to render the upload/download listings -->
<!-- <script src="http://blueimp.github.io/JavaScript-Templates/js/tmpl.min.js"></script> -->
<script type="text/javascript" src="<c:url value="/scripts/fileupload/blueimp/tmpl.min.js"/>"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<!-- <script src="http://blueimp.github.io/JavaScript-Load-Image/js/load-image.min.js"></script> -->
<script type="text/javascript" src="<c:url value="/scripts/fileupload/blueimp/load-image.min.js"/>"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<!-- <script src="http://blueimp.github.io/JavaScript-Canvas-to-Blob/js/canvas-to-blob.min.js"></script> -->
<script type="text/javascript" src="<c:url value="/scripts/fileupload/blueimp/canvas-to-blob.min.js"/>"></script>
<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
<!-- <script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script> -->
<%-- <script type="text/javascript" src="<c:url value="/scripts/fileupload/vendor/bootstrap.min.js"/>"></script> --%>
<!-- blueimp Gallery script -->
<!-- <script src="http://blueimp.github.io/Gallery/js/jquery.blueimp-gallery.min.js"></script> -->
<script type="text/javascript" src="<c:url value="/scripts/fileupload/blueimp/jquery.blueimp-gallery.min.js"/>"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="<c:url value="/scripts/fileupload/jquery.iframe-transport.js"/>"></script>
<!-- The basic File Upload plugin -->
<script src="<c:url value="/scripts/fileupload/jquery.fileupload.js"/>"></script>
<!-- The File Upload processing plugin -->
<script src="<c:url value="/scripts/fileupload/jquery.fileupload-process.js"/>"></script>
<!-- The File Upload image preview & resize plugin -->
<script src="<c:url value="/scripts/fileupload/jquery.fileupload-image.js"/>"></script>
<!-- The File Upload audio preview plugin -->
<script src="<c:url value="/scripts/fileupload/jquery.fileupload-audio.js"/>"></script>
<!-- The File Upload video preview plugin -->
<script src="<c:url value="/scripts/fileupload/jquery.fileupload-video.js"/>"></script>
<!-- The File Upload validation plugin -->
<script src="<c:url value="/scripts/fileupload/jquery.fileupload-validate.js"/>"></script>
<!-- The File Upload user interface plugin -->
<script src="<c:url value="/scripts/fileupload/jquery.fileupload-ui.js"/>"></script>
<!-- The main application script -->
<script src="<c:url value="/scripts/fileupload/main.js"/>"></script>
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
<!--[if (gte IE 8)&(lt IE 10)]>
<script src="<c:url value="/scripts/fileupload/cors/jquery.xdr-transport.js"/>"></script>
<![endif]-->
</body> 
</html>