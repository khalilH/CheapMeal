$(function() {

	function readURL(input) {

		if (input.files && input.files[0]) {
			var reader = new FileReader();

			reader.onload = function(e) {
//				$('#previewImage').attr('src', e.target.result);
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
		data.append('file-0', this.uploadInput.files[0]);
		jQuery.ajax({
	    url: 'upload',
	    data: data,
	    cache: false,
	    contentType: false,
	    processData: false,
	    type: 'POST',
	    success: function(data){
	        $('#previewImage').attr('src',"data:image/png;base64,"+data);
	    }
		});
	});
	
});