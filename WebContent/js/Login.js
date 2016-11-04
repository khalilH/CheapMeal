$(function() {

	$("#login").validate({
		rules: {
			login:{required: true},
			password:{required: true, minlength:6}
	
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

	function verif_Form(login, mdp) {
		if (mdp == "" || login == "") {
			console.log("Attribute missing");
			return false;
		}
		if (mdp.length < 6) {
			console.log("Mdp trop court");
			return false;
		}

	}
});
