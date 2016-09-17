package com.cheng.learn.attendance.model.dbhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.cheng.learn.attendance.model.datastructure.Attendancedata;
import com.cheng.learn.attendance.model.datastructure.Clubdata;
import com.cheng.learn.attendance.model.datastructure.Studentdata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cheng on 7/2/16.
 *
 * * take a look in Dbhelper to understand the usage of every functions here
 */
public class DbOperator implements DboperatorInterface {

    private Dbhelper dbhelper;

    public DbOperator(Context context){
        dbhelper = new Dbhelper(context);
    }

    /**
        read from database
     */

    @Override
    public String getServerurl() throws SQLiteException {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String result = null;
        Cursor cursor = db.rawQuery(
            "SELECT value FROM config where id = 1",
            null
        );
        if(cursor.moveToFirst()) {//if have record
            result = cursor.getString(cursor.getColumnIndex("value"));
        }
        cursor.close();
        return result;
    }

    @Override
    public int getMainClubid() throws SQLiteException {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        int result = 0;
        Cursor cursor = db.rawQuery(
            "SELECT value FROM config where id = 2",
            null
        );
        if(cursor.moveToFirst()) {//if have record
            result = cursor.getInt(cursor.getColumnIndex("value"));
        }
        cursor.close();
        return result;
    }

    @Override
    public String getStudentnameByStudentno(int studentno) throws SQLiteException {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String result = null;
        Cursor cursor = db.rawQuery(
            "SELECT studentname FROM students_data where studentno = ?",
            new String[]{
                    Integer.toString(studentno)
            }
        );
        if(cursor.moveToFirst()) {//if have record
            result = cursor.getString(cursor.getColumnIndex("studentname"));
        }
        cursor.close();
        return result;
    }

    @Override
    public ArrayList<Clubdata> getClublist() throws SQLiteException {
        ArrayList<Clubdata> clubs_data = new ArrayList<Clubdata>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
            "SELECT * FROM clubs_data;", null
        );

