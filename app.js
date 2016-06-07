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
            var post = yield request.getpost(req);
            var attendance = JSON.parse(post.attendance)
            yield dboperator.attendance(attendance);
            res.end('done');
        }
        else if(req.url.substring(0,13)==('/getnamelist/')){
            var namelist = req.url.replace('/getnamelist/');
            //res.end(html);
            res.end(JSON.stringify(data));
        }
        else if(req.url==('/getclublist')){

        }
        else{
            res.end('NaN');
        }
    }).catch(function(err){
        res.end(err);
        console.error("co catched: "+err);
    });
}

dboperator.init();
https.createServer(ssl_cert,routes).listen(8080);
console.log('Server is running...');

