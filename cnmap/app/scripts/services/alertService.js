/**
 * Created by any on 2014/8/15.
 */
'use strict';

angular.module('ponmApp.services')
    .factory('alertService', ['$window', '$rootScope', '$timeout', 'jsUtils',
        function ($window, $rootScope, $timeout, jsUtils) {
            var alertService;
            return alertService = {
                options: {},
                add: function (type, message, options) {
                    var that = this;
                    var alert = {
                        type: type,
                        message: message,
                        close: function () {
                            return that.closeAlert(this);
                        }
                    };
                    // 只显示这一个消息，即清除之前残留的消息
                    if((options && options.alone) || this.options.alone) {
                        this.clear();
                    }
                    // 超时时间
                    var ttl = (options && options.ttl) || this.options.ttl;
                    if(ttl) {
                        alert.timeoutPromise = $timeout(function() {
                            alert.close();
                        }, ttl);
                    }
                    this.alerts.push(alert);
                    return alert;
                },
                closeAlert: function(alert) {
                    return this.closeAlertIdx(this.alerts.indexOf(alert));
                },
                closeAlertIdx: function(index) {
                    return this.alerts.splice(index, 1);
                },
                clear: function(){
                    angular.forEach(this.alerts, function(alert, key) {
                        $timeout.cancel(alert.timeoutPromise);
                    });
                    this.alerts = [];
                },
                alerts: []
            };
        }])
;