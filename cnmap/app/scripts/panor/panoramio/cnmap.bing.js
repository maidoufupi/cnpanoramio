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

})(function ($window, $jQuery) {

// 全局命名空间
    $window.cnmap = $window.cnmap || {};

    $window.cnmap.PanoramioLayer = function (opts/*?:PanoramioLayerOptions*/) {

        var infoWindow ;
        var labels = {};
        var thumbPhotoIds = [];

        this.preZoom = 0;
        this.preBounds = null;

        this.opts = $jQuery.extend( {clickable: true, auto: true, mapVendor: "gps"}, opts);

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

                // 当map出现时说明地图加载过，才创建地图的各种对象
                //infoWindow = new qq.maps.InfoWindow();
                //infoWindow.setMap(map);

                var listener = Microsoft.Maps.Events.addHandler(
                    map,
                    'tiledownloadcomplete',
                    getBoundsThumbnails
                );
                listener = Microsoft.Maps.Events.addHandler(
                    map,
                    'viewchangeend',
                    getBoundsThumbnails
                );
            } else {
                this.opts.map = undefined;
                // TODO
//                this.clearVisible();
            }

            var that = this;
            function getBoundsThumbnails() {

                $(that).trigger("map_changed", [that.getBounds(), that.getLevel(), that.getSize()]);

                if(!that.opts.auto) {
                    return;
                }

                var bounds = that.getBounds();
                // 地图为初始化完时 getBounds()为空
                if(!bounds.ne) {
                    return;
                }

                var thumbs = that.getBoundsThumbnails(that.getBounds(),
                                                     that.getLevel(),
                                                     that.getSize());
            }

        };

        this.hideLabel = function(photoId) {
          var label = labels[photoId];
            if(label) {
              label.setOptions({visible: false});
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
              label.setOptions({visible: true});
            }else {
                label = new Microsoft.Maps.Pushpin(new Microsoft.Maps.Location(photo.point.lat, photo.point.lng), {
                  htmlContent: that.getLabelContent(photo.oss_key)
                });
                label.photoId = photo.id;
                if(that.opts.clickable) {
                  Microsoft.Maps.Events.addHandler(
                        label,
                        'click',
                        function (e) {
                            if (that.opts.suppressInfoWindows) {
                                infoWindow.setOptions({
                                    content: that.getInfoWindowContent(photo),
                                    position: this.getPosition()
                                });
                                infoWindow.open();
                            }else {
                                $jQuery(that).trigger("data_clicked", [e.target.photoId]);
                            }
                        });
                }
              map.entities.push(label);
              labels[photo.id] = label;
            }
        };

        this.setOptions = function (options/*:PanoramioLayerOptions*/) { //	None
            this.opts = options;
        };

        this.trigger = function(event) {
            if(this.opts.map) {
              Microsoft.Maps.Events.invoke(this.opts.map, "viewchangeend");
            }
        };

        this.getBounds = function() {
            var bounds = this.opts.map.getBounds();
            if(bounds) {
                return {
                    ne: {
                        lat: bounds.getNorth(),
                        lng: bounds.getEast()
                    },
                    sw: {
                        lat: bounds.getSouth(),
                        lng: bounds.getWest()
                    }
                };
            }else {
                return {
                };
            }

        };

        this.getLevel = function() {
            return this.opts.map && parseInt(this.opts.map.getZoom());
        };

        this.getSize = function() {
            return {
                width: this.opts.map.getWidth(),
                height: this.opts.map.getHeight()
            };
        };

        this.clearMap = function() {
          if(this.opts.map) {
            this.opts.map.entities.clear();
          };
          labels = [];
          this.thumbPhotoIds = [];
        };

        this.setAuto = function(auto) {
            this.opts.auto = auto;
        };
    };

    $window.cnmap.PanoramioLayer.prototype = new $window.cnmap.Panoramio();

//    $.cnmap.PanoramioLayerOptions = {
//        clickable : boolean, // If true, the layer receives mouse events. Default value is true.
//        map : Map, // The map on which to display the layer.
//        suppressInfoWindows : boolean, //Suppress the rendering of info windows when layer features are clicked.
//        tag : string, // A panoramio tag used to filter the photos which are displayed. Only photos which have been tagged with the supplied string will be shown.
//        userId : string, // A Panoramio user ID. If provided, only photos by this user will be displayed on the map. If both a tag and user ID are provided, the tag will take precedence.
//    };

    return $window.cnmap.PanoramioLayer;
});
