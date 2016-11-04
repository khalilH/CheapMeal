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
			console.log("toyze");
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
	
	$("#forgot").validate({
		rules: {
			forgotPassword :{required : true}
		},
		messages:{
			forgotPassword:"<p> Vous devez saisir un mail</p>"
		},
		submitHandler : function(form){
			console.log("valid");
		}
	});
	$("#send").on('click',function(){
		$("#forgot").validate();
		console.log("validee");

	});
	$('#forgotModal').on('shown.bs.modal',function(){
	    document.activeElement.blur()
		$('#forgotPassword').focus();
		$('.tooltip').remove();
	})
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
