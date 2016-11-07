$(function (){
	
	$("#deconnexion").onClick(function(){
		//TODO Delete cookie + verifier sil existe (erreur)
		var key;
		console.log("Connexion de " + login + " mdp " + mdp);
		
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
				if (rep.erreur == undefined) {
					console.log("Deconnexion reussi ",rep);
					window.location.href="accueil.html";
				} else {
					console.log("Connexion Fail ",rep.message);
					changeErrorMessage("#ErrorLogin", json.message);
				}
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				console.log(resultat);
				alert("dawg");
			}
		});
		
	});
	
});