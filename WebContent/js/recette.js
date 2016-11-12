//Starrr plugin (https://github.com/dobtco/starrr)
var __slice = [].slice;

(function($, window) {
	var Starrr;

	Starrr = (function() {
		Starrr.prototype.defaults = {
				rating: void 0,
				numStars: 5,
				change: function(e, value) {

					var idRecette = $.urlParam("idRecette"); 

					$.ajax({
						url : 'recette/noter',
						type : 'POST',
						data : "cle="+getCookie(C_NAME_KEY)+"&idRecette="+idRecette+"&note="+value,
						contentType : 'application/x-www-form-urlencoded; charset=utf-8',
						dataType : 'json',
						success : function(rep){

							console.log("Notation reussie");
							console.log(JSON.stringify(rep));
							var obj = JSON.parse(JSON.stringify(rep), Note.revival);

							if(obj.erreur == undefined){
								/* Remplacer la note affichee avec la nouvelle note */
								$("#noter-recette").html("<span style:'text-align:center'>Vous avez noté cette recette</span>");
								$("#note").html("<span>"+(obj.moyenne).toFixed(2)+"/5"+" ("+obj.nbNotes+" vote(s))</span>");
							}else{
								if(obj.erreur == 40){
									alert("Votre session a expiré");
									/* dans navbnar.js */
									deconnexion();
								}else{
									alert("Erreur: "+obj.message);
								}
							}

						},
						error : function(resultat, statut, erreur) {
							console.log("Bug");
							console.log(resultat);
							alert("Erreur Ajax");
						}
					});

					/* Une fois la recette notée, remplacer par un texte "Vous avez note cette recette" */
					/* Si l'utilisateur a deja note la recette, afficher directement la note qu'il a mise */
				}
		};

		function Starrr($el, options) {
			var i, _, _ref,
			_this = this;

			this.options = $.extend({}, this.defaults, options);
			this.$el = $el;
			_ref = this.defaults;
			for (i in _ref) {
				_ = _ref[i];
				if (this.$el.data(i) != null) {
					this.options[i] = this.$el.data(i);
				}
			}
			this.createStars();
			this.syncRating();
			this.$el.on('mouseover.starrr', 'span', function(e) {
				return _this.syncRating(_this.$el.find('span').index(e.currentTarget) + 1);
			});
			this.$el.on('mouseout.starrr', function() {
				return _this.syncRating();
			});
			this.$el.on('click.starrr', 'span', function(e) {
				return _this.setRating(_this.$el.find('span').index(e.currentTarget) + 1);
			});
			this.$el.on('starrr:change', this.options.change);
		}

		Starrr.prototype.createStars = function() {
			var _i, _ref, _results;

			_results = [];
			for (_i = 1, _ref = this.options.numStars; 1 <= _ref ? _i <= _ref : _i >= _ref; 1 <= _ref ? _i++ : _i--) {
				_results.push(this.$el.append("<span class='glyphicon .glyphicon-star-empty'></span>"));
			}
			return _results;
		};

		Starrr.prototype.setRating = function(rating) {
			if (this.options.rating === rating) {
				rating = void 0;
			}
			this.options.rating = rating;
			this.syncRating();
			return this.$el.trigger('starrr:change', rating);
		};

		Starrr.prototype.syncRating = function(rating) {
			var i, _i, _j, _ref;

			rating || (rating = this.options.rating);
			if (rating) {
				for (i = _i = 0, _ref = rating - 1; 0 <= _ref ? _i <= _ref : _i >= _ref; i = 0 <= _ref ? ++_i : --_i) {
					this.$el.find('span').eq(i).removeClass('glyphicon-star-empty').addClass('glyphicon-star');
				}
			}
			if (rating && rating < 5) {
				for (i = _j = rating; rating <= 4 ? _j <= 4 : _j >= 4; i = rating <= 4 ? ++_j : --_j) {
					this.$el.find('span').eq(i).removeClass('glyphicon-star').addClass('glyphicon-star-empty');
				}
			}
			if (!rating) {
				return this.$el.find('span').removeClass('glyphicon-star').addClass('glyphicon-star-empty');
			}
		};

		return Starrr;

	})();
	return $.fn.extend({
		starrr: function() {
			var args, option;

			option = arguments[0], args = 2 <= arguments.length ? __slice.call(arguments, 1) : [];
			return this.each(function() {
				var data;

				data = $(this).data('star-rating');
				if (!data) {
					$(this).data('star-rating', (data = new Starrr($(this), option)));
				}
				if (typeof option === 'string') {
					return data[option].apply(data, args);
				}
			});
		}
	});
})(window.jQuery, window);

$(function() {
	return $(".starrr").starrr();
});

