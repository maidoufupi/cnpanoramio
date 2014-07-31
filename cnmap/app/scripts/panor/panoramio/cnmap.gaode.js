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
        var infoWindow = new AMap.InfoWindow({isCustom: false, closeWhenClickMap: true});

        this.preZoom = 0;
        this.preBounds = null;

        this.opts = $.extend(
            {clickable: true,
             auto: true},
            opts);

        if (this.opts.map) {
            this.setMap(this.opts.map);
        }

        /**
         * Renders the layer on the specified map. If map is set to null, the layer will be removed.
         *
         * @param map {Map}
         */
        this.setMap = function (map/*:Map*/) { //	None
            if (map) {
                this.opts.map = map;

                AMap.event.addListener(
                    map,
                    'complete',
                    mapChangedListener
                );

                AMap.event.addListener(
                    map,
                    'zoomend',
                    function (evt) {
                        if (map.getZoom() != this.preZoom) {
                            this.preZoom = map.getZoom();
//                            map.clearMap();
//                            panoramio.clearVisible();
                            mapChangedListener(evt);
                        }
                    });

                AMap.event.addListener(
                    map,
                    'moveend',
                    mapChangedListener
                );
            } else {
                opts.map;
                map.clearMap();
                clearVisible();
            }

            var getDataTimeoutHander;
            function mapChangedListener(e) {
                console.log(e);
                if(getDataTimeoutHander) {
                    clearTimeout(getDataTimeoutHander);
                }
                getDataTimeoutHander = setTimeout(function(){
                    getBoundsThumbnails();
                }, 500);
            }

            var that = this;
            function getBoundsThumbnails(e) {

                $(that).trigger("map_changed", [that.getBounds(), that.getLevel(), that.getSize()]);

                if(!that.opts.auto) {
                    return;
                }

                var bounds = map.getBounds();
                var size = map.getSize();
                var thumbs = that.getBoundsThumbnails(
                    that.getBounds(),
                    that.getLevel(),
                    that.getSize(),
                    function(thumbs) {
                        var photoIds = [];
                        var photos = {};
                        for (var i in thumbs) {
                            photoIds.push(thumbs[i].id);
                            photos[thumbs[i].id] = thumbs[i];
                        }
                        cnmap.utils.compareArray(
                            thumbPhotoIds,
                            photoIds,
                            function(a, c, b) {
                                if(a) {
                                    if(labels[a]) {
                                        labels[a].setMap(null);
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
                                    that.createMarker(photos[b]);
                                }
                            }
                        );
                    // trigger data_changed event
                    $(that).trigger("data_changed", [thumbs]);
                })
            }
        };

        /**
         * 创建图片图标
         *
         * @param photo
         */
        this.createMarker = function(photo) {
            var map = this.opts.map;
            var that = this;
            thumbPhotoIds.push(photo.id);
            if(labels[photo.id]) {
                labels[photo.id].setMap(map);
            }else {
                var label = new AMap.Marker({
                    map: map,
                    position: new AMap.LngLat(photo.point.lng, photo.point.lat), //基点位置
                    offset: new AMap.Pixel(0, 0), //相对于基点的偏移位置
                    content: that.getLabelContent(photo.oss_key)  //自定义点标记覆盖物内容
                });
                label.photoId = photo.id;
                if (that.opts.clickable) {
                    AMap.event.addListener(
                        label,
                        'click',
                        function () {
                            if (opts.suppressInfoWindows) {
                                if (infoWindow.getIsOpen()) {
                                    infoWindow.close();
                                } else {
                                    infoWindow.setContent(that.getInfoWindowContent(photos[this.photoId]));
                                    infoWindow.open(map, this.getPosition());
                                }
                            } else {
                                $(that).trigger("data_clicked", [this.photoId]);
                            }
                        });
                }
                labels[photo.id] = label;
                label.setMap(map);  //在地图上添加点
            }
        };

        /**
         * 由外部调用创建图片的图标
         *
         * @param photos
         */
        this.createPhotosMarker = function(photos) {
            // 清除缓存
            this.clearMap();
            var that = this;
            jQuery.each(photos, function (key, photo) {
                that.createMarker(photo);
            });
        };

        this.setOptions = function (options/*:PanoramioLayerOptions*/) { //	None
            opts = options;
        };

        this.trigger = function(event) {
            if(this.opts.map) {
                AMap.event.trigger(this.opts.map, "moveend");
            }
        };

//        function open() {
//            for (var infoWindow in infoWindows) {
//                infoWindows[infoWindow].open();
//            }
//        }

        this.center_changed = function () {
//            infoWindows = [];
        };

        this.getBounds = function() {
            var bounds = this.opts.map.getBounds();
            return {
                ne: {
                    lat: bounds.getNorthEast().lat,
                    lng: bounds.getNorthEast().lng
                },
                sw: {
                    lat: bounds.getSouthWest().lat,
                    lng: bounds.getSouthWest().lng
                }
            };
        };

        this.getLevel = function() {
            return this.opts.map.getZoom();
        };

        this.getSize = function() {
            var size = this.opts.map.getSize();
            return {
                width: size.getWidth(),
                height: size.getHeight()
            };
        };

        this.clearMap = function() {
            labels = [];
            thumbPhotoIds = [];
            this.opts.map && this.opts.map.clearMap();
        };

        this.setAuto = function(auto) {
            this.opts.auto = auto;
        };
    };

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