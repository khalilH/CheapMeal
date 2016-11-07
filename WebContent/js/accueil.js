$(function (){
	//TODO Onload de la page regarde le cookie et mettre mode connecte
	$("#deconnexion").onClick(function(){
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
	function Recette(id,auteur, titre,photo){
		this.id = id;
		this.auteur = auteur;
		this.titre = titre;
		this.photo = photo;
	}
	Recette.prototype.getHtml=function(){
		var s ="<div class='col-md-6 col-lg-4 recette'>"+
		"<div class='recette-container'>"+
		"<div class='recette-header'></div>"+
		"<div class='recette-img'></div>"+
			"<img width='100%' height='100%' src="+this.photo+"</img>"+
		"<div class='recette-footer'>"+
		"<hr><h3 class='titre-recette'>"+this.titre+"</h3>"+
		"</div></div></div>"
	}
	function parseRecetteRecente(jsonResponse){
		
	}
	
});