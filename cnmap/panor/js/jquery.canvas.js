/**
 * Created with JetBrains WebStorm.
 * User: tiwen.wang
 * Date: 14-1-7
 * Time: 下午9:47
 * To change this template use File | Settings | File Templates.
 */

(function($) {
    $.cloneCanvas = function(oldCanvas) {

        //create a new canvas
        var newCanvas = $("<canvas/>");
        var context = newCanvas[0].getContext('2d');

        //set dimensions
        newCanvas[0].width = oldCanvas.width;
        newCanvas[0].height = oldCanvas.height;

        //apply the old canvas to the new one
        context.drawImage(oldCanvas, 0, 0, oldCanvas.width, oldCanvas.height);
        return newCanvas;
    }
})(jQuery)