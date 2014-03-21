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
        define([window, 'jquery', 'Panoramio'], factory);
    } else {
        // Browser globals.
        factory(window, jQuery);
    }

})(function (window, $) {

// 全局命名空间
    window.cnmap = window.cnmap || {};

    window.cnmap.PanoramioLayer = function (opts/*?:PanoramioLayerOptions*/) {

        //var panoramio = new window.cnmap.Panoramio();

        var labels = [];
        var thumbPhotoIds = [];
        var infoWindow = new AMap.InfoWindow({isCustom: false});

        this.preZoom = 0;
        this.preBounds = null;

        this.opts = $.extend( {clickable: true}, opts);

        if (this.opts.map) {
            this.setMap(this.opts.map);
        }

        /**
         *
         * @param map {Map}
         */
        this.setMap = function (map/*:Map*/) { //	None	Renders the layer on the specified map. If map is set to null, the layer will be removed.
            if (map) {
                this.opts.map = map;

                AMap.event.addListener(
                    map,
                    'complete',
                    getBoundsThumbnails
                );

                AMap.event.addListener(
                    map,
                    'zoomend',
                    function () {
                        if (map.getZoom() != this.preZoom) {
                            this.preZoom = map.getZoom();
//                            map.clearMap();
//                            panoramio.clearVisible();
                            getBoundsThumbnails();
                        }
                    });

                AMap.event.addListener(
                    map,
                    'moveend',
                    getBoundsThumbnails
                );
            } else {
                opts.map;
                map.clearMap();
                clearVisible();
            }

            var that = this;
            function getBoundsThumbnails() {
                var bounds = map.getBounds();
                var size = map.getSize();
                var thumbs = that.getBoundsThumbnails({
                    ne: {
                        lat: bounds.getNorthEast().lat,
                        lng: bounds.getNorthEast().lng
                    },
                    sw: {
                        lat: bounds.getSouthWest().lat,
                        lng: bounds.getSouthWest().lng
                    }
                }, map.getZoom(),
                    {width: size.getWidth(),
                     height: size.getHeight()}, function(thumbs) {
                        var photoIds = [];
                        var photos = {};
                        for (var i in thumbs) {
                            photoIds.push(thumbs[i].photoId);
                            photos[thumbs[i].photoId] = thumbs[i];
                        }
                        cnmap.utils.compareArray(
                            thumbPhotoIds,
                            photoIds,
                            function(a, c, b) {
                                if(a) {
                                    if(labels[a]) {
                                        labels[a].setMap(null);
                                        console.log("delete labels[a]");
                                    }
                                    var index = thumbPhotoIds.indexOf(a);
                                    if (index > -1) {
                                        thumbPhotoIds.splice(index, 1);
                                    }
                                }

                                if(c) {
                                    // do nothing
                                }

                                if(b) {
                                    thumbPhotoIds.push(b);
                                    if(labels[b]) {
                                        labels[b].setMap(map);
                                    }else{
                                        var label = new AMap.Marker({
                                            map: map,
                                            position: new AMap.LngLat(photos[b].lng, photos[b].lat), //基点位置
                                            offset: new AMap.Pixel(0, 0), //相对于基点的偏移位置
                                            content: that.getLabelContent(b)  //自定义点标记覆盖物内容
                                        });
                                        label.photoId = b;
                                        if(that.opts.clickable) {
                                            AMap.event.addListener(
                                                label,
                                                'click',
                                                function () {
                                                    if (opts.suppressInfoWindows) {
                                                        if (infoWindow.getIsOpen()) {
                                                            infoWindow.close();
                                                        } else {
                                                            infoWindow.setContent(that.getInfoWindowContent(this.photoId));
                                                            infoWindow.open(map, this.getPosition());
                                                        }
                                                    }else {
                                                        $(that).trigger("data_clicked", [this.photoId]);
                                                    }
                                                });
                                        }
                                        labels[b] = label;
                                        label.setMap(map);  //在地图上添加点
                                    }

                                }
                            }
                        )
                    // trigger data_changed event
                    $(that).trigger("data_changed", [thumbs]);
                })
            }
        };

        this.setOptions = function (options/*:PanoramioLayerOptions*/) { //	None
            opts = options;
        }

        this.trigger = function(event) {
            AMap.event.trigger(this.opts.map, "moveend");
        }

//        function open() {
//            for (var infoWindow in infoWindows) {
//                infoWindows[infoWindow].open();
//            }
//        }

        this.center_changed = function () {
//            infoWindows = [];
        };
    }

    window.cnmap.PanoramioLayer.prototype = new window.cnmap.Panoramio();

//    window.cnmap.gaode.PanoramioLayer.prototype = new qq.maps.MVCObject();

//    window.cnmap.qq.PanoramioLayer.prototype.zoom_changed = function () {
//        infoWindows = [];
//    };

//    window.cnmap.PanoramioLayerOptions = {
//        clickable : boolean, // If true, the layer receives mouse events. Default value is true.
//        map : Map, // The map on which to display the layer.
//        suppressInfoWindows : boolean, //Suppress the rendering of info windows when layer features are clicked.
//        tag : string, // A panoramio tag used to filter the photos which are displayed. Only photos which have been tagged with the supplied string will be shown.
//        userId : string, // A Panoramio user ID. If provided, only photos by this user will be displayed on the map. If both a tag and user ID are provided, the tag will take precedence.
//    };
    return window.cnmap.PanoramioLayer;
});