$(function (){
	//TODO Onload de la page regarde le cookie et mettre mode connecte
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
		"<div class='recette-img'></div>"+
			"<img width='100%' height='100%' src='"+this.photo+"'</img>"+
		"<div class='recette-footer'>"+
		"<hr><h3 class='titre-recette'>"+this.titre+"</h3>"+
		"</div></div></div>"
	}
	
	function RecetteList(recetteRecente, recetteBest){
		this.recetteRecente = recetteRecente;
		this.recetteBest = recetteBest;
	}
	
	RecetteList.prototype.getHtmlRecent = function(){
		var s ="";
		for(var i = 0 ; i < this.recetteRecente.length; i++){
			s+=this.recetteRecente[i].getHtml();
		}
		return s;
	}
	RecetteList.prototype.revival= function(key, value) {
			if(key.length == 0) /* "haut" du JSON ==fin */
			{
				var r;
				if((value.Erreur == undefined) || (value.Erreur == 0)){ // Si l'on trouve pas un champs Erreur dans le JSON
					r = new RecetteList(value.resultats, value.recherche, value.contact, value.auteur, value.date);
				}
				else {
					r = new Object();
					r.Erreur = value.Erreur;
				}
				return (r);
			}
			else if((isNumber(key)) && (value.auteur instanceof User)) { // Si l'on est dans une case du tableau et que l'auteur est un utilisateur
				var c = new Commentaire(value._id, value.auteur, value.texte, value.date, value.score);

				return c;
			}
			else if(key == "date"){ // Si la clé  est une date
				var d=new Date(value);

				return (d);
			}
			else if(key == "auteur") { // Lorsquon doit crée un utilisateur
				var u;
				if((env != undefined) && (env.users != undefined) && (env.users[value.idauteur] != undefined)){	
					u = env.users[value.idauteur];
				}
				else {
					u = new User(value.idauteur, value.login, value.contact);
				}

				return u;
			}
			else{
				return (value);
			}
		};
	}
	
});