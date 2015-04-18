package net.gavinpower.twangr.Activities;

import java.util.Locale;

import android.content.Intent;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

import net.gavinpower.Models.Users;
import net.gavinpower.twangr.Fragments.ActiveChatsFrag;
import net.gavinpower.twangr.Fragments.NewsFeedFrag;
import net.gavinpower.twangr.Fragments.ProfileFrag;
import net.gavinpower.twangr.R;

import static net.gavinpower.twangr.TwangR.HubConnection;
import static net.gavinpower.twangr.TwangR.currentActivity;
import static net.gavinpower.twangr.TwangR.currentUser;
import static net.gavinpower.twangr.TwangR.myPosts;
import static net.gavinpower.twangr.TwangR.newsFeed;


public class MainActivity extends ActionBarActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private Users Suggestions;
    private SimpleCursorAdapter searchAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentActivity = this;

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager; mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(1);

        final String[] Columns = new String[] {"RealName"};
        final int[] labels = new int[] {android.R.id.text1};
        searchAdaptor = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, Columns, labels, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onResume()
    {
        super.onResume();
        currentActivity = this;

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                myPosts.getMyPosts();
                newsFeed.getNewsFeed();
                HubConnection.getFriendsList(currentUser.getUserId());
                HubConnection.getFriendRequests(currentUser.getUserId());
                HubConnection.getChats();
                ((NewsFeedFrag)mSectionsPagerAdapter.getItem(1)).populateNewsFeed();
                ((ProfileFrag)mSectionsPagerAdapter.getItem(0)).populateMyPosts();
                return null;
            }
        }.execute();


    }

    public void addPost(View view)
    {
        startActivity(new Intent(this, AddNewPostActivity.class));
    }

    public void myFriends(View view)
    {
        startActivity(new Intent(this, FriendsListActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSuggestionsAdapter(searchAdaptor);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String queryText)
            {
                HubConnection.getUsersByName(queryText);
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position)
            {
                Bundle information = new Bundle();
                Intent intent = new Intent();

                information.putInt("UserId", Suggestions.get(position).getUserId());
                intent.setClass(MainActivity.this, OtherProfileActivity.class);
                intent.putExtras(information);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position)
            {
                return true;
            }
        });
        return true;
    }

    public void populateSuggestions(Users userList)
    {
        Suggestions = userList;
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "RealName"});

            for(int i = 0; i < Suggestions.size(); i++) {
                c.addRow(new Object[] {i, Suggestions.get(i).getUserRealName()});
            }

        searchAdaptor.changeCursor(c);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0: return new ProfileFrag();
                case 1: return new NewsFeedFrag();
                case 2: return new ActiveChatsFrag();
                default: return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "MyTwangR";
                case 1:
                    return "NewsFeed";
                case 2:
                    return "OnlineFriends";
            }
            return null;
        }
    }

}
