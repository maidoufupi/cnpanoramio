$( "#photoUpload" ).submit(function( event ) {
 
  // Stop form from submitting normally
  event.preventDefault();
 
  // Get some values from elements on the page:
  var $form = $( this ),
  term = $form.find( "input[name='file']" ).val(),
  url = $form.attr( "action" );
 
  // Send the data using post
  var posting = $.post( url, $('#photoUpload').serialize() );
 
  // Put the results in a div
  posting.done(function( data ) {
    alert(data);
  });
});
$("#photoUpload input").change(function() {
    $("#photoUpload").submit();
});