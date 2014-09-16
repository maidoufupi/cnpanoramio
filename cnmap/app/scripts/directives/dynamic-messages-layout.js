/**
 * Created by any on 2014/8/25.
 */
'use strict';

angular.module('ponmApp.directives')
.directive('dynamicMessagesLayout', ['$timeout', '$filter', '$log', '$q', 'ponmCtxConfig', 'jsUtils',
    function ($timeout, $filter, $log, $q, ponmCtxConfig, jsUtils) {

        var defaults = {
            type: "photo"
        };

        return {
            restrict: 'A',
            scope: {
//                messages: "=messages",
                columnWidth: "=columnWidth",
                columnSize: "=columnSize"
            },
            templateUrl: 'views/dynamic-messages.html',
            link: function (scope, element, attrs) {
                var opts, options;

                options = scope.$eval(attrs.ponmMessageLayout || "{}");

                opts = angular.extend({}, defaults, options);

                scope.owner = ponmCtxConfig;

                var elemCols;

//                scope.$watch("messages.length", function() {
//
//                });

                scope.cols = [{
                        messages: [],
                        height: 0
                    }
                    ,{
                        messages: [],
                        height: 0
                        }
                    ,{
                        messages: [],
                        height: 0
                        }
                    ,{
                        messages: [],
                        height: 0
                        }];

                scope.messages = [];
                scope.cursor = 0;
//                addMessage();
                function addMessage() {
                    if(!scope.messages) {
                        return ;
                    }
                    if(scope.cursor < scope.messages.length) {
                        var message = scope.messages[scope.cursor];
                        scope.cursor++;
                        if(message) {
                            if(!elemCols) {
                                elemCols = element.find(".messages-col");
                                switch(scope.columnSize) {
                                    case 4:
                                        elemCols.addClass("col-md-3");
                                        break;
                                    case 3:
                                        elemCols.addClass("col-md-4");
                                        break;
                                    case 2:
                                        elemCols.addClass("col-md-6");
                                        break;
                                    case 1:
                                        elemCols.addClass("col-md-12");
                                        break;
                                    default:
                                        if(scope.columnWidth) {
                                            elemCols.css("width", scope.columnWidth);
                                        }else {
                                            elemCols.addClass("col-md-6");
                                        }
                                }

                            }
                            angular.forEach(scope.cols, function(col, key) {
                                col.height = angular.element(elemCols[key]).outerHeight();
                            });

                            var cols = scope.cols.slice(0, scope.columnSize);
                            cols = $filter('orderBy')(cols, "height");

                            cols[0].messages.push(message);

                            scope.layoutHander = $timeout(function() {
                                addMessage();
                            }, 500);
                        }
                    }
                }

                scope.onWaypoint = function(element, direction) {
                    if(direction == "down") {
                        element.addClass("shown");
                    }
                };

                scope.$watch("columnSize", function(columnSize) {
                    if(scope.layoutHander) {
                        $timeout.cancel(scope.layoutHander);
                    }
                    if(columnSize > 0) {
                        scope.cursor = 0;
                        angular.forEach(scope.cols, function(col, key) {
                            col.messages = [];
                        });
                        elemCols = null;
                        addMessage();
                    }
                });

                scope.$on("message.insert", function(e, message) {
                    scope.cols[0].messages.splice(0, 0, message);
                    scope.messages.splice(0, 0, message);
                });

                scope.$on("message.add", function(e, messages) {
                    if(messages&&messages.length) {
                        scope.messages = scope.messages.concat(messages);
                        addMessage();
                    }
                });

                scope.$on("message.deleted", function(e, message) {
                    angular.forEach(scope.cols, function(col, key) {
                        jsUtils.Array.removeItem(col.messages, "id", message.id);
                    });
                    jsUtils.Array.removeItem(scope.messages, "id", message.id);
                });
            }
        };

    }]);