$(document).ready(function(){

	$.getScript("/js/functions.js");
	
	$("#osszesites_container").hide();
	
	$("#elszamolasLekerdForm").submit(function(e){
		e.preventDefault();
		$("#osszesites_container").show();
		let data_tomb = {id:$("#osszProjekt").val(), ev:$("#projekthezEv").val(), honap:$("#projektEvhezHonap").val()};
		$.ajax({
			url:"/admin/elszamolas/dolgozotabla",
			type:"POST",
			dataType:"json",
			data:data_tomb,
			success: function(succ){
				console.log(succ);
				$(succ).each(function(){
					$("<tr><td>"+succ.dolgozo+"</td><td>"+(new Intl.NumberFormat().format(succ.netto))+" Ft</td><td>"+(new Intl.NumberFormat().format(succ.brutto))+" Ft</td></tr>").appendTo("#dolgozo_osszesito_tabla");
				});
			},
			error: function(err){
				console.log(JSON.stringify(err));
			}
		});
	});

	$("#modositasModalForm").submit(function(e){
		e.preventDefault();
		
		let raforditas = ($("#munkaOra").val()/10)*10 + ($("#munkaPerc").val()/60);
		raforditas = Math.round((raforditas + Number.EPSILON) * 100) / 100;
		
		$("#biztosText").text("Biztos módosít?");
		biztosModal.toggle();
		$("#igenBiztos").click(function(){
			Modositas({id: $("#munid").val(), datum: $("#munkavegzesDatum").val(), projekt: $("#projektNev").val(), user: $("#usid").val(), raforditas: raforditas, muntipus: $("#munkaTipus").val(), leiras: $("#munkaLeiras").val(), tipus: "munka"});		
			biztosModal.hide();
			modositasModal.hide();
			location.reload();
		});

	});
	
	$("#osszProjekt").change(function(){
		
		if($("#osszProjekt option").is(":selected")){
			
			//$("#osszProjekt option").remove();
			
			$.ajax({
				url:"/admin/munkak/projektev",
				type:"POST",
				dataType:"json",
				data:{projektid:$("#osszProjekt").val()},
				success: function(succ){
					
					$(succ).each(function(i){
						$("<option value="+succ[i]+">"+succ[i]+"</option>").appendTo("#projekthezEv");
					});	
					
							$("#projektEvhezHonap option").remove();
							
							$('<option id="nincs_ho" value="nincs">Nincs</option>').appendTo('#projektEvhezHonap');
			
							$.ajax({
								url:"/admin/munkak/projekthonap",
								type:"POST",
								dataType:"json",
								data:{projektid:$("#osszProjekt").val(), ev:$("#projekthezEv").val()},
								success: function(succ){
									if($("#projekthezEv option").is(":selected")){
										
										$(succ).each(function(i){
											if(i == 0){
												$("<option value="+succ[i]+" selected>"+succ[i]+"</option>").appendTo("#projektEvhezHonap");
											}else{
												$("<option value="+succ[i]+">"+succ[i]+"</option>").appendTo("#projektEvhezHonap");
											}
										});
									
									}
								},
								error: function(err){
									console.log(JSON.stringify(err));
								}
							});
						
				},
				error: function(err){
					
					console.log(JSON.stringify(err));
					
				}
				
			});
			
		}
		
	});
	
	$("#projekthezEv").change(function(){

            if($("#projekthezEv option").is(":selected")){

              $("#projektEvhezHonap option").remove();

              $('<option id="nincs_ho" value="nincs">Nincs</option>').appendTo('#projektEvhezHonap');

				$.ajax({
					url:"/admin/munkak/projekthonap",
					type:"POST",
					dataType:"json",
					data:{projektid:$("#osszProjekt").val(), ev:$("#projekthezEv").val()},
					success: function(succ){
										
						$(succ).each(function(i){
							$("<option value="+succ[i]+">"+succ[i]+"</option>").appendTo("#projektEvhezHonap");
						});
										
					},
					error: function(err){
						console.log(JSON.stringify(err));
					}
				});

            }

    });
	
	$(".torles").click(function(){
		let id = $(this).attr("id");
		$("#biztosText").text("Biztos törli?");
		biztosModal.toggle();
		$("#igenBiztos").click(function(){
			Torles({id: id, tipus: "munka"});
			biztosModal.hide();	
		});

	});

	$(".modosit").click(function(){
		let munkaid = $(this).attr('id');
		$("#munid").val(munkaid);
		$.ajax({
			url:"/admin/munka/"+munkaid,
			type:"GET",
			dataType:"json",
			success: function(succ){
				$("#munkavegzesDatum").val(succ.datum);
				$("#munkaTipus").val(succ.tipus);
				$("#munkaLeiras").val(succ.munkaleiras);
				let raforditas = succ.raforditas.toString().split(".");
				$("#munkaOra").val(raforditas[0]);
				if(raforditas.length == 1){
					$("#munkaPerc").val("0");
				}else{
					if(parseInt(raforditas[1]) == 5){
						$("#munkaPerc").val((((parseInt(raforditas[1])*60)/100)*10).toString());
					}else{
						$("#munkaPerc").val(((parseInt(raforditas[1])*60)/100).toString());
					}
				}
				
				$("table tr").each(function(){
				
					let sor = $(this);
				
					if(sor.attr("id") == munkaid){
								$("#usid").val($(sor.find("td")[1]).attr("value"));
								$("#projektNev option").remove();
								$.ajax({
									url:"/admin/dolgozo/"+$(sor.find("td")[1]).attr("value"),
									type:"GET",
									dataType:"json",
									success: function(succ){
										$(succ.userpro).each(function(i){
											if(succ.userpro[i].projekt.id == $(sor.find("td")[2]).attr("value")){
												$("<option value='"+succ.userpro[i].projekt.id+"' selected>"+succ.userpro[i].projekt.name+"</option>").appendTo("#projektNev");
											}else{
												$("<option value='"+succ.userpro[i].projekt.id+"'>"+succ.userpro[i].projekt.name+"</option>").appendTo("#projektNev");
											}
										});
									},
									error: function(err){
										console.log(JSON.stringify(err))
									}
								});
					}
					
					
					
				});
				
			},
			error: function(err){
				console.log(JSON.stringify(err));
			}
		});
	});
	
	
});