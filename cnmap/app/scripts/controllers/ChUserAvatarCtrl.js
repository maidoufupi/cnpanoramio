/**
 * Created by any on 14-3-12.
 */
angular.module('ponmApp.controllers')
    .controller('ChUserAvatarCtrl', ['$window', '$scope', '$log', '$sce', '$http', '$modalInstance', 'alertService',
        function ($window, $scope, $log, $sce, $http, $modalInstance, alertService) {

            $scope.alertService = angular.copy(alertService);

            var url = $window.apirest + "/user/avatar";

            $scope.cancel = function () {
                $modalInstance.dismiss($scope.avatar);
            };

            var getBlobURL = ($window.URL && $window.URL.createObjectURL.bind($window.URL)) ||
                ($window.webkitURL && $window.webkitURL.createObjectURL.bind($window.webkitURL)) ||
                $window.createObjectURL;

            $scope.fileUpload = function () {

                $scope.alertService.clear();

                if(!$scope.$$childTail.$$childTail.previewUrl) {
                    return;
                }

                var boundary = Math.random().toString().substr(2);
                var multipart = "";
                multipart += "--" + boundary
                    + "\r\nContent-Disposition: form-data; name=" + "\"file\"" + '; filename="avatar.png"'
                    + "\r\nContent-type: application/octet-stream"
                    + "\r\n\r\n" + $scope.$$childTail.$$childTail.previewUrl + "\r\n";

                multipart += "--" + boundary + "--\r\n";

                $http({method: 'POST', url: url,
                    data: multipart,
                    headers: {
                        "Content-Type": "multipart/form-data; charset=utf-8; boundary=" + boundary
                    }
                }).success(function (data, status, headers, config) {
                        if (data.status == "OK") {
                            $scope.avatar = data.open_info.avatar;
                            $scope.alertService.add("success", "上传成功!", 5000);
                        } else {
                            $scope.alertService.add("danger", "上传失败!", 5000);
                        }
                    });
            }

            $scope.onFileSelect = function ($files) {
                $scope.alertService.clear();
                var url = $sce.trustAsResourceUrl(getBlobURL($files[0]));
                $scope.imageUrl = url;
            }
        }])
    .directive('imgCropped', function () {
        return {
            restrict: 'E',
            replace: true,
            scope: { 'src': '@',
                'bind': '=previewUrl' },
            link: function (scope, element, attr) {

                var boxWidth = 800;
                var boxHeight = 420;
                var myImg;
                var preview;

                var clear = function () {
                    if (myImg) {
                        myImg.next().remove();
                        myImg.remove();
                        myImg = undefined;
                    }

                    if (preview) {
                        preview.next().remove();
                        preview.remove();
                        preview = undefined;
                    }
                };
                scope.$watch('src', function (nv) {
                    clear();
                    if (nv) {
                        element.after('<img />');
                        myImg = element.next();
                        myImg.attr('src', nv);
                        $(myImg).Jcrop({
                            trackDocument: true,
                            bgFade: true,
                            boxWidth: boxWidth,
                            boxHeight: boxHeight,
                            minSize: [16, 16],
                            onSelect: updatePreview,
                            aspectRatio: 1
                        });

                        myImg.after('<img />');
                        preview = myImg.next();

                    }
                });

                function updatePreview(coords) {
                    var image = myImg[0];
                    var imageWidth = image.width;
                    var imageHeight = image.height;
                    var width = $(image).width();
                    var height = $(image).height();
                    var sx = coords.x * imageWidth / width,
                        sy = coords.y * imageHeight / height,
                        swidth = coords.w * imageWidth / width,
                        sheight = coords.h * imageHeight / height;

                    var canvas = document.createElement('canvas');
                    canvas.width = 100;
                    canvas.height = 100;
                    var ctx = canvas.getContext("2d");
                    ctx.drawImage(image, sx, sy, swidth, sheight, 0, 0, 100, 100);
                    scope.$apply(function () {
                        scope.previewUrl = canvas.toDataURL("image/png");
                        preview.attr('src', scope.previewUrl);
                    })
                }

                scope.$on('$destroy', clear);
            }
        };
    })

    .directive('ngFileSelect', [ '$parse', '$timeout', function($parse, $timeout) {
        return function(scope, elem, attr) {
            var fn = $parse(attr['ngFileSelect']);
            elem.bind('change', function(evt) {
                var files = [], fileList, i;
                fileList = evt.target.files;
                if (fileList != null) {
                    for (i = 0; i < fileList.length; i++) {
                        files.push(fileList.item(i));
                    }
                }
                $timeout(function() {
                    fn(scope, {
                        $files : files,
                        $event : evt
                    });
                });
            });
            elem.bind('click', function(){
                this.value = null;
            });
        };
    } ])

;

