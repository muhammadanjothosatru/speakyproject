const functions = require("firebase-functions");
const tf = require("@tensorflow/tfjs");

exports.api = functions.https.onRequest((req, res) => {
  res.set("Access-Control-Allow-Origin", "*");
  res.set("Access-Control-Allow-Methods", "GET, POST");
  let data = req.query.data;
  predict(data).then((pred) => {
    res.status(200).send({ prediksi: pred[0] });
  });
  
});

async function predict(data) {
  data = data.replace(/e /g, 'e+');
  var arr = Array.from(data.split(' '), Number);
  let input = tf.tensor1d(arr);
  input = tf.reshape(input, [1,16,16,1]);
  let model = await tf.loadLayersModel(
    "https://firebasestorage.googleapis.com/v0/b/speaky-2021.appspot.com/o/model.json?alt=media&token=013ea099-107c-455e-8462-a4d4954bb6c2"
  );
  return model.predict(input).dataSync();
}