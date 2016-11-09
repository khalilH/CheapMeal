$(function (){
	function Auteur(id,login){
		this.id = id;
		this.login = login;
	}
	
	function Recette(id,auteur, titre,photo,ingredients){
		this.id = id;
		this.auteur = auteur;
		this.titre = titre;
		this.photo = photo;
		this.ingredients = ingredients;
	}
	Recette.prototype.getIngredientsString = function(){
		var s ="";
		for (var int i = 0; i < this.ingredients.length; i++){
			s+=this.ingredients[i];
			if(i != this.ingredients.length - 1)
				s+="; ";
		}
		return s;
	}
	Recette.prototype.getHtml=function(){
		var s ="<div class='col-sm-12'>"+
				"<div class='media well recette' id='"+this.id+"'>"+
				"<div class='media-left media-middle'>"
					"<img class='media-object img-recette' src='images/"+this.photo+"' alt='Generic placeholder image'>" +
				"</div>" +
				"<div class='media-body'>" +
					"<h4 class='media-heading'>"+this.titre+"</h4>" +
					"<p> Ingredients : <div class='text-muted'>"+this.getIngredientsString+"</div></p>" +
				"</div>";
				if(getCookie(C_NAME_ID) !=undefined){
					s+= "<div class='media-right media-middle'>" +
							"<button class='btn btn-lg btn-default btn-delete'>" +
								"<span class='glyphicon glyphicon-trash'></span>" +
						"</div>"
				}
				s+= "</div>" +
				"</div>";
		return s;
	}
	
	function RecetteList(recettes){
		this.recettes = recettes;
	}
	RecetteList.prototype.getHtml = function(){
		var s ="";
		for (var int i = 0 ; i < this.)
	}
	$(".recette").on('click',function(){
		console.log("toz");
	});
	
	$(".btn-delete").on('click',function(){
		console.log("mdr");
	});
});