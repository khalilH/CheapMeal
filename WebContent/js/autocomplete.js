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
//			traiter selection ingredient
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
	creerRecette(form.titre.value, ingredients, quantites, mesures, etapes);

};


function creerRecette(titre, ingredients, quantites, mesures, preparation){
/*	 $.ajax({
			url : 'recette/publier',
			type : 'POST',
			data : "cle=h4hB309V9S6XQJIY1McmQ7J0wguz9RhR&titre="+titre+"&ingredients="
			+ingredients+"&quantites="+quantites+"&mesures="+mesures+
			"&preparation="+preparation,
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : function(rep) {
				var jsonrep = JSON.stringify(rep)
				if(jsonrep.erreur == undefined){
					console.log("OK recette cree");
				}
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				console.log(resultat);
							}
		});
*/
	var data = new FormData();
	//console.log(this.uploadInput.files[0]);
//	data.append('file', this.uploadInput.files[0]);
	data.append('titre', titre);
	data.append('cle',"h4hB309V9S6XQJIY1McmQ7J0wguz9RhR");
	data.append("ingredients",ingredients);
	data.append("quantites",quantites);
	data.append("mesures",mesures);
	data.append("preparation", preparation);
	jQuery.ajax({
    url: 'recette/publier',
    data: data,
    cache: false,
    contentType: false,
    processData: false,
    type: 'POST',
    success: function(data){
    	var jsonrep = JSON.stringify(data);
    	if (jsonrep.erreur == undefined) {
    		console.log("OK recette cree");
        	console.log(data);	
    	}
    	else {
    		console.log("KO");
    		console.log(data);
    	}
    	
    },
	error: function(resultat, statut, erreur) {
		console.log("soucis");
	}
	});
}
