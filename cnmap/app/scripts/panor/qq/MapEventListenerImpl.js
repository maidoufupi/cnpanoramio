/**
 * Created by any on 14-4-12.
 */
'use strict';

(function (factory) {

    if (typeof define === 'function' && define.amd) {
        // AMD. Register as anonymous module.
        define([window, 'jquery', 'cnmap'], factory);
    } else {
        // Browser globals.
        factory(window, jQuery);
    }

})(function ($window, $jQuery) {

    $window.cnmap.MapEventListener = function(opts) {

        this.opts = opts || this.opts;

        this.addLocationHashListener = function(map, callback) {
            var listeners = [];
            listeners.push($window.qq.maps.event.addListener(map, 'idle', function() {
                var latLng = map.getCenter();
                callback.apply(this, [latLng.lat, latLng.lng, map.getZoom()]);
            }));
            return listeners;
        };

        this.removeListener = function(listeners) {
            if(listeners) {
                $.each(listeners, function (index, listener) {
                    qq.maps.event.removeListener(listener);
                });
            }
        };

        this.addToolBar = function(map) {
        };

        this.setCenter = function(map, lat, lng) {
            var point = new qq.maps.LatLng(lat, lng);
            map.setCenter(point);
        };

        this.setZoom = function(map, zoom) {
            map.setZoom(Number(zoom));
        };

        this.setZoomAndCenter = function(map, zoom, lat, lng) {
            var point = new qq.maps.LatLng(lat, lng);
            map.panTo(point);
            map.zoomTo(Number(zoom));
        };

        this.setBounds = function(map, southwest, northeast){
            map.fitBounds(new qq.maps.LatLngBounds(
                new qq.maps.LatLng(southwest.lat, southwest.lng),
                new qq.maps.LatLng(northeast.lat, northeast.lng)
            ))
        };

        this.clearMap = function(map) {

        };

        this.inMapView = function(lat, lng, map) {
            map = map || this.opts.map;
            var point = new qq.maps.LatLng(lat, lng);
            var bounds = map.getBounds();
            if(bounds) {
                return bounds.contains(point);
            }else {
                return false;
            }
        };

        this.pixelToPoint =function(map, pixel) {

            var bounds = map.getBounds(),
                size,
                point = {};

            var mapContainer = $(map.getContainer());
            size =  {
                    width: parseInt(mapContainer.width()),
                    height: parseInt(mapContainer.height())
                };

            point.lng = pixel.x / size.width * (bounds.getNorthEast().lng - bounds.getSouthWest().lng) + bounds.getSouthWest().lng;
            point.lat = bounds.getNorthEast().lat - pixel.y / size.height * (bounds.getNorthEast().lat - bounds.getSouthWest().lat);

            return point;
//            var projection = map.getProjection();
//            return projection.fromPointToLatLng(new qq.maps.Point(pixel.x, pixel.y));
        };

        this.pointToPixel =function(map, point) {
            var projection = map.getProjection();
            return projection.fromLatLngToPoint(new qq.maps.LatLng(point.lat, pixel.lng));
        };

        this.addMarker = function(map, lat, lng) {
            return new qq.maps.Marker({
                map: map,
                position: new qq.maps.LatLng(lat, lng)
            });
        };

        this.createDraggableMarker = function(map, lat, lng) {
            return new qq.maps.Marker({
                draggable: true,
                map: map,
                position: new qq.maps.LatLng(lat, lng)
            });
        };

        this.activeMarker = function(marker) {
            if(marker) {
                marker.setZIndex(2);
                marker.setIcon("images/marker.png");
            }
        };

        this.deactiveMarker = function(marker){
            if(marker) {
                marker.setZIndex(1);
                marker.setIcon("");
            }
        };

        this.addMarkerActiveListener = function(marker, callback) {
            $window.qq.maps.event.addListener(marker, "click", cback);
            $window.qq.maps.event.addListener(marker, "dragend", cback);
            function cback(evt) {
                callback.apply(marker, []);
            }
        };

        this.addDragendListener = function(marker, callback) {
            $window.qq.maps.event.addListener(marker, "dragend", function (event) {
                callback.apply(marker, [event.latLng.lat, event.latLng.lng]);
            });
        };

        this.removeMarker = function(marker) {
            marker.setMap(null);
        }

        this.addMapClickListener = function(map, callback) {
            $window.qq.maps.event.addListener(map, "click", function (event) {
                var point = event.latLng;
                callback.apply(map, [point.lat, point.lng]);
            });
        };

        this.setPosition = function(target, lat, lng) {
            var point = new qq.maps.LatLng(lat, lng);
            target.setPosition(point);
        };

        this.setMap = function(target, map){
            target.setMap(map);
        };
    };

    $window.cnmap.MapEventListener.prototype = $window.cnmap.IMapEventListener;
    $window.cnmap.MapEventListener.factory = function() {
        return new $window.cnmap.MapEventListener();
    }
    return $window.cnmap.MapEventListener;
});