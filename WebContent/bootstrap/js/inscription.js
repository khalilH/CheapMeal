$(function() {

	jQuery.validator.addMethod("pwdMatch", function(pwd, confirm_pwd) {
	    return pwd.toUpperCase() === confirm_pwd.toUpperCase();
	});	

	$("#prenom").validate({
		rules: {
			prenom:{required: true},
			nom:{required: true},
			nom_utilisateur:{required: true},
			mot_de_passe:{required: true, minLength:6},
			mot_de_passe:{required: true, minLength:6},
	
		},
		messages: {
			login:"<p class='text-nowrap'>Vous devez entrez un login</p>",
			password:{
				required:"<p>Mot de passe manquant</p>",
				minlength:"<p>Votre mot de passe est trop court</p>"
			}
		},
		tooltip_options:{
			login:{placement:'right',html:true},
			password:{placement:'right', html:true}
		},
		submitHandler: function(form) {
			console.log(form.login.value+" et "+form.password.value);
		}
	});
	
	/**
	 * Demande au serveur la creation d'un compte apres avoir 
	 * verifie la validite du formulaire
	 * @param formulaire
	 * @returns true si la creation du compte est demandee, false sinon
	 */
	function inscription(formulaire){
		var prenom = formulaire.prenom.value;
		var nom = formulaire.nom.value;
		var nom_utilisateur = formulaire.nom_utilisateur.value;
		var mdp = formulaire.mot_de_passe.value;
		var confirmation_mdp = formulaire.confirmation_mdp.value;
		var email = formulaire.email.value;
		var ok = verificationFormulaireInscription(prenom, nom, nom_utilisateur, mdp, confirmation_mdp, email);
		if(ok){
			creationUtilisateur(prenom, nom, nom_utilisateur, mdp, email);
			return true;
		}	
		return false;
	}

	/**
	 * Fait appel au serveur pour la creation du compte
	 * @param firstName
	 * @param lastName
	 * @param login
	 * @param pwd
	 * @param mail
	 */
	function creationUtilisateur(prenom, nom, nom_utilisateur, mdp, email){

		require.config({
			paths: { "bcrypt": "bootstrap/js/bcrypt" }
		});
		require(["bcrypt"], function(bcrypt) {

			var bcrypt = require('bcrypt');
			var salt = bcrypt.genSaltSync(10);
			var hash = bcrypt.hashSync(mdp, salt);

			/* lors de la connexion pour savoir si c'est le bon mdp */
			/* bcrypt.compareSync(mdp, hash); avec hash le mdp qu'on a récup dans la base */

			$.ajax({
				type: "POST",
				url: "inscription",
				data: "prenom="+prenom+"&nom="+nom+"&login="+nom_utilisateur+"&mdp="+hash+"&email="+email,
				dataType: "json",
				success: traitementReponseInscription,
				error:function(jaXHR, textStatus, errorThrown) {
					alert(jaXHR+" "+textStatus+" "+errorThrown);
				}
			});

		});

		return;
	}

	/**
	 * Informe l'utilisateur du la reponse du serveur;
	 * @param rep
	 */
	function traitementReponseInscription(rep) {
		if (rep.erreur != undefined) {
			func_erreur(rep.message);
		}
		else {
			func_valid("Compte créé avec succès !")
			/* redirection vers la page de connexion */
			/* window.location.href = "login.jsp"; */
		}
	}


	/**************************************** Fonctions utilitaires ****************************************/

	function verificationFormulaireInscription(prenom, nom, nom_utilisateur, mdp, confirmation_mdp, email){
		if(mdp.length < 6){
			func_erreur("Le mot de passe doit contenir au moins 6 caractères");
			return false;
		}
		if (!emailValide(email)){
			func_erreur("L'adresse email n'est pas valide");
			return false;
		}
		if (!mdpValide(mdp)) {
			func_erreur("Le mot de passe doit contenir seulement des chiffres et des lettres");
			return false;
		}
		if (mdp != confirmation_mdp){
			func_erreur("Les mots de passe doivent correspondre");
			return false;
		}
		return true;
	}

	function mdpValide(mdp){
		var caracteresAutorises = new RegExp('[0-9a-z]{6,}','i');
		return caracteresAutorises.test(mdp);
	}

	function emailValide(email){
		var mailRE = /^[a-zA-Z0-9._-]+@[a-z0-9._-]{2,}\.[a-z]{2,4}$/;
		return mailRE.test(email);
	}

	function func_valid(msg){
		alert("Succès: "+msg)
		/* var msg_box="<div id=msg_valid>"+msg+"</div>";
	var old_msg1 = $("#msg_err");
	var old_msg2 = $("#msg_valid");
	if (old_msg1.length == 0 && old_msg2.length == 0) {
		$("#lastdiv").append(msg_box);
	}
	else if(old_msg1.length != 0){
		old_msg1.replaceWith(msg_box);
	}
	else if(old_msg2.length != 0){
		old_msg2.replaceWith(msg_box);
	} */
	}

	function func_erreur(msg) {
		alert("Erreur: "+msg);
		/*var msg_box="<div id=msg_err>"+msg+"</div>";
	var old_msg1 = $("#msg_err");
	var old_msg2 = $("#msg_valid");
	if (old_msg1.length == 0 && old_msg2.length == 0) {
		$("test").append(msg_box);
	}
	else if(old_msg1.length != 0){
		old_msg1.replaceWith(msg_box);
	}
	else if(old_msg2.length != 0){
		old_msg2.replaceWith(msg_box);
	} */
	}

});