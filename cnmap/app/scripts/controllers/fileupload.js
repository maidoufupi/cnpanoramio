/*
 * jQuery File Upload Plugin Angular JS Example 1.2.1
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2013, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */

/* jshint nomen:false */
/* global window, angular */

(function () {
    'use strict';

    var isOnGitHub = window.location.hostname === 'blueimp.github.io',
        url = isOnGitHub ? '//jquery-file-upload.appspot.com/' : 'server/php/';

    angular.module('cnmapApp', [
            'ngResource',
            'ui.bootstrap',
            'blueimp.fileupload',
            'ui.mapgaode',
            'bootstrap-tagsinput'
        ])
        .config([
            '$httpProvider', 'fileUploadProvider',
            function ($httpProvider, fileUploadProvider) {
                delete $httpProvider.defaults.headers.common['X-Requested-With'];
                fileUploadProvider.defaults.redirect = window.location.href.replace(
                    /\/[^\/]*$/,
                    '/cors/result.html?%s'
                );
                if (isOnGitHub) {
                    // Demo settings:
                    angular.extend(fileUploadProvider.defaults, {
                        // Enable image resizing, except for Android and Opera,
                        // which actually support image resizing, but fail to
                        // send Blob objects via XHR requests:
                        disableImageResize: /Android(?!.*Chrome)|Opera/
                            .test(window.navigator.userAgent),
                        maxFileSize: 5000000,
                        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i
                    });
                }
            }
        ])

        .controller('DemoFileUploadController', [
            '$scope', '$http', 'fileUpload', '$modal', '$log', '$window',
            function ($scope, $http, fileUpload, $modal, $log) {
                $scope.options = {
                    //url: url,
                    formData: function () {
                        var lat = this.files[0].lat || 0,
                            lng = this.files[0].lng || 0,
                            address = this.files[0].address || '';
                        return [
                            {
                                name: "lat",
                                value: lat
                            },
                            {
                                name: "lng",
                                value: lng
                            },
                            {
                                name: "address",
                                value: address
                            },
                            {
                                name: "vendor",
                                value: "gaode"
                            }
                        ]
                    },
                    add: function (e, data) {
                        if (e.isDefaultPrevented()) {
                            return false;
                        }
                        // call default add method
                        fileUpload.defaults.add(e, data);
                        // load image's gps info
                        var file = data.files[0];
                        loadImage.parseMetaData(file, function (data) {
                            if (data.exif) {
                                var lat = data.exif.getText('GPSLatitude');
                                if (lat && lat != "undefined") {
                                    file.lat = cnmap.GPS.convert(lat);
                                    file.latPritty = cnmap.GPS.convert(file.lat);
                                    var latRef = data.exif.getText('GPSLatitudeRef');
                                    file.latRef = latRef;
                                }

                                var lng = data.exif.getText('GPSLongitude');
                                if (lng && lng != "undefined") {
                                    file.lng = cnmap.GPS.convert(lng);
                                    file.lngPritty = cnmap.GPS.convert(file.lng);
                                    var lngRef = data.exif.getText('GPSLongitudeRef');
                                    file.lngRef = lngRef;
                                }
                            }
                        });
                    },
                    done: function (e, data) {
                        console.log(data)
                        if (data.result.id) {
                            data.files[0].$submit = false;
                            data.files[0].$cancel = false;
                            data.files[0].$endestroy = true;
                            data.files[0].photoId = data.result.id;
                        } else {
                            data.files[0].error = '上传出错';
                            data.files[0].$endestroy = false;
                        }
                    },
                    abort: function (data) {

                    }
                };
                if (!isOnGitHub) {
                    $scope.loadingFiles = true;
                    $http.get(url)
                        .then(
                        function (response) {
                            $scope.loadingFiles = false;
                            $scope.queue = response.data.files || [];
                        },
                        function () {
                            $scope.loadingFiles = false;
                        }
                    );
                }

                /**
                 * change image's gps location
                 *
                 * @param file
                 */
                $scope.changeLocation = function (files) {
                    var modalInstance = $modal.open({
                        templateUrl: 'views/changeLocationModal.html',
                        controller: "ChLocModalCtrl",
                        resolve: {
                            'files': function () {
                                return files;
                            }
                        }
                    });

                    modalInstance.result.then(function (selectedItem) {
                        $scope.selected = selectedItem;
                    }, function () {
                        $log.info('Modal dismissed at: ' + new Date());
                    });
                }

                $scope.getTagClass = function (city) {
                    switch (city.continent) {
                        default:
                            return 'label label-primary';
                    }
                };
            }
        ])

        .controller('FileDestroyController', [
            '$scope', '$http', 'PhotoService',
            function ($scope, $http, PhotoService) {
                var file = $scope.file,
                    state;

                file.$state = function () {
                    return state;
                };
                file.$destroy = function () {
                    state = 'pending';
                    PhotoService.delete({photoId: file.photoId}, function (data) {
                        if (data) {
                            state = 'resolved';
                            $scope.clear(file);
                        } else {
                            state = 'rejected';
                        }
                    })
                };
                if (!file.$cancel && !file._index) {
                    file.$cancel = function () {
                        $scope.clear(file);
                    };
                }
            }
        ]);

}());
