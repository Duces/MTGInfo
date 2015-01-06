package com.duces.mtginfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by David Doose on 12/9/2014.
 */
public class SearchFragment extends Fragment implements Button.OnClickListener{
EditText namesrch, manacost, powersrch, toughsrch;
CheckBox red_cb, blue_cb, white_cb, black_cb, green_cb, gold_cb, exclude_cb, colorless_cb, land_cb, mythic_cb, rare_cb, uncommon_cb, common_cb, bland_cb, special_cb;
Button search_but;
String base_search_url;
Spinner mana_compare_spinner, power_compare_spinner, tough_compare_spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        namesrch = (EditText) view.findViewById(R.id.name_et);
        search_but = (Button) view.findViewById(R.id.search_but);
        search_but.setOnClickListener(this);
        base_search_url = getResources().getString(R.string.base_search_url);
        red_cb = (CheckBox) view.findViewById(R.id.srch_red_cb);
        blue_cb = (CheckBox) view.findViewById(R.id.srch_blue_cb);
        black_cb = (CheckBox) view.findViewById(R.id.srch_black_cb);
        green_cb = (CheckBox) view.findViewById(R.id.srch_green_cb);
        white_cb = (CheckBox) view.findViewById(R.id.srch_white_cb);
        gold_cb = (CheckBox) view.findViewById(R.id.srch_gold_cb);
        exclude_cb = (CheckBox) view.findViewById(R.id.srch_exclude_cb);
        colorless_cb = (CheckBox) view.findViewById(R.id.srch_colorless_cb);
        land_cb = (CheckBox) view.findViewById(R.id.srch_land_cb);
        mythic_cb = (CheckBox) view.findViewById(R.id.mythic_cb);
        rare_cb = (CheckBox) view.findViewById(R.id.rare_cb);
        uncommon_cb = (CheckBox) view.findViewById(R.id.uncommon_cb);
        common_cb = (CheckBox) view.findViewById(R.id.common_cb);
        bland_cb = (CheckBox) view.findViewById(R.id.bland_cb);
        special_cb = (CheckBox) view.findViewById(R.id.special_cb);
        manacost = (EditText) view.findViewById(R.id.mana_et);
        powersrch = (EditText) view.findViewById(R.id.power_et);
        toughsrch = (EditText) view.findViewById(R.id.tough_et);
        mana_compare_spinner = (Spinner) view.findViewById(R.id.mana_spinner);
        power_compare_spinner = (Spinner) view.findViewById(R.id.power_spinner);
        tough_compare_spinner = (Spinner) view.findViewById(R.id.tough_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.compares, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mana_compare_spinner.setAdapter(adapter);
        power_compare_spinner.setAdapter(adapter);
        tough_compare_spinner.setAdapter(adapter);

//        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        boolean isLand;
        boolean morethantworarity;
        StringBuilder sb = new StringBuilder();
        if (!namesrch.getText().toString().equals("")){
            sb.append("&q="+namesrch.getText().toString()+"+");
        }else{
            sb.append("&q=");
        }
        if (land_cb.isChecked()){
            sb.append("%28c%3Al+or+");
            isLand = true;
        }else{
            isLand = false;
        }

        if (exclude_cb.isChecked()){
            sb.append("c%21");
        }else{
            sb.append("c%3A");
        }
        if (white_cb.isChecked()){
            sb.append("w");
        }
        if (blue_cb.isChecked()){
            sb.append("u");
        }
        if (black_cb.isChecked()){
            sb.append("b");
        }
        if (red_cb.isChecked()){
            sb.append("r");
        }
        if (green_cb.isChecked()){
            sb.append("g");
        }
        if (gold_cb.isChecked()){
            sb.append("m");
        }
        if (isLand){
            sb.append("%29");
        }
        if (!manacost.getText().toString().equals("")){
            sb.append(("+mana"+mana_compare_spinner.getSelectedItem()+manacost.getText().toString()).replace("=","%3D"));
        }
        if (!powersrch.getText().toString().equals("")){
            sb.append(("+pow"+power_compare_spinner.getSelectedItem()+powersrch.getText().toString()).replace("=","%3D"));
        }
        if (!toughsrch.getText().toString().equals("")){
            sb.append(("+tou"+tough_compare_spinner.getSelectedItem()+toughsrch.getText().toString()).replace("=","%3D"));
        }
//        if (){
//
//        }

            String query = sb.toString();


            new CheckResults().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getResources().getString(R.string.base_search_url)+query);

    }

    private class CheckResults extends AsyncTask<String,Void,String>{
    boolean isresult;
        @Override
        protected String doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect(params[0]).get();
                Elements results = doc.select("h1");
//                Log.v("TAG","results: "+results.get(0).toString());
                if (results.size()==1){
                    isresult = false;
                }else{
                    isresult = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(String s) {
            if (isresult){
                Intent intent = new Intent(getActivity(), CardActivity.class);
                intent.putExtra("set", s);
                Log.v("TAG","search url: "+s);
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Your query did not match any cards.", Toast.LENGTH_SHORT).show();
            }


            super.onPostExecute(s);
        }
    }
}
