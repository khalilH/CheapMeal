$(function() {

  $('.autocomplete').autocomplete({
    serviceUrl: 'autocomplete/ingredients',
    //lookup: arr,
    onSelect: function (suggestion) {
      var thing = "<p>"+suggestion.value+"</p>";
      $('#choix').append(thing);
    }
  });
});
