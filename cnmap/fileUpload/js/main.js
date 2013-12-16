/*
 * jQuery File Upload Plugin JS Example 8.9.0
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2010, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */

/* global $, window */

$(function () {
    'use strict';

    var mapPhotoThumbTemplate;

    // Initialize the jQuery File Upload widget:
    $('#fileupload').fileupload({
        // Uncomment the following to send cross-domain cookies:
        //xhrFields: {withCredentials: true},
        //url: '/services/api/photo'
        formData: function() {
            var lat = this.lat,
                lng = this.lng,
                address = this.address;
            return [{
                name:"lat",
                value:lat
            },
                {
                    name:"lng",
                    value:lng
                },
                {
                    name:"address",
                    value:address
                }]
        },
        done: function (e, data) {
            if (e.isDefaultPrevented()) {
                return false;
            }
            if (data.result.id) {
                $(data.context.find("td#uploadButton button.start")).prop("disabled", true);
                $(data.context.find("td#uploadButton button.cancel")).prop("disabled", true);
                data.context.find("td#progress div.progress div").removeClass();
                data.context.find("td#progress div").removeClass();
                $("<a>").attr('href', '/panor-web/photo/' + data.result.id)
                    .append(data.context.find('td#preview span canvas'))
                    .appendTo(data.context.find('td#preview span:first'))
            } else {

            }
        },
        // The add callback is invoked as soon as files are added to the fileupload
        // widget (via file input selection, drag & drop or add API call).
        // See the basic file upload widget for more information:
        add: function (e, data) {
            if (e.isDefaultPrevented()) {
                return false;
            }
            var $this = $(this),
                that = $this.data('blueimp-fileupload') ||
                    $this.data('fileupload'),
                options = that.options;
            data.context = that._renderUpload(data.files)
                .data('data', data)
                .addClass('processing');
            options.filesContainer[
                options.prependFiles ? 'prepend' : 'append'
                ](data.context);
            that._forceReflow(data.context);
            $.when(
                    that._transition(data.context),
                    data.process(function () {
                        return $this.fileupload('process', data);
                    })
                ).always(function () {
                    data.context.each(function (index) {
                        $(this).find('.size').text(
                            that._formatFileSize(data.files[index].size)
                        );
                    }).removeClass('processing');
                    that._renderPreviews(data);
                }).done(function () {
                    data.context.find('.start').prop('disabled', false);
                    if ((that._trigger('added', e, data) !== false) &&
                        (options.autoUpload || data.autoUpload) &&
                        data.autoUpload !== false) {
                        data.submit();
                    }
                }).fail(function () {
                    if (data.files.error) {
                        data.context.each(function (index) {
                            var error = data.files[index].error;
                            if (error) {
                                $(this).find('.error').text(error);
                            }
                        });
                    }
                });

            $(data.context).find(".a-change-location").click(function () {

                var result = mapPhotoThumbTemplate({
                    items: [
                        {src: "", name: $(this).closest(".template-upload").data("data").files[0].name}
                    ]
                });
                if (result instanceof $) {
                    return result;
                }

                var oldCanvas = $(this).closest(".template-upload").data("data").files[0].preview;

                //create a new canvas
                var newCanvas = $("<canvas/>");
                var context = newCanvas[0].getContext('2d');

                //set dimensions
                newCanvas[0].width = oldCanvas.width;
                newCanvas[0].height = oldCanvas.height;

                //apply the old canvas to the new one
                context.drawImage(oldCanvas, 0, 0);

                $("#map-photo-list").html("");
                $("#map-photo-list").append(result);

                $("#map-photo-list a.list-group-item").data("data", {
                    lat: data.lat,
                    lng: data.lng,
                    updateLatlng: function () {
                        data.lat = this.lat;
                        data.lng = this.lng;
                        data.address = this.address;
                        $(data.context).find("div.location-display-place span.lat").text(this.lat);
                        $(data.context).find("div.location-display-place span.comma").text(",");
                        $(data.context).find("div.location-display-place span.lng").text(this.lng);
                        $(data.context).find("div.location-display-address").text(this.address);
                    }});
                $("#map-photo-list a.list-group-item p.map_photo_thumbnail").append(newCanvas);
                $.cnmap.modal.clearPlace();
                $.cnmap.modal.addEventListener();
                $('#myModal').modal('show');
                $('#myModal').on('shown.bs.modal', function (e) {
                    $('.list-group-item.map_photo_cell.active').click();
                });
            })
        }
    });

    $('#fileupload').fileupload(
        'option',
        'autoUpload',
        false
    );
// Enable iframe cross-domain access via redirect option:
    $('#fileupload').fileupload(
        'option',
        'redirect',
        window.location.href.replace(
            /\/[^\/]*$/,
            '/cors/result.html?%s'
        )
    );

    // Load existing files:
    $('#fileupload').addClass('fileupload-processing');
    $.ajax({
        // Uncomment the following to send cross-domain cookies:
        //xhrFields: {withCredentials: true},
        url: $('#fileupload').fileupload('option', 'url'),
        dataType: 'json',
        context: $('#fileupload')[0]
    }).always(function () {
            $(this).removeClass('fileupload-processing');
        }).done(function (result) {
            $(this).fileupload('option', 'done')
                .call(this, $.Event('done'), {result: result});
        });

    if (tmpl) {
        mapPhotoThumbTemplate = tmpl("template-mapPhotoThumb");
    }
});
