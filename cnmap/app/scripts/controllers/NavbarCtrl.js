/**
 * Created by any on 14-3-21.
 */
angular.module('ponmApp')
    .controller('NavbarCtrl',
    ['$window', '$scope', '$log', '$http', 'ponmCtxConfig',
    function ($window, $scope, $log, $http, ponmCtxConfig) {

        $scope.ctx = ponmCtxConfig.ctx;
        $scope.staticCtx = ponmCtxConfig.staticCtx;
        $scope.apirest = ponmCtxConfig.apirest;
        $scope.ponmCtxConfig = ponmCtxConfig;

        $scope.$on("login", function() {
            $log.debug($scope.ponmCtxConfig.userId);
        });

        $scope.getLocation = function(val) {

            return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: val,
                    sensor: false
                }
            }).then(function(res){
                    var addresses = [];
                    angular.forEach(res.data.results, function(item){
                        addresses.push(item);
                    });
                    return addresses;
                });
        };

        $scope.update = function(address) {
            var geometry = address.geometry;
            if(geometry) {
                $window.location = $scope.ctx + "/map##lat=" + geometry.location.lat
                    + "&lng=" + geometry.location.lng
                    + "&bounds=" + geometry.bounds.southwest.lat + ","
                    + geometry.bounds.southwest.lng + ","
                    + geometry.bounds.northeast.lat + ","
                    + geometry.bounds.northeast.lng;
            }
        }
    }]);

angular.module('ponm.Navbar', ['ngResource', 'ui.bootstrap', 'ponmApp'])
    .controller('SearchLocCtrl',
    ['$window', '$scope', '$log', '$http', 'ponmCtxConfig',
        function ($window, $scope, $log, $http, ponmCtxConfig) {

            $scope.ctx = ponmCtxConfig.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = ponmCtxConfig.apirest;

            $scope.getLocation = function(val) {

                return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
                    params: {
                        address: val,
                        sensor: false
                    }
                }).then(function(res){
                    var addresses = [];
                    angular.forEach(res.data.results, function(item){
                        addresses.push(item);
                    });
                    return addresses;
                });
            };

            $scope.update = function(address) {
                var geometry = address.geometry;
                if(geometry) {
                    $window.location = $scope.ctx + "/map##lat=" + geometry.location.lat
                        + "&lng=" + geometry.location.lng
                        + "&bounds=" + geometry.bounds.southwest.lat + ","
                        + geometry.bounds.southwest.lng + ","
                        + geometry.bounds.northeast.lat + ","
                        + geometry.bounds.northeast.lng;
                }
            }
        }]);
