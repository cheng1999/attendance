global.appROOT = __dirname;

const http = require('http');
const https = require('https'),
    fs = require('fs'),
    routes = require('./routes.js');

const ssl_cert={
    key: fs.readFileSync('ssl/server.key'),
    cert: fs.readFileSync('ssl/server.crt')
}

https.createServer(ssl_cert,routes).listen(8080);
http.createServer(routes).listen(8081);
console.log('Server is running...');

