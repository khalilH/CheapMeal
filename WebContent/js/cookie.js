var C_NAME   = "Cheap_Meal_cookie";

/************************ Cookie mnam mnam mnam ***********************/
function setCookie(cname, cvalue,login) {

    var login = "login=" +login;
    document.cookie = cname + "=" + cvalue + "; " + login;
    console.log("[SetCookie] OK " + cname + ", " + cvalue + ", " + login);
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');

    for ( var i = 0; i < ca.length; i++) {
        var c = ca[i];

        while (c.charAt(0) == ' ')
            c = c.substring(1);

        if (c.indexOf(name) == 0) {
            str = c.substring(name.length, c.length);
            if(str == "-1") {
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


function checkCookie(name) {
    us = getCookie(name);
    if (us != null) {
        // alert("Welcome again " + us);
    } else {
        console.log("[CheckCookie] " + name + " FAILED! ");
    }
}

function destroy_cookie() {
    setCookie(C_NAME, "-1", "-1");
}

//function isConnected(callBack) {
//
//    genId = getCookie(C_NAME);
//    if(genId == null) {
//        console.log("No previous session id.");
//        callBack({});
//        return;
//    }
//
//    $.ajax({
//        url : "http://vps197081.ovh.net:8080/Issa/isconnected?",
//        type : "get",
//        crossDomain: false,
//        data : "format=json" + "&session_id=" + genId,
//        dataType : "json",
//        success : function(rep) {
//            callBack(rep);
//        },
//        error : function(jqXHR, textStatus, errorThrown) {
//            // We do nothing if there isn't an active session
//            console.log("Error(" + textStatus + ") : " + jqXHR.responseText);
//            console.log("Maybe user is not connected.");
//            callBack({});        
//        }
//    });
//}


