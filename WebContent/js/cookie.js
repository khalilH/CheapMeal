var C_NAME_KEY = "Cheap_Meal_key";
var C_NAME_LOGIN = "Cheap_Meal_login"

/**
 * Créer un cookie
 * @param cname le nom du cookie
 * @param cvalue la valeur du cookie
 */
function setCookie(cname, cvalue) {

	document.cookie = cname + "=" + cvalue;
}
/**
 * Récupère la valeur d'un cookie
 * @param cname le nom du cookie dont on souhaite la valeur
 * 
 */
function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');

	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];

		while (c.charAt(0) == ' ')
			c = c.substring(1);

		if (c.indexOf(name) == 0) {
			str = c.substring(name.length, c.length);
			if (str == "-1") {
				return null;
			}
			return str;
		}
	}

	console.log("[GetCookie] Nothing to show");
	return null;
}

/**
 * Permet de détruire un cookie en mettant sa valeur à -1
 */
function destroy_cookie() {
	setCookie(C_NAME, "-1");
}
/**
 * Permet de charger une navbar en mode déconnecte 
 * 
 */
function loadNavbarDisconnected() {
	var leftNavbarHtml = "<li class='active'><a href='accueil.html' class='menu-button'>"
			+ "<span class='glyphicon glyphicon-home'></span> Accueil</a></li>";
	$("#leftNavbar").html(leftNavbarHtml);

	var rightNavbarHtml = "<div class='row'><div class='col-xs-5'>"
			+ "<button id='deconnexion' type='button' class='btn btn-primary navbar-btn'>"
			+ "<span class='glyphicon glyphicon-log-in'></span> Connexion</button></div>"
			+ "<div class='col-xs-5 col-xs-offset-1'><button id='deconnezxion' type='button'"
			+ "class='btn btn-success navbar-btn'>"
			+ "<span class='glyphicon glyphicon-thumbs-up'></span> S'enregistrer</button></div></div>"
	$("#rightNavbar").html(rightNavbarHtml);
}

/**
 * Permet de charger une navbar en mode connecte 
 * 
 */
function loadNavbarConnected() {
	var leftNavbarHtml = "<li class='active'><a href='accueil.html' class='menu-button'>"
			+ "<span class='glyphicon glyphicon-home'></span> Accueil</a></li>"
			+ "<li><a href='profile.html' class='menu-button'>"
			+ "<span class='glyphicon glyphicon-user'></span> Profile</a></li>"
			+ "<li><a href='compte.html' class='menu-button'>"
			+ "<span class='glyphicon glyphicon-cog'></span> Compte</a></li>";

	var rightNavbarHtml = "<button id='deconnexion' type='button' class='btn btn-primary navbar-btn'>"
			+ "<span class='glyphicon glyphicon-log-out'></span> Déconnexion</button>";
	$("#rightNavbar").html(rightNavbarHtml);
}

/**
 * Requete ajax permettant de vérifier si un utilisateur est connecté
 * @param value la valeur de la clé à vérifier
 * @returns vrai si la clé est valide, faux sinon
 */
function ajaxKeyValideOrNot(value){
	$.ajax({
		 url : "isConnecte",
		 type : "GET",
		 data : "cle=" + value,
		 dataType : "json",
		 contentType : 'application/x-www-form-urlencoded; charset=utf-8',
		 success : function(rep) {
			 console.log("User is truly connected "+rep);
			 if(rep.Erreur == undefined)
				 return 1;
			 else
				 return 0;
		 },
		 error : function(jqXHR, textStatus, errorThrown) {
			 console.log("Crashed while doing ajax request for isConnected");
		 }
		 });
}
/**
 * Permet de vérifier si l'utilisateur est connecté
 * @returns vrai s'il est conencté
 */
function isConnected() {
	cookie_key = getCookie(C_NAME_KEY);
	if (cookie_key != null) {
		return ajaxKeyValideOrNot(cookie_key);
	}
	
	return -1;
}

