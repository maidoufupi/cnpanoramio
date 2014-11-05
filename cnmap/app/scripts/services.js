/**
 * Created by any on 2014/4/26.
 */
'use strict';

angular.module('ponmApp.services', [
    'ngCookies',
    'ngResource'])
    .factory('AuthService', ['$window', '$resource', '$cookies', '$log', '$q', 'ponmCtxConfig', 'SettingsService',
    function ($window, $resource, $cookies, $log, $q, ponmCtxConfig, SettingsService) {

        var loginService = $resource(ponmCtxConfig.apirest + '/auth/login/:type'),
            loginPost = $resource(ponmCtxConfig.ctx+'/login/j_spring_security_check');

        function loginCheck(user) {
            var deferred = $q.defer();
            loginService.save({type: 'check'}, {username: user.username, password: user.password},
                function(res) {
                    if(res.status == "OK") {
                        deferred.resolve();
                    }
                },
                function(error) {
                    $log.debug(error.data);
                    deferred.reject(1);
                }
            );
            return deferred.promise;
        }

        function login(user) {
            var deferred = $q.defer();
            loginPost.save({username: user.username, password: user.password});
//            loginService.save({username: user.username, password: user.password},
//                function(res) {
//                    $log.debug(res);
//                    if(res.status == "OK") {
//                        $log.debug("login success")
//                        ponmCtxConfig.userId = res.user.id;
//                        ponmCtxConfig.username = res.user.username;
//                        ponmCtxConfig.name = res.user.name;
//                        ponmCtxConfig.avatar = res.user.avatar;
//                        ponmCtxConfig.login = true;
//
//                        $log.debug($cookies.JSESSIONID);
//                        $log.debug($cookies);
//                        deferred.resolve();
//                    }else {
//                        deferred.reject(1);
//                    }
//                });
            return deferred.promise;
        }

        function checkLogin() {
            var deferred = $q.defer();
            if(ponmCtxConfig.login && ponmCtxConfig.userId) {
                deferred.resolve();
            }else {
                loginService.get({}, function(res) {
                    if(res.status == "OK") {
                        ponmCtxConfig.userId = res.user.id;
                        ponmCtxConfig.id = res.user.id;
                        ponmCtxConfig.username = res.user.username;
                        ponmCtxConfig.name = res.user.name;
                        ponmCtxConfig.avatar = res.user.avatar;
                        ponmCtxConfig.login = true;

                        $log.debug(res.user);
                        deferred.resolve();
                        SettingsService.get({userId: ponmCtxConfig.userId}, function(res) {
                            if(res.status == "OK") {
                                ponmCtxConfig.settings.autoUpload = res.settings.auto_upload;
                            }

                        });
                    }else if(res.status == "NO_AUTHORIZE") {
                        deferred.reject(1);
                    }
                });
            }

            return deferred.promise;
        }

        return {
            loginCheck: loginCheck,
            login: login,
            checkLogin: checkLogin
        };
    }])
    .factory('CommentService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/comment/:commentId/:type',
            {commentId: "@id"},
            {
                modify: {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                like: {
                    method: 'GET',
                    params: {'type': 'like'}
                },
                unLike: {
                    method: 'DELETE',
                    params: {'type': 'like'}
                }
            });
    }])
    .factory('UserPhoto', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/user/:userId/photos/:type/:tag/:pageSize/:pageNo',
            {'userId': "@id"}, {
                query: {
                    method: 'GET',
                    isArray: true
                },
                getByTag: {
                    method: 'GET',
                    params: {
                        'type': 'tag'
                    }
                },
                getBounds: {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                }
            });
    }])
    .factory('UserService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/user/:userId/:type/:value/:action',
            {'userId': "@id"}, {
                getMe: {
                    method: 'GET'
                },
                getOpenInfo: {
                    method: 'GET',
                    params: {'type': 'openinfo'}
                },
                getSettings: {
                    method: 'GET',
                    params: {'type': 'settings'}
                },
                updateSettings: {
                    method: 'POST',
                    params: {'type': 'settings'}
                },
                // 所有旅行
                getTravels: {
                    method: 'GET',
                    params: {'type': 'travel'}
                },
                // 所有标签
                getTags: {
                    method: 'GET',
                    params: {'type': 'tag'}
                },
                // 创建标签
                createTag: {
                    method: 'GET',
                    params: {'type': 'tag'}
                },
                // 回收站
                getRecycleBin: {
                    method: 'GET',
                    params: {'type': 'recycle'}
                },
                // 清空回收站
                emptyRecycleBin: {
                    method: 'DELETE',
                    params: {'type': 'recycle'}
                },
                // 彻底删除
                removeRecycle: {
                    method: 'DELETE',
                    params: {'type': 'recycle'}
                },
                // 取消删除
                cancelRecycle: {
                    method: 'GET',
                    params: {'type': 'recycle', action: 'cancel'}
                },
                // 动态列表
                getMessages: {
                    method: 'GET',
                    params: {'type': 'messages'}
                },
                // 自己的消息
                getMyMessages: {
                    method: 'GET',
                    params: {'type': 'mymessages'}
                }
                // 跟随者
                ,getFollowers: {
                    method: 'GET',
                    params: {'type': 'follower'}
                }
                // 关注
                ,following: {
                    method: 'POST',
                    params: {'type': 'following'}
                }
                // 取消关注
                ,unFollowing: {
                    method: 'DELETE',
                    params: {'type': 'following'}
                }
                // 所有圈子
                ,getCircles: {
                    method: 'GET',
                    params: {'type': 'circle'}
                }
                // 推荐关注
                ,getFollowSuggested: {
                    method: 'GET',
                    params: {'type': 'follow', 'value': 'suggested'}
                }
                // 可能感兴趣的
                ,getFollowInterested: {
                    method: 'GET',
                    params: {'type': 'follow', 'value': 'interested'}
                }
            });
    }])
    .factory('SettingsService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/settings/:userId/:type',
            {'userId': "@id"},
            {
                changeMapVendor: {
                    method: 'POST',
                    params: {'type': 'map'}
                },
                changePassword: {
                    method: 'POST',
                    params: {'type': 'password'}
                },
                changeAccount: {
                    method: 'POST',
                    params: {'type': 'account'}
                }
                ,changeUpload: {
                    method: 'POST',
                    params: {'type': 'upload'}
                }
            });
    }])
    .factory('PhotoService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/photo/:photoId/:type/:pageSize/:pageNo',
            {'photoId': "@id"},
            {
                getCameraInfo: {
                    method: 'GET',
                    params: {'type': 'camerainfo'}
                },
                delete: {
                    method: 'DELETE'
                },
                tag: {
                    method: 'POST',
                    params: {'type': 'tag'}
                },
                updateProperties: {
                    method: 'POST',
                    params: {'type': 'properties'}
                },
                getPhoto: {
                    method: 'GET'
                },
                getGPSInfo: {
                    method: 'GET',
                    params: {'type': 'gps'}
                },
                favorite: {
                    method: 'PUT',
                    params: {'type': 'favorite'}
                },
                removeFavorite: {
                    method: 'DELETE',
                    params: {'type': 'favorite'}
                }
                ,getComments: {
                    method: 'GET',
                    params: {'type': 'comment'}
                },
                like: {
                    method: 'GET',
                    params: {'type': 'like'}
                },
                unLike: {
                    method: 'DELETE',
                    params: {'type': 'like'}
                }
            });
    }])
    .service('GPSConvertService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/gps', {},
            {
                convert: {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                }
            });
    }])
    .service('AvatarService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/user/avatar', {},
            {
                upload: {
                    method: 'POST',
                    headers: {
                        'Content-Type': false,
                        'Accept': 'application/json'
                    }
                }
            });
    }])
    .factory('TravelService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/travel/:travelId/:type/:typeId/:action',
            {'travelId': '@id'},
            {
                create: {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                addPhoto: {
                    method: 'POST',
                    params: {'type': 'photo'},
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                getTravel: {
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json'
                    }
                },
                changeTravel: {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    }
                },
                getSpot: {
                    method: 'GET',
                    params: {'type': 'spot'}
                },
                changeSpot: {
                    method: 'POST',
                    params: {'type': 'spot'},
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    }
                },
                createSpot: {
                    method: 'POST',
                    params: {
                        'type': 'spot'
                    }
                },
                addSpotPhoto: {
                    method: 'POST',
                    params: {
                        'type': 'spot',
                        'action': 'photo'
                    },
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                changeSpotPhotoPosition: {
                    method: 'POST',
                    params: {
                        'type': 'spot',
                        'action': 'position'
                    }
                }
                ,deleteSpot: {
                    method: 'DELETE',
                    params: {
                        'type': 'spot'
                    }
                },
                deletePhoto: {
                    method: 'DELETE',
                    params: {'type': 'photo'},
                    headers: {
                        'Accept': 'application/json'
                    }
                },
                like: {
                    method: 'GET',
                    params: {'type': 'like'}
                },
                unLike: {
                    method: 'DELETE',
                    params: {'type': 'like'}
                }
            });
    }])
    .factory('PanoramioService', ['$window', '$resource', function ($window, $resource) {
        return $resource($window.apirest + '/panoramio/:action',
            {'id': '@id'},
            {
                getLatest: {
                    method: 'GET',
                    params: {
                        action: 'photo',
                        latest: true
                    },
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                },
                search: {
                    method: 'GET',
                    params: {
                        action: 'search',
                        type: 'all'
                    },
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        'Accept': 'application/json'
                    }
                }
            });
    }])
    .factory('MessageService', ['ponmCtxConfig', '$resource', function (ponmCtxConfig, $resource) {
        return $resource(ponmCtxConfig.apirest + '/message/:id/:type/:value',
            {'id': '@id'},
            {
                like: {
                    method: 'GET',
                    params: {
                        type: 'like'
                    }
                }
                ,unLike: {
                    method: 'DELETE',
                    params: {
                        type: 'like'
                    }
                }
                ,share: {
                    method: 'POST',
                    params: {
                        type: 'share'
                    }
                }
            });
    }])
    .factory('RecycleService', ['ponmCtxConfig', '$resource', function (ponmCtxConfig, $resource) {
        return $resource(ponmCtxConfig.apirest + '/recycle/:id/:type/:value',
            {'id': '@id'},
            {
                cancel: {
                    method: 'GET',
                    params: {
                        type: 'cancel'
                    }
                }
            });
    }])

    .service('LoginUserService', ['$window', '$resource', '$rootScope',
        function ($window, $resource, $rootScope) {
            this.getUserId = function() {
                return $rootScope.user.id;
            };
        }])
    .factory('safeApply', [function($rootScope) {
        return function($scope, fn) {
            var phase = $scope.$root && $scope.$root.$$phase;
            if(phase == '$apply' || phase == '$digest') {
                if (fn) {
                    $scope.$eval(fn);
                }
            } else {
                if (fn) {
                    $scope.$apply(fn);
                } else {
                    $scope.$apply();
                }
            }
        }
    }])
    .factory('ponmCtxConfig', ['$window', '$resource', function ($window, $resource) {
        return {
            ctx: $window.ctx,
            staticCtx: $window.staticCtx || "http://static.photoshows.cn",
            corsproxyCtx: $window.corsproxyCtx || "http://www.corsproxy.com/static.photoshows.cn",
            apirest: $window.apirest
            ,userId: $window.user && $window.user.id
            ,name: $window.user && $window.user.name
            ,login: $window.login
            ,mapVendor: $window.mapVendor
            // Coordinate System Standards 坐标体系标准
            ,getCoordSS: function(mapVendor) {
                if(!mapVendor) {
                    mapVendor = this.mapVendor;
                }
                if(mapVendor == 'qq') {
                    return 'gaode';
                }
                return mapVendor;
            }
            ,dev: $window.dev
            ,settings: {
                autoUpload: false
            }
        }
    }])
;