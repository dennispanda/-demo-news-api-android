package tutho.com.newsapiapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import tutho.com.newsapiapp.fragments.SourceFragment;
import tutho.com.newsapiapp.utils.CommonTasks;

public class NewAPIAPPActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SourceFragment sourceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_apiapp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sourceFragment = new SourceFragment();
        CommonTasks.addFragment(sourceFragment,false, FragmentTransaction.TRANSIT_NONE,R.id.lyt_content,getSupportFragmentManager(),SourceFragment.class.getName());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_apiap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all_sources) {
            sourceFragment = new SourceFragment();
            CommonTasks.addFragment(sourceFragment,true, FragmentTransaction.TRANSIT_NONE,R.id.lyt_content,getSupportFragmentManager(),SourceFragment.class.getName());

        } else if (id == R.id.nav_tech_sources) {
            sourceFragment = new SourceFragment();
            Bundle args = new Bundle();
            args.putBoolean(CommonTasks.HAS_CATEGORY,true);
            sourceFragment.setArguments(args);
            CommonTasks.addFragment(sourceFragment,true, FragmentTransaction.TRANSIT_NONE,R.id.lyt_content,getSupportFragmentManager(),SourceFragment.class.getName());

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
