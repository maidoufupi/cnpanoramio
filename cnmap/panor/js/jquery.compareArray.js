/**
 * Created with JetBrains WebStorm.
 * User: tiwen.wang
 * Date: 14-1-7
 * Time: 下午9:47
 * To change this template use File | Settings | File Templates.
 */

(function($) {
    $.compareArray = function(arrayA, arrayB, callback) {
        var newArray = $.unique($.merge( $.merge( [], arrayA ), arrayB ));
        $.each(newArray, function(index, value) {
            if($.inArray( value, arrayA) > -1) {
               if($.inArray( value, arrayB) > -1) {
                   callback.apply(value, [null, value, null]);
               }else {
                   callback.apply(value, [value, null, null]);
               }
            }else {
                if($.inArray( value, arrayB) > -1) {
                    callback.apply(value, [null, null, value]);
                }
            }
        })
    }
})(jQuery)