/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function cargarPolarInscripciones(ingresados, realizados, pendientes, eliminados) {
    Chart.pluginService.register({
        beforeRender: function (chart) {
            if (chart.config.options.showAllTooltips) {
// create an array of tooltips
// we can't use the chart tooltip because there is only one tooltip per chart
                chart.pluginTooltips = [];
                chart.config.data.datasets.forEach(function (dataset, i) {
                    chart.getDatasetMeta(i).data.forEach(function (sector, j) {
                        chart.pluginTooltips.push(new Chart.Tooltip({
                            _chart: chart.chart,
                            _chartInstance: chart,
                            _data: chart.data,
                            _options: chart.options.tooltips,
                            _active: [sector]
                        }, chart));
                    });
                });
                // turn off normal tooltips
                chart.options.tooltips.enabled = false;
            }
        },
        afterDraw: function (chart, easing) {
            if (chart.config.options.showAllTooltips) {
                // we don't want the permanent tooltips to animate, so don't do anything till the animation runs atleast once
                if (!chart.allTooltipsOnce) {
                    if (easing !== 1)
                        return;
                    chart.allTooltipsOnce = true;
                }

                // turn on tooltips
                chart.options.tooltips.enabled = true;
                Chart.helpers.each(chart.pluginTooltips, function (tooltip) {
                    tooltip.initialize();
                    tooltip.update();
                    // we don't actually need this since we are not animating tooltips
                    tooltip.pivot();
                    tooltip.transition(easing).draw();
                });
                chart.options.tooltips.enabled = false;
            }
        }
    });
// Show tooltips always even the stats are zero
    var canvas = $('#polar-chart-inscripciones').get(0).getContext('2d');
    var polarChart = new Chart(canvas, {
        type: 'polarArea',
        data: {
            labels: ["Ingresados", "Pendientes", "Finalizados", "Eliminados"],
            datasets: [
                {
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(153, 102, 255, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255,99,132,1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(153, 102, 255, 1)'
                    ],
                    borderWidth: 1,
                    data: [
                        ingresados,
                        pendientes,
                        realizados,
                        eliminados
                    ]
                }
            ]
        },
        options: {
            //showAllTooltips: true,
            title: {
                display: true,
                text: 'Tramites administrativos'
            },
            scale: {
                ticks: {
                    callback: function () {
                        return ""
                    },
                    backdropColor: "rgba(0, 0, 0, 0)"
                }
            },
            plugins: {
                datalabels: {
                    align: function (context) {
                        var index = context.dataIndex;
                        var value = context.dataset.data[index];
                        var invert = Math.abs(value) <= 1;
                        return value < 1 ? 'end' : 'start'
                    },
                    anchor: 'end',
                    backgroundColor: null,
                    borderColor: null,
                    borderRadius: 4,
                    borderWidth: 1,
                    color: '#223388',
                    font: {
                        size: 20,
                        weight: 600
                    },
                    offset: -30,
                    padding: 0,
                    formatter: function (value, context) {
                        var tipo = context.chart.data.labels[context.dataIndex];
                        if (value === ingresados && tipo === 'Ingresados') {
                            return '';
                        } else {
                            var result = ((Math.round((value * 100) / ingresados) / 100) * 100);
                            if (result === 0) {
                                return '';
                            } else {
                                result = (Math.round(result * 100) / 100);
                                return  result + '%';
                            }
                            //return  ((Math.round(((value * 100) / ingresados)) * 100) / 100) + '%';
                        }
                    }
                }
            }
        }
    });

    document.getElementById("polar-chart-inscripciones").onclick = function (evt) {
        var activePoints = polarChart.getElementsAtEvent(evt);
        if (activePoints.length > 0) {
            //get the internal index of slice in pie chart
            var clickedElementindex = activePoints[0]["_index"];
            //get specific label by index 
            var labelPolar = polarChart.data.labels[clickedElementindex];
            //get value by index      
            //var valuePolar = polarChart.data.datasets[0].data[clickedElementindex];
            totalesInscripciones([{name: 'estadoJS', value: labelPolar}]);
        }
    }

}

