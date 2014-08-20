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
        factory(window);
    }
}(function (window) {

// 全局命名空间
    window.cnmap = window.cnmap || {};
//    window.cnmap.baidu = window.cnmap.baidu || {};

    window.cnmap.PanoramioLayer = function (opts/*?:PanoramioLayerOptions*/) {

        var labels = [];
        var marker = new BMap.Marker();
        var infoWindow = new BMap.InfoWindow("");
        infoWindow.enableCloseOnClick();

        this.preZoom = 0;
        this.preBounds = null;

        this.opts = $.extend(
            {clickable: true, auto: true, mapVendor: "baidu"},
            opts);

        if (this.opts.map) {
            this.setMap(this.opts.map);
        }

        this.getMap = function () { //    Map    Returns the map on which this layer is displayed.
            return this.opts.map;
        };

        /**
         *
         * @param map {Map}
         */
        this.setMap = function (map/*:Map*/) { //	None	Renders the layer on the specified map. If map is set to null, the layer will be removed.

            if (map) {
                this.opts.map = map;
                map.addOverlay(infoWindow);

                map.addEventListener('load', getBoundsThumbnails);
                map.addEventListener('tilesloaded', getBoundsThumbnails);

                map.addEventListener('zoomend', getBoundsThumbnails);
//                map.addEventListener('zoomend', function () {
//                    if (map.getZoom() != this.preZoom) {
//                        this.preZoom = map.getZoom();
//                        getBoundsThumbnails();
//                    }
//                });

                map.addEventListener('moveend',
                    getBoundsThumbnails
                );
            } else {
                opts.map = null;
                map.clearOverlays();
//                panoramio.clearVisible();
                map.removeEventListener('tilesloaded');
                map.removeEventListener('zoomend');
                map.removeEventListener('moveend');
            }

            var that = this;
            function getBoundsThumbnails() {
                that.loadPhotos();
            }
        };

        this.loadPhotos = function() {

            var that = this,
                map = this.opts.map;

            $(that).trigger("map_changed", [that.getBounds(), that.getLevel(), that.getSize()]);

            if(!that.opts.auto) {
                return;
            }

            var bounds = map.getBounds();
            var size = map.getSize();
            var thumbs = that.getBoundsThumbnails(
                that.getBounds(),
                that.getLevel(),
                that.getSize());
        }

        this.hideLabel = function(photoId) {
            if(labels[photoId]) {
                this.opts.map.removeOverlay(labels[photoId]);
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

            var label = labels[photo.id];
            if(label) {
                map.addOverlay(label);
            }else {
                label = new BMap.Label();
                //自定义点标记覆盖物内容
                label.setContent(that.getLabelContent(photo.oss_key));
                label.setPosition(new BMap.Point(photo.point.lng, photo.point.lat));
                label.photoId = photo.id;
                label.setZIndex(1);
                label.setStyle({
                    border: 0,
                    padding: "0",
//                            color : ,
                    fontSize: "12px",
//                    height: "20px",
                    lineHeight: "20px",
                    fontFamily: "微软雅黑"
                });
                if (that.opts.clickable) {
                    label.addEventListener(
                        'click',
                        function () {
                            if (opts.suppressInfoWindows) {
                                if (infoWindow.isOpen()) {
                                    infoWindow.close();
                                } else {
                                    infoWindow.setContent(panoramio.getInfoWindowContent(this.photoId));
                                    map.openInfoWindow(infoWindow, this.getPosition());
                                }
                            }else {
                                $(that).trigger("data_clicked", [this.photoId]);
                            }
                        });
                }
                labels[photo.id] = label;
                map.addOverlay(label); //在地图上添加点
            }
        };

        this.setOptions = function (options/*:PanoramioLayerOptions*/) { //	None
            opts = options;
        };

        this.trigger = function(event) {
            if(this.opts.map) {
                this.loadPhotos();
            }
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
            return this.opts.map.getSize();
        };

        this.clearMap = function() {
            labels = [];
            this.thumbPhotoIds = [];
            this.opts.map && this.opts.map.clearOverlays();
        };
    };

    window.cnmap.PanoramioLayer.prototype = new window.cnmap.Panoramio();

//    window.cnmap.PanoramioLayerOptions = {
//        clickable : boolean, // If true, the layer receives mouse events. Default value is true.
//        map : Map, // The map on which to display the layer.
//        suppressInfoWindows : boolean, //Suppress the rendering of info windows when layer features are clicked.
//        tag : string, // A panoramio tag used to filter the photos which are displayed. Only photos which have been tagged with the supplied string will be shown.
//        userId : string, // A Panoramio user ID. If provided, only photos by this user will be displayed on the map. If both a tag and user ID are provided, the tag will take precedence.
//    };
    return window.cnmap.PanoramioLayer;
}));