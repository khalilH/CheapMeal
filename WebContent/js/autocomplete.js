$(function() {

  /*
  Modifier la fonction anonyme en fonction du traitement voulu avec les
  ingredients selectionnes
  va falloir bien reflechir pour ca

  */



  $(document).on('click', '.btn-add-ingredient', function(e)
  {
    e.preventDefault();

    var controlForm = $('.controls-ingredient:first'),
    currentEntry = $(this).parents('.entry-ingredient:first'),
    newEntry = $(currentEntry.clone()).appendTo(controlForm);

    newEntry.find('input').val('');
    controlForm.find('.entry-ingredient:not(:last) .btn-add-ingredient')
    .removeClass('btn-add-ingredient').addClass('btn-remove-ingredient')
    .removeClass('btn-success').addClass('btn-danger')
    .html('<span class="glyphicon glyphicon-minus"></span>');
    $('.autocomplete').autocomplete({
    serviceUrl: 'ingredients/autocomplete',
    noCache: true,
    maxHeight: 100,
    onSelect: function (suggestion) {
    //   traiter selection ingredients
    var thing = "<p>"+suggestion.value+"</p>";
    $('#choix').append(thing);
  }
});

}).on('click', '.btn-remove-ingredient', function(e)
{
  /* trouver comment supprimer l'ingredient du input (si il y en a un)
  de la liste des ingredients a envoyer au serveur */
  $(this).parents('.entry-ingredient:first').remove();

  e.preventDefault();
  return false;
});


/* Code pour la liste des étapes (ca marche)*/

$(document).on('click', '.btn-add-etapes', function(e)
{
  e.preventDefault();

  var controlForm = $('.controls-etapes'),
  currentEntry = $(this).parents('.entry-etapes:first'),
  newEntry = $(currentEntry.clone()).appendTo(controlForm);

  newEntry.find('input').val('');
  controlForm.find('.entry-etapes:not(:last) .btn-add-etapes')
  .removeClass('btn-add-etapes').addClass('btn-remove-etapes')
  .removeClass('btn-success').addClass('btn-danger')
  .html('<span class="glyphicon glyphicon-minus"></span>');
}).on('click', '.btn-remove-etapes', function(e)
{
  $(this).parents('.entry-etapes:first').remove();

  e.preventDefault();
  return false;
});


/* trouver comment faire pour pas dupliquer le code */
$('.autocomplete').autocomplete({
serviceUrl: 'ingredients/autocomplete',
noCache: true,
maxHeight: 100,
onSelect: function (suggestion) {
// traiter selection ingredient
var thing = "<p>"+suggestion.value+"</p>";
$('#choix').append(thing);
}
});

});

/* Cote MongoDB les mesures utilise son Rien, g, et cl */

function publierRecette(form) {
  var ingredients, quantites, mesures, etapes;

  if (form.elements["ingredients[]"].length == undefined) {
    ingredients = form.elements["ingredients[]"].value;
    quantites = form.elements["quantites[]"].value;
    mesures = form.elements["mesures[]"].value;
  }
  else {
    tmpI = form.elements["ingredients[]"];
    tmpQ = form.elements["quantites[]"];
    tmpM = form.elements["mesures[]"];
    ingredients = tmpI[0].value+"";
    quantites = tmpQ[0].value+"";
    mesures = tmpM[0].value+"";
    for(i=1; i<tmpI.length; i++) {
      ingredients += ","+tmpI[i].value;
      quantites += ","+tmpQ[i].value;
      mesures += ","+tmpM[i].value;
    }
  }

  if (form.elements["etapes[]"].length == undefined) {
    etapes = form.elements["etapes[]"].value;
  }
  else {
    tmp = form.elements["etapes[]"];
    etapes = tmp[0].value;
    for(i=1; i<tmp.length ; i++)
      etapes += "@"+tmp[i].value;
  }
  console.log(form.titre);
  console.log(ingredients);
  console.log(quantites);
  console.log(mesures);
  console.log(etapes);

  /* requete AJAX */

};
