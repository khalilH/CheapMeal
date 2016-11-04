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
					console.log("Connexion Fail ",rep.message);
					changeErrorMessage(rep.erreur);
				}
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				alert("dawg");
			}
		});

		return false;

	}
	function changeErrorMessage(msg){
		var ErrorBox= $("#ErrorMessage");
		ErrorBox.html("<div class='alert alert-danger' id='ErrorMessage'>" +
				"<a class='close' data-dismiss='alert' aria-label='close'>×</a>" +
				msg + 
		"</div>");
	}

});
