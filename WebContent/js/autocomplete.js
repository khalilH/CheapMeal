$(function() {

/*
Modifier la fonction anonyme en fonction du traitement voulu avec les
ingredients selectionnes
va falloir bien reflechir pour ca

*/



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
