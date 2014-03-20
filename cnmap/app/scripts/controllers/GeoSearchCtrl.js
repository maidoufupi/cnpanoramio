/**
 * Created by any on 14-3-17.
 */
'use strict';

angular.module('map.AMap', ['ngRoute', 'ngResource', 'ui.bootstrap'])
    .controller('TypeaheadCtrl', ['$scope', '$log', '$http', function ($scope, $log, $http) {
        $scope.getLocation = function(val) {

            return $http.get('http://restapi.amap.com/v3/geocode/geo', {
                params: {
                    'key': '53f7e239ddb8ea62ba552742a233ed1f',
                    'address': val
                }
            }).then(function(res){
                    var addresses = [];
                    angular.forEach(res.data.geocodes, function(item){
                        $log.debug(item);
                        addresses.push(item);
                    });
                    return addresses;
                });
        }

        $scope.update = function(address) {
            var location = address.location;
            if(location) {
                location = location.split(",");
                $log.debug(location);
//                $scope.myMap.setCenter(new AMap.LngLat(location[0], location[1]))
//                $scope.myMap.setBounds(new AMap.Bounds(
//                    new AMap.LngLat(geometry.bounds.southwest.lng, geometry.bounds.southwest.lat),
//                    new AMap.LngLat(geometry.bounds.northeast.lng, geometry.bounds.northeast.lat)
//                ))
            }

        }
    }]);