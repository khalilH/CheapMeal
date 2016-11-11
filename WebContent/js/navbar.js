$(function(){

	function deconnexion(){
		// TODO Delete cookie + verifier sil existe (erreur)
		var key = getCookie(C_NAME_KEY);
		console.log("DeConnexion de " + key);

		$.ajax({
			url : 'user/deconnexion',
			type : 'POST',
			data : 'cle=' + key,
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : function(rep) {
				console.log(JSON.stringify(rep));
//				var jsonrep = JSON.stringify(rep)
//				var json = JSON.parse(jsonrep);
				destroy_cookie(C_NAME_KEY);
				destroy_cookie(C_NAME_ID);
				destroy_cookie(C_NAME_LOGIN);
				window.location.href="accueil.html";
				return;
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				console.log(resultat);
				alert("Erreur Ajax");
			}
		});

	}

	$(document.body).on('click',"#deconnexion", deconnexion);

	$(document.body).on('click',"#connexion", function() {
		window.location.href = "connexion.html";
		return;
	});
	$(document.body).on('click',"#signup", function() {
		window.location.href = "inscription.html";
		return;
	});

});

