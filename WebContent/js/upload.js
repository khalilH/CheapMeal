$(function() {

	function readURL(input) {

		if (input.files && input.files[0]) {
			var reader = new FileReader();

			reader.onload = function(e) {
				$('#previewImage').attr('src', e.target.result);
			}

			reader.readAsDataURL(input.files[0]);
		}
	}

	$("#uploadInput").change(function() {
		readURL(this);
	});

	$("#uploadForm").on('submit',function(event){
		event.preventDefault();
		var data = new FormData();
		console.log(this.uploadInput.files[0]);
		data.append('file', this.uploadInput.files[0]);
		data.append('titre','Super Recette');
		data.append('cle',"w4HTyQ9BSE02lcSbSgoNAF2XIk9VXgvj");
		data.append("ingredients","pomme");
		data.append("quantites","10");
		data.append("mesures","g");
		data.append("preparation","mettre le toz dans le four");
		jQuery.ajax({
	    url: 'recette/publier',
	    data: data,
	    cache: false,
	    contentType: false,
	    processData: false,
	    type: 'POST',
	    success: function(data){
	    	console.log(data);
	    }
		});
	});
	
});