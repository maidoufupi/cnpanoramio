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
'use strict';
(function () {
    'use strict';

    angular.module('fileuploadApp', [
        'ngResource',
        'ui.bootstrap',
        'blueimp.fileupload',
        'ui.map',
        'ponmApp'
    ])
        .config([
            '$httpProvider', 'fileUploadProvider', '$logProvider',
            function ($httpProvider, fileUploadProvider, $logProvider) {
                delete $httpProvider.defaults.headers.common['X-Requested-With'];
                fileUploadProvider.defaults.redirect = window.location.href.replace(
                    /\/[^\/]*$/,
                    '/cors/result.html?%s'
                );

//                if (isOnGitHub) {
//                    // Demo settings:
                angular.extend(fileUploadProvider.defaults, {
                    autoUpload: true,
                    // Enable image resizing, except for Android and Opera,
                    // which actually support image resizing, but fail to
                    // send Blob objects via XHR requests:
//                        disableImageResize: /Android(?!.*Chrome)|Opera/
//                            .test(window.navigator.userAgent),
                    maxFileSize: 6000000
//                        loadImageMaxFileSize: 10000000,
//                        imageQuality: 2000000,
//                        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
//                        disableImageResize: /Android(?!.*Chrome)|Opera/
//                            .test(window.navigator && navigator.userAgent),
                    //previewMaxWidth: 200,
                    //previewMaxHeight: 200,
                    //previewCrop: true, // Force cropped images,
                    //previewCanvas: false
//                        disableImageMetaDataLoad: true,
//                        imageMinHeight: 1098,
//                        imageMaxWidth: 5000,
//                        imageMaxHeight: 2000

                });
//                }
            }
        ])

        .controller('DemoFileUploadController', [ '$window',
            '$scope', '$http', 'fileUpload', '$modal', '$log', 'PhotoService', 'GPSConvertService',
            'LoginUserService', 'UserService', 'TravelService', 'GeocodeService',
            function ($window, $scope, $http, fileUpload, $modal, $log, PhotoService, GPSConvertService,
                      LoginUserService, UserService, TravelService, GeocodeService) {

                $scope.apirest = $window.apirest;
                $scope.ctx = $window.ctx;
                $scope.userId = $window.userId;

                var mapService = new $window.cnmap.MapService();

                $scope.options = {
                    //url: url,
                    formData: function () {
                        var lat = this.files[0].lat || 0,
                            lng = this.files[0].lng || 0,
                            address = this.files[0].address || '',
                            vendor = this.files[0].vendor || 'gps';
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
                                value: vendor
                            }
                        ]
                    },

                    done: function (e, data) {
                        if (data.result.status == "OK") {
                            var photo = data.result.prop;
                            data.files[0].$submit = false;
                            data.files[0].$cancel = false;
                            data.files[0].$endestroy = true;
                            extractProps(data.files[0], photo);

                            data.files[0].saveProperties();
                            data.files[0].saveTags();
                            updateTravelPhotos();
                        } else if (data.result.status == "NO_AUTHORIZE") {
                            data.files[0].error = "请重新登录";
                            data.files[0].$endestroy = false;
                        } else {
                            data.files[0].error = data.result.info;
                            data.files[0].$endestroy = false;
                        }
                    },
                    fail: function(e, data) {
                        if (data.result) {
                            if(data.result.status == "NO_AUTHORIZE") {
                                data.files[0].error = "请重新登录";
                            }
                            data.files[0].$cancel = function () {
                                data.files[0].$destroy();
                            };
                        }else {
                            fileUpload.defaults.fail(e, data);
                        }
                    }
                };

                if(!fileUpload.defaults.autoUpload) {
                    $scope.options.add =
                        function (e, data) {
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
                                        var latRef = data.exif.getText('GPSLatitudeRef');
                                        file.latRef = latRef;
                                    }

                                    var lng = data.exif.getText('GPSLongitude');
                                    if (lng && lng != "undefined") {
                                        file.lng = cnmap.GPS.convert(lng);
                                        var lngRef = data.exif.getText('GPSLongitudeRef');
                                        file.lngRef = lngRef;
                                    }

                                    file.latPritty = cnmap.GPS.convert(file.lat);
                                    file.lngPritty = cnmap.GPS.convert(file.lng);

                                    // 转换坐标体系
                                    if (file.lng || file.lat) {
                                        GPSConvertService.convert({
                                            'lat': file.lat,
                                            'lng': file.lng
                                        }, function (data) {
                                            file.lat = data.lat;
                                            file.lng = data.lng;
                                            file.vendor = "gaode";
                                            file.latPritty = cnmap.GPS.convert(file.lat);
                                            file.lngPritty = cnmap.GPS.convert(file.lng);

                                            mapService.getAddress(file.lat, file.lng, function(res) {
                                                file.address = res;
                                            });

//                                            GeocodeService.regeo({lat: file.lat, lng: file.lng}, function(regeocode) {
//                                                file.address = regeocode.formatted_address;
//                                            });
                                        })
                                    }
                                }
                            });
                        }
                }

                /**
                 * 从server返回的图片属性中抽取信息给file
                 *
                 * @param file
                 * @param photo
                 */
                function extractProps(file, photo) {
                    file.photoId = photo.id;
                    file.is360 = photo.is360;
                    if(photo.point) {
                        file.lat = photo.point.lat;
                        file.lng = photo.point.lng;
                        file.vendor = photo.vendor;
                        file.latPritty = cnmap.GPS.convert(file.lat);
                        file.lngPritty = cnmap.GPS.convert(file.lng);

                        mapService.getAddress(file.lat, file.lng, function(res) {
                            file.address = res;
                            file.saveProperties && file.saveProperties();
                        });

//                        GeocodeService.regeo({lat: file.lat, lng: file.lng}, function(regeocode) {
//                            file.address = regeocode.formatted_address;
//                        });
                    }

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
                        windowClass: 'map-photo-modal',
                        resolve: {
                            'files': function () {
                                return files;
                            }
                        }
                    });

                    modalInstance.result.then(function (selectedItem) {
                        angular.forEach(selectedItem, function (file, key) {
                            file.saveProperties();
                        })
                    }, function () {
                        $log.info('Modal dismissed at: ' + new Date());
                    });
                };

                /**
                 * 获取tag的class
                 *
                 * @param data
                 * @returns {string}
                 */
