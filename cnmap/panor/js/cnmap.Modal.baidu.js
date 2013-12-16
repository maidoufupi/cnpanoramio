/**
 * Created with JetBrains WebStorm.
 * User: tiwen.wang
 * Date: 13-12-14
 * Time: 下午6:14
 * To change this template use File | Settings | File Templates.
 */

(function ($) {
    "use strict";

    var map;
    var geocoder;
    var marker;
    $.cnmap = $.cnmap || {};
    $.cnmap.modal = {

        setPlace: function (lat, lng) {
            $('.list-group-item.map_photo_cell.active').data("data").lat = lat;
            $('.list-group-item.map_photo_cell.active').data("data").lng = lng;
            $("#the-place span.lng").text(lng);
            $("#the-place span.comma").show();
            $("#the-place span.alt").hide();
            $("#the-place span.lat").text(lat);
            geocoder.getLocation(new BMap.Point(lng, lat), function (rs) {
                if (rs) {
                    var addComp = rs.addressComponents;
                    $("#the-address").text(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
                    $(".coder_place span.alt").hide();
                }
            })
        },
        initMap: function (mapCanvas) {
            map = new BMap.Map(mapCanvas);          // 创建地图实例
            map.enableScrollWheelZoom();
            map.addControl(new BMap.NavigationControl());
            map.addControl(new BMap.ScaleControl());
            var overviewMapControl = new BMap.OverviewMapControl();
            overviewMapControl.changeView();
            map.addControl(overviewMapControl);
            map.addControl(new BMap.MapTypeControl());

            geocoder = new BMap.Geocoder();
            geocoder.getPoint("中国", function (point) {
                map.centerAndZoom(point, 5);
            })
        },
        initGeocoder: function () {
            // 创建地址解析器实例
            var setPlace = this.setPlace,
                savePlace = this.savePlace;
            marker = new BMap.Marker();
            marker.enableDragging();
            marker.addEventListener("dragend", function (event) {
                setPlace(event.point.lat, event.point.lng);
            });

            $('#geocoder_form').submit(function (event) {
                event.preventDefault();
                // 将地址解析结果显示在地图上,并调整地图视野
                geocoder.getPoint($("#location-search-input").val(), function (point) {
                    if (point) {
                        map.centerAndZoom(point, map.getZoom());
                        if (marker) {
                            marker.setPosition(point);
                            if (!marker.getMap()) {
                                map.addOverlay(marker);
                            }

                            setPlace(point.lat, point.lng);
                        }
                    }
                }, "中国");
            });

            $("#button-set-place").click(function () {
                var latlng = map.getCenter();
                marker.setPosition(latlng);
                if (!marker.getMap()) {
                    map.addOverlay(marker);
                }
                setPlace(latlng.lat, latlng.lng);
            });

            $("#button-save-complete").click(function () {
                savePlace();
                $("#map-photo-list .list-group-item.map_photo_cell").each(function (index) {
                    $(this).data("data").updateLatlng.apply($(this).data("data"));
                });
                $('#myModal').modal('hide');
            });
        },
        savePlace: function () {
            var placeData = {
                lat: $("#the-place span.lat").text(),
                lng: $("#the-place span.lng").text(),
                address: $("#the-address").text()
            };
            if (placeData.lat || placeData.lng) {
                var data = $('.list-group-item.map_photo_cell.active').data("data");
                data = $.extend({}, data, placeData);
                $('.list-group-item.map_photo_cell.active').data("data", data);
            }
        },
        setMarkerPoint: function(lat, lng) {
            var point = new BMap.Point(lng, lat);
            if (marker) {
                marker.setPosition(point);
                if (!marker.getMap()) {
                    map.addOverlay(marker);
                }
                map.centerAndZoom(marker.getPosition(), map.getZoom());
            }
        },
        addEventListener: function () {
            var setPlace = this.setPlace,
                savePlace = this.savePlace,
                setMarkerPoint = this.setMarkerPoint;
            $('.list-group-item.map_photo_cell').click(function () {
                savePlace();
                $('.list-group-item.map_photo_cell.active').removeClass("active");
                $(this).addClass("active");
                var data = $(this).data("data");
                if (data.lat || data.lng) {
                    setPlace(data.lat, data.lng);
                    setMarkerPoint(data.lat, data.lng);
                }
            });
        },
        clearPlace: function() {
            $("#the-place span.lat").text("");
            $("#the-place span.comma").hide();
            $("#the-place span.alt").show();
            $("#the-place span.lng").text("");
            $("#the-address").text("");
            $(".coder_place span.alt").show();
        }
    }
})(jQuery);