/***** DOWN HERE CODE FOR PARSING JSON RESPONSE **********/
function Auteur(id,login){
	this.idAuteur = id;
	this.loginAuteur = login;
}

function Ingredient(nom,quantite,mesure){
	this.nomIngredient = nom;
	this.quantite = quantite;
	this.mesure = mesure;
}

function Note(moyenne,nbNotes){
	this.moyenne = moyenne;
	this.nbNotes = nbNotes;
}

function Recette(id,auteur,titre,ingredients,preparation,note,photo,prix){
	this.id = id;
	this.auteur = auteur;
	this.titre = titre;
	this.photo = photo;
	this.ingredients = ingredients;
	this.preparation = preparation;
	this.note = note;
	this.photo = photo;
	this.prix = prix;
}

function Note(moyenne,nbNotes,usersNotes){
	this.moyenne = moyenne;
	this.nbNotes = nbNotes;
	this.usersNotes = usersNotes;
}

function NoteUser(idUser, userNote){
	this.idUser = idUser;
	this.userNote = userNote;
}

Note.revival = function(key, value){
	if(key.length == 0){
		var n;
		if((value.Erreur == undefined) || (value.Erreur == 0)){ 
			// Si l'on trouve pas un champs Erreur dans le JSON
			n = new Note(value.moyenne, value.nbNotes, value.usersNotes);
			return n;
		}
		else{
			n = new Object();
			n.Erreur = value.Erreur;
			return n;
		}
	}else{
		return value;
	}
}

Recette.revival = function(key, value){
	if(key.length == 0){
		var r;
		if((value.Erreur == undefined) || (value.Erreur == 0)){ 
			// Si l'on trouve pas un champs Erreur dans le JSON
			r = new Recette(value._id, value.auteur, value.titre, value.ingredients, value.preparation, value.note, value.photo, value.prix.toFixed(2));
		}
		else {
			r = new Object();
			r.Erreur = value.Erreur;
		}
		return (r);
	}else if((isNumber(key)) && value.nomIngredient != undefined) {
		//cas où on est dans le tableau d'ingrédients
		var ingr = new Ingredient(value.nomIngredient, value.quantite, value.mesure);
		return ingr;
	}else if((isNumber(key)) && value.idUser != undefined){
		var usrNote = new NoteUser(value.idUser, value.userNote);
		return usrNote;
	}
	else if(key == "auteur"){
		var auteur;
		auteur = new Auteur(value.idAuteur, value.loginAuteur);
		return auteur;
	}
	else if(key == "note"){
		var note;
		note = new Note(value.moyenne, value.nbNotes, value.usersNotes);
		return note;
	}
	else {
		return value;
	}
}

Recette.traiteReponseJSON = function(json_text){

	//console.log(JSON.stringify(json_text));

	//obj est une Recette
	var obj = JSON.parse(JSON.stringify(json_text), Recette.revival);

	//console.log(JSON.stringify(obj));

	if(obj.erreur == undefined){

		$("#titre-recette").html("<span>"+obj.titre+"</span>");

		/* photo recette */
		$("#photo-recette").html("<center><img class='img-responsive' id='recettePhoto' " +
				"src='"+"../images/"+obj.photo+".png"+"' alt='Recette Picture'/></center>");

		$("#nom-auteur-recette").html("<a href='profile.html?login="+obj.auteur.loginAuteur+"'>"+obj.auteur.loginAuteur+"</a>");

		/* photo auteur */
		$("#photo-auteur").html("<center><img class='img-responsive' id='profilPicture' " +
				"src='"+"../images/profil/"+obj.auteur.loginAuteur+".png"+"' alt='Profil Picture' onerror=\"this.onerror=null;this.src='../images/profil/genericImage.png'\"/></center>");

		$("#note").html("<span>"+obj.note.moyenne.toFixed(2)+"/5"+" ("+obj.note.nbNotes+" votes)</span>");

		/* si l'utilisateur a deja note la recette, afficher le nombre d'etoile attribue */
		var varUsersNotes = obj.note.usersNotes;
		
		var i, aNote = false;
		var id = getCookie(C_NAME_ID);
		for(i=0; i<varUsersNotes.length; i++){
			if(varUsersNotes[i].idUser == id){
				aNote = true;
				break;
			}
		}

		if(isConnected() === 1){
			if(getCookie(C_NAME_LOGIN) != obj.auteur.loginAuteur){
				console.log("cookielogin="+getCookie(C_NAME_LOGIN)+" loginAuteur="+obj.auteur.loginAuteur);
				if(aNote == false){
					$("#notez").html("Notez cette recette:");
					$("#stars").show();
				}else{
					$("#stars").hide();
					$("#notez").html("<p>Vous avez donné "+varUsersNotes[i].userNote+" étoile(s) à cette recette.</p>");
				}
			}else{
				$("#stars").hide();
			}
		}else{
			$("#stars").hide();
		}

		var s = "<ul>";
		for(i=0; i<obj.ingredients.length; i++){
			var ingr = obj.ingredients[i];
			s+="<li><span class='txt-size-25'>"+ingr.nomIngredient+": "+ingr.quantite+" "+ingr.mesure+"</span></li>";
		}
		s+="</ul>";
		$("#ingr").html(s);
//		$("#prix").html("<span class='txt-size-35'>Prix estimé: </span><span class='txt-size-25'>"+obj.prix+"€</span>");
//		$("#prix").html("<div id=\"loading\"><ul class=\"bokeh\"> <li></li><li></li><li></li></ul></div>");
		$("#prix").html("<img class=\"loading\" src=\"images/loading_icon.gif\" width=\"110\" height=\"72\">");

		//$("#prep").html("<span class='txt-size-25'>"+obj.preparation+"</span>");

		var p = "<ul>";
		for(i=0; i<obj.preparation.length; i++){
			var prep = obj.preparation[i];
			p+="<li><span class='txt-size-25'>"+prep+"</span></li>";
		}
		p+="</ul>";
		$("#prep").html(p);

	}else{
		alert(obj)
	}
}

