/**
 * Created with JetBrains WebStorm.
 * User: tiwen.wang
 * Date: 13-12-15
 * Time: 下午5:45
 * To change this template use File | Settings | File Templates.
 */
(function ($) {
    "use strict";

    var map;
    var geocoder;
    var marker;
    var template_preview_thumb;

    $.cnmap = $.cnmap || {};
    $.cnmap.explore = {
       initMap: function(mapCanvas) {
           map = new AMap.Map(mapCanvas, {resizeEnable: true});
           map.plugin(["AMap.ToolBar"], function(){
               var toolBar = new AMap.ToolBar();
               map.addControl(toolBar);
           });
           map.plugin(["AMap.MapType"],function(){
               //地图类型切换
               var type= new AMap.MapType({
                   defaultType:0 //使用2D地图
               });
               map.addControl(type);
           });
           return map;
       },
       setPanoramioLayer: function() {
           var panoramioLayer = new $.cnmap.PanoramioLayer({suppressInfoWindows: true});
           panoramioLayer.setMap(map);

           if (tmpl) {
               template_preview_thumb = tmpl("template-preview-thumb");
           }
           $(panoramioLayer).bind("data_changed", function(e, data) {
               var result = template_preview_thumb({
                   items: data
               });
               $("#preview").html("");
               $("#preview").append(result);
//               $(".imgLiquidFill").imgLiquid({fill: true});
           })
       }
    }
})(jQuery)