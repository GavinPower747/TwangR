package net.gavinpower.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBDesigner extends SQLiteOpenHelper {

    public static enum Table
    {
        Statuses,
        Messages,
        Users
    }

	private static final String DATABASE_NAME = "TwangR.db";
	private static final int 	DATABASE_VERSION = 1;
	private static String DATABASE_CREATE_TABLE;
    private static final String[] chatColumns = new String[] {"MessageID text primary key,", "Sender text,", "Message text,", "TimeStamp text,", "ChatID text"};
    private static final String[] statusesCols = new String[] {"StatusId integer primary key,", "StatusContent text,", "StatusAuthorId integer,", "StatusAuthor text,", "StatusLikes integer default 0,","LogDate text"};
    private static final String[] userCols = new String[] {"UserId integer primary key,", "UserName text unique,", "UserPassword text,", "UserRealName text,", "UserEmail text unique,", "UserNickName text,", "UserStatus text"};
		
	public DBDesigner(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
        for(Table table : Table.values()) {
            DATABASE_CREATE_TABLE = "create table " + table.toString() + "(";
            switch(table)
            {
                case Messages:
                    for(String column : chatColumns)
                    {
                        DATABASE_CREATE_TABLE = ConstructString(column);
                    }
                    break;
                case Statuses :
                    for(String column : statusesCols)
                    {
                        DATABASE_CREATE_TABLE = ConstructString(column);
                    }
                    break;
                case Users :
                    for(String column : userCols)
                    {
                        DATABASE_CREATE_TABLE = ConstructString(column);
                    }
                    break;
            }
            DATABASE_CREATE_TABLE = ConstructString(");");
            database.execSQL(DATABASE_CREATE_TABLE);
        }
	}

    public String ConstructString(String column)
    {
        return DATABASE_CREATE_TABLE.concat(column);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBDesigner.class.getName(), "Updating local cache from " + oldVersion + " to " + newVersion + " this may result in some slower performance than normal on first run");

        for(Table TABLE_NAME : Table.values())
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME.toString());

		onCreate(db);
	}
}