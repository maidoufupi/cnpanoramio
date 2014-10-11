/**
 * Created by any on 2014/10/9.
 */
(function (factory) {

    if (typeof define === 'function' && define.amd) {
        // AMD. Register as anonymous module.
        define([window, 'jquery', 'Panoramio'], factory);
    } else {
        // Browser globals.
        factory(window, jQuery);
    }

})(function (window, $jQuery) {

// 全局命名空间
    window.cnmap = window.cnmap || {};

    window.cnmap.PanoramioLayer = function (opts/*?:PanoramioLayerOptions*/) {

        var labels = [];
        var thumbPhotoIds = [];
        var infoWindow = null;

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

                infoWindow = new google.maps.InfoWindow();

                google.maps.event.addListener(map,
                    'idle',
                    getBoundsThumbnails);
                google.maps.event.addListener(map,
                    'dragend',
                    getBoundsThumbnails);

            } else {
                delete this.opts.map;
            }

            var that = this;
            function getBoundsThumbnails() {

                $(that).trigger("map_changed", [that.getBounds(), that.getLevel(), that.getSize()]);

                if(!that.opts.auto) {
                    return;
                }

                var bounds = that.getBounds();
                // 地图为初始化完时 getBounds()为空
                if(!bounds) {
                    return;
                }

                var thumbs = that.getBoundsThumbnails(bounds,
                    that.getLevel(),
                    that.getSize());
            }
        };

        this.hideLabel = function(photoId) {
            if(labels[photoId]) {
                labels[photoId].setVisible(false);
                labels[photoId].setMap(null);
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
                label.setVisible(true);
                label.setMap(map);
            }else {
                label = new google.maps.Marker({
                    map: map,
                    position: new google.maps.LatLng(photo.point.lat, photo.point.lng),
                    icon: this.staticCtx + "/" + photo.oss_key + "@!panor-lg"
                });
                label.photoId = photo.id;
                if(that.opts.clickable) {
                    google.maps.event.addListener(
                        label,
                        'click',
                        function (e) {
                            if (that.opts.suppressInfoWindows) {
                                infoWindow.setPosition(this.getPosition());
                                infoWindow.setContent(that.getInfoWindowContent(photo));
                                infoWindow.open();
                            }else {
                                $jQuery(that).trigger("data_clicked", [this.photoId]);
                            }
                        });
                }
                labels[photo.id] = label;
            }
        };

//        this.getLabelContent = function(photoOssKey) {
//
//            if(this.opts.phone) {
//                return "<img src='" + this.staticCtx + "/"
//                    + photoOssKey + "@!panor-lg' style='width: 34px; height: 34px;'>";
//            }else {
//                return "<img src='" + this.staticCtx + "/" + photoOssKey
//                    + "@!panor-lg' style='width: 34px; height: 34px;'>";
//            }
//
//        };

//        this.getInfoWindowContent = function(photoId) {
//            var infoWindow = document.createElement("div");
//            $jQuery(infoWindow).css({
//                'width': '200px',
//                'height': '200px',
//                'display': 'inline-block'
//            }).append(
//                "<a href='" + this.ctx + "/photo/" + photoId+ "'><img src='" + this.ctx + "/api/rest/photo/" + photoId + "/2' ></a>"
//            )
//            return infoWindow;
//        };

        this.setOptions = function (options/*:PanoramioLayerOptions*/) { //	None
            this.opts = options;
        };

        this.trigger = function(event) {
            if(this.opts.map) {
                google.maps.event.trigger(this.opts.map, "dragend");
            }
        };

        this.center_changed = function () {
//            infoWindows = [];
        };

        this.getBounds = function() {
            var bounds = this.opts.map.getBounds();
            if(bounds) {
                return {
                    ne: {
                        lat: bounds.getNorthEast().lat(),
                        lng: bounds.getNorthEast().lng()
                    },
                    sw: {
                        lat: bounds.getSouthWest().lat(),
                        lng: bounds.getSouthWest().lng()
                    }
                };
            }
        };

        this.getLevel = function() {
            return this.opts.map.getZoom();
        };

        this.getSize = function() {
            var mapContainer = this.opts.map.getDiv();

            return {
                width: $jQuery(mapContainer).width(),
                height: $jQuery(mapContainer).height()
            };
        };

        this.clearMap = function() {
            $.each(labels, function( index, label ) {
                label && label.setMap(null);
            });
            labels = [];
            this.thumbPhotoIds = [];
        };

        this.setAuto = function(auto) {
            this.opts.auto = auto;
        };
    };

    window.cnmap.PanoramioLayer.prototype = new window.cnmap.Panoramio();

    return window.cnmap.PanoramioLayer;
});