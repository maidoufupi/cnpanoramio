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
//    $.cnmap.baidu = $.cnmap.baidu || {};

    $.cnmap.PanoramioLayer = function (opts/*?:PanoramioLayerOptions*/) {

        var panoramio = new $.cnmap.Panoramio();

        var labels = [];
        var marker = new BMap.Marker();
        var infoWindow = new BMap.InfoWindow("");
        infoWindow.enableCloseOnClick();

        this.preZoom = 0;
        this.preBounds = null;

        opts = opts ? opts : {
            autoLoad: true
        };

        if (opts.map) {
            this.setMap(opts.map);
        }

        this.getMap = function () { //    Map    Returns the map on which this layer is displayed.
            return opts.map;
        };

        this.getTag = function () { //     string
        };

        this.getUserId = function () { //	string
        };

        /**
         *
         * @param map {Map}
         */
        this.setMap = function (map/*:Map*/) { //	None	Renders the layer on the specified map. If map is set to null, the layer will be removed.
            if (map) {
                opts.map = map;
                map.addOverlay(infoWindow);

                map.addEventListener('load', getBoundsThumbnails);
                map.addEventListener('tilesloaded', getBoundsThumbnails);

                map.addEventListener('zoomend', function () {
                    if (map.getZoom() != this.preZoom) {
                        this.preZoom = map.getZoom();
                        map.clearOverlays();
                        panoramio.clearVisible();
                        getBoundsThumbnails();
                    }
                });

                map.addEventListener('moveend',
                    getBoundsThumbnails
                );
            } else {
                opts.map = null;
                map.clearOverlays();
                panoramio.clearVisible();
                map.removeEventListener('tilesloaded');
                map.removeEventListener('zoomend');
                map.removeEventListener('moveend');
            }
            var that = this;
            function getBoundsThumbnails() {
                var bounds = map.getBounds();
                var size = map.getSize();
                var thumbs = panoramio.getBoundsThumbnails({
                    ne: {
                        lat: bounds.getNorthEast().lat,
                        lng: bounds.getNorthEast().lng
                    },
                    sw: {
                        lat: bounds.getSouthWest().lat,
                        lng: bounds.getSouthWest().lng
                    }
                }, size, function(thumbs) {
                    for (var i in thumbs) {
                        var photoId = thumbs[i].photoId;
                        if(panoramio.getVisible(photoId)) {
                            continue;
                        }
                        if(labels[photoId]) {
                            map.addOverlay(labels[photoId]);
                            continue;
                        }
                        var label = new BMap.Label();
                        label.photoId = photoId;
                        label.setContent(panoramio.getLabelContent(photoId));
                        label.setPosition(new BMap.Point(thumbs[i].lng, thumbs[i].lat));
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
                                }
                            });
                        label.setZIndex(1);
                        label.setStyle({
                            border: 0,
                            padding: "2px",
//                            color : ,
                            fontSize: "12px",
                            height: "20px",
                            lineHeight: "20px",
                            fontFamily: "微软雅黑"
                        });
                        map.addOverlay(label);
                        labels[photoId] = label;
                    }
                    // trigger data_changed event
                    $(that).trigger("data_changed", [thumbs]);
                });

            }
        };

        this.setOptions = function (options/*:PanoramioLayerOptions*/) { //	None
            opts = options;
        };

        this.setTag = function (tag/*:string*/) { //	None
        };

        this.setUserId = function (userId/*:string*/) { //	None
        };

        this.center_changed = function () {
//            infoWindows = [];
        };
    };

//    $.cnmap.PanoramioLayerOptions = {
//        clickable : boolean, // If true, the layer receives mouse events. Default value is true.
//        map : Map, // The map on which to display the layer.
//        suppressInfoWindows : boolean, //Suppress the rendering of info windows when layer features are clicked.
//        tag : string, // A panoramio tag used to filter the photos which are displayed. Only photos which have been tagged with the supplied string will be shown.
//        userId : string, // A Panoramio user ID. If provided, only photos by this user will be displayed on the map. If both a tag and user ID are provided, the tag will take precedence.
//    };

}));