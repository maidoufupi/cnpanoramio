/*!
 * jQuery Cookie Plugin v1.4.0
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2013 Klaus Hartl
 * Released under the MIT license
 */
(function (factory) {

    if (typeof define === 'function' && define.amd) {
        // AMD. Register as anonymous module.
        define([window, 'jquery', 'cnmap'], factory);
    } else {
        // Browser globals.
        factory(window, jQuery);
    }

})(function (window, $) {

// 全局命名空间
    window.cnmap = window.cnmap || {};

    window.cnmap.Panoramio = function (opts/*?:PanoramioOptions*/) {

        // rest service client
        var client;

        var photos = {};

        this.opts = opts || {};

        this.getBoundsThumbnails = function (bounds/*:LatLngBounds*/, zoomLevel, size/*:Size*/, callback) {
            var clearVisible = this.clearVisible,
                setVisible = this.setVisible;
            var thumbnails;

            var params = {
                nelat: bounds.ne.lat,
                nelng: bounds.ne.lng,
                swlat: bounds.sw.lat,
                swlng: bounds.sw.lng,
                level : zoomLevel,
                vendor: (this.opts && this.opts.mapVendor) || "gps",
                width: size.width,
                height: size.height
            };

            if(!!this.opts.latest) {
                params.latest = true;
            }

            if(!!this.opts.userId) {
                params.userId = this.opts.userId;
                if(!!this.opts.favorite) {
                    params.favorite = !!this.opts.favorite;
                }
            }

            if(!!this.opts.tag) {
                params.tag = this.opts.tag;
            }

            client.photo.read(params).done(function (data) {
                if(data.status == "OK") {
                    for (var i in data.photos) {
                        if (!photos[data.photos[i].photoId]) {
                            photos[data.photos[i].photoId] = data.photos[i];
                        }
                    }
                    callback.apply(null, [data.photos]);
                }
                });
        };

//        this.getThumbnail = function (id/*PhotoId*/) {
//            return client.photos.read(id);
//        }

//        this.clearVisible = function () {
//            for (var photo in photos) {
//                photo.visible = false;
//            }
//        }

//        this.setVisible = function (photoId, visible) {
//            if (!visible) {
//                photos[photoId].visible = false;
//            } else {
//                photos[photoId].visible = true;
//            }
//        }

//        this.getVisible = function (photoId) {
//            return photos[photoId] ? photos[photoId].visible : false;
//        }

        this.getLabelContent = function(photoOssKey) {
            if(this.opts.phone) {
                return "<img src='" + this.staticCtx + "/"
                    + photoOssKey + "@!panor-lg' style='border: 2px solid white; width: 34px; height: 34px;'>";
            }else {
                return "<img src='" + this.staticCtx + "/" + photoOssKey
                    + "@!panor-lg' style='border: 2px solid white; width: 34px; height: 34px;'>";
            }

        };

        this.getInfoWindowContent = function(photo) {

            var date = new Date(photo.create_date),
                dates;
            dates = date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate();
            return '<div class="panoramio-info-window">' +
                        '<div class="header">' +
                        photo.address +
                        '</div>' +
                    '<div class="body">' +
                        '<a href="'+ this.ctx + '/photo/' + photo.photo_id +'">' +
                            '<img src="'+ this.ctx + '/api/rest/photo/' + photo.photo_id +'/2">' +
                        '</a>' +
                    '</div>' +
                    '<div class="media">' +
                        '<a class="pull-left" href="'+ this.ctx + '/user/' + photo.user_id +'">' +
                            '<img class="media-object" src="'+ this.ctx + '/api/rest/user/' + photo.user_id +'/avatar" alt="'
                                +photo.username+'">' +
                        '</a>' +
                        '<div class="media-body">' +
                            '<h4 class="media-heading">作者：<a href="'+ this.ctx + '/user/' + photo.user_id +'">'
                                +photo.username+'</a></h4>' +
                                dates +
                        '</div>' +
                    '</div>' +
                    '</div>';

//            return "<a href='" + this.ctx + "/photo/" + photoId +"'><img src='" + this.ctx + "/api/rest/photo/"
//                + photoId + "/2' style='max-height: 200px; max-width: 200px;'></a>";
        };

        /**
         * Renders the layer on the specified map. If map is set to null, the layer will be removed.
         *
         * @param map
         */
        this.setMap = function (map/*:Map*/) {
        };

        /**
         * init Environment
         */
        this.initEnv = function(ctx, staticCtx) {
            this.ctx = ctx;
            var url = ctx + '/api/rest/panoramio/';
            jQuery.support.cors = true;
            client = new jQuery.RestClient(url, {
                //stringifyData: true
            });
            client.add('photo');

            this.staticCtx = staticCtx || "";
        };

        /**
         * 设置用户ID
         * A user ID. If provided, only photos by this user will be displayed on the map.
         * If both a tag and user ID are provided, the tag will take precedence.
         *
         * @param userId
         */
        this.setUserId = function(userId) {
            this.opts.userId = userId;
        };

        this.getUserId = function() {
            return this.opts.userId;
        };

        /**
         * A tag used to filter the photos which are displayed.
         * Only photos which have been tagged with the supplied string will be shown.
         *
         * @param tag
         */
        this.setTag = function(tag) {
            this.opts.tag = tag;
        };

        this.getTag = function() {
            return this.opts.tag;
        };

        /**
         * 设置用户收藏标记，如果为true怎获取用户收藏的图片
         *
         * @param fav
         */
        this.setFavorite = function(fav) {
            this.opts.favorite = !!fav;
        };

        this.getFavorite = function() {
            return this.opts.favorite;
        };

        /**
         * 设置最新照片
         *
         * @param latest
         */
        this.setLatest = function(latest) {
            this.opts.latest = !!latest;
        };

        this.getLatest = function() {
            return this.opts.latest;
        };

        /**
         * 出发地图状态改变事件，使数据重新获取
         *
         * @param event
         */
        this.trigger = function(event) {
        };
    };

//    window.cnmap.PanoramioOptions = {
//        clickable : boolean, // If true, the layer receives mouse events. Default value is true.
//        map : Map, // The map on which to display the layer.
//        suppressInfoWindows : boolean, //Suppress the rendering of info windows when layer features are clicked.
//        tag : string, // A panoramio tag used to filter the photos which are displayed. Only photos which have been tagged with the supplied string will be shown.
//        userId : string, // A Panoramio user ID. If provided, only photos by this user will be displayed on the map. If both a tag and user ID are provided, the tag will take precedence.
//        mapVendor : string // map vendor, for example "gaode" "baidu" "gps" ...
//    };

//    window.cnmap.LatLngBounds = {
//        sw?:LatLng,
//        ne?:LatLng
//    }

//    window.cnmap.Size = {
//        width:number,
//        height:number,
//        widthUnit?:string,
//        heightUnit?:string
//    }

//    window.cnmap.LatLng = {
//        lat:number,
//        lng:number,
//        noWrap?:boolean
//    }

    return window.cnmap.Panoramio;
});