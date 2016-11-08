$(function (){

	/***** DOWN HERE CODE FOR PARSING JSON RESPONSE **********/
	function Auteur(id,login){
		this.id = id;
		this.login = login;
	}

	function Recette(id,auteur, titre,photo){
		this.id = id;
		this.auteur = auteur;
		this.titre = titre;
		this.photo = photo;
	}
	Recette.prototype.getHtml=function(){
		var s ="<div  class='col-md-6 col-lg-4'>"+
		"<div class='recette-container recette' id='"+this.id+"' >"+
		"<div class='recette-header'></div>"+
		"<div class='recette-img'>"+
		"<img width='100%' height='100%' src='"+this.photo+"'</img></div>"+
		"<div class='recette-footer'>"+
		"<hr><h3 class='titre-recette'>"+this.titre+"</h3>"+
		"</div></div></div>";
		return s;
	}

	function RecetteList(recetteRecente, recetteBest){
		this.recettesRecentes = recetteRecente;
		this.recettesBest = recetteBest;
	}
	RecetteList.prototype.getHtmlRecent = function(){
		var s ="";
		for(var i = 0 ; i < this.recettesRecentes.length; i++){
			s+=this.recettesRecentes[i].getHtml()+"\n \n";
		}
		return s;

	}
	RecetteList.prototype.getHtmlBest = function(){
		var s ="";
		for(var i = 0 ; i < this.recettesBest.length; i++){
			s+=this.recettesBest[i].getHtml()+"\n \n";
		}
		return s;
	}

	function isNumber(s){
		return ! isNaN (s-0);
	}

	function recetteRevival(key, value) {
		if(key.length == 0) /* "haut" du JSON ==fin */
		{
			var r;
			if((value.Erreur == undefined) || (value.Erreur == 0)){ // Si l'on trouve pas un champs Erreur dans le JSON
				r = new RecetteList(value.recettesRecentes, value.recettesBest);
			}
			else {
				r = new Object();
				r.Erreur = value.Erreur;
			}
			return (r);
		}
		else if((isNumber(key)) && (value.auteur instanceof Auteur)) { // Si l'on est dans une case du tableau et que l'auteur est un objet de la classe auteur
			var recette = new Recette(value._id, value.auteur, value.titre, value.photo);
			return recette;
		}
		else if(key == "auteur") { // Lorsquon doit crée un utilisateur
			var auteur;
			auteur = new Auteur(value.idAuteur, value.loginAuteur);
			return auteur;
		}
		else{
			return (value);
		}
	}


	/**** BACK TO NORMAL JAVASCRIPT ****/

	if((bool = isConnected()) === 1){
		loadNavbarConnected();
	}
	else if(bool === -1){ //User doesnt have a cookie let him browse
		loadNavbarDisconnected();
	}else // User have an expirated key let him reconnect
		window.href.location="connexion.html";

	var json = {
			"recettesRecentes": [
				{"auteur": {
					"idAuteur": "10",
					"loginAuteur": "Maitre"
				},
				"_id" : "100",
				"titre" : "Pates pizza",
				"photo" : "images/favicon.png"
				},
				{"auteur": {
					"idAuteur": "12",
					"loginAuteur": "Maitre toz"
				},
				"_id" : "120",
				"titre" : "Pates crous",
				"photo" : "images/cheapmeal_logo.png"
				}
				],
				"recettesBest": [
					{"auteur": {
						"idAuteur": "10",
						"loginAuteur": "Maitre"
					},
					"_id" : "100",
					"titre" : "Pates pizza",
					"photo" : "images/favicon.png"
					},
					{"auteur": {
						"idAuteur": "12",
						"loginAuteur": "Maitre toz"
					},
					"_id" : "120",
					"titre" : "Pates crous",
					"photo" : "images/cheapmeal_logo.png"
					}
					]
	};
	json = JSON.stringify(json);
	var obj = JSON.parse(json,recetteRevival);
	if(!(obj instanceof RecetteList))
		console.log("toz");
	else{
		$("#recentRecipe").html(obj.getHtmlRecent());
		$("#BestRecipe").html(obj.getHtmlBest());
	}

	$("#deconnexion").on('click',function(){
		// TODO Delete cookie + verifier sil existe (erreur)
		var key;
		console.log("DeConnexion de "+key);

		$.ajax({
			url : 'deconnexion',
			type : 'POST',
			data : 'cle='+key,
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : function(rep) {
				console.log(JSON.stringify(rep));
				var jsonrep = JSON.stringify(rep)
				var json = JSON.parse(jsonrep);
				window.location.href="accueil.html";
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				console.log(resultat);
				alert("dawg");
			}
		});

	});

	$('.recette').on('click',function(){
		console.log("J'ai clique sur une recette ",this.id);
		//TODO Afficher la page de recette
	});

});