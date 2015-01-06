package com.duces.mtginfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by David Doose on 12/9/2014.
 */
public class BlocksFragment  extends Fragment {
    ExpandableListView blocklist;
    android.widget.ExpandableListAdapter adapter;
    List<String> blocksarray;
    HashMap<String, List<String>> sets;
    HashMap<String, List<String>> seturls;
    ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blocks, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        blocklist = (ExpandableListView) view.findViewById(R.id.setexpandableListView);
        blocklist.setBackground(getResources().getDrawable(R.drawable.magic_background));
        blocklist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), CardActivity.class);
                intent.putExtra("set", getResources().getString(R.string.base_url)+seturls.get(blocksarray.get(groupPosition)).get(childPosition));
                startActivity(intent);
                return false;
            }
        });
        blocksarray = new ArrayList<String>();
        sets = new HashMap<String, List<String>>();
        seturls = new HashMap<String, List<String>>();
        String sitemapurl = getResources().getString(R.string.sitemap_url);
        pb = (ProgressBar) view.findViewById(R.id.progressBar2);
        new ParseCardBlocksHTMLTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sitemapurl);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.blocks, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class ParseCardBlocksHTMLTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect(params[0]).get();
                String title = doc.title();
                Elements ele = doc.select("table");
                Element blockstable = ele.get(1);
                Elements blocks = blockstable.select("li");
                for (Element e: blocks){
                    if (e.toString().contains("<ul>")){
                        String block = e.toString().substring(e.toString().indexOf("<li>"), e.toString().indexOf("<ul>")).replace("<li>", "");
                        blocksarray.add(block);
                        Elements links = e.select("a[href]");
                        List<String> currentsets = new ArrayList<String>();
                        List<String> currentseturls = new ArrayList<String>();
                        for (Element link: links){
                            currentsets.add(link.text());
                            currentseturls.add(link.attr("href"));
                        }
                        sets.put(blocksarray.get(blocksarray.size()-1), currentsets);
                        seturls.put(blocksarray.get(blocksarray.size()-1), currentseturls);

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pb.setVisibility(View.INVISIBLE);
            adapter = new com.duces.mtginfo.ExpandableListAdapter(getActivity(), blocksarray, sets);
            blocklist.setAdapter(adapter);
        }
    }

}
