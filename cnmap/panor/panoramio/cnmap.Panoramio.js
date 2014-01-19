/*!
 * jQuery Cookie Plugin v1.4.0
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2013 Klaus Hartl
 * Released under the MIT license
 */
/**
 * baidu
 */
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as anonymous module.
        define(['jquery'], factory);
    } else {
        // Browser globals.
        factory(jQuery);
    }
}(function ($) {

// 全局命名空间
    $.cnmap = $.cnmap || {};

    $.cnmap.Panoramio = function (opts/*?:PanoramioOptions*/) {
        var url = ctx + '/services/api/';
        var client = new $.RestClient(url, {
            stringifyData: true
        });
        client.add('photos');
        client.add('panoramiothumbnail');

        var photos = {};

        this.getBoundsThumbnails = function (bounds/*:LatLngBounds*/, zoomLevel, size/*:Size*/, callback) {
            var clearVisible = this.clearVisible,
                setVisible = this.setVisible;
            var thumbnails;
            client.panoramiothumbnail.create({
                boundNELat: bounds.ne.lat,
                boundNELng: bounds.ne.lng,
                boundSWLat: bounds.sw.lat,
                boundSWLng: bounds.sw.lng,
                zoomLevel : zoomLevel,
                width: size.width,
                height: size.height
            }).done(function (data) {
                    for (var i in thumbnails) {
                        if (!photos[thumbnails[i].photoId]) {
                            photos[thumbnails[i].photoId] = thumbnails[i];
                        }
                    }
                    callback.apply(null, [data.thumbnails]);
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
            return "<img src='" + ctx + "/services/api/photos/" + photoId + "/3' style='width: 34px; height: 34px;'>";
        }

        this.getInfoWindowContent = function(photoId) {
            return "<a href='" + ctx + "/photo/" + photoId +"'><img src='" + ctx + "/services/api/photos/" + photoId + "/2' style='width: 180px; height: 180px;'></a>";
        }
    };

//    $.cnmap.PanoramioOptions = {
//        clickable : boolean, // If true, the layer receives mouse events. Default value is true.
//        map : Map, // The map on which to display the layer.
//        suppressInfoWindows : boolean, //Suppress the rendering of info windows when layer features are clicked.
//        tag : string, // A panoramio tag used to filter the photos which are displayed. Only photos which have been tagged with the supplied string will be shown.
//        userId : string, // A Panoramio user ID. If provided, only photos by this user will be displayed on the map. If both a tag and user ID are provided, the tag will take precedence.
//    };

//    $.cnmap.LatLngBounds = {
//        sw?:LatLng,
//        ne?:LatLng
//    }

//    $.cnmap.Size = {
//        width:number,
//        height:number,
//        widthUnit?:string,
//        heightUnit?:string
//    }

//    $.cnmap.LatLng = {
//        lat:number,
//        lng:number,
//        noWrap?:boolean
//    }
}));