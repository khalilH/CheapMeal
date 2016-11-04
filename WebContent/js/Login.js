$(function() {
	$("#login").validate({
		rules : {
			login : {
				required : true
			},
			password : {
				required : true,
				minlength : 6
			}

		},
		messages : {
			login : "<p class='text-nowrap'>Vous devez entrez un login</p>",
			password : {
				required : "<p>Mot de passe manquant</p>",
				minlength : "<p>Votre mot de passe est trop court</p>"
			}
		},
		tooltip_options : {
			login : {
				placement : 'right',
				html : true
			},
			password : {
				placement : 'right',
				html : true
			}
		},
		submitHandler : function(form) {
			requeteAJAX(form.login.value,form.password.value);
		}
	});

	function requeteAJAX(login, mdp) {
		console.log("Connexion de " + login + " mdp " + mdp);
		$.ajax({
			url : 'connexion',
			type : 'POST',
			data : 'login=' + login + "&mdp=" + mdp,
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : function(rep) {
				console.log(JSON.stringify(rep));
				if (rep.erreur == undefined) {
					console.log("Connexion reussi ",rep);
				} else {
					var ErrorBox = $("#ErrorMesage");
					ErrorBox.removeClass("hidden");
					console.log(ErrorBox.val+ "et "+ErrorBox.value);
					console.log("Connexion rate ",rep);
				}
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				alert("dawg");
			}
		});
	}
	
});
