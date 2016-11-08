//Starrr plugin (https://github.com/dobtco/starrr)
var __slice = [].slice;

(function($, window) {
	var Starrr;

	Starrr = (function() {
		Starrr.prototype.defaults = {
				rating: void 0,
				numStars: 5,
				change: function(e, value) {
					/* A implémenter */

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
	this.prix = prix;
}

Recette.revival = function(key, value){
	if(key.length == 0){
		var r;
		if((value.Erreur == undefined) || (value.Erreur == 0)){ 
			// Si l'on trouve pas un champs Erreur dans le JSON
			r = new Recette(value._id, value.auteur, value.titre, value.ingredients, value.preparation, value.note, value.photo, value.prix);
		}
		else {
			r = new Object();
			r.Erreur = value.Erreur;
		}
		return (r);
	}else if((isNumber(key)) && !(typeof value === "string")) {
		//cas où on est dans le tableau d'ingrédients
		var ingr = new Ingredient(value.nomIngredient, value.quantite, value.mesure);
		return ingr;
	}
	else if(key == "auteur"){
		var auteur;
		auteur = new Auteur(value.idAuteur, value.loginAuteur);
		return auteur;
	}
	else if(key == "note"){
		var note;
		note = new Note(value.moyenne, value.nbNotes);
		return note;
	}
	else {
		return value;
	}
}

Recette.traiteReponseJSON = function(json_text){

	//obj est une Recette
	var obj = JSON.parse(JSON.stringify(json_text), Recette.revival);
	
	if(obj.erreur == undefined){
		
		if(isConnected() === -1){
			$("#noter-recette").html("");
		}
		
		
		$("#titre-recette").html("<span>"+obj.titre+"</span>");
		$("#nom-auteur-recette").html("<span>"+obj.auteur.loginAuteur+"</span>");
		/* photo auteur */
		$("#note").html("<span>"+obj.note.moyenne+"/5"+" ("+obj.note.nbNotes+" votes)</span>");
		/* si l'utilisateur a deja note la recette, afficher le nombre d'etoile attribue */

		var s = "<ul>";
		var i;
		for(i=0; i<obj.ingredients.length; i++){
			var ingr = obj.ingredients[i];
			s+="<li><span class='txt-size-25'>"+ingr.nomIngredient+": "+ingr.quantite+" "+ingr.mesure+"</span></li>";
		}
		s+="</ul>";
		$("#ingr").html(s);
		$("#prix").html("<span class='txt-size-35'>Prix estimé: </span><span class='txt-size-25'>"+obj.prix+"€</span>");

		var p = "<ul>";
		for(i=0; i<obj.preparation.length; i++){
			p+="<li><span class='txt-size-25'>"+obj.preparation[i]+"</span></li>";
		}
		p+="</ul>";
		$("#prep").html(p);

	}else{
		alert(obj.erreur)
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

	/*json_text = {
			"_id" : "5820a24696aa58767018a53f",
			"titre" : "Tarte aux pommes",
			"auteur" : { 
				"idAuteur" : 10, 
				"loginAuteur" : "patra" 
			},
			"ingredients" : [ 
				{ "nomIngredient" : "pomme", "quantite" : 5, "mesure" : "unite(s)" },
				{ "nomIngredient" : "pâte feuilleté", "quantite" : 1, "mesure" : "unite(s)" },
				{ "nomIngredient" : "sucre", "quantite" : 500, "mesure" : "g" }
				],
				"preparation" : [ 
					"couper les pommes", 
					"mettre à cuir pendant 50min"
					], 
					"note" : { 
						"moyenne" : 0,
						"nbNotes" : 0
					},
					"date" : "1478533702656",
					"prix" : "7.45"
	};
	
	Recette.traiteReponseJSON(json_text);*/
	
	var idRecette = $.urlParam("idRecette"); /* idRecette = getParameter */
	
	$.ajax({
		url : 'recette/afficher',
		type : 'GET',
		data : 'idRecette='+idRecette,
		contentType : 'application/x-www-form-urlencoded; charset=utf-8',
		dataType : 'json',
		success : Recette.traiteReponseJSON,
		error : function(resultat, statut, erreur) {
			console.log("Bug");
			console.log(resultat);
			alert("dawg");
		}
	});

});

