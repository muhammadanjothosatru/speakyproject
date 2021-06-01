var XMLHttpRequest = require("xhr2")

var req = new XMLHttpRequest();
req.addEventListener("load", () => {
	data = req.responseText;
    var arr = Array.from(data.split(' '), Number);
    arr.pop();
    console.log(arr);
    var x = arr.toString();
    console.log(x);
});
console.log("a");
req.open('GET', 'https://firebasestorage.googleapis.com/v0/b/speaky-2021.appspot.com/o/mfcc%2FJFl0MTqaKMhlIzzu4kwPJ5oMPHW2%2Fmfcc?alt=media&token=7c00eba2-bfea-4eb3-9d22-c68f3acd6a6a');
req.send();