var admin = require("firebase-admin");

var serviceAccount = require("../private/firebase-private-key.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://zophie-508e8.firebaseio.com"
});
