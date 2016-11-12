$(function() {
	var sumNote = 0;
	var nbRecettes = 0;
	
	
	function Auteur(id, login) {
		this.id = id;
		this.login = login;
	}
	function Profil(bio, login, photo) {
		this.bio = bio;
		this.login = login;
		this.photo = "../images/profil/" + photo + ".png?"+Date.now();
	}
	function Recette(id, auteur, titre, photo, ingredients, note) {
		this.id = id;
		this.auteur = auteur;
		this.titre = titre;
		this.photo = "../images/" + photo + ".png";
		this.ingredients = ingredients;
		this.note = note;
	}
	Recette.prototype.getIngredientsString = function() {
		var s = "";
		for (var i = 0; i < this.ingredients.length; i++) {
			s += this.ingredients[i].nomIngredient;
			if (i != this.ingredients.length - 1)
				s += "; ";
		}
		return s;
	}
	Recette.prototype.getHtml = function() {
		var s = "<div class='col-sm-12'>"
				+ "<div class='media well recette' id='" + this.id + "'>"
				+ "<div class='media-left media-middle'>"
				+ "<img class='media-object img-recette' src='" + this.photo
				+ "' alt='Generic placeholder image'>" + "</div>"
				+ "<div class='media-body'>" + "<h3 class='media-heading'>"
				+ this.titre + "</h3>"
				+ "<p> Ingredients : <div class='text-muted'>"
				+ this.getIngredientsString() + "</div></p>" + "</div>";
		if (getCookie(C_NAME_LOGIN) == this.auteur.login) {
			s += "<div class='media-right media-middle'>" + "<button data-id='"
					+ this.id + "' class='btn btn-lg btn-default btn-delete'>"
					+ "<span class='glyphicon glyphicon-trash'></span>"
					+ "</div>"
		}
		s += "</div>" + "</div>";
		return s;
	}

	function InformationProfil(recettes, profil) {
		this.recettes = recettes;
		this.profil = profil;
	}
	InformationProfil.prototype.getHtmlRecettes = function() {
		var s = "";
		if (getCookie(C_NAME_LOGIN) == this.profil.login) {
			s += "<div class='col-sm-12'>"
					+ "<button id='addRecette' type='button'"
					+ "class='btn btn-primary form-control mg-bottom-20'>"
					+ "<span class='glyphicon glyphicon-plus'></span> Ajouter une recette"
					+ "</button></div>";
			
		}
		for (var i = 0; i < this.recettes.length; i++) {
			s += this.recettes[i].getHtml();
		}

		if (this.recettes.length == 0) {
			s += "<div class='panel panel-primary mg-top-50'>"
					+ "<div class='panel-heading'>"
					+ "<span class='glyphicon glyphicon-thumbs-down'></span> Dommage"
					+ "</div>"
					+ "<div class='panel-body'>Malheuresement, cet utilisateur n'a jamais posté de recettes.</div>"
					+ "</div>";
		}
		return s;
	}

	InformationProfil.prototype.getHtmlProfilCard = function() {
		var s = "<div class='row pad15'><div class='col-sm-12'>" + "<h3>"
				+ this.profil.login + "</h3>"
				+ "<img class='img-responsive' id='profilPicture'" + "src='"
				+ this.profil.photo + "' alt='Profil Picture'>";
		if (getCookie(C_NAME_LOGIN) == this.profil.login) {
			s += "<div>"
					+ "<div id='connected' class='arrow-bottom'></div>"
					+ "<label class='form-control btn btn-primary btn-file'>Ajouter une image" +
					"<input id='inputFile' type='file' style='display: none;'></label></div>";
		}
		if (this.profil.bio != undefined) {
			s += "<blockquote>" + this.profil.bio + "</blockquote>";
		}
		s += "</div><div class='row '>"
				+ "<div class='col-sm-12 white-text mg-top-10'>"
				+ "<div class='col-sm-4 no-pad bg-blue'>" + "<div class='h4'>"
				+ "<span class='glyphicon glyphicon-user'></span> 0 Amis</div>"
				+ "</div><div class='col-sm-4 no-pad bg-pink'>"
				+ "<div class='h4'>"
				+ "<span class='glyphicon glyphicon-star'></span> " + getStars()+"/5"
				+ "</div>" + "</div><div class='col-sm-4 no-pad bg-green'>"
				+ "<div class='h4'>" 
				+ "<span class='glyphicon glyphicon-education'></span> " + nbRecettes
				+ "</div>" + "</div></div></div></div>";
		return s;
	}
	function getStars() {
		if (nbRecettes != 0) {
			return (sumNote / nbRecettes).toFixed(2);
		}
		return 0;
	}
	function isNumber(s) {
		return !isNaN(s - 0);
	}
	function InfoRevival(key, value) {
		if (key.length == 0) /* "haut" du JSON ==fin */
		{
			var r;
			if ((value.Erreur == undefined) || (value.Erreur == 0)) { // Si
																		// l'on
																		// trouve
																		// pas
																		// un
																		// champs
																		// Erreur
																		// dans
																		// le
																		// JSON
				r = new InformationProfil(value.recettes, value.profil);
			} else {
				r = new Object();
				r.Erreur = value.Erreur;
			}
			return (r);
		} else if ((isNumber(key)) && (value.auteur instanceof Auteur)) {
			var recette = new Recette(value._id, value.auteur, value.titre,
					value.photo, value.ingredients, value.note.moyenne);
			sumNote += recette.note;
			nbRecettes += 1;
			return recette;
		} else if (key == "auteur") { // Lorsquon doit crée un utilisateur
			var auteur;
			auteur = new Auteur(value.idAuteur, value.loginAuteur);

			return auteur;
		} else if (key == "profil") {
			var profil = new Profil(value.bio, value.login, value.photo);
			return profil;
		} else {
			return (value);
		}
	}

	/** ** BACK TO NORMAL JAVASCRIPT *** */
	
	
	if ((bool = isConnected()) === 1) {
		loadNavbarConnected();
		console.log("connecte")

	} else if (bool === -1) { // User doesnt have a cookie let him browse
		loadNavbarDisconnected();
		console.log("deconnecte")
	} else { // User have an expirated key let him reconnect
		console.log("Invalide")
		window.location.href = "connexion.html";
		return;
	}
	searchForProfil();

	function searchForProfil() {
		var login = findLoginInURL();
		if(login == undefined)
			login = getCookie(C_NAME_LOGIN);
		var url = "login="+login;
		if(getCookie(C_NAME_KEY) != undefined){
			url+="&cle="+getCookie(C_NAME_KEY);
		}
		$.ajax({
			url : 'profil/afficher',
			type : 'GET',
			data : url,
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : function(rep) {
				var jsonrep = JSON.stringify(rep)
				console.log(jsonrep);
				console.log(jsonrep.erreur);
				if (jsonrep.erreur == undefined) {
					var infoProfil = JSON.parse(jsonrep, InfoRevival);
					console.log(infoProfil);
					updatePage(infoProfil);
				}else{
					errorMessage();
				}
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				console.log(resultat);
				consol.log("Erreur Ajax Affichage Prix");
			}
		});
	}
	function errorMessage(){
		$("#leftPanel").html("<div class='alert alert-danger'><h1><strong>Attention !</strong> Vous tentez d'accéder à une page inconnue.</h1></div>");
	}
	function updatePage(infoProfil) {
		$("#rightPanel").html(infoProfil.getHtmlRecettes());
		$("#leftPanel").html(infoProfil.getHtmlProfilCard());
	}
	function findLoginInURL() {
		var geturl = window.location.search.substr(1)
		var regex = new RegExp(/login=(\w+)/);
		var match = regex.exec(geturl);
		if(match != undefined)
			return match[1];
		else
			return undefined;
	}

	function readURL(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#profilPicture').attr('src', e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	$("#leftPanel").on('change',"#inputFile",function() {
		event.preventDefault();
		var data = new FormData();
		var input = this;
		console.log(this.files[0]);
		data.append('file', this.files[0]);
		data.append('cle',getCookie(C_NAME_KEY));
		data.append('login',getCookie(C_NAME_LOGIN));
		console.log(this.files[0]);
		jQuery.ajax({
	    url: 'profil/uploadImage',
	    data: data,
	    cache: false,
	    contentType: false,
	    processData: false,
	    type: 'POST',
	    success: function(data){
	    	console.log(data);
	    	if(data.erreur == undefined){
	    		readURL(input);
	    	}
	    }
		});
	});
	
	$("#leftPanel").on('click', "#connected", function() {
		console.log("connecte");
		$("#inputFile").click();
	});

	$("#rightPanel").on('click', "#addRecette", function() {
		console.log("add recette");
		window.location.href="autocomplete.html";
	});

	$("#rightPanel").on('click', ".recette", function() {
		console.log("Clique sur recette " + this.id);
		window.location.href="recette.html?idRecette="+this.id;
	});

	$("#rightPanel").on('click', ".btn-delete", function(event) {
		console.log($(this).data('id'));
		var idRecette = $(this).data('id');
		event.stopPropagation();
		$('#myModal').modal('show'); 
		$('#confirmDelete').val(idRecette);
		

	});
	
	$('#confirmDelete').on('click',function(event){
		console.log("Confirm "+this.value);
		var idRecette = this.value;
		$('#'+idRecette).fadeOut(300, function(){ $(this).remove();});
		$.ajax({
			url : 'recette/supprimer',
			type : 'POST',
			data : 'cle='+getCookie(C_NAME_KEY)+"&idRecette="+idRecette,
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			dataType : 'json',
			success : function(rep) {
				console.log(JSON.stringify(rep));
				if(rep.erreur == undefined){
					$('#'+idRecette).fadeOut(300, function(){ $(this).remove();});
					$('#myModal').modal('hide');
				}else{
					if(obj.erreur == 40){
						alert("Votre session a expiré");
						/* dans navbnar.js */
						deconnexion();
					}else{
						alert("Erreur: "+obj.message);
					}
				}
				return;
			},
			error : function(resultat, statut, erreur) {
				console.log("Bug");
				console.log(resultat);
				alert("Erreur Ajax");
			}
		});
	});
});