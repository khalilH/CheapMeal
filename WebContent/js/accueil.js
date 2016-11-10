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
		this.photo = "../images/"+photo+".png";
	}
	Recette.prototype.getHtml=function(){
		var s ="<div  class='col-md-6 col-lg-4'>"+
		"<div class='recette-container recette' id='"+this.id+"' >"+
		"<div class='recette-img'>"+
			"<img width='100%' height='100%' src='"+this.photo+"'</img></div>"+
		"<div class='recette-footer'>"+
		"<h3 class='titre-recette'>"+this.titre+"</h3>"+
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
		
		
	/**
	 *  CODE FIRING WHEN JQUERY IS READY 
	 */	
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
	searchForHomePage();
	
	function searchForHomePage(){
		//TODO remplacer par la bonne requete
		console.log("Loading home page")
		$.ajax({
			url : 'recette/getAccueil',
			type : 'GET',
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : function(rep) {
				var jsonrep = JSON.stringify(rep)
				if(jsonrep.erreur == undefined){
					var recetteListe = JSON.parse(jsonrep,recetteRevival);
					console.log(recetteListe);
					updatePage(recetteListe);
				}
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				console.log(resultat);
				alert("dawg");
			}
		});
		
	}
	
	function updatePage(liste){
		$("#recentRecipe").html(liste.getHtmlRecent());
		$("#BestRecipe").html(liste.getHtmlBest());
		

	}
	$("#searchForm").on('submit',function(event){
		event.preventDefault();
		var query = this.search.value;
		console.log(query);
	});
	
	$("#deconnexion").on('click',function(){
		// TODO Delete cookie + verifier sil existe (erreur)
		var key = getCookie(C_NAME_KEY);
		console.log("DeConnexion de "+key);
		
		$.ajax({
			url : 'user/deconnexion',
			type : 'POST',
			data : 'cle='+key,
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : function(rep) {
				console.log(JSON.stringify(rep));
				var jsonrep = JSON.stringify(rep)
				var json = JSON.parse(jsonrep);
				destroy_cookie(C_NAME_KEY);
				destroy_cookie(C_NAME_LOGIN);
				window.location.href="accueil.html";
				return;
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				console.log(resultat);
				alert("dawg");
			}
		});
		
	});
	
	$(document.body).on('click','.recette',function(){
		console.log("J'ai clique sur une recette ",this.id);
		window.location.href="recette.html?idRecette="+this.id;
	});

	
	
	
	
	
	
});