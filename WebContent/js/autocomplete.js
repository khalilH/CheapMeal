$(function() {

	/*
	 * Modifier la fonction anonyme en fonction du traitement voulu avec les
	 * ingredients selectionnes va falloir bien reflechir pour ca
	 * 
	 */

	function readURL(input) {

		if (input.files && input.files[0]) {
			var reader = new FileReader();

			reader.onload = function(e) {
				 $('#previewImage').attr('src', e.target.result);
			}

			reader.readAsDataURL(input.files[0]);
		}
	}
	var changedFile = false;
	$("#uploadInput").change(function() {
		readURL(this);
		changedFile = true;
	});

	$(document)
			.on(
					'click',
					'.btn-add-ingredient',
					function(e) {
						e.preventDefault();

						var controlForm = $('.controls-ingredient:first'), currentEntry = $(
								this).parents('.entry-ingredient:first'), newEntry = $(
								currentEntry.clone()).appendTo(controlForm);

						newEntry.find('input').val('');
						controlForm
								.find(
										'.entry-ingredient:not(:last) .btn-add-ingredient')
								.removeClass('btn-add-ingredient')
								.addClass('btn-remove-ingredient')
								.removeClass('btn-success')
								.addClass('btn-danger')
								.html(
										'<span class="glyphicon glyphicon-minus"></span>');
						$('.autocomplete').autocomplete({
							serviceUrl : 'ingredients/autocomplete',
							noCache : true,
							maxHeight : 100,
							onSelect : function(suggestion) {
								// traiter selection ingredients
								var thing = "<p>" + suggestion.value + "</p>";
								$('#choix').append(thing);
							}
						});

					}).on('click', '.btn-remove-ingredient', function(e) {
				/*
				 * trouver comment supprimer l'ingredient du input (si il y en a
				 * un) de la liste des ingredients a envoyer au serveur
				 */
				$(this).parents('.entry-ingredient:first').remove();

				e.preventDefault();
				return false;
			});

	/* Code pour la liste des étapes (ca marche) */

	$(document)
			.on(
					'click',
					'.btn-add-etapes',
					function(e) {
						e.preventDefault();

						var controlForm = $('.controls-etapes'), currentEntry = $(
								this).parents('.entry-etapes:first'), newEntry = $(
								currentEntry.clone()).appendTo(controlForm);

						newEntry.find('input').val('');
						controlForm
								.find(
										'.entry-etapes:not(:last) .btn-add-etapes')
								.removeClass('btn-add-etapes')
								.addClass('btn-remove-etapes')
								.removeClass('btn-success')
								.addClass('btn-danger')
								.html(
										'<span class="glyphicon glyphicon-minus"></span>');
					}).on('click', '.btn-remove-etapes', function(e) {
				$(this).parents('.entry-etapes:first').remove();

				e.preventDefault();
				return false;
			});

	/* trouver comment faire pour pas dupliquer le code */
	$('.autocomplete').autocomplete({
		serviceUrl : 'ingredients/autocomplete',
		noCache : true,
		maxHeight : 100,
		onSelect : function(suggestion) {
			// traiter selection ingredient

		}
	});

	/* Cote MongoDB les mesures utilise son Rien, g, et cl */
	$("#formulaire_recette").on('submit',function(){
		publierRecette(this);
	});
	function publierRecette(form) {
		var ingredients, quantites, mesures, etapes;

		if (form.elements["ingredients[]"].length == undefined) {
			ingredients = form.elements["ingredients[]"].value;
			quantites = form.elements["quantites[]"].value;
			mesures = form.elements["mesures[]"].value;
		} else {
			tmpI = form.elements["ingredients[]"];
			tmpQ = form.elements["quantites[]"];
			tmpM = form.elements["mesures[]"];
			ingredients = tmpI[0].value + "";
			quantites = tmpQ[0].value + "";
			mesures = tmpM[0].value + "";
			for (i = 1; i < tmpI.length; i++) {
				ingredients += "," + tmpI[i].value;
				quantites += "," + tmpQ[i].value;
				mesures += "," + tmpM[i].value;
			}
		}

		if (form.elements["etapes[]"].length == undefined) {
			etapes = form.elements["etapes[]"].value;
		} else {
			tmp = form.elements["etapes[]"];
			etapes = tmp[0].value;
			for (i = 1; i < tmp.length; i++)
				etapes += "@" + tmp[i].value;
		}
		console.log(form.titre.value);
		console.log(ingredients);
		console.log(quantites);
		console.log(mesures);
		console.log(etapes);
		console.log("before Creer");
		if(form.titre.value == "" || ingredients == "" || quantites == "" || mesures == "" || etapes == ""){
			alert("Il manque des informations");
			return;
		}
		var re = new RegExp("-");
		if(re.test(quantites)){
			console.log("no");
			alert("Vous devez rentre une quantite positive");

			return;
		}
		if(!changedFile){
			console.log("photo");
			alert("Vous devez rentre une photo");
			return;
		}
		/* requete AJAX */
		creerRecette(form, form.titre.value, ingredients, quantites, mesures,
				etapes);

	};
	if ((bool = isConnected()) === 1) {
		loadNavbarConnected();
		console.log("connecte")

	} else if (bool === -1) { // User doesnt have a cookie let him browse
		console.log("Lutilisateur ne devrait pas etre la");
		window.location.href="accueil.html"
	} else { // User have an expirated key let him reconnect
		console.log("Invalide")
		window.location.href = "connexion.html";
		return;
	}
	function creerRecette(form, titre, ingredients, quantites, mesures,
			preparation) {

		var data = new FormData();
		console.log(form.uploadInput.files[0]);
		data.append('file', form.uploadInput.files[0]);
		data.append('titre', titre);
		data.append('cle',getCookie(C_NAME_KEY));
		data.append("ingredients", ingredients);
		data.append("quantites", quantites);
		data.append("mesures", mesures);
		data.append("preparation", preparation);
		console.log("Envoie");
		jQuery.ajax({
			url : 'recette/publier',
			data : data,
			cache : false,
			contentType : false,
			processData : false,
			type : 'POST',
			success : function(data) {
				var jsonrep = JSON.stringify(data);
				if (data.erreur == undefined) {
					console.log("OK recette cree");
					console.log(data);
					window.location.href="accueil.html";
					return;
				} else {
					
					if(data.erreur == 41){
						alert("Votre session a expiré");
						/* dans navbnar.js */
						deconnexion();
					}else{
						alert("Erreur: "+data.message);
					}
					
//					console.log("KO");
//					console.log(data);
//					alert("Probleme lors de la creation de la recette");
				}

			},
			error : function(resultat, statut, erreur) {
				console.log(resultat);
				console.log(statut);
				console.log(erreur);
				console.log("soucis");
			}
		});
	}

});
