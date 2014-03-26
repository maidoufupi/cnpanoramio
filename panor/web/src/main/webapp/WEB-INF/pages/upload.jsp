<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>

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
<meta name="description" content="File Upload widget with multiple file selection, drag&amp;drop support, progress bars, validation and preview images, audio and video for AngularJS. Supports cross-domain, chunked and resumable file uploads and client-side image resizing. Works with any server-side platform (PHP, Python, Ruby on Rails, Java, Node.js, Go etc.) that supports standard HTML form file uploads.">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap styles -->
<link rel="stylesheet" href="<c:url value="/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput.css"/>">

<!-- blueimp Gallery styles -->
<link rel="stylesheet" href="<c:url value="/bower_components/blueimp-gallery/css/blueimp-gallery.min.css"/>">
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="<c:url value="/bower_components/blueimp-file-upload/css/jquery.fileupload.css"/>">
<link rel="stylesheet" href="<c:url value="/bower_components/blueimp-file-upload/css/jquery.fileupload-ui.css"/>">
<!-- CSS adjustments for browsers with JavaScript disabled -->

<noscript><link rel="stylesheet" href="<c:url value="/bower_components/blueimp-file-upload/css/jquery.fileupload-noscript.css"/>"></noscript>
<noscript><link rel="stylesheet" href="<c:url value="/bower_components/blueimp-file-upload/css/jquery.fileupload-ui-noscript.css"/>"></noscript>
<style>
/* Hide Angular JS elements before initializing */
.ng-cloak {
    display: none;
}
</style>
	<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
    <link rel="stylesheet" href="<c:url value="/styles/modal.css"/>">
</head>
<body>
    <script src="<c:url value="/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput.js"/>"></script>
    <script src="<c:url value="/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput-angular.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-ui-utils/event.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/bower_components/angular-ui-mapgaode/src/ui-map.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/jquery.canvas.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/scripts/panor/panoramio/cnmap.comm.js"/>"></script>
    <!-- The main application script -->
    <script src="<c:url value="/scripts/services/main.js"/>"></script>
    <script src="<c:url value="/scripts/controllers/ChLocModalCtrl.js"/>"></script>
    <script src="<c:url value="/scripts/controllers/fileupload.js"/>"></script>
    
<c:choose>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=41cd06c76f253eebc6f322c863d4baa1"></script>
    <script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
	<script type="text/javascript" src="<c:url value="/scripts/panor/js/modal/cnmap.Modal.baidu.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "qq"}'>
  	<script charset="utf-8" src="http://map.qq.com/api/js?v=2.0"></script>
	<script type="text/javascript" src="<c:url value="/scripts/panor/js/modal/cnmap.Modal.qq.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "gaode"}'>
  	<script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value="/scripts/panor/js/modal/cnmap.Modal.gaode.js"/>"></script>
  </c:when>
  <c:otherwise>
  	<script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value="/scripts/panor/js/modal/cnmap.Modal.gaode.js"/>"></script>
   </c:otherwise>
</c:choose>
<script>
	$(document).ready(function () {
			angular.bootstrap(document.getElementById("fileupload"), ['fileuploadApp']);
	})
