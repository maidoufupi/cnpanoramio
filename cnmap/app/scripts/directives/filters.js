/**
 * Created by any on 2014/5/23.
 */
;angular.module('ponmApp.directives')
    .filter('newlines', function() {
        return function(text) {
            if(!text) {
                return "";
            }
            return text.split(/\n/g);
        };
    })
    .filter('calculatetime', function() {
        return function(time) {
            if(!time || !angular.isNumber(time)) {
                return "";
            }
            var date = new Date(time),
                cuTime = new Date();
            var diff = cuTime - date;
            var day = Math.round(diff / (1000 * 60 * 60 * 24));
            if(day < 1) {
                var hour = Math.round(diff / (1000 * 60 * 60));
                if(hour < 1) {
                    var minute = Math.round(diff / (1000 * 60)) + 1;
                    return minute+"分钟前";
                }else {
                    return hour+"小时前";
                }
            }else if(day > 5) {
                return time;
            }else {
                return day+"天前";
            }
        };
    })
;