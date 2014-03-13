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

        setPlace: function (lat, lng, address) { // 此参数为baidu坐标
            $("#the-place span.lng").text($.cnmap.GPS.convert(lng));
            $("#the-place span.comma").show();
            $("#the-place span.alt").hide();
            $("#the-place span.lat").text($.cnmap.GPS.convert(lat));

            if(address) {
                $("#the-address").text(address);
                $(".coder_place span.alt").hide();
            }else {
                $.cnmap.utils.qq.getAddress(lat, lng, function(data) {
                    if(data.type == qq.maps.ServiceResultType.GEO_INFO) {
                        if(data.detail.address) {
                            $("#the-address").text(data.detail.address);
                            $(".coder_place span.alt").hide();
                        }
                    }
                })
            }
        },
        initMap: function (mapCanvas) {
            map = new qq.maps.Map(document.getElementById(mapCanvas));

            $.cnmap.utils.qq.getLocation("中国", function (type, detail) {
                console.log(detail.address);
            })
        },
        initGeocoder: function () {
            // 创建地址解析器实例
            var setPlace = this.setPlace,
                savePlace = this.savePlace,
                setMarkerPoint = this.setMarkerPoint;
            marker = new qq.maps.Marker();
            marker.setDraggable(true);
            qq.maps.event.addListener(marker, "dragend", function (event) {
                setPlace(event.latLng.lat, event.latLng.lng);
            });

            $('#geocoder_form').submit(function (event) {
                event.preventDefault();
                // 将地址解析结果显示在地图上,并调整地图视野
                $.cnmap.utils.qq.getLocation($("#location-search-input").val(), function (data) {
                    if(data.type == qq.maps.ServiceResultType.GEO_INFO) {
                        var location = data.detail.location;
                        if (location) {
                            map.panTo(location);
                            if (marker) {
                                marker.setPosition(location);
                                if (!marker.getMap()) {
                                    marker.setMap(map);
                                }
                                setPlace(location.lat, location.lng, data.detail.address);
                            }
                        }
                    }

                })
            });

            $("#button-set-place").click(function () {
                var latlng = map.getCenter();
                setMarkerPoint(latlng);
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
                lat: $.cnmap.GPS.convert($("#the-place span.lat").text()),
                lng: $.cnmap.GPS.convert($("#the-place span.lng").text()),
                address: $("#the-address").text(),
                vendor: "qq"
            };
            if (placeData.lat || placeData.lng) {
                var data = $('.list-group-item.map_photo_cell.active').data("data");
                data = $.extend({}, data, placeData);
                $('.list-group-item.map_photo_cell.active').data("data", data);
            }
        },
        setMarkerPoint: function (latlng) {
            if (marker) {
                marker.setPosition(latlng);
                if (!marker.getMap()) {
                    marker.setMap(map);
                }
                map.panTo(latlng)
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
                    setMarkerPoint(new qq.maps.LatLng(data.lat, data.lng));
                }
            });
        },
        clearPlace: function () {
            $("#the-place span.lat").text("");
            $("#the-place span.comma").hide();
            $("#the-place span.alt").show();
            $("#the-place span.lng").text("");
            $("#the-address").text("");
            $(".coder_place span.alt").show();
        }
    }
})(jQuery);
