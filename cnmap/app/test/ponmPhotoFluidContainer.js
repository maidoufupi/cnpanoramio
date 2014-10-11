/**
 * Created by any on 2014/6/6.
 */
'use strict';
angular.module('ponmApp.test', ['ponmApp', 'ui.bootstrap'])
    .controller('photoFluidContainerCtrl', ['$scope', '$http', '$log', 'TravelService', 'jsUtils', '$location',
        'ponmCtxConfig',
        function($scope, $http, $log, TravelService, jsUtils, $location, ponmCtxConfig) {

            $scope.apirest = window.apirest;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.travelEnedit = true;

//            getTravel(11);
            $scope.$watch(function () {
                return $location.search();
            }, function (searchObject) {
                $log.debug(jsUtils.deparam("id=12"));
                if(searchObject.id) {
                    $scope.travelId = searchObject.id;
                    getTravel(searchObject.id);
                }
            });

            function getTravel(travelId) {
                TravelService.getTravel({travelId: travelId}, function(res) {
                    if(res.status == "OK") {
                        $scope.travel = res.travel;
                        if(!angular.isArray($scope.travel.spots)) {
                            $scope.travel.spots = [];
                        }
                        if($scope.travel.spot) {
                            $scope.travel.spots.push($scope.travel.spot);
                        }
                    }
                });
            }

            $scope.enterHover = function(e) {
                $log.debug("enterHover");
            }
            $scope.leaveHover = function(e) {
                $log.debug("leaveHover");
            };

            $scope.$on('photoDeleteEvent', function(event, data) {
                $log.debug("photo delete event: photoId = " + data);

                angular.forEach($scope.travel.spots, function (spot, key) {
                    angular.forEach(spot.photos, function (photo, key) {
                        if (photo.id == data) {
                            delete spot.photos.splice(key, 1);
                        }
                    });
                });

                $scope.$broadcast('ponmPhotoFluidResize');
            });
        }])

    .controller('spotCtrl', ['$scope', '$http', '$log', 'TravelService', '$location',
        'ponmCtxConfig',
        function($scope, $http, $log, TravelService, $location, ponmCtxConfig) {

        }])
;