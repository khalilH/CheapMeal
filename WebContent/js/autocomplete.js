$(function() {

/*
Modifier la fonction anonyme en fonction du traitement voulu avec les
ingredients selectionnes
va falloir bien reflechir pour ca

*/



  $(document).on('click', '.btn-add-ingredient', function(e)
      {
          e.preventDefault();

          var controlForm = $('.controls-ingredient form:first'),
              currentEntry = $(this).parents('.entry-ingredient:first'),
              newEntry = $(currentEntry.clone()).appendTo(controlForm);

          newEntry.find('input').val('');
          controlForm.find('.entry-ingredient:not(:last) .btn-add-ingredient')
              .removeClass('btn-add-ingredient').addClass('btn-remove-ingredient')
              .removeClass('btn-success').addClass('btn-danger')
              .html('<span class="glyphicon glyphicon-minus"></span>');
      }).on('click', '.btn-remove-ingredient', function(e)
      {
  		$(this).parents('.entry-ingredient:first').remove();

  		e.preventDefault();
  		return false;
  	});


    /* Code pour la liste des étapes (ca marche)*/
    /*
    $(document).on('click', '.btn-add-etapes', function(e)
        {
            e.preventDefault();

            var controlForm = $('.controls-etapes form:first'),
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
