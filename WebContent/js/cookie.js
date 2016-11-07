var C_NAME_KEY = "Cheap_Meal_key";

/** ********************** Cookie mnam mnam mnam ********************** */
function setCookie(cname, cvalue) {

	document.cookie = cname + "=" + cvalue;
}

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
				console.log("Oh oh it has been reinitialised.");
				return null;
			}
			console.log("[GetCookie] OK " + cname + ": " + str);
			return str;
		}
	}

	console.log("[GetCookie] Nothing to show");
	return null;
}

function destroy_cookie() {
	setCookie(C_NAME, "-1", "-1");
}
function loadNavbarDisconnected() {
	console.log("No cookie found, user is disconnected");
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
function loadNavbarConnected() {
	console.log("Cookie found, user is connected");
	var leftNavbarHtml = "<li class='active'><a href='accueil.html' class='menu-button'>"
			+ "<span class='glyphicon glyphicon-home'></span> Accueil</a></li>"
			+ "<li><a href='profile.html' class='menu-button'>"
			+ "<span class='glyphicon glyphicon-user'></span> Profile</a></li>"
			+ "<li><a href='compte.html' class='menu-button'>"
			+ "<span class='glyphicon glyphicon-cog'></span> Compte</a></li>";
	console.log($("#leftNavbar").html(leftNavbarHtml));

	var rightNavbarHtml = "<button id='deconnexion' type='button' class='btn btn-primary navbar-btn'>"
			+ "<span class='glyphicon glyphicon-log-out'></span> Déconnexion</button>";
	$("#rightNavbar").html(rightNavbarHtml);
}
function isConnected() {
	cookie = getCookie(C_NAME_KEY);
	if (cookie == null) {
		loadNavbarDisconnected();
	} else {
		loadNavbarConnected();
	}		

}
// function isConnected(callBack) {
//
// genId = getCookie(C_NAME);
// if(genId == null) {
// console.log("No previous session id.");
// callBack({});
// return;
// }
//
// $.ajax({
// url : "http://vps197081.ovh.net:8080/Issa/isconnected?",
// type : "get",
// crossDomain: false,
// data : "format=json" + "&session_id=" + genId,
// dataType : "json",
// success : function(rep) {
// callBack(rep);
// },
// error : function(jqXHR, textStatus, errorThrown) {
// // We do nothing if there isn't an active session
// console.log("Error(" + textStatus + ") : " + jqXHR.responseText);
// console.log("Maybe user is not connected.");
// callBack({});
// }
// });
// }

