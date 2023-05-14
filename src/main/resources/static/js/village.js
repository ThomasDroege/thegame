var secsPerHour = 3600;
var millisecsPerSec = 1000;
var food = resources.filter(res => res.resourceName == 'Food')[0];
var secsDiffFoodTime = (new Date(timestampNow) -  new Date(food.updateTime))/millisecsPerSec;
var foodActual = food.resourceAtUpdateTime + secsDiffFoodTime*food.resourceIncome/secsPerHour;
var foodIncome = food.resourceIncome/secsPerHour;
var foodVal = document.getElementById("foodValId");

var wood = resources.filter(res => res.resourceName == 'Wood')[0];
var secsDiffWoodTime = (new Date(timestampNow) -  new Date(wood.updateTime))/millisecsPerSec;
var woodActual = wood.resourceAtUpdateTime + secsDiffWoodTime*wood.resourceIncome/secsPerHour;
var woodIncome = wood.resourceIncome/secsPerHour;
var woodLabel = document.getElementById("woodLabelId");

var stone = resources.filter(res => res.resourceName == 'Stone')[0]
var secsDiffStoneTime = (new Date(timestampNow) -  new Date(stone.updateTime))/millisecsPerSec;
var stoneActual = stone.resourceAtUpdateTime + secsDiffStoneTime*stone.resourceIncome/secsPerHour;
var stoneIncome = stone.resourceIncome/secsPerHour;
var stoneLabel = document.getElementById("stoneLabelId");

var iron = resources.filter(res => res.resourceName == 'Iron')[0];
var secsDiffIronTime = (new Date(timestampNow) -  new Date(iron.updateTime))/millisecsPerSec;
var ironActual = iron.resourceAtUpdateTime + secsDiffIronTime*iron.resourceIncome/secsPerHour;
var ironIncome = iron.resourceIncome/secsPerHour;
var ironLabel = document.getElementById("ironLabelId");


setInterval(setResources, millisecsPerSec);

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
