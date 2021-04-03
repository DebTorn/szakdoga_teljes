let biztosModal = new bootstrap.Modal(document.getElementById('biztos'));
let modositasModal = new bootstrap.Modal(document.getElementById('modositasModal'));


function Feltoltes(json, alert=undefined){
	$.ajax({
			url:"/admin/feltoltes",
			type:"POST",
			dataType:"json",
			data:json,
			success: function(succ){
				if(alert != undefined){
					$(alert).toggleClass("alert-success");
					$(alert).text("Sikeres feltöltés!");
					setTimeout(window.location.reload.bind(window.location), 1000);	
				}else{
					$("#restAlertFeltoltes").toggleClass("alert-success");
					$("#restAlertFeltoltes").text("Sikeres feltöltés!");
					setTimeout(window.location.reload.bind(window.location), 1000);	
				}
			},
			error: function(err){
				if(alert != undefined){
					if(err.responseText == "success"){
						$(alert).toggleClass("alert-success");
						$(alert).text("Sikeres feltöltés!");
						setTimeout(window.location.reload.bind(window.location), 1000);
					}else{
						$(alert).toggleClass("alert-danger");
						$(alert).text("Sikertelen feltöltés!");
					}	
				}else{
					if(err.responseText == "success"){
						$("#restAlertFeltoltes").toggleClass("alert-success");
						$("#restAlertFeltoltes").text("Sikeres feltöltés!");
						setTimeout(window.location.reload.bind(window.location), 1000);
					}else{
						$("#restAlertFeltoltes").toggleClass("alert-danger");
						$("#restAlertFeltoltes").text("Sikertelen feltöltés!");
					}	
				}
			}
	});
}

function DolgozoFeltoltes(json){
	$.ajax({
			url:"/dolgozo/feltoltes",
			type:"POST",
			dataType:"json",
			data:json,
			success: function(succ){
				$("#restAlertMunkaido").toggleClass("alert-success");
				$("#restAlertMunkaido").text("Sikeres feltöltés!");
				setTimeout(window.location.reload.bind(window.location), 1000);
			},
			error: function(err){
				if(err.responseText == "success"){
					$("#restAlertMunkaido").toggleClass("alert-success");
					$("#restAlertMunkaido").text("Sikeres feltöltés!");
					setTimeout(window.location.reload.bind(window.location), 1000);
				}else{
					$("#restAlertMunkaido").toggleClass("alert-danger");
					$("#restAlertMunkaido").text("Sikertelen feltöltés!");
				}
			}
	});
}

function Modositas(json,alert){
	$.ajax({
			url:"/admin/modositas",
			type:"POST",
			dataType:"json",
			data:json,
			success: function(succ){
				$(alert).toggleClass("alert-success");
				$(alert).text("Sikeres módosítás!");
				setTimeout(window.location.reload.bind(window.location), 1000);
			},
			error: function(err){
				if(err.responseText == "success"){
					$(alert).toggleClass("alert-success");
					$(alert).text("Sikeres módosítás!");
					setTimeout(window.location.reload.bind(window.location), 1000);
				}else{
					$(alert).toggleClass("alert-danger");
					$(alert).text("Sikertelen módosítás!");
				}
			}
	});
}

function DolgozoModositas(json){
	$.ajax({
		url:"/dolgozo/modositas",
		type:"POST",
		dataType:"json",
		data:json,
		success: function(succ){
			$("#restAlertModositas").toggleClass("alert-success");
			$("#restAlertModositas").text("Sikeres módosítás!");
			setTimeout(window.location.reload.bind(window.location), 1000);
		},
		error: function(err){
			if(err.responseText == "success"){
				$("#restAlertModositas").toggleClass("alert-success");
				$("#restAlertModositas").text("Sikeres módosítás!");
				setTimeout(window.location.reload.bind(window.location), 1000);
			}else if(err.responseText == "lejart"){
				$("#restAlertModositas").toggleClass("alert-danger");
				$("#restAlertModositas").text("Lejárt a módosíthatóság!");
			}else{
				$("#restAlertModositas").toggleClass("alert-danger");
				$("#restAlertModositas").text("Sikertelen módosítás!");
			}
		}
	});
}

function Torles(json){
	$.ajax({
		url:"/admin/torles",
		type:"POST",
		dataType:"json",
		data:json,
		success: function(succ){
			
		},
		error: function(err){
			console.log(JSON.stringify(err));
			if(err.responseText == "success"){
				$("#popModal").toggleClass("alert-success");
				$("#popModal").text("Sikeres törlés!");
				setTimeout(window.location.reload.bind(window.location), 1000);
			}else{
				$("#popModal").toggleClass("alert-danger");
				$("#popModal").text("Sikertelen törlés!");
			}
		}
	});
}

function KotesTorles(json){
	$.ajax({
			url:'/admin/kotes/torles',
			type:'POST',
			dataType:'json',
			data:json,
			success: function(succ){
				
			},
			error: function(err){
				if(err.responseText == "van_munka"){
					$("#popModal").toggleClass("bg-warning");
					$("#popModal").text("Van a kötéshez tartozó munka. Elsőként azokat kell törölni!");
				}
				if(err.responseText == "success"){
					$("#popModal").toggleClass("bg-success");
					$("#popModal").text("Sikeres törlés!");
					setTimeout(window.location.reload.bind(window.location), 1000);
				}
				if(err.responseText == "error"){
					$("#popModal").toggleClass("bg-danger");
					$("#popModal").text("Sikertelen törlés!");
				}
				
			}
	});
}

function KotesFeltoltes(json){
	$.ajax({
			url:"/admin/kotes/feltoltes",
			type:"POST",
			dataType:"json",
			data:json,
			success: function(succ){
				$("#popModal").toggleClass("alert-success");
				$("#popModal").text("Sikeres feltöltés!");
				setTimeout(window.location.reload.bind(window.location), 1000);
			},
			error: function(err){
				if(err.responseText == "success"){
					$("#popModal").toggleClass("alert-success");
					$("#popModal").text("Sikeres feltöltés!");
					setTimeout(window.location.reload.bind(window.location), 1000);
				}else{
					$("#popModal").toggleClass("alert-danger");
					$("#popModal").text("Sikertelen feltöltés!");
				}
			}
	});
}

function Aktivitas(json){
	$.ajax({
			url:"/admin/aktivitas",
			type:"POST",
			dataType:"json",
			data:json,
			success: function(succ){

			},
			error: function(err){
				if(err.responseText == "success"){
					$("#popModal").toggleClass("alert-success");
					$("#popModal").text("Sikeres aktiválás!");
					setTimeout(window.location.reload.bind(window.location), 1000);
				}else{
					$("#popModal").toggleClass("alert-danger");
					$("#popModal").text("Sikertelen aktiválás!");
				}
			}
	});
}

function KoteshezProjektek(json){

		$("#projektek option").remove();

		$.ajax({
			url:"/admin/kotes/projektek",
			type:"POST",
			dataType:'json',
			data: json,
			success: function(succ){
				$(succ).each(function(i){
					$("<option value="+succ[i].id+">"+succ[i].name+"</option>").appendTo("#projektek");
				});
			},
			error: function(err){
				console.log(JSON.stringify(err));
			}
		});
}