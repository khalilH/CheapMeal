$(function() {
	
	var login = getCookie(C_NAME_LOGIN);
	console.log(login);
	$("#login").val(login);
	$("#login").prop("disabled", true);
	
	function checkEmail(email){
		var mailRE = /^[a-zA-Z0-9._-]+@[a-z0-9._-]{2,}\.[a-z]{2,4}$/;
		return mailRE.test(email);
	}
	
//	$.validator.addMethod("emailValide", function(email) {
//		console.log("emailValide");
//		var mailRE = /^[a-zA-Z0-9._-]+@[a-z0-9._-]{2,}\.[a-z]{2,4}$/;
//		return mailRE.test(email);
//	});	
//	
//	$.validator.addMethod("pwdMatch", function(confirmation_mdp, dom, arg) {
//		console.log("confirmation="+confirmation_mdp + "  arg:" + arg.value.toString());
//		return confirmation_mdp === arg.value;
//	});
//	
//	console.log($("#form_compte").attr("id"));
//	
//	$("#form_compte").validate({
//		rules:{
//			newMail:{required: true, emailValide: newMail}
//		},
//		messages: {
//			newMail:{
//				required: "<p class='text-nowrap'>Email manquant</p>",
//				emailValide: "<p class='text-nowrap'>Email invalide</p>"
//			}
//		},
//		tooltip_options:{
//			newMail:{placement:'right', html:true},
//		},
//		submitHandler: function(form) {
//			/* appel ajax */
//			console.log("submitHandler");
//			
//		}
//	});
//	
//	$("#form-mdp").validate({
//		rules:{
//			currentMdp:{required: true, minLength:6},
//			newMdp:{required: true, minLength:6},
//			confirmationMdp:{required: true, pwdMatch: newMdp}
//		},
//		messages: {
//			currentMdp:{
//				required:"<p class='text-nowrap'>Mot de passe manquant</p>",
//				minlength:"<p class='text-wrap'>Contient au moins 6 caractères</p>"
//			},
//			 newMdp:{
//				required:"<p class='text-nowrap'>Mot de passe manquant</p>",
//				minlength:"<p class='text-wrap'>Doit contenir au moins 6 caractères</p>"
//			},
//			confirmationMdp:{
//				required:"<p class='text-wrap'>Les mots de passe doivent correspondre</p>",
//				pwdMatch:"<p class='text-wrap'>Les mots de passe doivent correspondre</p>",
//			},
//		},
//		tooltip_options:{
//			currentMdp:{placement:'right', html:true},
//			newMdp:{placement:'right', html:true},
//			confirmationMdp:{placement:'right', html:true}
//		},
//		submitHandler: function(form) {
//			/* appel ajax */
//			console.log("submitHandler");
//		}
//	});
	
	/**
	 * Fait appel au serveur pour la modification du mail
	 */
	function changerEmail(){
		console.log("changerEmail");
		
		var newEmail = $("#newMail").val();
		
		if(!checkEmail(newEmail)){
			alert("Veuillez entrer une adresse email valide");
			return;
		}
		
		console.log("changerEmail: ça passe");
		$.ajax({
			type: "POST",
			url: "user/changerEmail",
			data: "cle="+getCookie(C_NAME_KEY)+"&newemail="+newEmail,
			dataType: "json",
			success: function(rep){
				if (rep.erreur != undefined) {
					alert("Succès: votre adresse email a été modifiée");
				}else{
					alert("Erreur: "+rep.message);
				}
			},
			error:function(jaXHR, textStatus, errorThrown) {
				alert(jaXHR+" "+textStatus+" "+errorThrown);
			}
		});
		return;
	}
	$("#form_compte").on('submit', function(e){
		e.preventDefault();
		changerEmail();
	});
	
	/**
	 * Fait appel au serveur pour la modification du mot de passe
	 */
	function changerMdp(){
		
		var mdp = $("#currentMdp").val();
		var newMdp = $("#newMdp").val();
		var confirmationMdp = $("#confirmationMdp").val();
		
		if(newMdp.length < 6)
			alert("Le nouveau mot de passe doit contenir au moins 6 caractères");
		if($("#newMdp").val() != $("#confirmationMdp").val())
			alert("Les mots de passe doivent correspondre !");

		$.ajax({
			type: "POST",
			url: "user/changerMdp",
			data: "cle="+getCookie(C_NAME_KEY)+"&mdp="+mdp+"&newMdp="+newMdp+"&confirmationMdp="+confirmationMdp,
			dataType: "json",
			success: function(rep){
				if (rep.erreur != undefined) {
					alert("Succès: votre mot de passe a été modifiée");
				}else{
					alert("Erreur: "+rep.message);
				}
			},
			error:function(jaXHR, textStatus, errorThrown) {
				alert(jaXHR+" "+textStatus+" "+errorThrown);
			}
		});

		return;
	}
	$("#form-mdp").on('submit', function(e){
		e.preventDefault();
		changerMdp();
	});
	
	function changerBio(){
		
		var newBio = $("#bio").val();
		console.log(newBio);
		$.ajax({
			type: "POST",
			url: "profil/ajouterBio",
			data: "cle="+getCookie(C_NAME_KEY)+"&bio="+newBio,
			dataType: "json",
			success: function(rep){
				if (rep.erreur != undefined) {
					alert("Succès: votre bio a été modifiée");
				}else{
					alert("Erreur: "+rep.message);
				}
			},
			error:function(jaXHR, textStatus, errorThrown) {
				alert(jaXHR+" "+textStatus+" "+errorThrown);
			}
		});
		return;
	}
	
	$("#form-bio").on('submit', function(e){
		e.preventDefault();
		changerBio();
	});
		
		
});