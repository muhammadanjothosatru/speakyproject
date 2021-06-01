let recognizer;
let words;
let modelLoaded = false;
let countdis = 0;

const tf = require("@tensorflow/tfjs");
const tfn = require("@tensorflow/tfjs-node");

const handler = tfn.io.fileSystem("./model.json");
const model = await tf.loadModel(handler);

$(document).ready(function(){
	countdis ++;
	document.getElementById("prediction").innerText = countdis + "Disfluency detected";
})

function loadModel(){
	recognizer 
}

function startListening(){
	recognizer.listen()
}

if(!modelLoaded){
	loadModel();
	startListening();
}
