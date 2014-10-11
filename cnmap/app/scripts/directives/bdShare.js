/**
 * Created by any on 2014/6/19.
 */
'use strict';

angular.module('ponmApp.directives')
    .directive('bdShare', ['$window', '$animate', '$log', 'jsUtils', '$q', '$sce', '$location',
        function ($window, $animate, $log, jsUtils, $q, $sce, $location) {
            return {
                restrict: 'EA',
                scope: {
                    bdText: "@text",
                    bdDesc: "@desc",
                    bdComment: "@comment",
                    bdUrl: "@url",
                    bdPic: "@pic",
                    bdBind: "@bdBind"
                },
                templateUrl: "views/bdShare.html",
                link: function (scope, element, attrs) {

                    scope.$watch(function() {
                        return $location.absUrl();
                    }, function(url) {
                        if(url.indexOf("&")>0) {
                            scope.url = url+"&d";
                        }else {
                            scope.url = url;
                        }
                    });

                    $window._bd_share_config = {
                        "common": {
                            "bdSnsKey": {},
                            "bdMini": "2",
                            "bdMiniList": false,
                            "bdStyle": "1",
                            "bdSize": "16"},
                        "share": {
                            "bdSize": 16}
                    };

//                    scope.scriptSrc = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + new Date().getHours();

//                    document.getElementById("bdshell_js").src = scope.scriptSrc;
                    var s = document.createElement("script");
                    s.type="text/javascript";
                    s.src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + new Date().getHours();
                    document.getElementsByTagName("body")[0].appendChild(s);
                }
            };
        }])
;