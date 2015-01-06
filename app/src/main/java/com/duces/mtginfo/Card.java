package com.duces.mtginfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David Doose on 12/2/2014.
 */
public class Card implements Parcelable{
String num;
String Name = "";
String url = "";
String Type = "";
String Mana = "";
String Rarity = "";
String Rulings = "";
String cardimgurl = "";

    public String getCardimgurl() {
        return cardimgurl;
    }

    public void setCardimgurl(String cardimgurl) {
        this.cardimgurl = cardimgurl;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMana() {
        return Mana;
    }

    public void setMana(String mana) {
        Mana = mana;
    }

    public String getRarity() {
        return Rarity;
    }

    public void setRarity(String rarity) {
        Rarity = rarity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRulings() {
        return Rulings;
    }

    public void setRulings(String rulings) {
        Rulings = rulings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Card(Parcel in){
        num = in.readString();
        Name = in.readString();
        url = in.readString();
        Type = in.readString();
        Mana = in.readString();
        Rarity = in.readString();
        Rulings = in.readString();
        cardimgurl = in.readString();

    }
    public Card(){

    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(num);
        dest.writeString(Name);
        dest.writeString(url);
        dest.writeString(Type);
        dest.writeString(Mana);
        dest.writeString(Rarity);
        dest.writeString(Rulings);
        dest.writeString(cardimgurl);
    }
public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>(){

    @Override
    public Card createFromParcel(Parcel source) {
        return new Card(source);
    }

    @Override
    public Card[] newArray(int size) {
        return new Card[size];
    }
};

}
