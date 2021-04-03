$(document).ready(function(){
	
	$.getScript("/js/functions.js");
	
	$("#elszamolas_tablak_container").hide();
	
	let kotesModal = new bootstrap.Modal(document.getElementById('kotesModal'));
	
	$("#elszamolas_ugyfelek").change(function(){
		$.ajax({
			url:"/admin/ugyfel/projektek",
			type:"POST",
			dataType:"json",
			data:{ugyid:$(this).val()},
			success: function(succ){
				$("#elszamolas_projektek option").remove();
				$("<option value selected></option>").appendTo("#elszamolas_projektek");
				$(succ).each(function(i){
					$("<option value='"+succ[i].projekt.id+"'>"+succ[i].projekt.name+"</option>").appendTo("#elszamolas_projektek");
				});
			},
			error: function(err){
				console.log(JSON.stringify(err));
			}
		});
	});
	
	$("#elszamolas_projektek").change(function(){
		$.ajax({
			url:"/admin/ugyfel/ev",
			type:"POST",
			dataType:"POST",
			data:{projekt:$(this).val()},
			success: function(succ){
				console.log(JSON.stringify(succ));
			},
			error: function(err){
				$("#elszamolas_ev option").remove();
				$("<option value selected></option>").appendTo("#elszamolas_ev");
				let evSplit = err.responseText.split(",");
				$(evSplit).each(function(i){
					if(evSplit[i] != ""){
						$("<option value='"+evSplit[i]+"'>"+evSplit[i]+"</option>").appendTo("#elszamolas_ev");
					}
				});
			}
		});
	});
	
	$("#elszamolas_ev").change(function(){
		$.ajax({
			url:"/admin/ugyfel/honap",
			type:"POST",
			dataType:"json",
			data:{projekt:$("#elszamolas_projektek").val(),ev:$(this).val()},
			success: function(succ){

			},
			error: function(err){
				$("#elszamolas_honap option").remove();
				$("<option value selected></option>").appendTo("#elszamolas_honap");
				let splitString = err.responseText.split(",");
				console.log(splitString);
				$(splitString).each(function(i){
					if(splitString[i] != ""){
						$("<option value='"+splitString[i]+"'>"+splitString[i]+"</option>").appendTo("#elszamolas_honap");	
					}
				});
			}
		});
	});
	
	$("#elszamolas_form").submit(function(e){
		e.preventDefault();
		
		$.ajax({
			url:"/admin/ugyfel/elszamolas",
			type:"POST",
			dataType:"json",
			data:{ugyfel:$("#elszamolas_ugyfelek").val(),projekt:$("#elszamolas_projektek").val(),ev:$("#elszamolas_ev").val(),honap:$("#elszamolas_honap").val()},
			success: function(succ){
				
			},
			error: function(err){
				$("#elszamolas_tabla tbody tr").remove();
				let splitString = err.responseText.split(",");
				console.log(splitString);
				$("<tr><td>"+new Intl.NumberFormat({ style: 'currency'}).format(splitString[2])+" Ft</td><td>"+new Intl.NumberFormat({ style: 'currency'}).format(splitString[0])+" Ft</td><td id='"+splitString[1]+"'>"+splitString[1]+"</td><td>"+new Intl.NumberFormat({ style: 'currency'}).format(parseInt(splitString[0])*parseFloat(splitString[1]))+" Ft</td></tr>").appendTo("#elszamolas_tabla tbody");
				$("#elszamolas_tablak_container").show();
			}	
		});
		
	});
	
	$("#ugyfelek").change(function(){
	
		$("#projektek option").remove();
		
		KoteshezProjektek({id: $(this).val(), tipus: "ugyfel"});

	});
	
	$(".modosit").click(function(){
		let id = $(this).attr('id');
			$.ajax({
				url:'/admin/ugyfel/'+id,
				type:'GET',
				dataType:'json',
				success: function(succ){
					$(".nevMod").val(succ.name);
					$(".nevMod").attr('id', succ.id);
				},
				error: function(err){
					console.log(JSON.stringify(err));
				}
			});		
	});
	
	$(".kotesTorles").click(function(){
			let id = $(this).attr('id');
			$("#biztosText").text("Biztos törli?");
			biztosModal.toggle();
			$("#igenBiztos").click(function(){
				KotesTorles({id: id, tipus: "ugyfel"});
				biztosModal.hide();
				location.reload();
			});
		
	});
	
	
	
	$("#ugyfelModositasForm").submit(function(e){
		
		e.preventDefault();
				$("#biztosText").text("Biztos módosít?");
				biztosModal.toggle();
				$("#igenBiztos").click(function(){
					Modositas({id: $(".nevMod").attr('id'), nev: $(".nevMod").val(), tipus: "ugyfel"}, "#restAlertModositas");
					biztosModal.hide();
				});
		
	});
	
	$("#ujUgyfelForm").submit(function(e){
		
		e.preventDefault();
		
		Feltoltes({nev: $("#ujNev").val(), tipus: 'ugyfel'});
		
	});
	
	$("input[id='checkie']").click(function(){
		if($(this).prop('checked') == true){
		
			Aktivitas({id: $(this).attr('value'), aktiv: 1, tipus: 'ugyfel'});

		}else if($(this).prop('checked') == false){
		
			Aktivitas({id: $(this).attr('value'), aktiv: 0, tipus: 'ugyfel'});

		}
	});
	
	$("#kotesUgyfelForm").submit(function(e){
		
		e.preventDefault();
		kotesModal.hide();
		KotesFeltoltes({ugyfel: $("#ugyfelek").val(), projekt: $("#projektek").val(), oraber: $("#oraber").val(), tipus: "ugyfel"});
		
	});
	
});