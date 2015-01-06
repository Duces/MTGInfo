package com.duces.mtginfo;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by David Doose on 11/25/2014.
 */
public class CardFragment extends Fragment {
TextView rulestv;

    public static CardFragment newInstance(String url, String rules, String cardurl){
        CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putString("img", url);
        args.putString("rules", rules);
        args.putString("cardurl", cardurl);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        ImageView carimg = (ImageView) view.findViewById(R.id.card_iv);
        String img = bundle.getString("img");
        Picasso.with(getActivity()).load(img).placeholder(R.drawable.cardbac).into(carimg);

        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            String cardurl = bundle.getString("cardurl");
            rulestv = (TextView) view.findViewById(R.id.ruleing_tv);
            new GetRulingsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cardurl);
        }

    }
    private class GetRulingsTask extends AsyncTask<String, Void, String>{
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
                    rulestv.setText(rulingstext);
                }

//            super.onPostExecute(s);
        }
    }

}
