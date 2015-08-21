package com.estapps.zannamuzicplayer;



import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;


public class UtilMp3 {
    private ArrayList<Integer> listaMusicasInt=new ArrayList<>();
    private ArrayList<String> listaMusicasString=new ArrayList<>();
    public ArrayList<String> getListaMusicasString() {
        return listaMusicasString;
    }
    public ArrayList<Integer> getListaMusicasInt() {
        return listaMusicasInt;
    }




    public UtilMp3(){
        final R.raw raw = new R.raw();
        final Class<R.raw> r = R.raw.class;
        final Field[] fields = r.getDeclaredFields();
        for (int i = 0, max = fields.length; i < max; i++) {
            try {
                listaMusicasInt.add(fields[i].getInt(raw));
                listaMusicasString.add(fields[i].getName());
            } catch (Exception e) {
            }
        }
        Collections.shuffle(listaMusicasInt);
    }

    public UtilMp3(int num_music){
        int contagem=0;
        final R.raw raw = new R.raw();
        //recupe musicas a serem tocadas da pasta res/raw
        final Class<R.raw> r = R.raw.class;
        final Field[] fields = r.getDeclaredFields();
        //recupera vinhetas da pasta res/vinheta
        //final Class<R.raw> r02 = R.
        //final Field[] fields = r.getDeclaredFields();
        for (int i = 0, max = fields.length; i < max; i++) {
            try {
                listaMusicasInt.add(fields[i].getInt(raw));
                listaMusicasString.add(fields[i].getName());
            } catch (Exception e) {
            }
        }
        Collections.shuffle(listaMusicasInt);
    }



    private String path="android.resource://com.estapps.zannamuzicplayer/raw/";
    public String ExtractTitulo(Context context,Integer idmusic){
        try {
            Uri mediaPath = Uri.parse( path + new Integer(idmusic).toString());
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context,mediaPath);
            return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        }catch (Exception e){
            Log.e("error", e.getMessage(), e);
            return "Error";
        }
    }

    public String ExtractArtista(Context context,Integer idmusic) {
        try {Uri mediaPath = Uri.parse( path + new Integer(idmusic).toString());
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context,mediaPath);

            return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        } catch (Exception e) {

            Log.e("error", e.getMessage(), e);
            return "Error";
        }
    }



}
