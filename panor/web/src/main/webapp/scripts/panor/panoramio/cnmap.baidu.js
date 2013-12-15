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
    $.cnmap.baidu = $.cnmap.baidu || {};

    $.cnmap.baidu.PanoramioLayer = function (opts/*?:PanoramioLayerOptions*/) {

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

                map.addEventListener('zoomend', function() {
                    if (map.getZoom() != this.preZoom) {
                        this.preZoom = map.getZoom();
                        map.clearOverlays();
//                        for (var i in labels) {
//                            labels[i].setVisible(false);
//                        }
                    }
                });

                map.addEventListener('moveend',
                    function () {

                        var label = new BMap.Label();
                        label.setContent("<img src='image/1.jpg' style='width: 34px; height: 34px;'>");
                        label.setPosition(map.getCenter());
                        label.addEventListener(
                            'click',
                            function () {
                                if (opts.suppressInfoWindows) {
                                    if (infoWindow.isOpen()) {
                                        infoWindow.close();
                                    } else {
                                        infoWindow.setContent("<img src='image/1.jpg' style='width: 100px; height: 100px;'>");
//                                        infoWindow.setPosition(label.getPosition());
                                        map.openInfoWindow(infoWindow, label.getPosition());
//                                        marker.openInfoWindow(infoWindow);
//                                        infoWindow.restore();
                                    }
                                }
                            });
                        label.setZIndex(1);
                        label.setStyle({
                            border: 0,
                            padding: "2px",
//                            color : ,
                            fontSize : "12px",
                            height : "20px",
                            lineHeight : "20px",
                            fontFamily:"微软雅黑"
                        });
                        labels.push(label);
                        map.addOverlay(label);
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