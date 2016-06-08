var dbBuilt = require('fs').existsSync(appROOT+'/attendance.db'),//variable for check exists of database
    dblite = require(appROOT+'/lib/dblite.js'),
    db = dblite(appROOT+'/attendance.db');

//status code of attendance
var statuscode = {
    'attend':1,
    'official_leave':2,
    'absence_for_reason':3,
    'absenteeism':4,
    'other':5
}

db.on('error', function(err) {console.error(err);});// to prevent server will process end by default
db.on('close', function (code) {});// by default, it logs "bye bye", but I want it to shut up

//initial
module.exports.init = function(){
    //turn the foreign keys on
    db.query('PRAGMA foreign_keys = ON;',function(){});//add function at the back to prevent it print out result on console

    //build database if not yet
    if(!dbBuilt){
        //students' data
        db.query('\
            CREATE TABLE students_data (\
                studentno INTEGER NOT NULL PRIMARY KEY CHECK (studentno BETWEEN 0 AND 9999999),\
                studentname VARCHAR(255) NOT NULL\
            )'
        );
        //clubs' data, define clubs as id can make keys which write into 'attendance' table slimmer
        db.query('\
             CREATE TABLE clubs_data (\
                clubid INTEGER PRIMARY KEY AUTOINCREMENT,\
                clubname VARCHAR(255) UNIQUE NOT NULL\
            )'
        );
        //clubs' members
        db.query('\
            CREATE TABLE clubs_members (\
                clubid INTEGER NOT NULL,\
                studentno INTEGER NOT NULL,\
                FOREIGN KEY(clubid) REFERENCES clubs_data(clubid),\
                FOREIGN KEY(studentno) REFERENCES students_data(studentno)\
            )'
        );
        //clubs' members' attendance
        /*  status code:
             1:attend
             2:official leave
             3:absence for reason
             4:ABSENTEEISM
             5:other
        */
        db.query('\
            CREATE TABLE attendance (\
                date_clubid_studentno INTEGER PRIMARY KEY,\
                date INTEGER NOT NULL CHECK (date BETWEEN 0 AND 99999999),\
                clubid INTEGER NOT NULL,\
                studentno INTEGER NOT NULL,\
                status INTEGER NOT NULL CHECK (status BETWEEN 1 AND 5),\
                remarks text,\
                FOREIGN KEY(clubid) REFERENCES clubs_data(clubid),\
                FOREIGN KEY(studentno) REFERENCES students_data(studentno)\
            )'
        );
    }
}


function isundefined(variable){
    if(typeof(variable)=='undefined') return true;
    else return false;
}

function query(arg1,arg2,arg3){//db.query() have maximun 4 arguments,but 1 for callback,so here's 3 arguments only
    return new Promise(function(resolve,reject){

        var callback = function(err,rows){ //the args 4, or the last argements
            if(err) reject(err);
            else resolve(rows);
        }
        try{
            if(arg3) db.query(arg1,arg2,arg3,callback);
            else if(arg2) db.query(arg1,arg2,callback);
            else if(arg1) db.query(arg1,callback);
        }catch(err){
            reject(err);
        }
    });
}

//a function for get clubid with club name
function* getclubid(clubname){
    var rows  = yield query('SELECT clubid FROM clubs_data WHERE clubname = ?',[clubname], {'clubid' : Number});
    if(isundefined(rows[0])) throw new Error("db error :no results for clubid");
    return rows[0].clubid;
}

//store the namelist of attendance into databases
module.exports.attendance = function*(json){
    //json structure will be like this:
    /*{
     "date":"20161231",
     "clubname":"five_idiots_club",
     "attendance":[
      {"studentno":2013045,"status":1,"remarks":null},
      {"studentno":2013046,"status":1,"remarks":null},
      {"studentno":2013047,"status":2,"remarks":null},
      {"studentno":2013048,"status":3,"remarks":"death and wait for reborn"},
      {"studentno":2013049,"status":4,"remarks":"back home and play cs"},
     ]
    }*/
    var clubid = yield* getclubid(json.clubname);
    yield query("BEGIN TRANSACTION");//woohuuuu
        for(var c=0;c<json.attendance.length;c++){
            yield query('INSERT INTO attendance VALUES ( :date_clubid_studentno, :date, :clubid, :studentno, :status, :remarks )',{
                'date_clubid_studentno':''+json.date+clubid+json.attendance[c].studentno,
                'date':json.date,
                'clubid':clubid,
                'studentno':json.attendance[c].studentno,
                'status':json.attendance[c].status,
                'remarks':json.attendance[c].remarks
            });
        }
    yield query('COMMIT');
}

//get name list (included name and studentno) of specified club
module.exports.getnamelist = function*(clubname){
    var clubid = yield* getclubid(clubname);
    var studentno_rows = yield query('SELECT studentno FROM clubs_members WHERE clubid = ?', [clubid], {'studentno':Number});

    var namelist = [];//prepare this array will response to client
    for(let c=0;c<studentno_rows.length;c++){
        //get the studentname
        var rows = yield query('SELECT studentname FROM students_data WHERE studentno = ?',[studentno_rows[c].studentno], {'studentname':String});
        var studentname = rows[0].studentname;

        //push the data
        namelist.push({
            "studentno":studentno_rows[c].studentno,
            "name":studentname
        })
    }
    return namelist;
}

//get the club list
module.exports.getclublist = function*(){

    console.log('asd');
    var clubs_data_rows = yield query('SELECT * FROM clubs_data', {'clubid':Number,'clubname':String});

    var clublist = [];
    for(let c=0;c<clubs_data_rows.length;c++){
        clublist.push({
            "clubname":clubs_data_rows[c].clubname,
            "clubid":clubs_data_rows[c].clubid
        });
    }
    console.log(clublist);
    return clublist;
}


