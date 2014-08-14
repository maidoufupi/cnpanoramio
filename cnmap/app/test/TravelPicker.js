/**
 * Created by any on 2014/4/22.
 */

angular.module('travelPickerApp', [
    'ngResource',
    'ui.bootstrap',
    'ponmApp'
])
    .config(function () {
    })
    .run(['$log', function($log) {
        $log.info("test app runing");
    }])
    .controller('TravelPickerCtrl', ['$scope', '$http', '$log', '$timeout', '$q',
        function($scope, $http, $log, $timeout, $q) {

            $scope.travels = ["无锡之旅",
                "南京行",
                "北京行"];

            $scope.$watch('atravel2', function(newValue) {
                $log.debug("a travel 2: " + newValue);
            });

            $scope.loadTravelData = function(callback) {
                $log.debug("load data");
                var deferred = $q.defer();
                $timeout(function() {
                    deferred.resolve($scope.travels);
                }, 500);

                return deferred.promise;
            };

            $scope.newTravelData = function(newObj) {
                $log.debug("new data: " + newObj);
                var deferred = $q.defer();
//                $scope.travels.push(newObj);
                deferred.resolve(newObj);
                return deferred.promise;
            };

            $scope.items = [{
                id: 1,
                name: "无锡之旅"
            },
                {   id: 2,
                    name: "南京行"
                } ,
                {
                    id: 1,
                    name: "北京行"
                }];
            $scope.loadItemObj = function() {
                $log.debug("load data");
                var deferred = $q.defer();
                $timeout(function() {
                    deferred.resolve($scope.items);
                }, 2000);

                return deferred.promise;
            };

            var i = 3;
            $scope.newItemObj = function(newObj) {
                $log.debug("new data: " + newObj);
                var deferred = $q.defer();
//                $scope.items.push(newObj);
                i++;
                $timeout(function() {
                    deferred.resolve({id: i, name: newObj});
                }, 2000);

                return deferred.promise;
            };

            $scope.$watch('atravel3', function(newValue) {
                $log.debug("a travel 3: ");
                $log.debug(newValue);
            });
    }])

;