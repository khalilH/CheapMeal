$(function(){
	
function readURL(input) {

    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#previewImage').attr('src', e.target.result);
            console.log($('#previewImage'));
            }

        reader.readAsDataURL(input.files[0]);
    }
}

$("#uploadInput").change(function(){
	console.log($(this).val);
    readURL(this);
});

});