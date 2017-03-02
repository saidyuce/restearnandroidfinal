package com.theoc.restapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.theoc.restapp.adapters.NavAdapter;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.ServerYanıt;
import com.theoc.restapp.dataorganization.SocketMessage;
import com.theoc.restapp.dataorganization.barcode.ConnectionPc;
import com.theoc.restapp.dataorganization.screendata.GetDataHomeScreen;

public class HomeActivity extends AppCompatActivity {
    public ListView listView, navListView;
    LinearLayout bottomBar;
    GetDataHomeScreen getDataHome;
    public SearchView actionView2;
    ConnectionPc connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getDataHome = new GetDataHomeScreen(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        findViewById(R.id.footerRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("asdasfa", "CIKIS CLICKED");
                LoginManager.getInstance().logOut();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("id", 0);
                editor.putString("temp_key", "");
                GeneralSync.id = 0;
                GeneralSync.temp_key = "";
                editor.apply();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        navListView = (ListView) findViewById(R.id.navListView);
        NavAdapter adapter = new NavAdapter(this);
        navListView.setAdapter(adapter);

        ImageView navLogoImageView = (ImageView) findViewById(R.id.navLogoImageView);
        navLogoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        LinearLayout MyPointsLL = (LinearLayout) findViewById(R.id.MyPointsLL);
        MyPointsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyPointsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        ImageView midiconImageView = (ImageView) findViewById(R.id.midiconImageView);
        midiconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(HomeActivity.this, QRActivity.class);
                startActivity(intent); //bu orjinal hali*/
                Intent intent = new Intent(HomeActivity.this, CafeJoinActivity.class);
                intent.putExtra("qrText", "{\"cafe\":\"3\",\"masa\":\"D14\"}");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        bottomBar = (LinearLayout) findViewById(R.id.bottomBar);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int lastFirstVisibleItem = 0;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getId() == listView.getId()) {
                    final int currentFirstVisibleItem = listView.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > lastFirstVisibleItem) {
                        bottomBar.animate().translationY(bottomBar.getHeight());//.setInterpolator(new AccelerateInterpolator(2));;
                    } else if (currentFirstVisibleItem < lastFirstVisibleItem) {
                        bottomBar.animate().translationY(0);//.setInterpolator(new DecelerateInterpolator(2));
                    }
                    lastFirstVisibleItem = currentFirstVisibleItem;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    bottomBar.animate().translationY(0);
                }
            }
        });
        //get user id and city
        GeneralSync generalSync=new GeneralSync(this);
        generalSync.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        final MenuItem toggleservice = menu.findItem(R.id.action_switch);
        final SwitchCompat actionView = (SwitchCompat) toggleservice.getActionView();
        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean check = buttonView.isChecked();
                if (check) {
                    Intent intent = new Intent(HomeActivity.this, MapActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    // asd
                }
            }
        });
        final MenuItem searchservice = menu.findItem(R.id.action_search);
        actionView2 = (SearchView) searchservice.getActionView();
        actionView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //getDataHome.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getDataHome.populate(newText);
                return true;
            }
        });
        actionView2.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                getDataHome.navigate(position);
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                getDataHome.navigate(position);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            getDataHome.createFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void start(ServerYanıt serverYanıt){
        // onCreate'te
        // onResume'da checkLocation() bak.
        // Location kapalıysa dialog ver. Dialog takip edilip location açılırsa, googleLocationClass onLocationChanged'de refresh ver. (tekrar start)
        // Location açıksa start ver locationclasstan.
     // connection=new ConnectionPc();
     //   connection.connect(this);

        if (serverYanıt==ServerYanıt.bos){}
        else  if (serverYanıt==ServerYanıt.fail){}
       else  if (serverYanıt==ServerYanıt.misafir){
            ((TextView) findViewById(R.id.navNameTextView)).setText("Misafir Girisi");
        }
     else   if (serverYanıt==ServerYanıt.success){  //isim soyisim dolu oluyo GeneralSync.isim...
            ((TextView) findViewById(R.id.navNameTextView)).setText(GeneralSync.isim + " " + GeneralSync.soyisim);
        }

       getDataHome.start_paralel();

    }
}
