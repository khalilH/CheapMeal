$(function (){
	var sumNote = 0;
	var nbRecettes = 0; 
	function Auteur(id,login){
		this.id = id;
		this.login = login;
	}
	function Profil(bio,login,photo){
		this.bio = bio;
		this.login = login;
		this.photo="../images/profil/"+photo+".png";
	}
	function Recette(id,auteur, titre,photo,ingredients, note){
		this.id = id;
		this.auteur = auteur;
		this.titre = titre;
		this.photo = "../images"+photo+".png";
		this.ingredients = ingredients;
	}
	Recette.prototype.getIngredientsString = function(){
		var s ="";
		for (var i = 0; i < this.ingredients.length; i++){
			s+=this.ingredients[i].nomIngredient;
			if(i != this.ingredients.length - 1)
				s+="; ";
		}
		return s;
	}
	Recette.prototype.getHtml=function(){
		var s ="<div class='col-sm-12'>"+
				"<div class='media well recette' id='"+this.id+"'>"+
				"<div class='media-left media-middle'>"
					"<img class='media-object img-recette' src='"+this.photo+"' alt='Generic placeholder image'>" +
				"</div>" +
				"<div class='media-body'>" +
					"<h4 class='media-heading'>"+this.titre+"</h4>" +
					"<p> Ingredients : <div class='text-muted'>"+this.getIngredientsString+"</div></p>" +
				"</div>";
				if(getCookie(C_NAME_ID) == this.auteur.id){
					console.log("L'user est connecte");
					s+= "<div class='media-right media-middle'>" +
							"<button name='"+this.id+"' class='btn btn-lg btn-default btn-delete'>" +
								"<span class='glyphicon glyphicon-trash'></span>" +
						"</div>"
				}
				s+= "</div>" +
				"</div>";
		return s;
	}
	
	function InformationProfil(recettes,profil){
		this.recettes = recettes;
		this.profil = profil;
	}
	InformationProfil.prototype.getHtml = function(){
		var s ="";
		for (var  i = 0 ; i < this.recettes.length; i++){
			s+=this.recettes[i].getHtml();
		}
	}
	function isNumber(s){
		return ! isNaN (s-0);
	}
	 function InfoRevival(key, value) {
			if(key.length == 0) /* "haut" du JSON ==fin */
			{
				var r;
				if((value.Erreur == undefined) || (value.Erreur == 0)){ // Si l'on trouve pas un champs Erreur dans le JSON
					r = new InformationProfil(value.recettes,value.profil);
					console.log("J'ai cree un r");
				}
				else {
					r = new Object();
					r.Erreur = value.Erreur;
				}
				return (r);
			}
			else if((isNumber(key)) && (value.auteur instanceof Auteur)) { // Si l'on est dans une case du tableau et que l'auteur est un objet de la classe auteur
				var recette = new Recette(value._id, value.auteur, value.titre, value.photo,value.ingredients,value.note.moyenne);
				console.log("J'ai cree recette");
				return recette;
			}
			else if(key == "auteur") { // Lorsquon doit crée un utilisateur
				var auteur;
				auteur = new Auteur(value.idAuteur, value.loginAuteur);
				console.log("J'ai cree toz "+auteur);

				return auteur;
			}else if(key == "profil"){
				var profil= new Profil(value.bio,value.login,value.photo);
				console.log("J'ai cree toz "+profil);
			}
			else{
				return (value);
			}
		}
		
		
		/**** BACK TO NORMAL JAVASCRIPT ****/
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
	 searchForProfil();
	 
	 function searchForProfil(){
		 var login = findLoginInURL();
		 $.ajax({
				url : 'profil/afficher',
				type : 'GET',
				data : "cle=w4HTyQ9BSE02lcSbSgoNAF2XIk9VXgvj&login="+login,
				contentType : 'application/x-www-form-urlencoded; charset=utf-8',
				dataType : 'json',
				success : function(rep) {
					var jsonrep = JSON.stringify(rep)
					if(jsonrep.erreur == undefined){
						var infoProfil = JSON.parse(jsonrep,InfoRevival);
						console.log(infoProfil);
					}
				},
				error : function(resultat, statut, erreur) {
					console.log("Bug");
					console.log(resultat);
					alert("dawg");
				}
			});
	 }
	 function findLoginInURL(){
		 var geturl = window.location.search.substr(1)
		 var regex = new RegExp(/login=(\w+)/);
		 var match = regex.exec(geturl);
		 console.log(match[1]);
		 return match[1];
	 }
	$("#connected").on('click',function(){
		console.log("connecte");
	});
	
	$("#addRecette").on('click',function(){
		console.log("add recette");
	});
	
	$(".recette").on('click',function(){
		console.log("Clique sur recette "+this.id);
		
	});
	
	$(".btn-delete").on('click',function(){
		console.log(this.name);
		
	});
});