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
        define(['jquery'], factory);
    } else {
        // Browser globals.
        factory(jQuery);
    }
}(function ($) {

// 全局命名空间
    $.cnmap = $.cnmap || {};
    $.cnmap.qq = $.cnmap.qq || {};

    $.cnmap.qq.PanoramioLayer = function (opts/*?:PanoramioLayerOptions*/) {

        var infoWindows = [];

        var infoWindow = new qq.maps.InfoWindow();

        this.preZoom = 0;
        this.preBounds = null;

        opts = opts ? opts : {};

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
                this.bindTo("zoom", map);
                this.bindTo("center", map);

                infoWindow.setMap(map);

                var listener = qq.maps.event.addListener(
                    map,
                    'idle',
                    function () {

                        if (this.zoom != this.preZoom) {
                            this.preZoom = this.zoom;
                            for (var i in infoWindows) {
                                infoWindows[i].setVisible(false);
                            }
                        }
                        var label = new qq.maps.Label();
                        label.setContent("<img src=\"1.jpg\" style=\"width: 34px; height: 34px;\"></img>");
                        label.setMap(map);
//                        var latLng = qq.maps.LatLng(25, 102.8);
                        label.setPosition(map.getCenter());
                        qq.maps.event.addListener(
                            label,
                            'click',
                            function () {
                                if (opts.suppressInfoWindows) {
                                    if (infoWindow.opened) {
                                        infoWindow.close();
                                        infoWindow.opened = false;
                                    } else {
                                        infoWindow.setContent("<img src=\"1.jpg\" style=\"width: 100px; height: 100px;\"></img>");
                                        infoWindow.setPosition(label.getPosition());
                                        infoWindow.open();
                                        infoWindow.opened = true;
                                    }
                                }
                            });
                        infoWindows.push(label);
                    }
                );
            } else {
                opts.map;
            }
        };

        this.setOptions = function (options/*:PanoramioLayerOptions*/) { //	None
            opts = options;
        };

        this.setTag = function (tag/*:string*/) { //	None
        };

        this.setUserId = function (userId/*:string*/) { //	None
        };

//        function open() {
//            for (var infoWindow in infoWindows) {
//                infoWindows[infoWindow].open();
//            }
//        }

        this.center_changed = function () {
//            infoWindows = [];
        };
    };

    $.cnmap.qq.PanoramioLayer.prototype = new qq.maps.MVCObject();

//    $.cnmap.qq.PanoramioLayer.prototype.zoom_changed = function () {
//        infoWindows = [];
//    };

//    $.cnmap.PanoramioLayerOptions = {
//        clickable : boolean, // If true, the layer receives mouse events. Default value is true.
//        map : Map, // The map on which to display the layer.
//        suppressInfoWindows : boolean, //Suppress the rendering of info windows when layer features are clicked.
//        tag : string, // A panoramio tag used to filter the photos which are displayed. Only photos which have been tagged with the supplied string will be shown.
//        userId : string, // A Panoramio user ID. If provided, only photos by this user will be displayed on the map. If both a tag and user ID are provided, the tag will take precedence.
//    };

}));