</script>
<div class="container">
    <h1>上传图片</h1>
    <h2 class="lead">到地图上</h2>
    
     <!-- The file upload form used as target for the file upload widget -->
    <form id="fileupload" action="<c:url value="/api/rest/photo/upload"/>" method="POST" enctype="multipart/form-data" data-ng-controller="DemoFileUploadController" data-file-upload="options" data-ng-class="{'fileupload-processing': processing() || loadingFiles}">
        <!-- Redirect browsers with JavaScript disabled to the origin page -->
        <noscript><input type="hidden" name="redirect" value="http://blueimp.github.io/jQuery-File-Upload/"></noscript>
        <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
        <div class="row fileupload-buttonbar">
            <div class="col-lg-7">
                <!-- The fileinput-button span is used to style the file input field as button -->
                <span class="btn btn-success fileinput-button" ng-class="{disabled: disabled}">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>Add files...</span>
                    <input type="file" name="files[]" multiple ng-disabled="disabled">
                </span>
                <button type="button" class="btn btn-primary start" data-ng-click="submit()">
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>Start upload</span>
                </button>
                <button type="button" class="btn btn-warning cancel" data-ng-click="cancel()">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>Cancel upload</span>
                </button>
                <button type="button" class="btn btn-primary" data-ng-click="changeLocation(queue)">
                    <i class="glyphicon glyphicon-tint"></i>
                    <span>更改位置</span>
                </button>
                <!-- The global file processing state -->
                <span class="fileupload-process"></span>
            </div>
            <!-- The global progress state -->
            <div class="col-lg-5 fade" data-ng-class="{in: active()}">
                <!-- The global progress bar -->
                <div class="progress progress-striped active" data-file-upload-progress="progress()"><div class="progress-bar progress-bar-success" data-ng-style="{width: num + '%'}"></div></div>
                <!-- The extended global progress state -->
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>
        <div data-ng-show="queue.length">
            <label>为所有图添加标签：</label>
            <bootstrap-tagsinput ng-model="tags"
                                 tagclass="getTagClass"></bootstrap-tagsinput>
        </div>
        <!-- The table listing the files available for upload/download -->
        <table class="table table-striped files ng-cloak">
            <tr data-ng-repeat="file in queue" data-ng-class="{'processing': file.$processing()}">
                <td data-ng-switch data-on="!!file.thumbnailUrl">
                    <div class="preview" data-ng-switch-when="true">
                        <a data-ng-href="{{file.url}}" title="{{file.name}}" download="{{file.name}}" data-gallery><img data-ng-src="{{file.thumbnailUrl}}" alt=""></a>
                    </div>
                    <div class="preview" data-ng-switch-default data-file-upload-preview="file"></div>
                </td>
                <td>
                    <div data-ng-controller="TitleEditorCtrl">
                        <p>
                            <span class="center" ng-click="editable = 'title'"
                                  ng-hide="editable == 'title'"
                                  ng-mouseenter="editable == 'title'"
                                  ng-mouseleave="editable == ''">{{file.title || file.name || ''}}</span>
                            <span class="photo_description">
                                <input ng-show="editable == 'title'"
                                       type="text" size="30" name="title" ng-model="title"
                                       ng-blur="saveTitle()">
                            </span>
                        </p>
                        <p>
                            <span class="center" ng-click="editable = 'description'"
                                  ng-hide="editable == 'description'">{{file.description || '添加描述'}}</span>
                            <span ng-show="editable == 'description'" class="photo_description">
                                <textarea type="text" size="30" name="description" ng-model="text"
                                          ng-blur="saveDesc()"></textarea>
                            </span>
                        </p>
                    </div>
                    <bootstrap-tagsinput ng-model="file.tags"
                                         tagclass="getTagClass"
                                         placeholder="Last name"></bootstrap-tagsinput>
                    <strong data-ng-show="file.error" class="error text-danger">{{file.error}}</strong>
                </td>
                <td>
                    <p class="size">{{file.size | formatFileSize}}</p>
                    <div class="progress progress-striped active fade" data-ng-class="{pending: 'in'}[file.$state()]" data-file-upload-progress="file.$progress()"><div class="progress-bar progress-bar-success" data-ng-style="{width: num + '%'}"></div></div>
                </td>
                <td>
                    <button type="button" class="btn btn-primary start" data-ng-click="file.$submit()" data-ng-hide="!file.$submit || options.autoUpload" data-ng-disabled="file.$state() == 'pending' || file.$state() == 'rejected'">
                        <i class="glyphicon glyphicon-upload"></i>
                        <span>Start</span>
                    </button>
                    <button type="button" class="btn btn-warning cancel" data-ng-click="file.$cancel()" data-ng-hide="!file.$cancel">
                        <i class="glyphicon glyphicon-ban-circle"></i>
                        <span>Cancel</span>
                    </button>
                    <button data-ng-controller="FileDestroyController" type="button" class="btn btn-danger destroy" data-ng-click="file.$destroy()" data-ng-hide="!file.$endestroy">
                        <i class="glyphicon glyphicon-trash"></i>
                        <span>Delete</span>
                    </button>
                </td>
                <td>
                    <a class="a-change-location" href data-ng-click="changeLocation([file])">更改位置</a>
                    <div class="location-display-place">
                        <span class="lat">{{file.latPritty}} {{file.latRef}}</span>
                        <span class="comma">{{file.latPritty && ", "}} </span>
                        <span class="lng">{{file.lngPritty}} {{file.lngRef}}</span>
                    </div>
                    <div class="location-display-address">{{file.address}}</div>
                </td>
                <td data-ng-class="{'photo-upload-ok': file.photoId}">
                    <span data-ng-class="{'glyphicon': file.photoId, 'glyphicon-ok': file.photoId}"></span>
                    <a href="{{ctx}}/photo/{{file.photoId}}">
                        <span data-ng-class="{'glyphicon': file.photoId, 'glyphicon-globe': file.photoId}"></span>
                    </a>
                </td>
            </tr>
        </table>
    </form>

    <br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Demo Notes</h3>
        </div>
        <div class="panel-body">
            <ul>
                <li>The maximum file size for uploads in this demo is <strong>5 MB</strong> (default file size is unlimited).</li>
                <li>Only image files (<strong>JPG, GIF, PNG</strong>) are allowed in this demo (by default there is no file type restriction).</li>
                <li>You can <strong>drag &amp; drop</strong> files from your desktop on this webpage (see <a href="https://github.com/blueimp/jQuery-File-Upload/wiki/Browser-support">Browser support</a>).</li>
                <li>Please refer to the <a href="https://github.com/blueimp/jQuery-File-Upload">project website</a> and <a href="https://github.com/blueimp/jQuery-File-Upload/wiki">documentation</a> for more information.</li>
                <li>Built with Twitter's <a href="http://twitter.github.com/bootstrap/">Bootstrap</a> CSS framework and Icons from <a href="http://glyphicons.com/">Glyphicons</a>.</li>
            </ul>
        </div>
    </div>
