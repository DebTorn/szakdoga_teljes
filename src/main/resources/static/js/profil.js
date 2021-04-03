$(document).ready(function(){
	
	$.getScript("/js/functions.js");

	$("#jelszoVisszaForm").submit(function(e){
		e.preventDefault();
		console.log($("#jelszoDolgozoUser").val());
		Feltoltes({id: $("#jelszoDolgozoUser").val(), tipus:"jelszo"});
	});
});