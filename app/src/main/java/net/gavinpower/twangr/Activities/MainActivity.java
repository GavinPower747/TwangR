package net.gavinpower.twangr.Activities;

import java.util.Locale;

import android.content.Intent;
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

import net.gavinpower.Models.Statuses;
import net.gavinpower.twangr.Fragments.NewsFeedFrag;
import net.gavinpower.twangr.Fragments.OnlineFriendsFrag;
import net.gavinpower.twangr.Fragments.ProfileFrag;
import net.gavinpower.twangr.R;
import net.gavinpower.twangr.TwangR;

import static net.gavinpower.twangr.TwangR.currentUser;


public class MainActivity extends ActionBarActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    TwangR twangR;
    Statuses myPosts;
    Statuses newsFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twangR = (TwangR)getApplication();
        twangR.setActivity(this);

        myPosts = new Statuses();
        newsFeed = new Statuses();
        if(currentUser == null)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(1);
    }

    public void populateNewsFeed(Statuses statuses)
    {
        this.newsFeed = statuses;
    }

    public void populateProfile(Statuses statuses)
    {
        this.myPosts = statuses;
        ((ProfileFrag)mSectionsPagerAdapter.getItem(0)).populateMyPosts(statuses);
    }

    public Statuses getMyPosts() { return myPosts; }
    public Statuses getNewsFeed() { return newsFeed; }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return true;
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
                case 2: return new OnlineFriendsFrag();
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
