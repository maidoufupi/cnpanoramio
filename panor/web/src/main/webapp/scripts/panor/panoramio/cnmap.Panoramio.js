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

            client.photo.read({
                nelat: bounds.ne.lat,
                nelng: bounds.ne.lng,
                swlat: bounds.sw.lat,
                swlng: bounds.sw.lng,
                level : zoomLevel,
                vendor: (this.opts && this.opts.mapVendor) || "gps",
                width: size.width,
                height: size.height
            }).done(function (data) {
                    for (var i in data) {
                        if (!photos[data[i].photoId]) {
                            photos[data[i].photoId] = data[i];
                        }
                    }
                    callback.apply(null, [data]);
                });
        };

        this.getThumbnail = function (id/*PhotoId*/) {
            return client.photos.read(id);
        }

        this.clearVisible = function () {
            for (var photo in photos) {
                photo.visible = false;
            }
        }

        this.setVisible = function (photoId, visible) {
            if (!visible) {
                photos[photoId].visible = false;
            } else {
                photos[photoId].visible = true;
            }
        }

        this.getVisible = function (photoId) {
            return photos[photoId] ? photos[photoId].visible : false;
        }

        this.getLabelContent = function(photoId) {
            if(this.opts.suppressInfoWindows) {
                return "<img src='" + this.ctx + "/api/rest/photo/" + photoId
                    + "/3' style='border: 2px solid white; width: 34px; height: 34px;'>";
            }else {
                return "<a href='" + this.ctx + "/photo/" + photoId +"'><img src='" + this.ctx + "/api/rest/photo/"
                    + photoId + "/3' style='border: 2px solid white; width: 34px; height: 34px;'></a>";
            }
        }

        this.getInfoWindowContent = function(photoId) {
            return "<a href='" + this.ctx + "/photo/" + photoId +"'><img src='" + this.ctx + "/api/rest/photo/"
                + photoId + "/2' style='max-height: 200px; max-width: 200px;'></a>";
        }

        /**
         * init Environment
         */
        this.initEnv = function(ctx) {
            this.ctx = ctx;
            var url = ctx + '/api/rest/panoramio/';
            jQuery.support.cors = true;
            client = new jQuery.RestClient(url, {
                //stringifyData: true
            });
            client.add('photo');
        }
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