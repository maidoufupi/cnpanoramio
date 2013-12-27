/**
 * Created with JetBrains WebStorm.
 * User: tiwen.wang
 * Date: 13-12-23
 * Time: 下午6:47
 * To change this template use File | Settings | File Templates.
 */

$(document).ready(function () {

    var url = '/panor-web/services/api/';
    var client = new $.RestClient(url, {
        stringifyData: true
    });
    client.add('settings');

    $("form#settings").on("submit", function (event) {

        event.preventDefault();

        var $form = $(this);
        var $target = $($form.attr('data-target'));
        var formdata = $form.serializeArray();
        var data = {};
        $.each(formdata, function(i, element) {
           data[element.name] = element.value;
        })
        client.settings.create(data).done(function (data) {
                if(data == true) {
                    $(".alert.alert-warning").fadeOut("fast");
                    $(".alert.alert-success").fadeIn("fast");
                    $(".alert.alert-success").fadeOut(5000, function() {
                        // Animation complete.
                    });

                }else{
                    $(".alert.alert-success").fadeOut("fast");
                    $(".alert.alert-warning").fadeIn("fast");
                    $(".alert.alert-warning").fadeOut(5000, function() {
                        // Animation complete.
                    });
                }
            });
    })

    PN_updateFieldsetStyles();
    $('#some_rights_reserved').change(PN_updateFieldsetStyles);
    $('#all_rights_reserved').change(PN_updateFieldsetStyles);

    function PN_updateFieldsetStyles() {
        if ($('#some_rights_reserved:checked').val()) {
            $('#conditions').removeClass('disabled');
            $('#comm-use-yes').removeAttr('disabled');
            $('#comm-use-no').removeAttr('disabled');
            $('#modify-yes').removeAttr('disabled');
            $('#modify-sa').removeAttr('disabled');
            $('#modify-no').removeAttr('disabled');
        } else {
            $('#conditions').addClass('disabled');
            $('#comm-use-yes').attr('disabled', true);
            $('#comm-use-no').attr('disabled', true);
            $('#modify-yes').attr('disabled', true);
            $('#modify-sa').attr('disabled', true);
            $('#modify-no').attr('disabled', true);
        }
    }

});