if(!!(window.addEventListener)) window.addEventListener('DOMContentLoaded', main);
else window.attachEvent('onload', main);

function main() {
    lineChart();
}

function lineChart() {
    var data = {
        labels : ["January","February","March","April","May","June","July"],
        datasets : [
            {
            fillColor : "rgba(220,220,220,0.5)",
            strokeColor : "rgba(220,220,220,1)",
            pointColor : "rgba(220,220,220,1)",
            pointStrokeColor : "#fff",
            data : [65,59,90,81,56,55,40],
            label : 'Vendas 2014'
        },
        {
            fillColor : "rgba(151,187,205,0.5)",
            strokeColor : "rgba(151,187,205,1)",
            pointColor : "rgba(151,187,205,1)",
            pointStrokeColor : "#fff",
            data : [28,48,40,19,96,27,100],
            label : 'Vendas 2015'
        }
        ]
    };

    var ctx = document.getElementById("lineChart").getContext("2d");
    new Chart(ctx).Line(data);

    legend(document.getElementById("lineLegend"), data);
}