/**
 * Created by any on 14-4-11.
 */
angular.module('test', [
        'ngResource',
        'ui.bootstrap'
    ])
    .config(function () {
    })
    .run(['$log', function($log) {
        $log.info("test app runing");
    }])
    .controller('TypeaheadCtrl', ['$scope', '$http', function($scope, $http) {
        $scope.selected = undefined;
        $scope.states = [];
        // Any function returning a promise object can be used to load values asynchronously
        $scope.getLocation = function(val) {
            return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: val,
                    sensor: false
                }
            }).then(function(res){
                    var addresses = [];
                    angular.forEach(res.data.results, function(item){
                        addresses.push(item.formatted_address);
                    });
                    return addresses;
                });
        };

    }]);