//                $scope.getTagClass = function (data) {
//                    return 'label label-primary';
//                };

//                $scope.resizeFile = function (data) {
//                    return fileUpload.processActions.resizeImage.call(
//                        fileUpload,
//                        data,
//                        {
//                            maxWidth: 3000,
//                            maxHeight: 2000
//                        }
//                    );
//                };

                // 异步加载用户travels数据
                $scope.loadTravelData = function (callback) {
                    var travels = UserService.getTravels({userId: LoginUserService.getUserId()}, function (res) {
                        if (res.status == "OK") {
                            callback && callback.apply(undefined, [res.open_info.travels]);
                        }
                    });
                    $log.debug(travels);
                };

                // 用户创建travel
                $scope.newTravelData = function (newObj, callback) {
                    $log.debug("new data: " + newObj);
                    TravelService.create({}, jQuery.param({travel: newObj}), function (res) {
                        if (res.status == "OK") {
                            callback && callback.apply(undefined, [res.travels]);
                        }
                    });
                    // 返回false，让其在不刷新数据，在callback接口返回后刷新
                    return false;
                };

                $scope.loadTagData = function (callback) {
                    $log.debug("load tags data");
                    UserService.getTags({userId: LoginUserService.getUserId()}, function (res) {
                        if (res.status == "OK") {
                            callback && callback.apply(undefined, [res.open_info.tags]);
                        }
                    });
                    return true;
                };

                $scope.newTagData = function (newObj, callback) {
                    $log.debug("new data: " + newObj);
                    UserService.createTag({userId: LoginUserService.getUserId(), value: newObj},
                        function (res) {
                            if (res.status == "OK") {
                                callback && callback.apply(undefined, [res.open_info.tags]);
                            }
                        });
                    return false;
                };

                // 当travel值有变化时，更新所有photo到新的travel上
                $scope.$watch('travel', function (newTravel) {
                    updateTravelPhotos();
                });

                /**
                 * 更新photo所属的travel
                 */
                function updateTravelPhotos() {
                    if (!$scope.travel) {
                        return;
                    }
                    var photos = [];

                    angular.forEach($scope.queue, function (file, key) {
                        if (file.photoId) {
                            photos.push(file.photoId);
                        }
                    });
                    if (photos.length) {
                        TravelService.addPhoto({travelId: $scope.travel.id},
                            jQuery.param({photos: photos.join(",")}), function (res) {
                                if (res.status == "OK") {
                                }
                            });
                    }

                }

                // 当抬头tag值有变化时，更新所有photo的tags
                $scope.$watch('tags', function (newTags) {
                    angular.forEach($scope.queue, function (file, key) {
                        if (file.photoId) {
                            file.saveTags();
                        }
                    });
                });

                $scope.$watch('photoMap', function(map) {
                    mapService.init(map);
                });

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
                    if(file.photoId) {
                        state = 'pending';
                        PhotoService.delete({photoId: file.photoId}, function (data) {
                            if (data) {
                                state = 'resolved';

                            } else {
                                state = 'rejected';
                            }
                        })
                    }

                    $scope.clear(file);

                };

//                file.$cancel = function () {
//                    data.abort();
//                    $scope.clear(file);
//                };
                if (!file.$cancel && !file._index) {
                    file.$cancel = function () {
                        $scope.clear(file);
                    };
                }
            }
        ])

        .controller('TitleEditorCtrl', [
            '$scope', '$http', 'PhotoService', '$log',
            function ($scope, $http, PhotoService, $log) {

                var statusInit = "INIT",
                    statusPending = "PENDING",
                    statusDone = "DONE";

                var file = $scope.file,
                    state;

                file.saveProperties = function () {
                    if (this.photoId) {
                        var _this = this;
                        PhotoService.updateProperties({photoId: _this.photoId}, {
                            'title': _this.title,
                            'description': _this.description,
                            'point': {
                                'lat': _this.lat,
                                'lng': _this.lng,
                                'address': _this.address
                            },
                            'vendor': _this.vendor,
                            'file_size': _this.size,
                            'is360': _this.is360
                        }, function (data) {
                            if (data) {
                                $log.debug("properties update successful");
                            }
                        })
                    }
                };

                file.saveTags = function () {
                    var _this = this;
                    if (_this.photoId && _this.tags) {
                        var tags = [];
                        tags = tags.concat(_this.tags || []).concat($scope.tags || []);

                        if (tags.length) {
                            PhotoService.tag({photoId: _this.photoId}, tags, function (data) {
                                if (data) {
                                    $log.debug("tags update successful");
                                }
                            })
                        }
                    }
                };

                $scope.$watch('title', function (title) {
                    $scope.file.title = $scope.title;
                    file.saveProperties();
                });

                $scope.$watch('description', function (title) {
                    $scope.file.description = $scope.description;
                    file.saveProperties();
                });

                $scope.$watch('file.tags', function (newTags) {
                    file.saveTags();
                });

//                var updatePhotoDStatus = statusInit;
//                file.updatePhotoProps = function() {
//                    if(updatePhotoDStatus == statusInit) {
//
//                    }
//                };
            }]);

}());
