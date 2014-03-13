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
        factory(window);
    }
}(function ($) {

// 全局命名空间
    $.cnmap = $.cnmap || {};
    $.cnmap.alimap = $.cnmap.alimap || {};

    $.cnmap.alimap.PanoramioLayer = function (opts/*?:PanoramioLayerOptions*/) {

        var labels = [];

        var infoWindow = new AliInfoWindow();

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
//                this.bindTo("zoom", map);
//                this.bindTo("center", map);
                AliEvent.addListener(
                    map,
                    'zoom',
                    function () {
                        if (map.getZoom() != this.preZoom) {
                            this.preZoom = map.getZoom();
                            for (var i in labels) {
                                labels[i].setVisible(false);
                            }
                        }
                    });
                AliEvent.addListener(
                    map,
                    'mouseup',
                    function () {

                        var label = new AliMarker(map.getCenter());
                        label.setIcon(new AliIcon("1.jpg", {x:30, y:30}, {x:10, y:10}));

                        AliEvent.addListener(
                            label,
                            'click',
                            function () {
                                if (opts.suppressInfoWindows) {
                                    map.openInfoWindow(label.getLatLng(), "", "<img src=\"1.jpg\" style=\"width: 100px; height: 100px;\"></img>");
                                }
                            });
                        map.addOverlay(label);
                        labels.push(label);
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
//            for (var infoWindow in labels) {
//                labels[infoWindow].open();
//            }
//        }

        this.center_changed = function () {
//            labels = [];
        };
    };

//    $.cnmap.qq.PanoramioLayer.prototype = new qq.maps.MVCObject();

//    $.cnmap.qq.PanoramioLayer.prototype.zoom_changed = function () {
//        labels = [];
//    };

//    $.cnmap.PanoramioLayerOptions = {
//        clickable : boolean, // If true, the layer receives mouse events. Default value is true.
//        map : Map, // The map on which to display the layer.
//        suppressInfoWindows : boolean, //Suppress the rendering of info windows when layer features are clicked.
//        tag : string, // A panoramio tag used to filter the photos which are displayed. Only photos which have been tagged with the supplied string will be shown.
//        userId : string, // A Panoramio user ID. If provided, only photos by this user will be displayed on the map. If both a tag and user ID are provided, the tag will take precedence.
//    };

}));