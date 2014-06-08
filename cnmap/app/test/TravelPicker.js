/**
 * Created by any on 2014/4/22.
 */

angular.module('travelPickerApp', [
    'ngResource',
    'ui.bootstrap',
    'ponmApp',
    'ponmApp.services',
    'ponmApp.directives'
])
    .config(function () {
    })
    .run(['$log', function($log) {
        $log.info("test app runing");
    }])
    .controller('TravelPickerCtrl', ['$scope', '$http', '$log',
        function($scope, $http, $log) {

            $scope.travels = ["无锡之旅",
                "南京行",
                "北京行"];

            $scope.$watch('atravel2', function(newValue) {
                $log.debug("a travel 2: " + newValue);
            })

            $scope.loadTravelData = function() {
                $log.debug("load data");
                return $scope.travels;
            }

            $scope.newTravelData = function(newObj) {
                $log.debug("new data: " + newObj);
                $scope.travels.push(newObj);
                return $scope.travels ;
            }

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
                return $scope.items;
            }

            $scope.newItemObj = function(newObj) {
                $log.debug("new data: " + newObj);
                $scope.items.push(newObj);
                return $scope.items;
            }

            $scope.$watch('atravel3', function(newValue) {
                $log.debug("a travel 3: ");
                $log.debug(newValue);
            })
    }])

;