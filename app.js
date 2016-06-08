global.appROOT = __dirname;
const https = require('https'),
    fs = require('fs'),
    co = require('./lib/co'),
    dboperator = require('./models/dboperator.js'),
    request = require('./models/request.js');

const ssl_cert={
    key: fs.readFileSync('ssl/server.key'),
    cert: fs.readFileSync('ssl/server.crt')
}

function attendance(req, res) {

}

var html=fs.readFileSync('views/index.html','utf8');

var routes = function(req,res){
    co(function* (){
        //response code 200 first
        res.writeHead(200, {'Content-Type': 'application/json'});

        if(req.url == '/attendance' && req.method == 'POST'){
            var post = yield request.getpost(req);//get the post from req data
            var attendance = JSON.parse(post.attendance)//turn the post data 'attendance' which is in string format into json format
            yield* dboperator.attendance(attendance);//write to database
            res.end('done');
        }
        else if(req.url.substring(0,13)==('/getnamelist/')){
            var clubname = req.url.replace('/getnamelist/','');
            var namelist = yield* dboperator.getnamelist(clubname);
            res.end(JSON.stringify(namelist));
        }
        else if(req.url==('/getclublist')){

        }
        else{
            res.end('NaN');
        }
    }).catch(function(err){
        console.error("co catched: "+err.stack);
        res.end(err.stack);
    });
}

dboperator.init();
https.createServer(ssl_cert,routes).listen(8080);
console.log('Server is running...');

