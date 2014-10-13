/**
 * Created by any on 2014/8/25.
 */
'use strict';

angular.module('ponmApp.directives')
.directive('dynamicMessagesLayout', ['$window', '$timeout', '$filter', '$log', '$q', 'ponmCtxConfig', 'jsUtils',
    function ($window, $timeout, $filter, $log, $q, ponmCtxConfig, jsUtils) {

        var defaults = {
            type: "photo",
            rules: {
                phone : 767,
                tablet : 979,
                desktop : 1200
            }
        };

        return {
            restrict: 'A',
            scope: {
                columnWidth: "=columnWidth",
                columnSize: "=columnSize"
            },
            templateUrl: 'views/dynamic-messages.html',
            link: function (scope, element, attrs) {
                var opts, options;

                options = scope.$eval(attrs.dynamicMessagesLayout || "{}");

                opts = angular.extend({}, defaults, options);

                scope.owner = ponmCtxConfig;

                var elemCols;

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
                                elemCols.removeClass("col-md-3");
                                elemCols.removeClass("col-md-4");
                                elemCols.removeClass("col-md-6");
                                elemCols.removeClass("col-md-12");
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

                scope.updateLayout = function(width) {
                    var layout;
                    if(width > opts.rules.phone) {
                        if(width > opts.rules.tablet) {
                            if(width > opts.rules.desktop) {

                            }else {
                                layout = "desktop";
                            }

                        }else {
                            layout = "tablet";
                        }

                    }else {
                        layout = "phone";
                    }

                    if(scope.layout != layout) {
                        scope.layout = layout;
                        if(layout == "phone") {
                            scope.columnSize = 1;
                        }else if(layout == "tablet") {
                            scope.columnSize = 2;
                        }else if(layout == "desktop") {
                            scope.columnSize = 3;
                        }else {
                            scope.columnSize = 4;
                        }
                        $log.debug("layout changed columnSize = " + scope.columnSize);
                        relayout(scope.columnSize);
                    }
                };

                function relayout(columnSize) {
                    if(scope.layoutHander) {
                        $timeout.cancel(scope.layoutHander);
                    }
                    if(columnSize > 0) {
                        $log.debug("layout changed layout");
                        scope.cursor = 0;
                        angular.forEach(scope.cols, function(col, key) {
                            col.messages = [];
                        });
                        elemCols = null;
                        addMessage();
                    }
                }

                scope.$watch("columnSize", function(columnSize) {
                    relayout(columnSize);
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

                scope.$watch(function () {
                    return element.innerWidth && element.innerWidth();
                }, function (width) {
                    $log.debug("container width changed");
                    scope.updateLayout(width);
                });

                angular.element($window).bind("resize", function (e) {
                    var width = element.innerWidth && element.innerWidth();
                    width && scope.updateLayout(width);
                });

            }
        };

    }]);