</div>
<!-- The blueimp Gallery widget -->
<div id="blueimp-gallery" class="blueimp-gallery blueimp-gallery-controls" data-filter=":even">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev">?</a>
    <a class="next">?</a>
    <a class="close">¡@</a>
    <a class="play-pause"></a>
    <ol class="indicator"></ol>
</div>

<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="<c:url value="/bower_components/blueimp-file-upload/js/vendor/jquery.ui.widget.js"/>"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="<c:url value="/bower_components/blueimp-load-image/js/load-image.min.js"/>"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="<c:url value="/bower_components/blueimp-canvas-to-blob/js/canvas-to-blob.min.js"/>"></script>
<!-- blueimp Gallery script -->
<script src="<c:url value="/bower_components/blueimp-gallery/js/jquery.blueimp-gallery.min.js"/>"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="<c:url value="/bower_components/blueimp-file-upload/js/jquery.iframe-transport.js"/>"></script>
<!-- The basic File Upload plugin -->
<script src="<c:url value="/bower_components/blueimp-file-upload/js/jquery.fileupload.js"/>"></script>
<!-- The File Upload processing plugin -->
<script src="<c:url value="/bower_components/blueimp-file-upload/js/jquery.fileupload-process.js"/>"></script>
<!-- The File Upload image preview & resize plugin -->
<script src="<c:url value="/bower_components/blueimp-file-upload/js/jquery.fileupload-image.js"/>"></script>
<!-- The File Upload audio preview plugin -->
<script src="<c:url value="/bower_components/blueimp-file-upload/js/jquery.fileupload-audio.js"/>"></script>
<!-- The File Upload video preview plugin -->
<script src="<c:url value="/bower_components/blueimp-file-upload/js/jquery.fileupload-video.js"/>"></script>
<!-- The File Upload validation plugin -->
<script src="<c:url value="/bower_components/blueimp-file-upload/js/jquery.fileupload-validate.js"/>"></script>
<!-- The File Upload Angular JS module -->
<script src="<c:url value="/bower_components/blueimp-file-upload/js/jquery.fileupload-angular.js"/>"></script>

</body>
</html>