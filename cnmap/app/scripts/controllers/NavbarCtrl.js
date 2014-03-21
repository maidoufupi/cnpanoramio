/**
 * Created by any on 14-3-21.
 */
angular.module('ponm.Navbar', ['ngResource', 'ui.bootstrap'])
    .controller('SearchLocCtrl', ['$window', '$scope', '$log', '$http', function ($window, $scope, $log, $http) {
        $scope.getLocation = function(val) {

            return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: val,
                    sensor: false
                }
            }).then(function(res){
                    var addresses = [];
//                    $scope.addresses = {};
                    angular.forEach(res.data.results, function(item){
//                        $log.debug(item);
//                        $scope.addresses[item.formatted_address] = item;
                        addresses.push(item);
                    });
                    return addresses;
                });
        }

        $scope.update = function(address) {
            var geometry = address.geometry;
            if(geometry) {
                $window.location = $window.ctx + "/exploreWorld#lat=" + geometry.location.lat + "&lng=" + geometry.location.lng;
//                $scope.myMap.setCenter(new AMap.LngLat(geometry.location.lng, geometry.location.lat))
//                $scope.myMap.setBounds(new AMap.Bounds(
//                    new AMap.LngLat(geometry.bounds.southwest.lng, geometry.bounds.southwest.lat),
//                    new AMap.LngLat(geometry.bounds.northeast.lng, geometry.bounds.northeast.lat)
//                ))
            }
        }
    }]);