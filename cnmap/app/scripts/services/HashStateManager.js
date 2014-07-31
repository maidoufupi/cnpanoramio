/**
 * Created by any on 2014/7/30.
 */
'use strict';

angular.module('ponmApp.services')
    .factory('HashStateManager', ['$window', '$timeout', 'jsUtils',
        function ($window, $timeout, jsUtils) {

            var HashStateManager = function(scope, location) {
                this.scope = scope;
                this.location = location;
                this.scope.hashStateObj = {};
                this.hashStateCache = {};

                var that = this;
                scope.$watch(function () {
                    return location.hash();
                }, function (hash) {
                    that.scope.hashStateObj = jsUtils.deparam(hash);
                });
            };

            HashStateManager.prototype.watch = function(state, callback) {
                var states = state.split(" ");
                var that = this;
                angular.forEach(states, function(state, key) {
                    that.scope.$watch("hashStateObj."+state, function (value) {
                        if(that.hashStateCache[state] != value) {
                            callback.apply(null, [value]);
                            that.hashStateCache[state] = value;
                        }
                    });
                });
            };

            HashStateManager.prototype.bindingWatch = function(state, callback) {
                var states = state.split(" ");
                var that = this;
                var timeoutHander;
                angular.forEach(states, function(state, key) {
                    that.scope.$watch("hashStateObj."+state, function (value) {
                        if(that.hashStateCache[state] != value) {
                            if(timeoutHander) {
                                $timeout.cancel(timeoutHander);
                            }
                            timeoutHander = $timeout(function () {
                                var params = [];
                                angular.forEach(states, function(state, key) {
                                    params.push(that.get(state));
                                });
                                callback.apply(null, params);
                            }, 0);

                            that.hashStateCache[state] = value;
                        }
                    });
                });
            };

            HashStateManager.prototype.get = function(state) {
                return this.scope.hashStateObj[state];
            };

            HashStateManager.prototype.set = function(state, value) {
                var that = this;
                if(angular.isObject(state)) {
                    angular.forEach(state, function(value, name) {
                        if(value) {
                            that.hashStateCache[name] = value;
                        }else {
                            delete that.hashStateCache[name];
                        }
                    });
                }else {
                    if(value) {
                        this.hashStateCache[state] = value;
                    }else {
                        delete this.hashStateCache[state];
                    }
                }

                this.location.hash(jsUtils.param(this.hashStateCache));
            };

            return HashStateManager;
        }])
;