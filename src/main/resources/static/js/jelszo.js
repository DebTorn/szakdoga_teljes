$(document).ready(function(){

	$("#jelszoValtForm").submit(function(e){
		e.preventDefault();
		
		//DolgozoFeltoltes({});
		
	});
	
	$("#elfelejtettJelszoForm").submit(function(e){
		e.preventDefault();
		
		$.ajax({
				url:"/feltoltes",
				type:"POST",
				dataType:"json",
				data:json,
				success: function(succ){
					console.log(JSON.stringify(succ));
				},
				error: function(err){
					console.log(JSON.stringify(err));
				}
		});
		
	});

});