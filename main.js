var app = require('express')();
var hell = require('mongoose');
var server = require('http').Server(app);
var io = require('socket.io')(server);
var bodyParser = require('body-parser');
var mongoClient = require('mongodb').MongoClient;
var url = 'mongodb://localhost:27017/myappdatabase';
var myDb;

/*var question = new Question(qid, desc, image_id, caption, insert_user){
this.qid = qid;
this.desc = desc;
this.image_id = image_id;
this.caption = caption;
this.insert_user = insert_user;
}*/
app.use(bodyParser.json());

mongoClient.connect(url,
   function(err,db){
if(err){
   console.log('Could not connect to mongodb server');
}
console.log('server running at 3000 port');
console.log('conn to mongodb server');
myDb = db;
});

server.listen(3000,function(){
   console.log('server running at 3000 port');
});

var createCursor = function(collection,callback){
var cursor = collection.find({});
cursor.limit(5);
cursor.toArray(callback);
}

var retrieveQuestion = function(callback){
var collection = myDb.collection('Questions');
createCursor(collection,function(err,arr){
if(err){
console.log('couldnt retrieve data from mongodb');
}

else
{
console.log(' data from mongodb'+ JSON.stringify(arr));
callback(arr);
}

});
}

var addQuestion = function(myJson,callback){
var collection = myDb.collection('Questions');
collection.insert(myJson,function(err,result){
if(err){
   console.log('Could not insert');
}
else{
   console.log('rows inserted ' + result.insertedCount);
}

});
}

var io = require('socket.io').listen(server);
io.on('connection',function(socket){
console.log('the socket is added ' + socket.id);


socket.on("Question-Added",function(data){
console.log('the new Question is' + JSON.stringify(data));
io.emit('Question-Added',data);
addQuestion(data);
});

socket.on("message",function(data){
console.log('the event message is ON ' + data);
io.emit('chat-message',{"name":"Welcome to Chat Room"});
});

});

 /*var arr = retrieveQuestion(function(arr){
         res.send(arr);
      });*/
app.get('/', function(req, res){

    var arr = retrieveQuestion(function(arr){
         res.send(arr);
      });
	/*res.send(
		 [
		{
   "qid": 1,
   "desc":"Which is the Capital of India",
   "image_id":1234,
   "caption":"INDIA",
   "insert_user":"samla"
},
{
   "qid": 2,
   "desc":"Which is the Capital of Pakistan",
   "image_id":1234,
   "caption":"PAK",
   "insert_user":"appu"
},
{
   "qid": 3,
   "desc":"Which is the Capital of ZIMBABWE",
   "image_id":1234,
   "caption":"ZIM",
   "insert_user":"akshi"
},
{
   "qid": 4,
   "desc":"Which is the Capital of usa",
   "image_id":1234,
   "caption":"USA",
   "insert_user":"ronik"
},
{
   "qid": 5,
   "desc":"Which is the Capital of AUS",
   "image_id":1234,
   "caption":"AUSSIE",
   "insert_user":"lee"
}


]

);
*/
});



