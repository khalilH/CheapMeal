$(function() {

  $('.autocomplete').autocomplete({
    serviceUrl: 'autocomplete/ingredients',
    noCache: true,
    maxHeight: 100,
    onSelect: function (suggestion) {
      var thing = "<p>"+suggestion.value+"</p>";
      $('#choix').append(thing);
    }
  });
});
