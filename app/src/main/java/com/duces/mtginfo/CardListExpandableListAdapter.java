package com.duces.mtginfo;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * Created by David Doose on 12/15/2014.
 */
public class CardListExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Card> cardList;
    private TextView mRulesTV;

    public CardListExpandableListAdapter(Context c, List<Card> cards) {
        this.context = c;
        cardList = cards;
    }


    @Override
    public int getGroupCount() {
        return cardList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return cardList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return cardList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        String card_text = cardList.get(groupPosition).getName()+" "+cardList.get(groupPosition).getType()+" "+cardList.get(groupPosition).getMana();
        lblListHeader.setText(card_text);
        int colorcount = 0;
        if (cardList.get(groupPosition).getMana().contains("W")){
            lblListHeader.setBackgroundColor(Color.WHITE);
            lblListHeader.setTextColor(Color.BLACK);
            colorcount++;
        }
        if (cardList.get(groupPosition).getMana().contains("U")){
            lblListHeader.setBackgroundColor(Color.BLUE);
            lblListHeader.setTextColor(Color.WHITE);
            colorcount++;
        }
        if (cardList.get(groupPosition).getMana().contains("R")){
            lblListHeader.setBackgroundColor(Color.RED);
            lblListHeader.setTextColor(Color.BLACK);
            colorcount++;
        }
        if (cardList.get(groupPosition).getMana().contains("G")){
            lblListHeader.setBackgroundColor(Color.GREEN);
            lblListHeader.setTextColor(Color.BLACK);
            colorcount++;
        }
        if (cardList.get(groupPosition).getMana().contains("B")){
            lblListHeader.setBackgroundColor(Color.BLACK);
            lblListHeader.setTextColor(Color.WHITE);
            colorcount++;
        }
        if (colorcount>1){
            lblListHeader.setBackgroundColor(Color.YELLOW);
            lblListHeader.setTextColor(Color.BLACK);
        }
        if (colorcount==0){
            lblListHeader.setBackgroundColor(Color.GRAY);
            lblListHeader.setTextColor(Color.BLACK);
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment_card, null);
        }
        String currentcardimgurl = cardList.get(groupPosition).getCardimgurl() + "/" + cardList.get(groupPosition).getNum() + ".jpg";
        ImageView iv = (ImageView) convertView.findViewById(R.id.card_iv);
        Picasso.with(context).load(currentcardimgurl).fit().placeholder(R.drawable.cardbac).into(iv);

        if (context.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            String cardurl = context.getResources().getString(R.string.base_url)+cardList.get(groupPosition).getUrl();
            mRulesTV = (TextView) convertView.findViewById(R.id.ruleing_tv);
            new GetRulingsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cardurl);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class GetRulingsTask extends AsyncTask<String, Void, String> {
        String rulingstext = "";
        @Override
        protected String doInBackground(String... params) {
            Document carddoc = null;
            try {
                carddoc = Jsoup.connect(params[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements tabl = carddoc.select("table");
            Elements paras = tabl.get(3).select("ul");
            StringBuilder sb = new StringBuilder();
            sb.append("Rulings: ");
            sb.append("\n");
            if (paras.size()>1){
                Elements rulingli = paras.get(0).select("li");
                Elements legalli = paras.get(1).select("li");
                for (Element e : rulingli){
                    sb.append(e.text());
                    sb.append("\n");
                    sb.append("\n");
                }
                for (Element r : legalli){
                    sb.append(r.text());
                    sb.append("\n");
                }
            }else{
                Elements legalli = paras.get(0).select("li");
                for (Element r : legalli){
                    sb.append(r.text());
                    sb.append("\n");
                }
            }

            rulingstext = sb.toString();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (!rulingstext.equals("")){
                mRulesTV.setText(rulingstext);
            }

//            super.onPostExecute(s);
        }
    }
}
