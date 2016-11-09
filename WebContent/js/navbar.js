$(function(){
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
				destroy_cookie();
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
	

	
	$("#connexion").on('click',function(){
		window.location.href="connexion.html";
		return;
	});
	$("#signup").on('click',function(){
			window.location.href="inscription.html";
			return;
	});
		
	

	
});