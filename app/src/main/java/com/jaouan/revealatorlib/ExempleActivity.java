package com.jaouan.revealatorlib;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.jaouan.revealator.Revealator;

public class ExempleActivity extends AppCompatActivity {

    public static final String STATEKEY_THE_AWESOME_VIEW_IS_VISIBLE = "the_awesome_view_is_visible";

    private View theAwesomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exemple);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        theAwesomeView = findViewById(R.id.the_awesome_view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.reveal(theAwesomeView)
                        .from(fab)
                        .withCurvedTranslation()
                        .withChildsAnimation()
                        //.withDelayBetweenChildAnimation(...)
                        //.withChildAnimationDuration(...)
                        //.withTranslateDuration(...)
                        //.withRevealDuration(...)
                        //.withEndAction(...)
                        .start();
            }
        });

        final View theWonderfulButton = findViewById(R.id.the_wonderful_button);
        theWonderfulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.unreveal(theAwesomeView)
                        .to(fab)
                        .withCurvedTranslation()
//                        .withUnrevealDuration(...)
//                        .withTranslateDuration(...)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                //fab.show();
                                Snackbar.make(fab, "What a beautiful snackbar !", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .start();

            }
        });

        if (savedInstanceState != null && savedInstanceState.getBoolean(STATEKEY_THE_AWESOME_VIEW_IS_VISIBLE)) {
            theAwesomeView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATEKEY_THE_AWESOME_VIEW_IS_VISIBLE, theAwesomeView.getVisibility() == View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exemple, menu);
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
}
