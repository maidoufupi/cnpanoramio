/**
 * Created by any on 2014/5/7.
 */
'use strict';

angular.module('indexApp', ["ponmApp", "ui.map"])
    .controller('IndexCtrl', ['$window', '$scope', function ($window, $scope) {

        var mapEventListener = $window.cnmap.MapEventListener.factory();
        $scope.mapOptions = {
            // map plugin config
            // map-self config
            resizeEnable: true,
            panControl: false,
            zoomControl: false,

            // ui map config
            uiMapCache: false
        }

        $("div.imgLiquidFill").imgLiquid({
            fill : true
        });

        var photos, photo_index;

        var restclient = new $.RestClient(window.ctx + '/api/rest/');
        restclient.add('index');

        function setPhoto(photo) {
            $(".front-photo_sizer img").attr("src", ctx + "/api/rest/photo/" + photo.id + "/1");
            $(".front-photo_sizer a").attr("href", ctx + "/photo/" + photo.id);

            mapEventListener.setCenter($scope.map, photo.lat, photo.lng);
            if(!photo.mark) {
                photo.mark = true;
                mapEventListener.createDraggableMarker($scope.map, photo.lat, photo.lng);
            }

            $("div.imgLiquidFill").imgLiquid({
                fill : true
            });

            if(photos.length > 1) {
                setTimeout(function() {
                    setPhoto(photos[photo_index]);
                    photo_index = (photo_index + 1) % photos.length;
                }, 4000);
            }
        }

        restclient.index.read('photo').done(function(data) {
            photos = data;
            photo_index = 0;
            if(photos.length) {
                setPhoto(photos[photo_index]);
                photo_index = (photo_index + 1) % photos.length;
            }

        })
    }]);