Recette.afficherPrix = function(json_text){

	console.log(JSON.stringify(json_text));

	var obj = JSON.parse(JSON.stringify(json_text));
	if (obj.erreur != undefined && obj.erreur == 666) {
		$.ajax({
			url : 'recette/prix',
			type : 'GET',
			data : 'idRecette='+idRecette+cle,
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : Recette.afficherPrix,
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				console.log(resultat);
			}
		});
	}
	else {
		console.log(obj.success);
		$("#prix").html("<span class='txt-size-35'>Prix estimé: </span><span class='txt-size-25'>"+obj.success+"€</span>");
	}

}


function isNumber(s){
	return ! isNaN (s-0);
}

/* Permet de recuperer un parametre qu ise trouve dans l'url */
$.urlParam = function(name){
	var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
	if (results==null){
		return null;
	}
	else{
		return results[1] || 0;
	}
}

$(document).ready(function() {

	$('#hearts').on('starrr:change', function(e, value){
		$('#count').html(value);
	});

	if((bool = isConnected()) === 1){
        loadNavbarConnected();
        console.log("connecte")
    }
    else if(bool === -1){ //User doesnt have a cookie let him browse
        loadNavbarDisconnected();
        console.log("deconnecte")
    }else{ // User have an expirated key let him reconnect
        console.log("Invalide")
        window.location.href="connexion.html";
        return;
    }
	
	/*
	if(isConnected() == -1){
		loadNavbarDisconnected();
	}else{
		loadNavbarConnected();
	}
	*/

	/* json_text = {
  		"date": 1478640910844,
  		"note": {
    		"moyenne": 4,
    		"nbNotes": 4,
    	"usersNotes": [
      				{
        				"idUser": 1,
        				"noteUser": 3
      				},
      				{
        				"idUser": 2,
        				"noteUser": 5
      				}
    			]
    		},
  		"titre": "Super Recette",
  		"ingredients": [
    				{
      					"mesure": "g",
      					"nomIngredient": "pomme",
      					"quantite": 10
    				}	
  			],
  		"_id": "5822450e96aa58213b3299a7",
  		"auteur": {
    		"idAuteur": 10,
    		"loginAuteur": "patra"
  		},
  		"preparation": [
    		"mettre le toz dans le four"
  		]
	}

	console.log(Recette.traiteReponseJSON(json_text));*/

	var idRecette = $.urlParam("idRecette"); 

	var cle = "";
	if(getCookie(C_NAME_KEY) != undefined)
		cle = "&cle="+getCookie(C_NAME_KEY);

	$.ajax({
		url : 'recette/afficher',
		type : 'GET',
		data : 'idRecette='+idRecette+cle,
		contentType : 'application/x-www-form-urlencoded; charset=utf-8',
		dataType : 'json',
		success : Recette.traiteReponseJSON,
		error : function(resultat, statut, erreur) {
			console.log("Bug");
			console.log(resultat);
			alert("Erreur Ajax");
		}
	});

	$.ajax({
		url : 'recette/prix',
		type : 'GET',
		data : 'idRecette='+idRecette+cle,
		contentType : 'application/x-www-form-urlencoded; charset=utf-8',
		dataType : 'json',
		success : Recette.afficherPrix,
		error : function(resultat, statut, erreur) {
			console.log("Bug");
			console.log(resultat);
		}
	});

});
