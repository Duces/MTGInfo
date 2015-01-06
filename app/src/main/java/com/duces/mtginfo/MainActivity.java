package com.duces.mtginfo;

import android.app.Activity;
import android.os.Bundle;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
BlocksFragment blocksFragment;
SearchFragment searchFragment;
FragmentManager fm;
Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blocksFragment = new BlocksFragment();
        searchFragment = new SearchFragment();
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, blocksFragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_search:
                showSearch();
                break;
            case R.id.action_blocks:
                showBlocks();
                break;
        }
        return true;
    }

    private void showSearch(){
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(true);
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim);
        ft.addToBackStack("search");
        ft.replace(R.id.container, searchFragment);
        ft.commit();
    }
    private void showBlocks(){
        menu.getItem(1).setVisible(true);
        menu.getItem(2).setVisible(false);
        onBackPressed();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim);
//        ft.replace(R.id.container, blocksFragment);
//        ft.commit();
    }
}
