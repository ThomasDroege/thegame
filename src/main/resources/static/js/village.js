var secsPerMin = 60;

var food = resources.filter(element => element.resourceName == 'Food')[0];
var foodActual = food['resourceAtUpdateTime'];
var foodIncome = food['resourceIncome']/secsPerMin;
var foodVal = document.getElementById("foodValId");

var wood = resources.filter(res => res.resourceName == 'Wood')[0];
var woodActual = wood['resourceAtUpdateTime'];
var woodIncome = wood['resourceIncome']/secsPerMin;
var woodLabel = document.getElementById("woodLabelId");

var stone = resources.filter(res => res.resourceName == 'Stone')[0]
var stoneActual = stone['resourceAtUpdateTime'];
var stoneIncome = stone['resourceIncome']/secsPerMin;
var stoneLabel = document.getElementById("stoneLabelId");

var iron = resources.filter(res => res.resourceName == 'Iron')[0];
var ironActual = iron['resourceAtUpdateTime'];
var ironIncome = iron['resourceIncome']/secsPerMin;
var ironLabel = document.getElementById("ironLabelId");

setInterval(setResources, 1000);

function setResources() {
    foodActual = foodActual + foodIncome;
    foodVal.innerHTML = (Math.round(foodActual*100)/100).toFixed(0);
    woodActual = woodActual + woodIncome;
    woodLabel.innerHTML = (Math.round(woodActual*100)/100).toFixed(0);
    stoneActual = stoneActual + stoneIncome;
    stoneLabel.innerHTML = (Math.round(stoneActual*100)/100).toFixed(0);
    ironActual = ironActual + ironIncome;
    ironLabel.innerHTML = (Math.round(ironActual*100)/100).toFixed(0);
    }
