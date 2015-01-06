package com.duces.mtginfo;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by David Doose on 11/25/2014.
 */
public class CardActivity extends FragmentActivity {
FragmentManager fm;
CardsFragment cardsFragment;
CardsListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_cards);
        if (getActionBar()!=null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        cardsFragment = new CardsFragment();
        listFragment = new CardsListFragment();
        if (savedInstanceState==null){
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, listFragment);
            ft.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }


}
