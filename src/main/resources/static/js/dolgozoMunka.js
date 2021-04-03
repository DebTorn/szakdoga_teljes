$(document).ready(function(){
	
	$.getScript("/js/functions.js");
	
	let datum = new Date();
	let honap = datum.getMonth()+1;
	if(honap < 10){
		honap = "0"+(datum.getMonth()+1);
	}
	$("<option value='"+datum.getFullYear()+"-"+honap+"-"+datum.getDate()+"'>"+datum.getFullYear()+". "+honap+". "+datum.getDate()+"</option>").appendTo("#munidoDate");
	
	datum.setDate(datum.getDate() - 1);
	honap = datum.getMonth()+1;;
	if(honap < 10){
		honap = "0"+(datum.getMonth()+1);
	}
	
	$("<option value='"+datum.getFullYear()+"-"+honap+"-"+datum.getDate()+"'>"+datum.getFullYear()+". "+honap+". "+datum.getDate()+"</option>").appendTo("#munidoDate");
	
	$.ajax({
		url:"/dolgozo/munka/tabla",
		type:"POST",
		dataType:"json",
		data:{tipus:""},
		success: function(succ){
			
			console.log(succ);
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
				console.log(raford[1]);
				if(parseInt(raford[1]) == 5) {
					kifele = raford[0] +" óra "+ ((parseInt(raford[1])*60)/100)*10 + " perc";
				}else {
					kifele = raford[0] +" óra "+ (parseInt(raford[1])*60)/100 + " perc";
				}
				$("<tr id='"+succ[i].id+"'><td id='datum'>"+succ[i].datum+"</td><td id='projekt' value='"+succ[i].userpro.projekt.id+"'>"+succ[i].userpro.projekt.name+"</td><td id='tipus'><p>"+succ[i].tipus.megnevezes+"</p></td><td id='leiras'>"+succ[i].munkaleiras+"</td><td id='raforditas' value='"+succ[i].raforditas+"'>"+kifele+"</td><td id='oraber' value='"+succ[i].userpro.oraber+"'>"+(new Intl.NumberFormat().format(succ[i].userpro.oraber))+" Ft</td><td id='osszeg' value='"+(succ[i].userpro.oraber*succ[i].raforditas)+"'>"+(new Intl.NumberFormat().format((succ[i].userpro.oraber*succ[i].raforditas)))+" Ft</td><td><button class='btn btn-success modosit' id='"+succ[i].id+"' data-bs-toggle='modal' data-bs-target='#modositasModal'>Módosítás</button></td></tr>").appendTo("#munkak_tabla");
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
			
				//Modosit gomb
				$(".modosit").click(function(){
					let id = $(this).attr("id");
					
					$("#modositasMunId").val(id);
							$("table tr").each(function(){
							
								let sor = $(this);
							
								if(sor.attr("id") == id){
									$.ajax({
										url:"/dolgozo/"+$("#dolgozoId").val(),
										type:"GET",
										dataType:"json",
										success: function(succ){
											$("#modositasProjektek option").remove();
											$(succ.userpro).each(function(i){
												if(succ.userpro[i].projekt.id == $(sor.find("td")[1]).attr("value")){
													$("<option value='"+succ.userpro[i].projekt.id+"' selected>"+succ.userpro[i].projekt.name+"</option>").appendTo("#modositasProjektek");	
												}else{
													$("<option value='"+succ.userpro[i].projekt.id+"'>"+succ.userpro[i].projekt.name+"</option>").appendTo("#modositasProjektek");
												}		
											});
										},
										error: function(err){
											console.log(JSON.stringify(err));
										}
									});
								}
								
								
								
							});
					
					$.ajax({
						url:"/dolgozo/munka/"+id,
						type:"GET",
						dataType:"json",
						success: function(succ){
							$("#modositasDate option").remove();
							let d = new Date();
							let h = d.getMonth()+1;
							if(h < 10){
								h = "0"+(d.getMonth()+1);
							}
							
							$("<option value='"+d.getFullYear()+"-"+h+"-"+d.getDate()+"'>"+d.getFullYear()+". "+h+". "+d.getDate()+"</option>").appendTo("#modositasDate");
							
							d.setDate(d.getDate() - 1);
							h = d.getMonth()+1;;
							if(h < 10){
								h = "0"+(d.getMonth()+1);
							}
							
							$("<option value='"+d.getFullYear()+"-"+h+"-"+d.getDate()+"'>"+d.getFullYear()+". "+h+". "+d.getDate()+"</option>").appendTo("#modositasDate");
							
							$("#modositasTipus").val(succ.tipus);
							$("<option value='"+succ.datum+"' selected>"+succ.datum+"</option>").appendTo("#modositasDate");
							let splites = succ.raforditas.toString().split(".");
							console.log(splites);
							if(splites.length == 1){
								$("#modositasOra").val(succ.raforditas);
								$("#modositasPerc").val(0);
							}else if(splites.length == 2){
								$("#modositasOra").val(splites[0]);
								if(splites[1] == 25){
									$("#modositasPerc").val(15);
								}else if(splites[1] == 5){
									$("#modositasPerc").val(30);
								}else if(splites[1] == 75){
									$("#modositasPerc").val(45);
								}
							}
							
							$("#modositasLeiras").val(succ.munkaleiras);
						},
						error: function(err){
							console.log(JSON.stringify(err));
						}
					});
		
				});
				
					//Modositas form
					$("#modositasForm").submit(function(e){
						e.preventDefault();
						let raforditas = ($("#modositasOra").val()/10)*10 + ($("#modositasPerc").val()/60);
						raforditas = Math.round((raforditas + Number.EPSILON) * 100) / 100;
						
						DolgozoModositas({id: $("#modositasMunId").val(), datum: $("#modositasDate").val(), projekt: $("#modositasProjektek").val(), user: $("#modositasUserId").val(), raforditas: raforditas, muntipus: $("#modositasTipus").val(), leiras: $("#modositasLeiras").val(), tipus: "dolgmunka"});		
					});
					
			/*$('#munkak_tabla').DataTable({
                "order": [[ 0, "asc" ]],
                "paging": false,
                "language":{
                      "decimal": "",
                      "emptyTable":     "A tábla üres",
                      "info":           "Jelenleg _END_ sor van megjelenítve a(z) _TOTAL_ sorból",
                      "infoEmpty":      "Jelenleg 0 sor van megjelenítve a(z) 0 sorból",
                      "infoFiltered":   "(Filterezve _MAX_ adat)",
                      "infoPostFix":    "",
                      "thousands":      " ",
                      "lengthMenu":     "Mutass _MENU_ adatot",
                      "loadingRecords": "Töltés...",
                      "processing":     "Folyamatban...",
                      "search":         "Keresés:",
                      "zeroRecords":    "Nincs találat",
                      "paginate": {
                          "first":      "Első",
                          "last":       "Utolsó",
                          "next":       "Tovább",
                          "previous":   "Vissza"
                      },
                      "aria": {
                          "sortAscending":  ": ASC rendezés",
                          "sortDescending": ": DESC rendezés"
                      }
                },
                        initComplete: function () {
                            this.api().columns().every( function (i) {
                                if(i == 1 || i == 2){
                                  var column = this;
                                  var select = $('<select class="form-control form-control-sm"><option value=""></option></select>')
                                      .appendTo( $(column.header()).empty() )
                                      .on( 'change', function () {
                                          var val = $.fn.dataTable.util.escapeRegex(
                                              $(this).val()
                                          );
                                          column
                                              .search( val ? '^'+val+'$' : '', true, false )
                                              .draw();
                                      });
                                  column.data().unique().sort().each( function ( d, j ) {
                                      select.append( '<option value="'+d+'">'+d+'</option>' )
                                  });
                                }
                            });
                        }
              });*/
			
		},
		error: function(err){
			console.log(err);
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
			url:"/dolgozo/"+$("#dolgozoId").val(),
			type:"GET",
			dataType:"json",
			success: function(succ){
				$("#munidoProjektek option").remove();
				$(succ.userpro).each(function(i){
					$("<option value='"+succ.userpro[i].projekt.id+"'>"+succ.userpro[i].projekt.name+"</option>").appendTo("#munidoProjektek");				
				});
			},
			error: function(err){
				console.log(JSON.stringify(err));
			}
		});
	});
	
	$("#munkaidoForm").submit(function(e){
		e.preventDefault();
		let raforditas = ($("#munidoOra").val()/10)*10 + ($("#munidoPerc").val()/60);
		raforditas = Math.round((raforditas + Number.EPSILON) * 100) / 100;
		console.log($("#munidoTipus").val());
		DolgozoFeltoltes({projekt: $("#munidoProjektek").val(), raforditas: raforditas, muntipus: $("#munidoTipus").val(), leiras: $("#munidoLeiras").val(), datum: $("#munidoDate").val(), tipus: "munka"});
	});
	
	$.ajax({
		url:"/dolgozo/munka/ev",
		type:"GET",
		dataType:"json",
		success: function(succ){
			$("#ev_feltolt option").remove();
			$("<option selected disabled>Kiválsztás</option>").appendTo("#ev_feltolt");
			$(succ).each(function(i){
				$("<option value='"+succ[i]+"'>"+succ[i]+"</option>").appendTo("#ev_feltolt");
			});
			$("#ev_feltolt").change(function(){
				$.ajax({
					url:"/dolgozo/munka/honap",
					type:"POST",
					dataType:"json",
					data:{ev:$("#ev_feltolt").val()},
					success: function(succ2){
						$("#honap_feltolt option").remove();
						$("<option value='nincs' selected>Nincs</option>").appendTo("#honap_feltolt");
						$(succ2).each(function(i){
							$("<option value='"+succ2[i]+"'>"+succ2[i]+"</option>").appendTo("#honap_feltolt");
						});
					},
					error: function(err2){
						console.log(err2);
					}
				});
			});
		},
		error: function(err){
			console.log(JSON.stringify(err));
		}
	});
	
	$("#dolg_datum_form").submit(function(e){
		e.preventDefault();
		console.log($("#honap_feltolt").val());
		let honap = "";
		if(parseInt($("#honap_feltolt").val()) < 10){
			honap = "0"+$("#honap_feltolt").val();
		}else{
			honap = $("#honap_feltolt").val();
		}
		$.ajax({
			url:"/dolgozo/munka/tabla",
			type:"POST",
			dataType:"json",
			data:{tipus:"evhonaptab", ev:$("#ev_feltolt").val(),honap:honap},
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
						$("<tr id='"+succ[i].id+"'><td id='datum'>"+succ[i].datum+"</td><td id='projekt' value='"+succ[i].userpro.projekt.id+"'>"+succ[i].userpro.projekt.name+"</td><td id='tipus'><p>"+succ[i].tipus.megnevezes+"</p></td><td id='leiras'>"+succ[i].munkaleiras+"</td><td id='raforditas' value='"+succ[i].raforditas+"'>"+kifele+"</td><td id='oraber' value='"+succ[i].userpro.oraber+"'>"+(new Intl.NumberFormat().format(succ[i].userpro.oraber))+" Ft</td><td id='osszeg' value='"+(succ[i].userpro.oraber*succ[i].raforditas)+"'>"+(new Intl.NumberFormat().format((succ[i].userpro.oraber*succ[i].raforditas)))+" Ft</td><td><button class='btn btn-success modosit' id='"+succ[i].id+"' data-bs-toggle='modal' data-bs-target='#modositasModal'>Módosítás</button></td></tr>").appendTo("#munkak_tabla");
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
					
					
				//Modosit gomb
				$(".modosit").click(function(){
					let id = $(this).attr("id");
					
					$("#modositasMunId").val(id);
							$("table tr").each(function(){
							
								let sor = $(this);
							
								if(sor.attr("id") == id){
									$.ajax({
										url:"/dolgozo/"+$("#dolgozoId").val(),
										type:"GET",
										dataType:"json",
										success: function(succ){
											$("#modositasProjektek option").remove();
											$(succ.userpro).each(function(i){
												if(succ.userpro[i].projekt.id == $(sor.find("td")[1]).attr("value")){
													$("<option value='"+succ.userpro[i].projekt.id+"' selected>"+succ.userpro[i].projekt.name+"</option>").appendTo("#modositasProjektek");	
												}else{
													$("<option value='"+succ.userpro[i].projekt.id+"'>"+succ.userpro[i].projekt.name+"</option>").appendTo("#modositasProjektek");
												}		
											});
										},
										error: function(err){
											console.log(JSON.stringify(err));
										}
									});
								}
								
								
								
							});
					
					$.ajax({
						url:"/dolgozo/munka/"+id,
						type:"GET",
						dataType:"json",
						success: function(succ){
							$("#modositasTipus").val(succ.tipus);
							$("<option value='"+succ.datum+"' selected>"+succ.datum+"</option>").appendTo("#modositasDate");
							let splites = succ.raforditas.toString().split(".");
							console.log(splites);
							if(splites.length == 1){
								$("#modositasOra").val(succ.raforditas);
								$("#modositasPerc").val(0);
							}else if(splites.length == 2){
								$("#modositasOra").val(splites[0]);
								if(splites[1] == 25){
									$("#modositasPerc").val(15);
								}else if(splites[1] == 5){
									$("#modositasPerc").val(30);
								}else if(splites[1] == 75){
									$("#modositasPerc").val(45);
								}
							}
							
							$("#modositasLeiras").val(succ.munkaleiras);
						},
						error: function(err){
							console.log(JSON.stringify(err));
						}
					});
		
				});
					
			},
			error: function(err){
				console.log(JSON.stringify(err));
			}
		});
	});
	
});