        while(cursor.moveToNext()){
            int clubid = cursor.getInt(cursor.getColumnIndex("clubid"));
            String clubname = cursor.getString(cursor.getColumnIndex("clubname"));

            clubs_data.add(new Clubdata(clubid,clubname));
        }
        cursor.close();
        return clubs_data;
    }

    @Override
    public ArrayList<Studentdata> getNamelist(int clubid) throws SQLiteException {
        ArrayList<Studentdata> students_data = new ArrayList<Studentdata>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
            "SELECT * FROM club_members WHERE clubid = ?;",
            new String[]{Integer.toString(clubid)}
        );

        while(cursor.moveToNext()){
            //int clubid = cursor.getInt(cursor.getColumnIndex("clubid"));
            int studentno = cursor.getInt(cursor.getColumnIndex("studentno"));
            String studentname = getStudentnameByStudentno(studentno);

            students_data.add(new Studentdata(clubid,studentno,studentname));
        }
        cursor.close();
        return students_data;
    }

    /**
     *
     * @param clubid
     * @param date this is nullable, if it is null then get all the attendance record
     * @param includedSynced want the records that included Synced or not
     * @return
     * @throws SQLiteException
     */
    //this two variable can use by other classes so code may more clearer
    public boolean INCLUDED_SYNCED = true,
            NOT_INCLUDED_SYNCED = false;
    @Override
    public ArrayList<Attendancedata> getAttendancelist(int clubid, Date date, boolean includedSynced) throws SQLiteException {
        ArrayList<Attendancedata> attendance_list = new ArrayList<Attendancedata>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        // if date is null
        String date_query_statement = null;
        if(date!=null) {
            SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
            int Int_date = Integer.getInteger(ft.format(date)); //date in yyyyMMdd format to store into database
            date_query_statement = "date = " + Int_date + " AND ";
        }

        // if included Synced
        String include_synced_query = (includedSynced ? "synced = 1 AND " : null);

        String queryString =
                "SELECT * FROM attendance WHERE " +
                        date_query_statement +
                        include_synced_query +
                        " clubid = " + Integer.toString(clubid);

        Cursor cursor = db.rawQuery(queryString, null);


        while (cursor.moveToNext()){
            int studentno = cursor.getInt(cursor.getColumnIndex("studentno"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            String remarks = cursor.getString(cursor.getColumnIndex("remarks"));

            attendance_list.add(new Attendancedata(date,clubid,studentno,status,remarks));
        }
        cursor.close();
        return attendance_list;
    }


    /**
        write into database
    */

    /**
     * the "id" of table "config" id correspond like this:
     *  id      :   data type
     *  1       :   server url
     *  2       :   main club's id
     */

    @Override
    public void setServerurl(String url) throws SQLiteException {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("INSERT OR REPLACE INTO config(id,value) values(?,?);",
            new String[]{
                "1",    //server url
                url
            }
        );
    }

    @Override
    public void setMainclub(int clubid) throws SQLiteException {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.execSQL("INSERT OR REPLACE INTO config(id,value) values(?,?);",
            new String[]{
                "2",    //main club's id
                Integer.toString(clubid)
            }
        );
    }

    /* json style:
        {'clubid':1, 'clubname':'five_idiots_club'},
        {'clubid':2, 'clubname':'blahblah'}
    */
    @Override
    public void importClublist(ArrayList<Clubdata> clubs_data) throws SQLiteException {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        //clear the table for new clublist
        db.execSQL("DELETE FROM clubs_data;");

        db.beginTransaction();
        for(int c=0;c<clubs_data.size();c++){
            int clubid = clubs_data.get(c).clubid;
            String clubname = clubs_data.get(c).clubname;

            db.execSQL("INSERT OR REPLACE INTO clubs_data(clubid,clubname) VALUES(?,?);",
                new String[]{
                    Integer.toString(clubid),
                    clubname
                }
            );
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void importNamelist(ArrayList<Studentdata> namelist) throws SQLiteException {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        //clear the table for new clublist
        db.execSQL("DELETE FROM club_members WHERE clubid = ?;");

        db.beginTransaction();
        for(int c=0;c<namelist.size();c++){
            int clubid = namelist.get(c).clubid;
            int studentno = namelist.get(c).studentno;
            String studentname = namelist.get(c).studentname;

            //add into the students_data
            db.execSQL("INSERT OR REPLACE INTO students_data(studentno,studentname) VALUES(?,?)",
                new String[]{
                    Integer.toString(studentno),
                    studentname
                }
            );

            //the primary key for database :[clubid][studentno]
            String primarykey = "" + clubid + studentno;
            db.execSQL("INSERT OR REPLACE INTO club_members(clubid_studentno,clubid,studentno) VALUES(?,?,?);",
                new String[]{
                    primarykey,
                    Integer.toString(clubid),
                    Integer.toString(studentno)
                }
            );
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void attendance(int date, int clubid, int studentno, int status, String remarks) throws SQLiteException {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        //the primary key for database :[date][clubid][studentno]
        String primary_key =
                "" + date + clubid + studentno;

        db.execSQL("INSERT OR REPLACE INTO attendance(date_clubid_studentno,date,clubid,studentno,status,remarks) VALUES (?,?,?,?,?,?)",
            new String[]{
                primary_key,
                Integer.toString(date),
                Integer.toString(clubid),
                Integer.toString(studentno),
                Integer.toString(status),
                remarks
            }
        );
    }

    @Override
    public void syncedAttendance(ArrayList<Attendancedata> attendancelist) throws SQLiteException {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        db.beginTransaction();
        for(int c=0;c<attendancelist.size();c++){
            Attendancedata attendancedata = attendancelist.get(c);

            db.execSQL("UPDATE attendance SET synced = 1 WHERE date_clubid_studentno = ?",
                    new String[]{
                            ""+attendancedata.date+attendancedata.clubid+attendancedata.studentno
                    }
            );
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
}