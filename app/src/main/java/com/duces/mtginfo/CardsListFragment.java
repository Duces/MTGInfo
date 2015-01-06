package com.duces.mtginfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by David Doose on 12/15/2014.
 */
public class CardsListFragment extends Fragment{
ExpandableListView cardslist;
CardListExpandableListAdapter adapter;
List<Card> mCardsList;
String url;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cardslist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        cardslist = (ExpandableListView) view.findViewById(R.id.cardlist_elv);
        mCardsList = new ArrayList<Card>();
        url = getActivity().getIntent().getStringExtra("set");
        new ParseCardListHTMLTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        super.onViewCreated(view, savedInstanceState);
    }

    private class ParseCardListHTMLTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            String total = "";
            try {
                Document doc = Jsoup.connect(url).get();
                Elements tables = doc.select("table");
                Elements td = tables.get(2).select("td");
                total = td.get(2).text().substring(0, td.get(2).text().indexOf("c"));
                Elements cardsdata = tables.get(3).select("tr");
                cardsdata.remove(0);
                for (Element e:cardsdata){
                    Elements carddata = e.select("td");
                    Card card = new Card();
                    card.setNum(carddata.get(0).text());
                    card.setName(carddata.get(1).text());
                    card.setUrl(carddata.get(1).select("a[href]").get(0).attr("href"));
                    String cardurlword = card.getUrl().substring(card.getUrl().indexOf("/"), card.getUrl().indexOf("/", 1)).replace("/", "");
                    card.setCardimgurl(getResources().getString(R.string.img_url)+cardurlword);
                    card.setType(carddata.get(2).text());
                    card.setMana(carddata.get(3).text());
                    card.setRarity(carddata.get(4).text());
                    mCardsList.add(card);
                }


            } catch (IOException e) {
                Log.v("TAG", "parsing exception: " + e.toString());
                e.printStackTrace();
            }
            return total;
        }

        @Override
        protected void onPostExecute(String s) {
                adapter = new CardListExpandableListAdapter(getActivity(), mCardsList);
                cardslist.setAdapter(adapter);
//             super.onPostExecute(s);
        }
    }
}
