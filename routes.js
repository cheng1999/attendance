const co = require('./lib/co'),
    dboperator = require('./models/dboperator.js'),
    request = require('./models/request.js');


module.exports = function(req,res){
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
            var clubid = req.url.replace('/getnamelist/','');
            clubid = parseInt(clubid);//by original it is string from url, but we convert it to integer
            var namelist = yield* dboperator.getnamelist(clubid);
            res.end(JSON.stringify(namelist));
        }
        else if(req.url==('/getclublist')){
            var clublist = yield* dboperator.getclublist();
            res.end(JSON.stringify(clublist));
        }
        else if(req.url==('/testserver')){//let clients validate server is work for them
            var version={'version':'attendance.v1'}
            res.end(JSON.stringify(version));
        }
        else{
            var res_error={'error':"404"}
            res.end(JSON.stringify(res_error));
        }
    }).catch(function(err){
        console.error("co catched: "+err.stack);
        var res_error={'error':err.toString()};
        res.end(JSON.stringify(res_error));
    });
}

