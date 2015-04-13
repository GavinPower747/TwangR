package net.gavinpower.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import net.gavinpower.Exceptions.LocalCacheException;
import net.gavinpower.Models.Status;
import net.gavinpower.Models.User;
import net.gavinpower.SignalR.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static net.gavinpower.Database.DBDesigner.Table;
import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.currentUser;

public class DBManager {

	private SQLiteDatabase database;
	private DBDesigner dbHelper;

	public DBManager(Context context) {
		dbHelper = new DBDesigner(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

    public void insertUser(User user)
    {
        try
        {
            database.beginTransaction();

            database.execSQL("insert into " + Table.Users.toString() + "(" + user.toDBFields() + ")" + "values (" + user.toDBString() + ")");

            database.setTransactionSuccessful();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            database.endTransaction();
        }
    }

    public User getUserById(int UserId)
    {
        User user = new User();

        try
        {
            database.beginTransaction();

            Cursor cursor = database.rawQuery("Select * from " + Table.Users.toString() + " where UserId = " + UserId, null);
            cursor.moveToFirst();

            if(cursor.getCount() == 0)
                throw new LocalCacheException("The Local Cache is empty");

            database.setTransactionSuccessful();

            user = toUser(cursor);
        }
        catch(LocalCacheException ex)
        {
            try
            {
                user = HubConnection.getUserById(UserId).get();
                insertUser(user);
            }
            catch (InterruptedException | ExecutionException exc)
            {
                exc.printStackTrace();

                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(currentActivity, "We were unable to retrieve this user please check your internet connection!", Toast.LENGTH_LONG).show();
                    }
                });

                user =  new User();
            }
        }
        finally
        {
            database.endTransaction();
        }

        return user;
    }

    public List<Status> getNewsFeed()
    {
        List<Status> statuses = new ArrayList<>();

        try
        {
            database.beginTransaction();

            Cursor cursor = database.rawQuery("Select * from " + Table.Statuses.toString(), null);
            cursor.moveToFirst();

            if(cursor.getCount() == 0)
                throw new LocalCacheException("The Local Cache is empty");

            while(!cursor.isAfterLast())
            {
                Status s = toStatus(cursor);
                statuses.add(s);
                cursor.moveToNext();
            }

            database.setTransactionSuccessful();
        }
        catch(LocalCacheException ex)
        {
            try
            {
                return HubConnection.getNewsFeed(currentUser.UserId).get()._Statuses;
            }
            catch(InterruptedException | ExecutionException exc)
            {
                exc.printStackTrace();
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(currentActivity, "There was an error in getting your news feed", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

                statuses = new ArrayList<>();
            }
        }
        finally
        {
            database.endTransaction();
        }

        return statuses;
    }

    public List<Status> getPostsByUserId(int UserId)
    {
        List<Status> posts = new ArrayList<>();
        try
        {
            database.beginTransaction();

            Cursor cursor = database.rawQuery("select * from " + Table.Statuses + " where UserId =" + UserId, null);
            cursor.moveToFirst();

            if(cursor.getCount() == 0)
                throw new LocalCacheException("Local Cache is empty");

            database.setTransactionSuccessful();
        }
        catch(LocalCacheException lcex)
        {
            Log.v("Local Cache", lcex.getMessage());
            try
            {
                posts = HubConnection.getMyPosts(UserId).get()._Statuses;
            }
            catch(Exception ex)
            {
                ex.printStackTrace();

                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(currentActivity, "There was an error in getting your posts!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        finally
        {
            database.endTransaction();
        }

        return posts;
    }

    private User toUser(Cursor cursor)
    {
        User pojo = new User();

        pojo.UserId = cursor.getInt(0);
        pojo.UserName = cursor.getString(1);
        pojo.UserPassword = cursor.getString(2);
        pojo.UserRealName = cursor.getString(3);
        pojo.UserEmail = cursor.getString(4);
        pojo.UserNickName = cursor.getString(5);
        pojo.UserStatus = cursor.getString(6);

        return pojo;
    }

    private Status toStatus(Cursor cursor)
    {
        Status pojo = new Status();

        pojo.StatusId = cursor.getInt(0);
        pojo.StatusContent = cursor.getString(1);
        pojo.StatusAuthorID = cursor.getInt(2);
        pojo.StatusAuthor = cursor.getString(3);
        pojo.StatusLikes = cursor.getInt(4);
        pojo.LogDate = cursor.getString(5);

        return pojo;
    }

    private Message toMessage(Cursor cursor)
    {
        Message pojo = new Message();

        pojo.messageID = cursor.getString(0);
        pojo.sender = cursor.getString(1);
        pojo.message = cursor.getString(2);
        pojo.TimeStamp = cursor.getString(3);
        pojo.ChatId = cursor.getString(4);

        return pojo;
    }



}
