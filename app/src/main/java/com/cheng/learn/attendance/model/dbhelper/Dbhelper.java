package com.cheng.learn.attendance.model.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cheng on 7/2/16.
 */
public class Dbhelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "attendance.db";
    public static final int DATABASE_VERSION = 1;

    public Dbhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //configuration
        /*  id  :   key
            1   :   server uri
            2   :   main in charge club's id
        */
        db.execSQL(
                "CREATE TABLE config ("
                        +   "id INTERGER PRIMARY KEY,"
                        +   "value VARCHAR(255) NOT NULL"
                        +")"
        );

        //students' data
        db.execSQL(
                "CREATE TABLE students_data ("
                    +   "studentno INTEGER NOT NULL PRIMARY KEY CHECK (studentno BETWEEN 0 AND 9999999),"
                    +   "studentname VARCHAR(255) NOT NULL"
                    +")"
        );

        //club's members
        db.execSQL(
                "CREATE TABLE club_members ("
                        +   "clubid_studentno PRIMARY KEY,"
                        +   "clubid INTEGER NOT NULL,"
                        +   "studentno INTEGER NOT NULL CHECK (studentno BETWEEN 0 AND 9999999),"
                        +   "FOREIGN KEY(clubid) REFERENCES clubs_data(clubid),"
                        +   "FOREIGN KEY(studentno) REFERENCES students_data(studentno)"
                        +")"
        );

        //clubs' data, define clubs as id can make keys which write into 'attendance' table slimmer
        //in database may contain more than one club's data
        db.execSQL(
                "CREATE TABLE clubs_data ("
                        +   "clubid INTEGER PRIMARY KEY AUTOINCREMENT,"
                        +   "clubname VARCHAR(255) UNIQUE NOT NULL"
                        +")"
        );

        //clubs' members' attendance
        /*  status code:
             1:attend
             2:official leave
             3:absence for reason
             4:ABSENTEEISM
             5:other
        */
        db.execSQL(
                "CREATE TABLE attendance ("
                        +   "date_clubid_studentno INTEGER PRIMARY KEY,"
                        +   "date INTEGER NOT NULL CHECK (date BETWEEN 0 AND 99999999),"
                        +   "clubid INTEGER NOT NULL,"
                        +   "studentno INTEGER NOT NULL,"
                        +   "status INTEGER NOT NULL CHECK (status BETWEEN 1 AND 5),"
                        +   "remarks text,"
                        +   "FOREIGN KEY(clubid) REFERENCES clubs_data(clubid),"
                        +   "FOREIGN KEY(studentno) REFERENCES club_members(studentno)"
                        +")"
        );

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
