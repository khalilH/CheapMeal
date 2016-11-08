$(function() {
	
	if(isConnected() === 1){
		//Users is logged in need to redirect him
		window.location.href="accueil.html";
	}
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
			login : "<p class='text-nowrap'>Nom d'utilisateur manquant</p>",
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
			connexionAJAX(form.login.value,form.password.value);
		}
	});

	function connexionAJAX(login, mdp) {
		console.log("Connexion de " + login + " mdp " + mdp);

		$.ajax({
			url : 'connexion',
			type : 'POST',
			data : 'login=' + login + "&mdp=" + mdp,
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : function(rep) {
				var jsonrep = JSON.stringify(rep)
				var json = JSON.parse(jsonrep);
				if (rep.erreur == undefined) {
					console.log("Connexion reussi ",rep);
					setCookie(C_NAME_KEY,rep.success);
					setCookie(C_NAME_LOGIN, login);
					window.location.href="accueil.html";
				} else {
					console.log("Connexion Fail ",rep.message);
					changeErrorMessage("#ErrorLogin", json.message);
				}
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				console.log(resultat);
				alert("dawg");
			}
		});

		return false;

	}

	function changeErrorMessage(selector,msg){
		var ErrorBox= $(selector);
		console.log(msg+" toz");
		ErrorBox.html("<div class='alert alert-danger' id='ErrorMessage'>" +
				"<a class='close' data-dismiss='alert' aria-label='close'>×</a>" +
				msg + 
		"</div>");
	}
	
	function changeSuccessMessage(selector,msg){
		var ErrorBox= $(selector);
		console.log(msg+" toz");
		ErrorBox.html("<div class='alert alert-success' id='SuccessMessage'>" +
				"<a class='close' data-dismiss='alert' aria-label='close'>×</a>" +
				msg + 
		"</div>");
	}

	
	/*
	 *  Anything down here is for modal
	 */
	$("#forgot").validate({
		rules: {
			forgotPassword :{required : true}
		},
		messages:{
			forgotPassword:"<p>Adresse email invalide</p>"
		},
		tooltip_options : {
			forgotPassword:{ placement:'bottom',html:true}
		},
		submitHandler : function(form){
			forgotAJAX(form.forgotPassword.value);
		}
	});
	$("#send").on('click',function(){
		$("#forgot").submit();
		console.log("validee");

	});
	$('#forgotModal').on('shown.bs.modal',function(){
		document.activeElement.blur()
		$('#forgotPassword').focus();
		$('.tooltip').remove();
	})

	function forgotAJAX(mail) {
		console.log("Forgot de "+mail);

		$.ajax({
			url : 'recupmdp',
			type : 'get',
			data : 'mail='+mail,
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : function(rep) {
				console.log(JSON.stringify(rep));
				var jsonrep = JSON.stringify(rep)
				var json = JSON.parse(jsonrep);
				if (rep.erreur == undefined) {
					console.log("Recuperation reussi ",rep);
					changeSuccessMessage("#AlertMdpOublie", json.success);
				} else {
					console.log("Recuperation Fail ",rep.message);
					changeErrorMessage("#AlertMdpOublie",json.message);
				}
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				alert("dawg");
			}
		});

		return false;
	}

	function alignModal(){
		var modalDialog = $(this).find(".modal-dialog");
		// Applying the top margin on modal dialog to align it vertically center
		modalDialog.css("margin-top", Math.max(0, ($(window).height() - modalDialog.height()) / 4));
	}
	// Align modal when it is displayed
	$(".modal").on("shown.bs.modal", alignModal);

	// Align modal when user resize the window
	$(window).on("resize", function(){
		$(".modal:visible").each(alignModal);
	});   
});
