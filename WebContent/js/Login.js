$(function() {

	$("form[name='login']").submit(function(event) {
		event.preventDefault(); // Empêche le navigateur de suivre le lien.
		var mdp = this.password.value;
		var login = this.login.value;
		if(verif_Form(login,mdp)){
			
		}else{
			
		}
	});
	
	
	function verif_Form(login,mdp){
		if(mdp == "" || login == ""){
			console.log("Attribute missing");
			return false;
		}
		if(mdp.length < 6){
			console.log("Mdp trop court");
			$
			return false;
		}
		
	}
});