/**
 * Created by any on 14-3-12.
 */
angular.module('cnmapApp')
    .controller('ChUserAvatarCtrl', ['$scope', '$log', '$sce', '$http', '$modalInstance', '$upload', 'AvatarService',
        function ($scope, $log, $sce, $http, $modalInstance, $upload, AvatarService) {

            var getBlobURL = (window.URL && URL.createObjectURL.bind(URL)) ||
                (window.webkitURL && webkitURL.createObjectURL.bind(webkitURL)) ||
                window.createObjectURL;

            $scope.fileReaderSupported = window.FileReader != null;
            $scope.uploadRightAway = true;
            $scope.changeAngularVersion = function() {
                window.location.hash = $scope.angularVersion;
                window.location.reload(true);
            }
            $scope.hasUploader = function(index) {
                return $scope.upload[index] != null;
            };
            $scope.abort = function(index) {
                $scope.upload[index].abort();
                $scope.upload[index] = null;
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.fileUpload = function () {

                var formData = new FormData();
                $log.debug($scope.imageUrl.$$unwrapTrustedValue());
                formData.append('file', [$scope.imageUrl.$$unwrapTrustedValue()]);

                $http({method: 'POST', url: 'http://127.0.0.1:8080/panor-web/api/rest/user/avatar',
                    data: formData, headers: {'Content-Type': "multipart/form-data"}, transformRequest: angular.identity})
                    .success(function(data, status, headers, config) {

                    });

                AvatarService.upload({}, formData, function(data) {
                    $log.debug(data);
                })
                return ;

                    $scope.upload = $upload.upload({
                        url: 'http://127.0.0.1:8080/panor-web/api/rest/photo/upload', //upload.php script, node.js route, or servlet url
                        method: "POST",
                        headers: {'Content-Type': "image/png"},
                        // withCredentials: true,
                        data: $scope.previewUrl
                        //file: file // or list of files: $files for html5 only
                        /* set the file formData name ('Content-Desposition'). Default is 'file' */
                        //fileFormDataName: myFile, //or a list of names for multiple files (html5).
                        /* customize how data is added to formData. See #40#issuecomment-28612000 for sample code */
                        //formDataAppender: function(formData, key, val){}
                    })
//                        .then(function(response) {
//                            $scope.uploadResult.push(response.data);
//                        }, null, function(evt) {
//                            $scope.progress[index] = parseInt(100.0 * evt.loaded / evt.total);
//                        });
                    //.error(...)
                    //.then(success, error, progress);
                    //.xhr(function(xhr){xhr.upload.addEventListener(...)})// access and attach any event listener to XMLHttpRequest.

            }


            $scope.onFileSelect = function ($files) {

                $scope.$apply(function () {
                    var url = $sce.trustAsResourceUrl(getBlobURL($files[0]))
                    $scope.imageUrl = url;
                })
            }

        }])
    .directive('imgCropped', function () {
        return {
            restrict: 'E',
            replace: true,
            scope: { src: '@', previewUrl: '@' },
            link: function (scope, element, attr) {

                var boxWidth = 800;
                var boxHeight = 520;
                var myImg;
                var preview;

                var clear = function () {
                    if (myImg) {
                        myImg.next().remove();
                        myImg.remove();
                        myImg = undefined;
                    }

                    if(preview) {
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
                            minSize: [16,16],
                            onSelect: updatePreview,
//                            onSelect: function (x) {
//                                scope.$apply(function () {
//                                    scope.selected({cords: x});
//                                });
//                            },
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
                    scope.previewUrl = canvas.toDataURL("image/png");
                    preview.attr('src', scope.previewUrl);
                }

                scope.$on('$destroy', clear);
            }
        };
    })

    ;

