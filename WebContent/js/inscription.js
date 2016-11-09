$(function() {
	if(isConnected() === 1){
		//Users is logged in need to redirect him
		window.location.href="accueil.html";
		return;
	}
	$.validator.addMethod("pwdMatch", function(confirmation_mdp, dom, arg) {
		console.log("confirmation="+confirmation_mdp + "  arg:" + arg.value.toString());
		return confirmation_mdp === arg.value;
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
			creationUtilisateur(form.prenom.value, form.nom.value, form.nom_utilisateur.value, form.mot_de_passe.value, form.email.value);
		}
	});

	/**
	 * Demande au serveur la creation d'un compte apres avoir 
	 * verifie la validite du formulaire
	 * @param formulaire
	 * @returns true si la creation du compte est demandee, false sinon
	 */
//	function inscription(formulaire){
//	var prenom = formulaire.prenom.value;
//	var nom = formulaire.nom.value;
//	var nom_utilisateur = formulaire.nom_utilisateur.value;
//	var mdp = formulaire.mot_de_passe.value;
//	var confirmation_mdp = formulaire.confirmation_mdp.value;
//	var email = formulaire.email.value;
//	var ok = verificationFormulaireInscription(prenom, nom, nom_utilisateur, mdp, confirmation_mdp, email);
//	if(ok){
//	creationUtilisateur(prenom, nom, nom_utilisateur, mdp, email);
//	return true;
//	}	
//	return false;
//	}

	/**
	 * Fait appel au serveur pour la creation du compte
	 * @param firstName
	 * @param lastName
	 * @param login
	 * @param pwd
	 * @param mail
	 */
	function creationUtilisateur(prenom, nom, nom_utilisateur, mdp, email){

		$.ajax({
			type: "POST",
			url: "user/inscription",
			data: "prenom="+prenom+"&nom="+nom+"&login="+nom_utilisateur+"&mdp="+mdp+"&email="+email,
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