function cargarLinearInscripciones(idComponent, days, values) {
    var valuesDataSet = [];
    values.forEach((element, index, array) => {
        var ins = {
            label: element.nombre,
            backgroundColor: element.color,
            borderColor: element.color,
            data: element.cantidades,
            fill: false
        };

        valuesDataSet.push(ins);
    });

    var lineInscripcion = new Chart(document.getElementById(idComponent), {
        type: 'line',
        data: {
            labels: days,
            datasets: valuesDataSet
        },
        options: {
            title: {
                display: false,
                text: 'Traspaso de Dominio'
            }
        }
    });

    document.getElementById(idComponent).onclick = function (evt) {
        var activePoints = lineInscripcion.getElementsAtEvent(evt);
        if (activePoints.length) {
            //12-Nov
            var fechaJS = lineInscripcion.data.labels[activePoints[0]["_index"]];
            var mouse_position = Chart.helpers.getRelativePosition(evt, lineInscripcion.chart);
            activePoints = $.grep(activePoints, function (activePoint, index) {
                var leftX = activePoint._model.x - 5,
                        rightX = activePoint._model.x + 5,
                        topY = activePoint._model.y + 5,
                        bottomY = activePoint._model.y - 5;

                return mouse_position.x >= leftX && mouse_position.x <= rightX && mouse_position.y >= bottomY && mouse_position.y <= topY;
            });
            //157.- Adjudicacion De Subsecretaria De Tierras
            var actoJS = lineInscripcion.data.datasets[activePoints[0]._datasetIndex].label;
            tramitesAsociados([{name: 'fechaJS', value: fechaJS},
                {name: 'actoJS', value: actoJS}]);
        }
    }
}

//, porcRealizados, porcPendientes
function cargarPolarCertificados(ingresados, realizados, pendientes, eliminados) {
    Chart.pluginService.register({
        beforeRender: function (chart) {
            if (chart.config.options.showAllTooltips) {
// create an array of tooltips
// we can't use the chart tooltip because there is only one tooltip per chart
                chart.pluginTooltips = [];
                chart.config.data.datasets.forEach(function (dataset, i) {
                    chart.getDatasetMeta(i).data.forEach(function (sector, j) {
                        chart.pluginTooltips.push(new Chart.Tooltip({
                            _chart: chart.chart,
                            _chartInstance: chart,
                            _data: chart.data,
                            _options: chart.options.tooltips,
                            _active: [sector]
                        }, chart));
                    });
                });
                // turn off normal tooltips
                chart.options.tooltips.enabled = false;
            }
        },
        afterDraw: function (chart, easing) {
            if (chart.config.options.showAllTooltips) {
                // we don't want the permanent tooltips to animate, so don't do anything till the animation runs atleast once
                if (!chart.allTooltipsOnce) {
                    if (easing !== 1)
                        return;
                    chart.allTooltipsOnce = true;
                }

                // turn on tooltips
                chart.options.tooltips.enabled = true;
                Chart.helpers.each(chart.pluginTooltips, function (tooltip) {
                    tooltip.initialize();
                    tooltip.update();
                    // we don't actually need this since we are not animating tooltips
                    tooltip.pivot();
                    tooltip.transition(easing).draw();
                });
                chart.options.tooltips.enabled = false;
            }
        }
    });
// Show tooltips always even the stats are zero


    var canvas = $('#polar-chart-certificaciones').get(0).getContext('2d');
    var polarChart = new Chart(canvas, {
        type: 'polarArea',
        data: {
            labels: ["Ingresados", "Pendientes", "Finalizados", "Eliminados"],
            datasets: [
                {
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(153, 102, 255, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255,99,132,1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(153, 102, 255, 1)'
                    ],
                    borderWidth: 1,
                    data: [
                        ingresados,
                        pendientes,
                        realizados,
                        eliminados
                    ]
                }
            ]
        },
        options: {
            // showAllTooltips: true,
            title: {
                display: true,
                text: 'Servicios en linea'
            },
            scale: {
                ticks: {

                    callback: function () {
                        return ""
                    },
                    backdropColor: "rgba(0, 0, 0, 0)"
                }
            },
            plugins: {
                datalabels: {
                    align: function (context) {
                        var index = context.dataIndex;
                        var value = context.dataset.data[index];
                        var invert = Math.abs(value) <= 1;
                        return value < 1 ? 'end' : 'start';
                    },
                    anchor: 'end',
                    backgroundColor: null,
                    borderColor: null,
                    borderRadius: 4,
                    borderWidth: 1,
                    color: '#223388',
                    font: {
                        size: 20,
                        weight: 600
                    },
                    offset: -30,
                    padding: 0,
                    formatter: function (value, context) {
                        var tipo = context.chart.data.labels[context.dataIndex];
                        if (value === ingresados && tipo === 'Ingresados') {
                            return '';
                        } else {
                            var result = ((Math.round((value * 100) / ingresados) / 100) * 100);
                            if (result === 0) {
                                return '';
                            } else {
                                result = (Math.round(result * 100) / 100);
                                return result  + '%';
                            }
                            //return  ((Math.round(((value * 100) / ingresados)) * 100) / 100) + '%';
                        }
                    }
                }
            }

        }
    });

    document.getElementById("polar-chart-certificaciones").onclick = function (evt) {
        var activePoints = polarChart.getElementsAtEvent(evt);
        if (activePoints.length > 0) {
            //get the internal index of slice in pie chart
            var clickedElementindex = activePoints[0]["_index"];
            //get specific label by index 
            var labelPolar = polarChart.data.labels[clickedElementindex];
            //get value by index      
            //var valuePolar = polarChart.data.datasets[0].data[clickedElementindex];
            totalesCertificaciones([{name: 'estadoJS', value: labelPolar}]);
        }
    }

}

