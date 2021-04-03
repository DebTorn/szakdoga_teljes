$(document).ready(function(){
	
	$.getScript("/js/functions.js");
	
	let elszamolasModal = new bootstrap.Modal(document.getElementById('elszamolasModal'));
	
	$.ajax({
		url:"/admin/dolgozo/munka/tabla",
		type:"POST",
		dataType:"json",
		data:{tipus:"",id:$("#dolgozoId").attr("value")},
		success: function(succ){
			
			//console.log("asd: "+succ);
			let tipus = "";
			let raford = [];
			let kifele = "";
			let raforditas_osszesen = 0;
			let atlag_oradij = 0;
			let oradij_osszesen = 0;
			let darab = 0;
			let teljes_osszeg = 0;
			
			//tabla feltoltes
			$(succ).each(function(i){

				raforditas_osszesen += succ[i].raforditas;
				oradij_osszesen += succ[i].userpro.oraber;
				teljes_osszeg += succ[i].userpro.oraber*succ[i].raforditas;
				darab++;
				raford = succ[i].raforditas.toString().split(".");
				//console.log(raford[1]);
				if(parseInt(raford[1]) == 5) {
					kifele = raford[0] +" óra "+ ((parseInt(raford[1])*60)/100)*10 + " perc";
				}else {
					kifele = raford[0] +" óra "+ (parseInt(raford[1])*60)/100 + " perc";
				}
				$("<tr id='"+succ[i].id+"'><td id='datum'>"+succ[i].datum+"</td><td id='projekt' value='"+succ[i].userpro.projekt.id+"'>"+succ[i].userpro.projekt.name+"</td><td id='tipus'><p>"+succ[i].tipus.megnevezes+"</p></td><td id='leiras'>"+succ[i].munkaleiras+"</td><td id='raforditas' value='"+succ[i].raforditas+"'>"+kifele+"</td><td id='oraber' value='"+succ[i].userpro.oraber+"'>"+(new Intl.NumberFormat().format(succ[i].userpro.oraber))+" Ft</td><td id='osszeg' value='"+(succ[i].userpro.oraber*succ[i].raforditas)+"'>"+(new Intl.NumberFormat().format((succ[i].userpro.oraber*succ[i].raforditas)))+" Ft</td><td id='check"+succ[i].id+"'><input type='checkbox' name='elszamolas' id='elszamolas' value='"+succ[i].id+"'></td></tr>").appendTo("#munkak_tabla");
			});
			let datumSet = new Set();
			$("#munkak_tabla tbody tr").find("td").each(function(i){
				
				if($(this).attr("id") == "datum"){
					datumSet.add($(this).text());
				}
					
			});
			raford = raforditas_osszesen.toString().split(".");
			if(isNaN(raford[1])){
				raford[1] = 0;
			}
			if(parseInt(raford[1]) == 5) {
				kifele = raford[0] +" óra "+ ((parseInt(raford[1])*60)/100)*10 + " perc";
			}else {
				kifele = raford[0] +" óra "+ (parseInt(raford[1])*60)/100 + " perc";
			}
			$("#napok").text(datumSet.size);
			$("#raforditasOssz").text(kifele);
			$("#atlagOradij").text((new Intl.NumberFormat().format(oradij_osszesen/darab))+" Ft");
			$("#teljesOsszeg").text((new Intl.NumberFormat().format(teljes_osszeg))+" Ft");
			
			//$("<tr><td>"+datumSet.size+"</td><td>"+kifele+"</td><td>"+(new Intl.NumberFormat().format(oradij_osszesen/darab))+" Ft</td><td>"+(new Intl.NumberFormat().format(teljes_osszeg))+" Ft</td></tr>").appendTo("#osszesito_tabla");

			$("#munkak_tabla tr").each(function(){
				if($(this).attr("id") != undefined){
					let sor = $(this);
					$.ajax({
						url:"/admin/dolgozo/elszamolasok",
						type:"POST",
						dataType:"json",
						data:{mid:$(this).attr("id")},
						success: function(ss){
							if(ss == 1){
								$(sor.children()).each(function(){
									if($(this).attr("id").includes("check")){
										$(this).text("Elszámolva");
									}
								});
							}
						},
						error: function(ee){
							//console.log(JSON.stringify(ee));
						}
					});
				}
			});

			$("input[id='elszamolas']").click(function(){
				let aktivitas = $(this);
				if($(this).prop('checked') == true){
					let asdid = $(this).val();
					//console.log(asdid);
					$("#munkak_tabla tr").each(function(){
						if($(this).attr("id") == asdid){
							$($(this).children()).each(function(){
								if($(this).attr("id") == "datum"){
									$("#elszamDatum").val($(this).text());
									//console.log("raford: "+$(this).attr("value"));
								}
								if($(this).attr("id") == "osszeg"){
									$("#elszamOsszeg").val($(this).attr("value"));
									//console.log("osszeg: "+$(this).attr("value"));
								}
								//console.log("osszeg: "+$(this).attr("id"));
							});
						}
					});
					elszamolasModal.show();
					$("#elszamolasForm").submit(function(e){
						e.preventDefault();
						//console.log($("#elszamDatum").val());
						//console.log($("#elszamOsszeg").val());
						$.ajax({
							url:"/admin/dolgozo/elszamolas",
							type:"POST",
							dataType:"json",
							data:{datum:$("#elszamDatum").val(),osszeg:$("#elszamOsszeg").val(),id:asdid},
							success: function(succ){
								//console.log(JSON.stringify(succ));
							},
							error: function(err){
								if(err.responseText == "sikeres"){
									location.reload();
								}
							}
						});
					});
				}
			});

			
		},
		error: function(err){
			//console.log(err);
		}
	});
	
	    $("#munkak_tabla").change(function(){
		    let munkanapok = new Set();
		    let osszRaforditas = 0;
		    let osszOradij = 0;
		    let db = 0;
		    let teljesOsszeg = 0;
		    let raford = [];
		    let kifele = "";
		    $("#osszesito_tabla tbody tr td").each(function(){
		      $(this).text(0);
		    });
		    $("#munkak_tabla tbody tr td").each(function(i){
		      if($(this).attr("id") != undefined){
		        if($(this).attr("id").includes("datum")){
		          munkanapok.add($(this).text());
		        }
		        if($(this).attr("id").includes("raforditas")){
		          osszRaforditas += parseFloat($(this).attr("value"));
		        }
		        if($(this).attr("id").includes("oraber")){
		          osszOradij += parseInt($(this).attr("value"));
		          db++;
		        }
		        if($(this).attr("id").includes("osszeg")){
		          teljesOsszeg += parseInt($(this).attr("value"));
		        }
		      }
		    });
		    $("#napok").text(munkanapok.size);
		    raford = osszRaforditas.toString().split(".");
		    if(isNaN(raford[1])){
		        raford[1] = 0;
		    }
		    if(parseInt(raford[1]) == 5) {
		        kifele = raford[0] +" óra "+ ((parseInt(raford[1])*60)/100)*10 + " perc";
		    }else {
		        kifele = raford[0] +" óra "+ (parseInt(raford[1])*60)/100 + " perc";
		    }
		    $("#raforditasOssz").text(kifele);
		    $("#atlagOradij").text((new Intl.NumberFormat().format(osszOradij/db))+" Ft");
		    $("#teljesOsszeg").text((new Intl.NumberFormat().format(teljesOsszeg))+" Ft");
		  });
	
	document.getElementById('munkaidoModal').addEventListener('shown.bs.modal', function (e) {
		$.ajax({
			url:"/admin/dolgozo/"+$("#dolgozoId").val(),
			type:"GET",
			dataType:"json",
			success: function(succ){
				$("#munidoProjektek option").remove();
				$(succ.userpro).each(function(i){
					$("<option value='"+succ.userpro[i].projekt.id+"'>"+succ.userpro[i].projekt.name+"</option>").appendTo("#munidoProjektek");				
				});
			},
			error: function(err){
				//console.log(JSON.stringify(err));
			}
		});
	});
	
	$("#munkaidoForm").submit(function(e){
		e.preventDefault();
		let raforditas = ($("#munidoOra").val()/10)*10 + ($("#munidoPerc").val()/60);
		raforditas = Math.round((raforditas + Number.EPSILON) * 100) / 100;

		DolgozoFeltoltes({projekt: $("#munidoProjektek").val(), raforditas: raforditas, muntipus: $("#munidoTipus").val(), leiras: $("#munidoLeiras").val(), datum: $("#munidoDate").val(), tipus: "munka"});
	});
	
	$.ajax({
		url:"/admin/dolgozo/munka/ev",
		type:"POST",
		dataType:"json",
		data:{id:$("#dolgozoId").val()},
		success: function(succ){
			$("#ev_feltolt option").remove();
			$("<option selected disabled>Kiválsztás</option>").appendTo("#ev_feltolt");
			$(succ).each(function(i){
				$("<option value='"+succ[i]+"'>"+succ[i]+"</option>").appendTo("#ev_feltolt");
			});
			$("#ev_feltolt").change(function(){
				$.ajax({
					url:"/admin/dolgozo/munka/honap",
					type:"POST",
					dataType:"json",
					data:{ev:$("#ev_feltolt").val(),id:$("#dolgozoId").val()},
					success: function(succ2){
						$("#honap_feltolt option").remove();
						$("<option value='nincs' selected>Nincs</option>").appendTo("#honap_feltolt");
						$(succ2).each(function(i){
							$("<option value='"+succ2[i]+"'>"+succ2[i]+"</option>").appendTo("#honap_feltolt");
						});
					},
					error: function(err2){
						//console.log(err2);
					}
				});
			});
		},
		error: function(err){
			//console.log(JSON.stringify(err));
		}
	});
	
	$("#dolg_datum_form").submit(function(e){
		e.preventDefault();
		//console.log($("#honap_feltolt").val());
		let honap = "";
		if(parseInt($("#honap_feltolt").val()) < 10){
			honap = "0"+$("#honap_feltolt").val();
		}else{
			honap = $("#honap_feltolt").val();
		}
		$.ajax({
			url:"/admin/dolgozo/munka/tabla",
			type:"POST",
			dataType:"json",
			data:{tipus:"evhonaptab", ev:$("#ev_feltolt").val(),honap:honap,id:$("#dolgozoId").val()},
			success: function(succ){
					$("#munkak_tabla tbody tr").remove();
					let tipus = "";
					let raford = [];
					let kifele = "";
					let raforditas_osszesen = 0;
					let atlag_oradij = 0;
					let oradij_osszesen = 0;
					let darab = 0;
					let teljes_osszeg = 0;
					//tabla feltoltes
					$(succ).each(function(i){

						raforditas_osszesen += succ[i].raforditas;
						oradij_osszesen += succ[i].userpro.oraber;
						teljes_osszeg += succ[i].userpro.oraber*succ[i].raforditas;
						darab++;
						raford = succ[i].raforditas.toString().split(".");
						if(isNaN(raford[1])){
							raford[1] = 0;
						}
						if(parseInt(raford[1]) == 5) {
							kifele = raford[0] +" óra "+ ((parseInt(raford[1])*60)/100)*10 + " perc";
						}else {
							kifele = raford[0] +" óra "+ (parseInt(raford[1])*60)/100 + " perc";
						}
						$("<tr id='"+succ[i].id+"'><td id='datum'>"+succ[i].datum+"</td><td id='projekt' value='"+succ[i].userpro.projekt.id+"'>"+succ[i].userpro.projekt.name+"</td><td id='tipus'><p>"+succ[i].tipus.megnevezes+"</p></td><td id='leiras'>"+succ[i].munkaleiras+"</td><td id='raforditas' value='"+succ[i].raforditas+"'>"+kifele+"</td><td id='oraber' value='"+succ[i].userpro.oraber+"'>"+(new Intl.NumberFormat().format(succ[i].userpro.oraber))+" Ft</td><td id='osszeg' value='"+(succ[i].userpro.oraber*succ[i].raforditas)+"'>"+(new Intl.NumberFormat().format((succ[i].userpro.oraber*succ[i].raforditas)))+" Ft</td><td id='check"+succ[i].id+"'><input type='checkbox' name='elszamolas' id='elszamolas' value='"+succ[i].id+"'></td></tr>").appendTo("#munkak_tabla");
					});
					
					let datumSet = new Set();
					$("#munkak_tabla tbody tr").find("td").each(function(i){
						
						if($(this).attr("id") == "datum"){
							datumSet.add($(this).text());
						}
							
					});
					raford = raforditas_osszesen.toString().split(".");
					if(isNaN(raford[1])){
						raford[1] = 0;
					}
					if(parseInt(raford[1]) == 5) {
						kifele = raford[0] +" óra "+ ((parseInt(raford[1])*60)/100)*10 + " perc";
					}else {
						kifele = raford[0] +" óra "+ (parseInt(raford[1])*60)/100 + " perc";
					}
					
					$("#napok").text(datumSet.size);
					$("#raforditasOssz").text(kifele);
					$("#atlagOradij").text((new Intl.NumberFormat().format(oradij_osszesen/darab))+" Ft");
					$("#teljesOsszeg").text((new Intl.NumberFormat().format(teljes_osszeg))+" Ft");
					
					//$("<tr><td>"+datumSet.size+"</td><td>"+kifele+"</td><td>"+(new Intl.NumberFormat().format(oradij_osszesen/darab))+" Ft</td><td>"+(new Intl.NumberFormat().format(teljes_osszeg))+" Ft</td></tr>").appendTo("#osszesito_tabla");
					
					$("#munkak_tabla tr").each(function(){
						if($(this).attr("id") != undefined){
							let sor = $(this);
							$.ajax({
								url:"/admin/dolgozo/elszamolasok",
								type:"POST",
								dataType:"json",
								data:{mid:$(this).attr("id")},
								success: function(ss){
									if(ss == 1){
										$(sor.children()).each(function(){
											if($(this).attr("id").includes("check")){
												$(this).text("Elszámolva");
											}
										});
									}
								},
								error: function(ee){
									//console.log(JSON.stringify(ee));
								}
							});
						}
					});
					
					$("input[id='elszamolas']").click(function(){
						let aktivitas = $(this);
						if($(this).prop('checked') == true){
							let asdid = $(this).val();
							//console.log(asdid);
							$("#munkak_tabla tr").each(function(){
								if($(this).attr("id") == asdid){
									$($(this).children()).each(function(){
										if($(this).attr("id") == "datum"){
											$("#elszamDatum").val($(this).text());
											//console.log("raford: "+$(this).attr("value"));
										}
										if($(this).attr("id") == "osszeg"){
											$("#elszamOsszeg").val($(this).attr("value"));
											//console.log("osszeg: "+$(this).attr("value"));
										}
										//console.log("osszeg: "+$(this).attr("id"));
									});
								}
							});
							elszamolasModal.show();
							$("#elszamolasForm").submit(function(e){
								e.preventDefault();
			
								$.ajax({
									url:"/admin/dolgozo/elszamolas",
									type:"POST",
									dataType:"json",
									data:{datum:$("#elszamDatum").val(),osszeg:$("#elszamOsszeg").val(),id:asdid},
									success: function(succ){
										//console.log(JSON.stringify(succ));
									},
									error: function(err){
										if(err.responseText == "sikeres"){
											location.reload();
										}
									}
								});
							});
						}
					});
					
			},
			error: function(err){
				//console.log(JSON.stringify(err));
			}
		});
	});
	
});