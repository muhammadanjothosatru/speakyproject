const functions = require("firebase-functions");
const tf = require("@tensorflow/tfjs");
var XMLHttpRequest = require("xhr2");

exports.api = functions.https.onRequest((req, res) => {
  res.set("Access-Control-Allow-Origin", "*");
  res.set("Access-Control-Allow-Methods", "GET, POST");
  let data = req.query.data;
  var request = new XMLHttpRequest();
  request.addEventListener("load", () => {
    predict(request.responseText).then((pred) => {
      res.status(200).send({ prediksi: pred[0] });
    });
  });
  request.open("GET", data);
  request.send();
  
});

async function predict(data) {
  var arr = Array.from(data.split(' '), Number);
  console.log(typeof arr);
  let input = tf.tensor1d(arr);
  input = tf.reshape(input, [1,16,16,1]);
  console.log(input.shape);
  let model = await tf.loadLayersModel(
    "https://firebasestorage.googleapis.com/v0/b/speaky-2021.appspot.com/o/model.json?alt=media&token=489addcc-2e5e-4f25-89e9-4347f0044c81"
  );
  return model.predict(input).dataSync();
}