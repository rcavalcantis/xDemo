$(function() {
	$("#new").focus();
	BookmarkProxy.findAll().done(findAllOk);

	MetadataProxy.getDemoiselleVersion().done(function(data) {
		$("#demoiselle-version").html(data);
	});

	$("form").submit(function(event) {
		event.preventDefault();
	});

	$("#new").click(function() {
		location.href = "bookmark-edit.html";
	});

	$("#relatorio").click(function() {
		location.href = BookmarkProxy.url + "/gerarRelatorioPDF";
	});

	$("#relatorioExcel").click(function() {
		location.href = BookmarkProxy.url + "/gerarRelatorioEXCEL";
	});

	//TODO 
	$("#relatorioChart").click(function() {
		var data = {
			    labels: ["January", "February", "March", "April", "May", "June", "July"],
			    datasets: [
			        {
			            label: "My First dataset",
			            fillColor: "rgba(220,220,220,0.5)",
			            strokeColor: "rgba(220,220,220,0.8)",
			            highlightFill: "rgba(220,220,220,0.75)",
			            highlightStroke: "rgba(220,220,220,1)",
			            data: [65, 59, 80, 81, 56, 55, 40]
			        },
			        {
			            label: "My Second dataset",
			            fillColor: "rgba(151,187,205,0.5)",
			            strokeColor: "rgba(151,187,205,0.8)",
			            highlightFill: "rgba(151,187,205,0.75)",
			            highlightStroke: "rgba(151,187,205,1)",
			            data: [28, 48, 40, 19, 86, 27, 90]
			        }
			    ]
			};
		var ctx = document.getElementById("myChart").getContext("2d");
		var myBarChart = new Chart(ctx).Bar(data);
	});
	

	$("#delete").click(function() {
		var ids = [];

		$("input:checked").each(function(index, value) {
			ids.push($(value).val());
		});

		if (ids.length == 0) {
			bootbox.alert({
				message : "Nenhum registro selecionado"
			});
		} else {
			bootbox.confirm("Tem certeza que deseja apagar?", function(result) {
				if (result) {
					BookmarkProxy.remove(ids).done(removeOk);
				}
			});
		}
	});
});

function findAllOk(data) {
	$('#resultList').dataTable({
		"aoColumns" : [ {
			"aTargets" : [ 0 ],
			"mDataProp" : "id",
			"mRender" : function(id) {
				return '<input id="remove-' + id + '" type="checkbox" value="' + id + '">';
			}
		}, {
			"aTargets" : [ 1 ],
			"mDataProp" : "name",
			"mRender" : function(data, type, full) {
				return '<a href="bookmark-edit.html?id=' + full.id + '">' + full.name + '</a>';
			}	
		}, {
			"aTargets" : [ 2 ],
			"mDataProp" : "description",
			"mRender" : function(data, type, full) {
				return '<a href="bookmark-edit.html?id=' + full.id + '">' + full.description + '</a>';
			}
		}, {
			"aTargets" : [ 3 ],
			"mDataProp" : "link",
			"mRender" : function(link) {
				return '<a href="' + link + '" target="_blank">' + link + '</a>';
			}
		}, {
			"aTargets" : [ 4 ],
			"mDataProp" : "salary",
			"mRender" : function(data, type, full) {
				return '<a href="bookmark-edit.html?id=' + full.id + '">' + full.salary + '</a>';
			}		
		} ],
		"oLanguage" : {
			"sInfo" : "Mostrando _START_ a _END_ de _TOTAL_ registros",
			"sEmptyTable" : "Não há dados disponíveis na tabela",
			"sLengthMenu" : "Mostrar _MENU_ registros",
			"sInfoThousands" : "",
			"oPaginate" : {
				"sFirst" : "Primeiro",
				"sLast" : "Último",
				"sNext" : "Próximo",
				"sPrevious" : "Anterior"
			}
		},
		"bFilter" : false,
		"bDestroy" : true,
		"sPaginationType" : "bs_full",
		"aaData" : data,
		"bSort" : true
	});
}

function removeOk() {
	BookmarkProxy.findAll().done(findAllOk);
}
