$(function() {
	if(isConnected() === 1){
		//Users is logged in need to redirect him
		window.location.href="accueil.html";
		return;
	}
	$.validator.addMethod("pwdMatch", function(confirmation_mdp, dom, arg) {
		console.log("confirmation="+confirmation_mdp + "  arg=" + arg.value.toString());
		return confirmation_mdp == arg.value;
	});

	$.validator.addMethod("emailValide", function(email) {
		var mailRE = /^[a-zA-Z0-9._-]+@[a-z0-9._-]{2,}\.[a-z]{2,4}$/;
		return mailRE.test(email);
	});	

	$("#formulaire_inscription").validate({
		rules: {
			prenom:{required: true},
			nom:{required: true},
			nom_utilisateur:{required: true},
			mot_de_passe:{required: true, minlength:6},
			confirmation_mdp:{required: true, pwdMatch: mot_de_passe},
			email:{required: true, emailValide: email}
		},
		messages: {
			prenom:"<p class='text-nowrap'>Prénom manquant</p>",
			nom:"<p class='text-nowrap'>Nom manquant</p>",
			nom_utilisateur:"<p class='text-nowrap'>Nom d'utilisateur manquant</p>",
			mot_de_passe:{
				required:"<p class='text-nowrap'>Mot de passe manquant</p>",
				minlength:"<p class='text-wrap'>Doit contenir au moins 6 caractères</p>"
			},
			confirmation_mdp:{
				required:"<p class='text-wrap'>Les mots de passe doivent correspondre</p>",
				pwdMatch:"<p class='text-wrap'>Les mots de passe doivent correspondre</p>",
			},
			email:{
				required: "<p class='text-nowrap'>Email manquant</p>",
				emailValide: "<p class='text-nowrap'>Email invalide</p>"
			}
		},
		tooltip_options:{
			prenom:{placement:'left', html:true},
			nom:{placement:'right', html:true},
			nom_utilisateur:{placement:'right', html:true},
			email:{placement:'right', html:true},
			mot_de_passe:{placement:'left', html:true},
			confirmation_mdp:{placement:'right', html:true}
		},
		submitHandler: function(form) {
			creationUtilisateur(form.prenom.value, form.nom.value, form.nom_utilisateur.value, form.mot_de_passe.value, form.confirmation_mdp.value, form.email.value);
		}
	});


	function creationUtilisateur(prenom, nom, nom_utilisateur, mdp, confirmationMdp, email){

		$.ajax({
			type: "POST",
			url: "user/inscription",
			data: "prenom="+prenom+"&nom="+nom+"&login="+nom_utilisateur+"&mdp="+mdp+"&confirmationMdp="+confirmationMdp+"&email="+email,
			dataType: "json",
			success: traitementReponseInscription,
			error:function(jaXHR, textStatus, errorThrown) {
				alert(jaXHR+" "+textStatus+" "+errorThrown);
			}
		});

		return;
	}

	/**
	 * Informe l'utilisateur du la reponse du serveur;
	 * @param rep
	 */
	function traitementReponseInscription(rep) {
		if (rep.erreur != undefined) {
//			func_erreur(rep.message);
			changeErrorMessage(rep.message);
		}
		else {
			console.log("je passe dans erreur");
			changeSuccessMessage(rep.message);
			/* redirection vers la page de connexion */
			window.location.href = "connexion.html"; 
			return;
		}
	}
	
	function changeErrorMessage(msg){
		var ErrorBox= $("#AlertMessage");
		ErrorBox.html("<div class='alert alert-danger' id='ErrorMessage'>" +
				"<a class='close' data-dismiss='alert' aria-label='close'>×</a>" +
				msg + 
		"</div>");
	}
	
	function changeSuccessMessage(msg){
		var ErrorBox= $("#AlertMessage");
		console.log(msg+" toz");
		ErrorBox.html("<div class='alert alert-success' id='SuccessMessage'>" +
				"<a class='close' data-dismiss='alert' aria-label='close'>×</a>" +
				msg + 
		"</div>");
	}


	/**************************************** Fonctions utilitaires ****************************************/

	function func_valid(msg){
		alert("Succès: "+msg)

	}

	function func_erreur(msg) {
		alert("Erreur: "+msg);
	}

});