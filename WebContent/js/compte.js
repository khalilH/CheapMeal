$(function() {
	
	
	$("#login").text(getCookie(C_NAME_LOGIN));
	$("#login").prop("disabled", true);
	
	$.validator.addMethod("emailValide", function(email) {
		var mailRE = /^[a-zA-Z0-9._-]+@[a-z0-9._-]{2,}\.[a-z]{2,4}$/;
		return mailRE.test(email);
	});	
	
	$("#form-compte").validate({
		rules:{
			newMail:{required: true, emailValide: newMail}
		},
		messages: {
			newMail:{
				required: "<p class='text-nowrap'>Email manquant</p>",
				emailValide: "<p class='text-nowrap'>Email invalide</p>"
			}
		},
		tooltip_options:{
			newMail:{placement:'right', html:true},
		},
		submitHandler: function(form) {
			/* appel ajax */
			console.log("helol");
			
		}
	});
	
	/**
	 * Fait appel au serveur pour la modification du mail
	 * @param cle
	 * @param newMail
	 */
	function changerEmail(newEmail){

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
	
	/**
	 * Fait appel au serveur pour la modification du mot de passe
	 * @param cle
	 * @param mdp
	 * @param newMdp
	 * @param confirmationMdp
	 */
	function changerMdp(mdp, newMdp, confirmationMdp){

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
	

	
	
});