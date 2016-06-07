const querystring = require('querystring');

module.exports.getpost = function(req){
    return new Promise(function(resolve, reject){
        //POST data's three stages of metamorphosis : chunks-->data-->post
        var data =[];   //this is in querystring format...
        req.on('data', function(chunk){ //metamorphosis...
            data += chunk;  //chunks build up data
        })
        req.on('end', function(){       //requests process done, write into database
            //turn querystring to JSON format, a readable POST, format will be like {"username":"idiot","password":"hallo"}
            var post = querystring.parse(data);
            resolve(post);
        });
        req.on('error', function(err){
            reject(err);
        });
    });
}
