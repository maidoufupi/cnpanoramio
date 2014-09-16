/**
 * @ngdoc overview
 * @name ponmApp.dynamic
 *
 * @description
 * user's dynamic message page
 *
 */
angular.module('ponmApp.dynamic', [
    'ui.bootstrap',
    'ngAnimate',
    'ui.router',
    'ponmApp',
    'xeditable',
    'ngDragDrop',
    'perfect_scrollbar'
])
    .config([   '$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {

            // Use $urlRouterProvider to configure any redirects (when) and invalid urls (otherwise).
            $urlRouterProvider

                // The `when` method says if the url is ever the 1st param, then redirect to the 2nd param
                // Here we are just setting up some convenience urls.
                .when('/dynamic', '/dynamic/my')

                // If the url is ever invalid, e.g. '/asdf', then redirect to '/' aka the home state
//                .otherwise('/popular')
            ;
            //////////////////////////
            // State Configurations //
            //////////////////////////

            // Use $stateProvider to configure your states.
            $stateProvider

                //////////
                // Home //
                //////////
                .state('dynamic', {

                    // With abstract set to true, that means this state can not be explicitly activated.
                    // It can only be implicitly activated by activating one of it's children.
                    abstract: true,

                    // This abstract state will prepend '/contacts' onto the urls of all its children.
                    url: '/dynamic',

                    views: {

                        // the main template will be placed here (relatively named)
                        '': { templateUrl: 'views/dynamic.html',
                            controller: 'DynamicCtrl'},
                        'navbar': {
                            templateUrl: 'views/ponm.navbar.html',
                            controller: 'NavbarCtrl'
                        }
                        ,'alert': {
                            templateUrl: 'views/ponm.alert.html',
                            controller: 'AlertsCtrl'
                        }
                    }
                })
                .state('dynamic.my', {
                    url: '/my',
                    templateUrl: 'views/dynamic.my.html',
                    resolve: {
                    },
                    controller: "DynamicMyCtrl"
                })
                .state('dynamic.popular', {
                    url: '/popular',
                    templateUrl: 'views/dynamic.popular.html',
                    resolve: {
                    },
                    controller: "DynamicPopularCtrl"
                })
                ;
        }])
    .run(['$rootScope', '$window', 'editableOptions',
        function ($rootScope, $window, editableOptions) {
            editableOptions.theme = 'bs3';
    }])
    .factory('MessageManager', ['$q', 'UserService', function ($q, UserService) {

        function MessageManager(userId) {

            this.userId = userId;

            this.pageSize = 20;
            this.pageNo = 1;
            this.ended = false;

            this.getMoreMessages = function() {
                var deferred = $q.defer();
                if(this.loading) {
                    deferred.reject("loading");
                }
                if(this.ended) {
                    deferred.reject("ended");
                }
                this.loading = true;
                var that = this;
                UserService.getMessages({userId: this.userId, value: this.pageSize, action: this.pageNo},
                    function(res) {
                        that.loading = false;
//                    $log.debug(res);
                        if(res.status == "OK") {
                            that.pageNo++;
                            if(res.messages.length < that.pageSize) {
                                that.ended = true;
                            }
                            deferred.resolve(res.messages);
                        }else {
                            deferred.reject(res.status);
                        }
                    }, function(error) {
                        that.loading = false;
                        deferred.reject(error);
                    });
                return deferred.promise;
            }
        }

        return MessageManager;
    }])
    .controller('DynamicCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q',
        '$modal', 'ponmCtxConfig', '$log', '$state', '$stateParams', '$timeout', 'safeApply', 'jsUtils',
        'HashStateManager',
        function ($window, $location, $rootScope, $scope, $q,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils,
                  HashStateManager) {

            $scope.ctx = ponmCtxConfig.ctx;
            $scope.staticCtx = ponmCtxConfig.staticCtx;
            $scope.apirest = ponmCtxConfig.apirest;
            $scope.userId = ponmCtxConfig.userId;
            $scope.login = ponmCtxConfig.login;
            $scope.ponmCtxConfig = ponmCtxConfig;

            $scope.displayPhoto = function(photoId) {

                $scope.photoDisplayModal = $modal.open({
                    templateUrl: 'views/photo.html',
                    controller: 'PhotoModalCtrl',
                    windowClass: 'photo-modal-fullscreen',
                    resolve: {
                        photoId: function () {
                            return photoId;
                        },
                        travelId: function() {
                            return '';
                        }
                    }
                });

                $scope.photoDisplayModal.result.then(function (selectedItem) {
                    $scope.photoDisplayModal = null;
                }, function () {
                    $scope.photoDisplayModal = null;
                });
            };

            $scope.$on('photo.click', function(e, photoId) {
                $scope.displayPhoto(photoId);
            });
        }])
    .controller('DynamicMyCtrl',
    [        '$log', '$location', '$rootScope', '$scope', '$q', '$modal', 'ponmCtxConfig', '$state', '$stateParams',
        '$timeout', 'safeApply', 'jsUtils', 'MessageManager',
        function ($log, $location, $rootScope, $scope, $q, $modal, ponmCtxConfig, $state, $stateParams,
                  $timeout, safeApply, jsUtils, MessageManager) {

            $scope.$watch(function() {
                return ponmCtxConfig.userId;
            }, function(userId) {
                if(userId) {
                    $scope.userId = userId;
                    $scope.messages = [];
                    $scope.messageManager = new MessageManager($scope.userId);
                    $scope.onLoadWaypoint();
                }
            });

            $scope.onLoadWaypoint = function(element, direction) {

                if($scope.messageManager) {
                    $scope.messageManager.getMoreMessages().then(function(messages) {
                        $scope.$broadcast("message.add", messages);
                    });

                }
            };
        }])
    .controller('DynamicPopularCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q',
        '$modal', 'ponmCtxConfig', '$log', '$state', '$stateParams', '$timeout', 'safeApply', 'jsUtils',
        'UserService',
        function ($window, $location, $rootScope, $scope, $q,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils,
                  UserService) {

            $scope.userId = ponmCtxConfig.userId;

            function getMessages(userId) {
                UserService.getMessages({userId: userId, value: 10, action: 1}, function(res) {
                    $log.debug(res);
                    if(res.status == "OK") {
                        $scope.messages = res.messages;
                    }
                });
            }

            if($scope.userId) {
//                getMessages($scope.userId);
            }

            $scope.messages = [];

            $scope.message = {
                "user": {
                    "id": 5,
                    "name": "暗梅幽闻花",
                    "avatar": 3,
                    "tags": [],
                    "travels": []
                },
                "type": "photo",
                "photo": {
                    "id": 2,
                    "point": {
                        "lat": 35,
                        "lng": 116,
                        "alt": 0
                    },
                    "vendor": "gaode",
                    "is360": false,
                    "tags": [],
                    "create_time": 1388509261000,
                    "user_id": 1,
                    "like_count": 0,
                    "fav_count": 0,
                    "comment_count": 1,
                    "file_name": "filename2.jpg",
                    "oss_key": "2.jpg",
                    "description": "日本平价超市玉出超市歧视中国货呀，同样的中国进口蔬菜只卖日本当地蔬菜25%～50%的价格，怒而支持中国货。#为洗白汉奸罪名努力﻿"
                },
                "travel": null,
                "content": "日本平价超市玉出超市歧视中国货呀，同样的中国进口蔬菜只卖日本当地蔬菜25%～50%的价格，怒而支持中国货。#为洗白汉奸罪名努力﻿",
                "tags": ["#中国", "#当地蔬菜同样", "#歧视"],
                "comment_count": 1,
                "comments": [
                    {
                        "id": 5,
                        "username": "卧似透春绿",
                        "content": "comment content 5",
                        "user_id": 2,
                        "user_avatar": 1,
                        "photo_id": 2,
                        "like_count": 0
                    }
                    ,{
                        "id": 6,
                        "username": "卧似透春绿",
                        "content": "comment content 5",
                        "user_id": 2,
                        "user_avatar": 1,
                        "photo_id": 2,
                        "like_count": 0
                    }
                    ,{
                        "id": 7,
                        "username": "卧似透春绿",
                        "content": "comment content 5",
                        "user_id": 2,
                        "user_avatar": 1,
                        "photo_id": 2,
                        "like_count": 0
                    }
                    ,{
                        "id": 8,
                        "username": "卧似透春绿",
                        "content": "comment content 5",
                        "user_id": 2,
                        "user_avatar": 1,
                        "photo_id": 2,
                        "like_count": 0
                    }
                ]
            };

            $scope.travelMessage = {
                "id": 2,
                "user": {
                    "id": 1,
                    "name": "暗梅幽闻花",
                    "avatar": 1,
                    "tags": [],
                    "travels": []
                },
                "type": "travel",
                "travel": {
                    "id": 1,
                    "username": "暗梅幽闻花",
                    "user": {
                        "id": 1,
                        "name": "暗梅幽闻花",
                        "avatar": 1,
                        "tags": [],
                        "travels": []
                    },
                    "spots": [
                        {
                            "id": 1,
                            "photos": [
                                {
                                    "id": 20,
                                    "description": "环境的人员落户问题\n。不断提高高校毕业生、技术工人、职业院校毕业生、留学\n回国人员等常住人口的城",
                                    "point": {
                                        "lat": 31.271875981148757,
                                        "lng": 121.22485131566845,
                                        "alt": 0,
                                        "address": "上海市嘉定区安亭镇新黄路4号"
                                    },
                                    "vendor": "gaode",
                                    "is360": false,
                                    "tags": [],
                                    "create_time": 1406690700000,
                                    "user_id": 1,
                                    "travel_id": 1,
                                    "travel_name": "无锡执行",
                                    "like_count": 0,
                                    "fav_count": 0,
                                    "comment_count": 0,
                                    "file_size": 147239,
                                    "file_name": "1280x800_174948801.jpg",
                                    "oss_key": "20.jpg"
                                },
                                {
                                    "id": 19,
                                    "description": "环境的人员落户问题。不断提高高校毕业生、技术工人、职业院校毕业生、留学回国人员等常住人口的城",
                                    "point": {
                                        "lat": 31.133046283915075,
                                        "lng": 121.55972960604899,
                                        "alt": 0,
                                        "address": "上海市浦东新区康桥镇秀沿路216号"
                                    },
                                    "vendor": "gaode",
                                    "is360": false,
                                    "tags": [],
                                    "create_time": 1406690700000,
                                    "user_id": 1,
                                    "travel_id": 1,
                                    "travel_name": "无锡执行",
                                    "like_count": 0,
                                    "fav_count": 0,
                                    "comment_count": 0,
                                    "file_size": 192250,
                                    "file_name": "7c1ed21b0ef41bd5e8024d4e51da81cb39db3d32.jpg",
                                    "oss_key": "19.jpg"
                                },
                                {
                                    "id": 18,
                                    "description": "意见要求，有效解决户口迁移中的重点问题。认真落实优先解决存量的要求，重点解决进城时间长、就业能力强、可以适",
                                    "point": {
                                        "lat": 31.078034825814516,
                                        "lng": 121.35820231534143,
                                        "alt": 0,
                                        "address": "上海市松江区新桥镇春申站"
                                    },
                                    "vendor": "gaode",
                                    "is360": false,
                                    "tags": [],
                                    "create_time": 1406690700000,
                                    "user_id": 1,
                                    "travel_id": 1,
                                    "travel_name": "无锡执行",
                                    "like_count": 0,
                                    "fav_count": 0,
                                    "comment_count": 0,
                                    "file_size": 817129,
                                    "file_name": "7.jpg",
                                    "oss_key": "18.jpg"
                                },
                                {
                                    "id": 17,
                                    "description": "意见要求，建立与统一城乡户口登记制度相适应的教育、卫生计生、就业、社保、住房、土地及人口统计制度。",
                                    "point": {
                                        "lat": 31.177952658241825,
                                        "lng": 121.47486773742938,
                                        "alt": 0,
                                        "address": "上海市浦东新区上钢新村街道后滩公园"
                                    },
                                    "vendor": "gaode",
                                    "is360": false,
                                    "tags": [],
                                    "create_time": 1406690700000,
                                    "user_id": 1,
                                    "travel_id": 1,
                                    "travel_name": "无锡执行",
                                    "like_count": 0,
                                    "fav_count": 0,
                                    "comment_count": 0,
                                    "file_size": 793901,
                                    "file_name": "5.jpg",
                                    "oss_key": "17.jpg"
                                },
                                {
                                    "id": 21,
                                    "description": "群众来信向中纪委反映党中央主席华国锋的三件事：\n一是华国锋去江苏视察，外出沿途搞戒严，影响交通，",
                                    "point": {
                                        "lat": 31.067125680105704,
                                        "lng": 121.62285129518345,
                                        "alt": 0,
                                        "address": "上海市浦东新区航头镇航头镇王楼村文化活动中心"
                                    },
                                    "vendor": "gaode",
                                    "is360": false,
                                    "tags": [],
                                    "create_time": 1406690700000,
                                    "user_id": 1,
                                    "travel_id": 1,
                                    "travel_name": "无锡执行",
                                    "like_count": 0,
                                    "fav_count": 0,
                                    "comment_count": 0,
                                    "file_size": 58388,
                                    "file_name": "1280x800_235523352.jpg",
                                    "oss_key": "21.jpg"
                                },
                                {
                                    "id": 16,
                                    "title": "美丽湖泊",
                                    "description": "口与非农业户口\n性质区分和由此衍生的蓝印户口等户口类型，\n统一登记为居民户口，体现户籍制度的人口登记管理功能。",
                                    "point": {
                                        "lat": 31.269543179119353,
                                        "lng": 121.49404005045348,
                                        "alt": 0,
                                        "address": "上海市虹口区欧阳路街道祥德路274弄小区"
                                    },
                                    "vendor": "gaode",
                                    "is360": true,
                                    "tags": [],
                                    "create_time": 1406690700000,
                                    "user_id": 1,
                                    "travel_id": 1,
                                    "travel_name": "无锡执行",
                                    "like_count": 0,
                                    "fav_count": 0,
                                    "comment_count": 2,
                                    "file_size": 448201,
                                    "file_name": "2.jpg",
                                    "oss_key": "16.jpg"
                                }
                            ],
                            "address": "云南省大理白族自治州大理市洱海管理处云洱渔家",
                            "title": "大理洱海",
                            "description": "大理洱海鱼鹰\n捕鱼表演\n十分好看",
                            "travel_id": 1
                        },
                        {
                            "id": 2,
                            "photos": [],
                            "travel_id": 1
                        }
                    ],
                    "title": "无锡执行",
                    "user_id": 1,
                    "album_cover": "1.jpg",
                    "photo_size": 6
                },
                "tags": [],
                "comments": []
            };

            $scope.messages.push(angular.copy($scope.travelMessage));
//            $scope.messages.push(angular.copy($scope.message));
//            $scope.messages.push(angular.copy($scope.message));
//            $scope.messages.push(angular.copy($scope.message));
//            $scope.messages.push(angular.copy($scope.message));
//            $scope.messages.push(angular.copy($scope.message));

        }])
    .controller('DynamicMessageCtrl',
    [        '$window', '$location', '$rootScope', '$scope', '$q',
        '$modal', 'ponmCtxConfig', '$log', '$state', '$stateParams', '$timeout', 'safeApply', 'jsUtils',
        'HashStateManager',
        function ($window, $location, $rootScope, $scope, $q,
                  $modal, ponmCtxConfig, $log, $state, $stateParams, $timeout, safeApply, jsUtils,
                  HashStateManager) {

            $scope.comment = {
                pageSize: 10,
                totalItems: 0,
                numPages: 0,
                currentPage: 1,
                maxSize: 10
            };

//            $scope.message = {
//                "user": {
//                    "id": 1,
//                    "name": "暗梅幽闻花",
//                    "avatar": 1,
//                    "tags": [],
//                    "travels": []
//                },
//                "type": "photo",
//                "photo": {
//                    "id": 2,
//                    "point": {
//                        "lat": 35,
//                        "lng": 116,
//                        "alt": 0
//                    },
//                    "vendor": "gaode",
//                    "is360": false,
//                    "tags": [],
//                    "create_time": 1388509261000,
//                    "user_id": 1,
//                    "like_count": 0,
//                    "fav_count": 0,
//                    "comment_count": 1,
//                    "file_name": "filename2.jpg",
//                    "oss_key": "2.jpg",
//                    "description": "日本平价超市玉出超市歧视中国货呀，同样的中国进口蔬菜只卖日本当地蔬菜25%～50%的价格，怒而支持中国货。#为洗白汉奸罪名努力﻿"
//                },
//                "travel": null,
//                "content": "日本平价超市玉出超市歧视中国货呀，同样的中国进口蔬菜只卖日本当地蔬菜25%～50%的价格，怒而支持中国货。#为洗白汉奸罪名努力﻿",
//                "tags": ["#中国", "#当地蔬菜同样", "#歧视"],
//                "comment_count": 1,
//                "comments": [
//                    {
//                        "id": 5,
//                        "username": "卧似透春绿",
//                        "content": "comment content 5",
//                        "user_id": 2,
//                        "user_avatar": 1,
//                        "photo_id": 2,
//                        "like_count": 0
//                    }
//                    ,{
//                        "id": 6,
//                        "username": "卧似透春绿",
//                        "content": "comment content 5",
//                        "user_id": 2,
//                        "user_avatar": 1,
//                        "photo_id": 2,
//                        "like_count": 0
//                    }
//                    ,{
//                        "id": 7,
//                        "username": "卧似透春绿",
//                        "content": "comment content 5",
//                        "user_id": 2,
//                        "user_avatar": 1,
//                        "photo_id": 2,
//                        "like_count": 0
//                    }
//                    ,{
//                        "id": 8,
//                        "username": "卧似透春绿",
//                        "content": "comment content 5",
//                        "user_id": 2,
//                        "user_avatar": 1,
//                        "photo_id": 2,
//                        "like_count": 0
//                    }
//                ]
//            };



        }])
;