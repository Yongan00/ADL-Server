/**
 * Visualize the Work or Sleep data in d3js.org
 */
var dataJsons = document.getElementById("dataId").value;
var dataObjects = JSON.parse(dataJsons);

//Counting work or sleep periods
var workingPeriod = 0;
var sleepingPeriod = 0;
var othersPeriod = 0;
for(var i=0;i<dataObjects.length; i++){
	dataObjects[i].timeStampInUnix = new Date(dataObjects[i].timeStampInUnix * 1000);
	//The first record does not count in the period
	if(i!=0){
	var period = Math.abs(dataObjects[i].timeStampInUnix - dataObjects[i-1].timeStampInUnix);
	if(dataObjects[i].workOrSleepPredic == "working"){
		workingPeriod += period;
	}else if(dataObjects[i].workOrSleepPredic == "sleeping"){
		sleepingPeriod += period;
	}else if(dataObjects[i].workOrSleepPredic == "others"){
		othersPeriod += period;
	}
	}
}

var dataInShow = [
	{"status": "Working", "timeInSeconds": workingPeriod/1000},
	{"status": "Sleeping", "timeInSeconds": sleepingPeriod/1000},
	{"status": "Others", "timeInSeconds": othersPeriod/1000},
	{"status": "Unrecorded", "timeInSeconds": 24*60*60 - workingPeriod/1000 - sleepingPeriod/1000 - othersPeriod/1000}
]
var svgWidth = 700, svgHeight = 400, radius =  Math.min(svgWidth, svgHeight) / 2;
var svg = d3.select("#chart1")
    .attr("width", svgWidth)
    .attr("height", svgHeight);

var g = svg.append("g")
	.attr("transform", "translate(" + radius + "," + radius + ")") ;

var color = d3.scaleOrdinal(d3.schemeCategory10);

var pie = d3.pie()
	.padAngle(0.05)
	.value(function(d) { 
    return d.timeInSeconds; 
});

var path = d3.arc()
	.outerRadius(radius)
	.innerRadius(radius*0.382);

var arc = g.selectAll("arc")
	.data(pie(dataInShow))
	.enter()
	.append("g");

arc.append("path")
	.attr("d", path)
	.attr("fill", function(d) { return color(d.data.timeInSeconds); });

var label = d3.arc()
	.outerRadius(radius)
	.innerRadius(radius*0.382);

arc.append("text")
	.attr("transform", function(d) { 
    return "translate(" + label.centroid(d) + ")"; 
		})
	.attr("text-anchor", "middle")
	.text(function(d) { return d.data.status+":"+d.data.timeInSeconds + "s"; });