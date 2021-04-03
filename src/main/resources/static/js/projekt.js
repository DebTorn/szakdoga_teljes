$(document).ready(function(){

	$.getScript("/js/functions.js");

	$("#projektFeltoltesForm").submit(function(e){
		
		e.preventDefault();
		
		Feltoltes({nev: $("#nev").val(), leiras: $("#leiras").val(), oradij: $("#oradij").val(), status: $("#status").val(), ptipus: $("#tipus").val(), tipus: 'projekt'});
		
	});
	
	$(".modosit").click(function(){
		
		let kieg = "#ModPro";
		
		$.ajax({
			url:'/admin/projekt/'+$(this).attr('id'),
			type:'GET',
			dataType:'json',
			success: function(succ){
				$(kieg + "Nev").val(succ.name);
				$(kieg + "Tipus").val(succ.type);
				$(kieg + "Status").val(succ.status);
				$(kieg + "Oraber").val(succ.tervezettoraber);
				$(kieg + "Leiras").val(succ.leiras);
				$("#hiddenId").val(succ.id);
			},
			error: function(err){
				console.log(JSON.stringify(err));
			}
		});
		
	});
	
	$("#projektModositasForm").submit(function(e){
		
		e.preventDefault();
		$("#biztosText").text("Biztos módosít?");
		biztosModal.toggle();
		$("#igenBiztos").click(function(){
			Modositas({id: $("#hiddenId").val(), nev: $("#ModProNev").val(), ptipus: $("#ModProTipus").val(), status: $("#ModProStatus").val(), oraber: $("#ModProOraber").val(), leiras: $("#ModProLeiras").val(), tipus: "projekt"},"#restAlertModositas");
			biztosModal.hide();
		});
		
	});
	
	$("#koltsegForm").submit(function(e){
		
		e.preventDefault();
		
		Feltoltes({proid: $("#projekt").val(), megnevezes: $("#megnevezes").val(), koltseg: $("#koltseg").val(), tipus: "koltseg"},"#restAlertKoltsegFeltoltes");
		
	});
	
	$("#koltsegModositasForm").submit(function(e){
		
		e.preventDefault();
		$("#biztosText").text("Biztos módosít?");
		biztosModal.toggle();
		$("#igenBiztos").click(function(){
			Modositas({id: $("#hiddenKoltsegId").val(), proid: $("#ModKoltsegProjekt").val(), megnevezes: $("#ModKoltsegMegnevezes").val(), koltseg: $("#ModKoltsegKoltseg").val(), tipus: "koltseg"},"#restAlertKoltsegModositas");
			biztosModal.hide();
		});
		
	});
	
	$(".koltsegModosit").click(function(){
	
		let kieg = "#ModKoltseg";
	
		$.ajax({
			url:"/admin/koltseg/"+$(this).attr("id"),
			type:"GET",
			dataType:"JSON",
			success: function(succ){
				$(kieg + "Projekt").val(succ.projekt.id);
				$(kieg + "Megnevezes").val(succ.megnevezes);
				$(kieg + "Koltseg").val(succ.koltseg);
				$("#hiddenKoltsegId").val(succ.id);
			},
			error: function(err){
				console.log(JSON.stringify(err));
			}
		});
		
	});
	
	$(".koltsegTorles").click(function(){
		let id = $(this).attr('id');
		$("#biztosText").text("Biztos törli?");
		biztosModal.toggle();
		$("#igenBiztos").click(function(){
			$.ajax({
				url:"/admin/koltseg/torles",
				type:"POST",
				dataType:"json",
				data:{id: id, tipus: "projekt"},
				success: function(succ){
					console.log(JSON.stringify(succ));
				},
				error: function(err){
					console.log(JSON.stringify(err));
				}
			});
		});
	});
	
});