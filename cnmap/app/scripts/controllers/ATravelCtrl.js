/**
 * Created by any on 2014/4/17.
 */

'use strict';
angular.module('aTravelApp', ['ponmApp', 'ponmApp.services', 'ponmApp.directives', 'ui.map'])
    .controller('ATravelCtrl', ['$window', '$scope', '$log', '$http', '$location', 'TravelService',
        function ($window, $scope, $log, $http, $location, TravelService) {
            $scope.ctx = $window.ctx;
            $scope.apirest = $window.apirest;
            var mapEventListener = $window.cnmap.MapEventListener.factory();

            var travelLayer = new $window.cnmap.TravelLayer({
                ctx: $scope.ctx,
                travel: $scope.travel
            });

//            getTravel(11);

            $scope.$watch(function () {
                return $location.search();
            }, function (searchObject) {
                if(searchObject.id) {
                    $scope.travelId = searchObject.id;
                    getTravel(searchObject.id);
                }
            });

            $scope.$watch('myMap', function(mapObj) {
                travelLayer.setMap(mapObj);
            });

            $scope.$watchCollection('travel', function(travel) {
                $log.debug(travel);
            });

            $scope.activePhoto = function(photo) {
                travelLayer.activePhoto(photo);
            };

            $scope.activeSpot = function(spot) {
                travelLayer.activeSpot(spot);
            };

            function getTravel(travelId) {
                TravelService.getTravel({travelId: travelId}, function(res) {
                    if(res.status == "OK") {
//                        $scope.$apply(function() {
                            $scope.travel = res.travel;
                            if(!angular.isArray($scope.travel.spots)) {
                                $scope.travel.spots = [];
                            }
                        if($scope.travel.spot) {
                            $scope.travel.spots.push($scope.travel.spot);
                        }
                        travelLayer.setTravel($scope.travel);
                        travelLayer.initMap();
//                        })

                    }
                });
            }

            $scope.mapOptions = {
                // map plugin config
//            toolbar: true,
                scrollzoom: true,
                maptype: true, //'SATELLITE',
                overview: true,
//                locatecity: true,
                // map-self config
                resizeEnable: true,
                scaleControl: true
//            panControl: false,
//            zoomControl: false
            };

            $scope.$watch('myMap', function () {
                var mapObj = $scope.myMap;
                mapEventListener.addToolBar(mapObj);
            });

            //
            $scope.scrollCallback = function(e) {
                $log.debug("scrollCallback");
            };
        }])
;