function cargarBarLibro(libros, valuesJS, colorsJS) {
    var barChartLibro = new Chart(document.getElementById("bar-chart"), {
        type: 'bar',
        data: {
            labels: libros,
            datasets: [
                {
                    backgroundColor: colorsJS,
                    data: valuesJS
                }
            ]
        },
        options: {
            legend: {display: false},
            title: {
                display: true,
                text: 'Estadísticas'
            }
        }
    });
}



////GRAFICO DE LINEA PARA MOSTRAR LOS TRAMITES SEGUN SU ESTADO POR USUARIOS
function cargarLinearTramitesUsuario(idComponent, days, valuesFinalizados,
        colorFinalizados, valuesPendientes, colorPendiente, usuario) {
    console.log('idComponent');
    console.log(idComponent);
    var lineInscripcion = new Chart(document.getElementById(idComponent), {
        type: 'line',
        data: {
            labels: days,
            datasets: [
                {
                    data: valuesFinalizados,
                    label: "FINALIZADO",
                    borderColor: colorFinalizados,
                    fill: false
                },
                {
                    data: valuesPendientes,
                    label: "PENDIENTE",
                    borderColor: colorPendiente,
                    fill: false
                },
            ]
        },
        options: {
            title: {
                display: false,
                text: 'Traspaso de Dominio'
            }
        }
    });

    document.getElementById(idComponent).onclick = function (evt) {
        var activePoints = lineInscripcion.getElementsAtEvent(evt);
        if (activePoints.length) {
            //12-Nov
            var fechaJS = lineInscripcion.data.labels[activePoints[0]["_index"]];
            var mouse_position = Chart.helpers.getRelativePosition(evt, lineInscripcion.chart);
            activePoints = $.grep(activePoints, function (activePoint, index) {
                var leftX = activePoint._model.x - 5,
                        rightX = activePoint._model.x + 5,
                        topY = activePoint._model.y + 5,
                        bottomY = activePoint._model.y - 5;

                return mouse_position.x >= leftX && mouse_position.x <= rightX && mouse_position.y >= bottomY && mouse_position.y <= topY;
            });
            //157.- Adjudicacion De Subsecretaria De Tierras
            var estadoTramiteAsociadoUsuario = lineInscripcion.data.datasets[activePoints[0]._datasetIndex].label;
            tramitesAsociadosUsuario([{name: 'fechaJS', value: fechaJS},
                {name: 'estadoTramiteAsociadoUsuario', value: estadoTramiteAsociadoUsuario},
                {name: 'tramiteAsociadoUsuario', value: usuario}]);
        }
    }
}



function cargarLinearTodosUsuarios(idComponent, days, values) {
    var valuesDataSet = [];
    console.log('holi');
    values.forEach((element, index, array) => {
        var ins = {
            label: element.nombre,
            backgroundColor: element.color,
            borderColor: element.color,
            data: element.cantidades,
            fill: false
        };

        valuesDataSet.push(ins);
    });

    var lineInscripcion = new Chart(document.getElementById(idComponent), {
        type: 'line',
        data: {
            labels: days,
            datasets: valuesDataSet
        }
    });


}


function roundToTwo(num) {
    return +(Math.round(num + "e+2") + "e-2");
}