package com.duces.mtginfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by David Doose on 12/14/2014.
 */
public class CardsFragment extends Fragment{

ViewPager pager;
String url;
int setcount, savedpos;
ArrayList<Card> mCardArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cards,container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pager = (ViewPager) view.findViewById(R.id.pager);
        if (savedInstanceState!=null){
            mCardArrayList = savedInstanceState.getParcelableArrayList("cards");
            savedpos = savedInstanceState.getInt("pos");
            CardPagerAdapter adapter = new CardPagerAdapter(getFragmentManager(), mCardArrayList, mCardArrayList.size());
            pager.setAdapter(adapter);
            pager.setPageTransformer(true, new DepthPageTransformer());
            pager.setCurrentItem(savedpos);
        }else{
            url = getActivity().getIntent().getStringExtra("set");
            mCardArrayList = new ArrayList<Card>();
            new ParseCardListHTMLTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        }

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int pos = pager.getCurrentItem();
        outState.putInt("pos", pos);
        outState.putParcelableArrayList("cards", mCardArrayList);
        super.onSaveInstanceState(outState);
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
                    mCardArrayList.add(card);
                }


            } catch (IOException e) {
                Log.v("TAG", "parsing exception: " + e.toString());
                e.printStackTrace();
            }
            return total;
        }

        @Override
        protected void onPostExecute(String s) {
            CardPagerAdapter adapter = new CardPagerAdapter(getFragmentManager(), mCardArrayList, mCardArrayList.size());
            pager.setPageTransformer(true, new DepthPageTransformer());
            pager.setAdapter(adapter);
//             super.onPostExecute(s);
        }
    }

//    private AlertDialog.Builder filterDialog(){
//        AlertDialog.Builder adb = new AlertDialog.Builder(this);
//        View v = getLayoutInflater().inflate(R.layout.dialog_filter, null);
//        final CheckBox redcb = (CheckBox) v.findViewById(R.id.red_cb);
//        final CheckBox bluecb = (CheckBox) v.findViewById(R.id.blue_cb);
//        final CheckBox greencb = (CheckBox) v.findViewById(R.id.green_cb);
//        final CheckBox blackcb = (CheckBox) v.findViewById(R.id.black_cb);
//        final CheckBox whitecb = (CheckBox) v.findViewById(R.id.white_cb);
//        final CheckBox colorlesscb = (CheckBox) v.findViewById(R.id.colorless_cb);
//        final CheckBox creaturecb = (CheckBox) v.findViewById(R.id.creaturecb);
//        final CheckBox enchantcb = (CheckBox) v.findViewById(R.id.enchantcb);
//        final CheckBox instantcb = (CheckBox) v.findViewById(R.id.instantcb);
//        final CheckBox sorcerycb = (CheckBox) v.findViewById(R.id.sorcerycb);
//        final CheckBox planescb = (CheckBox) v.findViewById(R.id.planeswalkercb);
//        final CheckBox artifactcb = (CheckBox) v.findViewById(R.id.artifactcb);
//        adb.setView(v);
//        adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ArrayList<String> colors = new ArrayList<String>();
//                ArrayList<String> types = new ArrayList<String>();
//                if (redcb.isChecked()){
//                    colors.add("R");
//                }
//                if (bluecb.isChecked()){
//                    colors.add("U");
//                }
//                if (greencb.isChecked()){
//                    colors.add("G");
//                }
//                if (blackcb.isChecked()){
//                    colors.add("B");
//                }
//                if (whitecb.isChecked()){
//                    colors.add("W");
//                }
//                if (colorlesscb.isChecked()){
//                    colors.add("C");
//                }
//                if (creaturecb.isChecked()){
//                    types.add("Creature");
//                }
//                if (enchantcb.isChecked()){
//                    types.add("Enchantment");
//                }
//                if (instantcb.isChecked()){
//                    types.add("Instant");
//                }
//                if (sorcerycb.isChecked()){
//                    types.add("Sorcery");
//                }
//                if (planescb.isChecked()){
//                    types.add("Planeswalker");
//                }
//                if (artifactcb.isChecked()){
//                    types.add("Artifact");
//                }
//
//                filterCards(colors, types);
//
//
//                dialog.dismiss();
//            }
//        });
//        adb.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//
//        return adb;
//    }
//    private void filterCards(ArrayList<String> filtercolors, ArrayList<String> filtertypes){
//        ArrayList<Card> filteredcolors = new ArrayList<Card>();
//        filteredcards = new ArrayList<Card>();
//        if (filtercolors.size()>0 || filtertypes.size()>0){
//            if (filtercolors.size()>0) {
//                for (Card c : mCardArrayList) {
//                    for (String s : filtercolors) {
//                        if (c.getMana().contains(s)) {
//                            filteredcolors.add(c);
//                        } else if (s.equals("C")) {
//                            if (!(c.getMana().contains("W") || c.getMana().contains("U") || c.getMana().contains("B") || c.getMana().contains("R") || c.getMana().contains("G"))) {
//                                filteredcolors.add(c);
//                            }
//                        }
//                    }
//                }
//            }
//            if (filtertypes.size()>0 && filtercolors.size()>0) {
//                for (Card a : filteredcolors) {
//                    for (String t : filtertypes) {
//                        if (a.getType().contains(t)) {
//                            filteredcards.add(a);
//                        }
//                    }
//                }
//            }else if (filtertypes.size()>0){
//                for (Card a : mCardArrayList) {
//                    for (String t : filtertypes) {
//                        if (a.getType().contains(t)) {
//                            filteredcards.add(a);
//                        }
//                    }
//                }
//            }
//            if (filtertypes.size()==0){
//                filteredcards.addAll(filteredcolors);
//            }
//            CardPagerAdapter filteredadapter = new CardPagerAdapter(getFragmentManager(), filteredcards, filteredcards.size());
//            pager.setAdapter(filteredadapter);
//            filtered = true;
//        }else{
//            CardPagerAdapter adapter = new CardPagerAdapter(getFragmentManager(), mCardArrayList, setcount);
//            pager.setAdapter(adapter);
//            filtered = false;
//        }
//
//    }
    private class CardPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Card> cards;
        int count;
        public CardPagerAdapter(FragmentManager fm, ArrayList<Card> cards, int count) {
            super(fm);
            this.cards = cards;
            this.count = count;
        }

        @Override
        public Fragment getItem(int i) {
            String currentcardimgurl = cards.get(i).getCardimgurl() + "/" + cards.get(i).getNum() + ".jpg";
            String rulings = cards.get(i).getRulings();
            String cardurl = "http://www.magiccards.info"+cards.get(i).getUrl();
            return CardFragment.newInstance(currentcardimgurl, rulings, cardurl);
        }

        @Override
        public int getCount() {
            return count;
        }
    }
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
