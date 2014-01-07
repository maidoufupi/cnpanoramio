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
    var marker;
    $.cnmap = $.cnmap || {};
    $.cnmap.modal = {

        setPlace: function (lat, lng, address) { // 此参数为非gps坐标
//            $('.list-group-item.map_photo_cell.active').data("data").lat = lat;
//            $('.list-group-item.map_photo_cell.active').data("data").lng = lng;
            $("#the-place span.lng").text($.cnmap.GPS.convert(lng));
            $("#the-place span.comma").show();
            $("#the-place span.alt").hide();
            $("#the-place span.lat").text($.cnmap.GPS.convert(lat));

            if (address) {
                $("#the-address").text(address);
                $(".coder_place span.alt").hide();
            } else {
                $.cnmap.utils.gaode.getAddress(lat, lng, function (res) {
                    if (res.info == "OK") {
                        $("#the-address").text(res.regeocode.formattedAddress);
                        $(".coder_place span.alt").hide();
                    }
                })
            }

        },
        initMap: function (mapCanvas) {
            map = new AMap.Map(mapCanvas, {resizeEnable: true});          // 创建地图实例

            map.plugin(["AMap.ToolBar"], function () {
                var toolBar = new AMap.ToolBar();
                map.addControl(toolBar);
            });
            map.plugin(["AMap.MapType"], function () {
                //地图类型切换
                var type = new AMap.MapType({
                    defaultType: 0 //使用2D地图
                });
                map.addControl(type);
            });
            // 新版geocoder配置
            $.cnmap.utils.gaode.map = map;
            $.cnmap.utils.gaode.init();
//            $.cnmap.utils.gaode.getLocation("中国", function (res) {
//                console.log(res);
//            })
        },
        initGeocoder: function () {
            // 创建地址解析器实例
            var setPlace = this.setPlace,
                savePlace = this.savePlace;
            marker = new AMap.Marker();
            //marker.setMap(map);
            marker.setDraggable(true);
            AMap.event.addListener(marker, "dragend", function (event) {
                setPlace(event.lnglat.lat, event.lnglat.lng);
            });

            $('#geocoder_form').submit(function (event) {
                event.preventDefault();
                // 将地址解析结果显示在地图上,并调整地图视野
                $.cnmap.utils.gaode.getLocation($("#location-search-input").val(), function (res) {
//                    console.log(res);
                    if (res.info == "OK") {
                        var latlng = res.geocodes[0].location;
                        map.setCenter(latlng);
                        switch(res.geocodes[0].level) {
                            case '省':
                                map.setZoom(6);
                                break;
                            case '市':
                                map.setZoom(8);
                                break;
                            case '区县':
                                map.setZoom(11);
                                break;
                            case '乡镇':
                                map.setZoom(12);
                                break;
                            case '村庄':
                                map.setZoom(13);
                                break;
                            case '道路':
                                map.setZoom(14);
                                break;
                            case '兴趣点':
                                map.setZoom(15);
                        }

                        if (marker) {
                            marker.setPosition(latlng);
                            marker.setMap(map);
                            marker.show();
                            setPlace(latlng.lat, latlng.lng, res.geocodes[0].formattedAddress);
                        }
                    }
                });
            });

            $("#button-set-place").click(function () {
                var latlng = map.getCenter();
                marker.setPosition(latlng);
                marker.setMap(map);
                marker.show();
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
                lat: $.cnmap.GPS.convert($("#the-place span.lat").text()) || 0,
                lng: $.cnmap.GPS.convert($("#the-place span.lng").text()) || 0,
                address: $("#the-address").text(),
                vendor: "gaode"
            };
            if (placeData.lat || placeData.lng) {
                var data = $('.list-group-item.map_photo_cell.active').data("data");
                data = $.extend({}, data, placeData);
                $('.list-group-item.map_photo_cell.active').data("data", data);
            }
        },
        setMarkerPoint: function (point) {

            if (marker) {
                marker.setPosition(point);
                marker.setMap(map);
                marker.show();
                map.setCenter(marker.getPosition());
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
                    if (data.vendor && data.vendor == "baidu") {
                        setPlace(data.lat, data.lng);
                        setMarkerPoint(new AMap.LngLat(data.lng, data.lat));
                    } else {
                        setPlace(data.lat, data.lng);
                        setMarkerPoint(new AMap.LngLat(data.lng, data.lat));
//                        $.cnmap.utils.baidu.convert(data.lat, data.lng, function(point) {
//                            setPlace(point.lat, point.lng);
//                            setMarkerPoint(new AMap.LngLat(point.lng, point.lat));
//                        })
                    }


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
