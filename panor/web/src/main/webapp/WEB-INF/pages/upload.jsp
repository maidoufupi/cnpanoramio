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
<%-- 	<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
    <link rel="stylesheet" href="<c:url value="/styles/modal.css"/>"> --%>
</head>
<body>
    <%-- <script src="<c:url value="/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput.js"/>"></script>
    <script src="<c:url value="/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput-angular.js"/>"></script> --%>
    <script type="text/javascript" src="<c:url value="/scripts/panor/js/jquery.canvas.js"/>"></script>
        
<c:choose>
  <c:when test='${sessionScope.mapVendor eq "baidu"}'>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=41cd06c76f253eebc6f322c863d4baa1"></script>
    <script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
	<script type="text/javascript" src="<c:url value="/scripts/panor/js/modal/cnmap.Modal.baidu.js"/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "qq"}'>
  	<script charset="utf-8" src="http://map.qq.com/api/js?v=2.0"></script>
	<script type="text/javascript" src="<c:url value='/bower_components/angular-ui-map-qq/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/scripts.qq.min.js'/>"></script>
  </c:when>
  <c:when test='${sessionScope.mapVendor eq "gaode"}'>
  	<script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
	<script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/scripts.gaode.min.js'/>"></script>
  </c:when>
  <c:otherwise>
  	<script src="http://webapi.amap.com/maps?v=1.2&key=53f7e239ddb8ea62ba552742a233ed1f" type="text/javascript"></script>
  	<script type="text/javascript" src="<c:url value='/bower_components/angular-ui-mapgaode/ui-map.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/panor/scripts.gaode.min.js'/>"></script>
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
                    <span>添加图片...</span>
                    <input type="file" name="files[]" multiple ng-disabled="disabled">
                </span>
                <button type="button" class="btn btn-warning cancel" data-ng-click="cancel()"
                	data-ng-show="queue.length">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>取消上传</span>
                </button>
                <button type="button" class="btn btn-primary" data-ng-click="changeLocation(queue)"
                	data-ng-show="queue.length">
                    <i class="glyphicon glyphicon-map-marker"></i>
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
            <div class="container-fluid">
                <div class="row">
                    <label class="col-sm-2 control-label">旅行：</label>
                    <item-picker ng-model="travel"
                                 load-data="loadTravelData"
                                 new-data="newTravelData"
                                 item-value-name="title"
                            class="col-sm-3"></item-picker>
                    <label class="col-sm-2 control-label">标签：</label>
                    <item-picker ng-model="tags"
                                 load-data="loadTagData"
                                 new-data="newTagData"
                                 multiple-select
                                 class="col-sm-5"></item-picker>

                </div>
            </div>
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
                        <div>
                            <div class="editable-input"
                                 data-ng-class="{'hover': mouseEnter}"
                                 contentEditable
                                 ng-model="title"
                                 data-place-holder="{{file.name}}"></div>
                        </div>
                        <div>
                            <pre class="description"
                                 data-ng-class="{'hover': mouseEnter}"
                                 contentEditable
                                 ng-model="description"
                                 data-place-holder="添加描述"
                                 multiple-line="4">{{description}}</pre>
                        </div>
                    </div>
                    <item-picker ng-model="file.tags"
                                 load-data="loadTagData"
                                 new-data="newTagData"
                                 multiple-select
                                 ></item-picker>
                </td>
                <td>
                    <p class="size">{{file.size | formatFileSize}}</p>
                    <div class="progress progress-striped active fade" data-ng-class="{pending: 'in'}[file.$state()]" data-file-upload-progress="file.$progress()"><div class="progress-bar progress-bar-success" data-ng-style="{width: num + '%'}"></div></div>
                    <strong data-ng-show="file.error" class="error text-danger">{{file.error}}</strong>
                </td>
                <td>
                    <button type="button" class="btn btn-primary start" data-ng-click="file.$submit()" data-ng-hide="!file.$submit || options.autoUpload" data-ng-disabled="file.$state() == 'pending' || file.$state() == 'rejected'">
                        <i class="glyphicon glyphicon-upload"></i>
                        <span>上传</span>
                    </button>
                    <button type="button" class="btn btn-warning cancel" data-ng-click="file.$cancel()" data-ng-hide="!file.$cancel">
                        <i class="glyphicon glyphicon-ban-circle"></i>
                        <span>取消</span>
                    </button>
                    <button data-ng-controller="FileDestroyController" type="button" class="btn btn-danger destroy" data-ng-click="file.$destroy()" data-ng-hide="!file.$endestroy">
                        <i class="glyphicon glyphicon-trash"></i>
                        <span>删除</span>
                    </button>
                </td>
                <td>
                    <a class="a-change-location" href data-ng-click="changeLocation([file])">更改位置</a>
                    <div class="location-display-place">
                        <span class="lat">{{file.latPritty}} {{file.latPritty && file.latRef}}</span>
                        <span class="comma">{{file.latPritty && ", "}} </span>
                        <span class="lng">{{file.lngPritty}} {{file.lngPritty && file.lngRef}}</span>
                    </div>
                    <div class="location-display-address">{{file.address}}</div>
                </td>
                <td class="info-tag">
                    <span class="label label-primary"
                          data-ng-show="file.is360">全景</span>
                </td>
                <td class="link-tag" data-ng-class="{'photo-upload-ok': file.photoId}">
                    <span data-ng-class="{'glyphicon': file.photoId, 'glyphicon-ok': file.photoId}"></span>
                    <a ng-href="{{ctx}}/map##lat={{file.lat}}&lng={{file.lng}}&zoom=18&userid={{userId}}"
                       tooltip="在地图中查看"
                       tooltip-trigger="mouseenter"
                       tooltip-placement="top">
                        <span data-ng-class="{'glyphicon': file.photoId, 'glyphicon-globe': file.photoId}"></span>
                    </a>
                </td>
            </tr>
        </table>
        <div data-ng-show="queue.length">
            <span class="btn btn-success fileinput-button" ng-class="{disabled: disabled}">
                        <i class="glyphicon glyphicon-plus"></i>
                        <span>添加图片...</span>
                        <input type="file" name="files[]" multiple ng-disabled="disabled">
                    </span>
            <a type="button" class="btn btn-primary" data-ng-href="{{ctx}}/map##userid={{userId}}">
                <i class="glyphicon glyphicon-ok-circle"></i>
                <span>完成</span>
            </a>
        </div>
        <div ui-map="photoMap" ui-options="mapOptions"></div>
    </form>

    <br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">注意事项</h3>
        </div>
        <div class="panel-body">
            <ul>
                <li>上传文件最大为<strong>5 MB</strong></li>
                <li>你可以拖动电脑上的图片到网页上进行上传</li>
            </ul>
        </div>
    </div>
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
