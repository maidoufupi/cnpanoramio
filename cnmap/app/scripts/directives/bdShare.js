/**
 * Created by any on 2014/6/19.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('bdShare', ['$window', '$animate', '$log', 'param', '$q', '$sce', '$timeout',
        function ($window, $animate, $log, param, $q, $sce, $timeout) {
            return {
                restrict: 'A',
//                templateUrl: "views/bdShare.html",
                link: function (scope, element, attrs) {

                    $window._bd_share_config = {
                        "common": {
                            "bdSnsKey": {},
                            "bdText": "",
                            "bdMini": "2",
                            "bdMiniList": false,
                            "bdPic": "",
                            "bdStyle": "1",
                            "bdSize": "16"},
                        "share": {
                            "bdSize": 16},
                        "image": {
                            "viewList": ["qzone", "tsina", "tqq", "renren", "weixin"],
                            "viewText": "分享到：",
                            "viewSize": "16"}
//                        ,
//                        "slide":{
//                            "type":"slide",
//                            "bdImg":"2",
//                            "bdPos":"right",
//                            "bdTop":"100"}
                    };
                    scope.scriptSrc = "http://bdimg.share.baidu.com/static/api/js/share.js?cdnversion="+~(-new Date()/36e5);

                    document.getElementById("bdshell_js").src = scope.scriptSrc;

                    element.on("mouseenter", function(e) {
                        if(scope.mouseLeaveTimer) {
                            $timeout.cancel(scope.mouseLeaveTimer);
                        }
                        scope.$apply(function() {
                            scope.mouseEnter = true;
                        });
                    });
                    element.on("mouseleave", function(e) {
                        scope.mouseLeaveTimer = $timeout(function() {
                            scope.$apply(function () {
                                scope.mouseEnter = false;
                            });
                        }, 1000);
                    });
                }
            };
        }])
;