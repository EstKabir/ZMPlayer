package com.estapps.zannamuzicplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


public class BotoesFragment extends Fragment implements View.OnClickListener,ServiceConnection{
    private static final String CATEGORIA = "Player ZM";
    private InterfaceMp3 interfaceMp3;
    //Classe que encapsula o media player
    private ImageButton stop,ford,play,back,pause;
    private Boolean playerIniciado=false;
    private Handler myHandler = new Handler();
    private SeekBar seekbar;
    private TextView time,musica,artista;
    public static int oneTimeOnly = 0;



    public void ConfiguracaoPlayer(){
        if (oneTimeOnly == 0) {
            seekbar.setMax((int) interfaceMp3.getFinalTime());
            oneTimeOnly = 1;
        }
        seekbar.setProgress((int) interfaceMp3.getStartTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_botoes, container, false);


        //Capturando os botoes

        ford = (ImageButton) view.findViewById(R.id.ford);
        ford.setOnClickListener(this);
        pause = (ImageButton) view.findViewById(R.id.pause);
        pause.setOnClickListener(this);
        stop = (ImageButton) view.findViewById(R.id.stop);
        stop.setOnClickListener(this);
        play = (ImageButton) view.findViewById(R.id.play);
        play.setOnClickListener(this);
        back = (ImageButton) view.findViewById(R.id.back);
        back.setOnClickListener(this);
        time = (TextView) view.findViewById(R.id.time);
        musica = (TextView) view.findViewById(R.id.musica);
        artista=(TextView) view.findViewById(R.id.artista);
        seekbar = (SeekBar) view.findViewById(R.id.seekBar);
        seekbar.setClickable(false);


        //inicia  p servico em segundo plano
        Log.i(CATEGORIA, "Chamando star service");
        final ServiceConnection conexao=this;
        Class servicoMp3=ServicoMp3.class;
        getActivity().getApplicationContext()
                .bindService(new Intent(getActivity(), servicoMp3), this, Context.BIND_AUTO_CREATE);
        Log.i(CATEGORIA, "Chamando bind Service");

        return view;
    }


    @Override
    public void onClick(View v) {
        try{
            if(v.getId()==R.id.play){
                Log.i(CATEGORIA, "Apertou o play");
                if (!playerIniciado){
                    interfaceMp3.play();
                    myHandler.postDelayed(UpdateSongTime, 500);
                }else{
                    interfaceMp3.resume();
                    myHandler.postDelayed(UpdateSongTime, 500);
                }
                ConfiguracaoPlayer();
                playerIniciado=true;

            }else if(v.getId() == R.id.pause){
                if (playerIniciado){
                    Log.i(CATEGORIA, "Pausou");
                    interfaceMp3.pause();
                    playerIniciado=true;
                    myHandler.removeCallbacksAndMessages(null);
                }else{
                    playerIniciado=false;
                }



            }else if(v.getId() == R.id.stop){
                oneTimeOnly = 0;
                Log.i(CATEGORIA, "Stop");
                interfaceMp3.stop();
                playerIniciado=false;
                myHandler.removeCallbacksAndMessages(null);
                time.setText("0 mim, 0 sec");
                seekbar.setProgress(0);


            }else if(v.getId() == R.id.ford) {
                if (!playerIniciado){
                    myHandler.postDelayed(UpdateSongTime, 500);
                }
                oneTimeOnly=0;
                Log.i(CATEGORIA, "ford");
                interfaceMp3.ford();
                ConfiguracaoPlayer();
                playerIniciado=true;


            }else if(v.getId() == R.id.back) {
                if (!playerIniciado){
                    myHandler.postDelayed(UpdateSongTime, 500);
                }
                oneTimeOnly=0;
                Log.i(CATEGORIA, "Stop");
                interfaceMp3.back();
                ConfiguracaoPlayer();
                playerIniciado=true;
            }

        }catch (Exception e){
            Log.e(CATEGORIA,e.getMessage(),e);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        ServicoMp3.LocalBinder binder=(ServicoMp3.LocalBinder) service;
        interfaceMp3=binder.getInterfaceMp3();
    }

    public void onServiceDisconnected(ComponentName className){
        interfaceMp3=null;
    }

    protected Runnable UpdateSongTime = new Runnable() {
        public void run() {
            time.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) interfaceMp3.getStartTime())
                            , TimeUnit.MILLISECONDS.toSeconds((long) interfaceMp3.getStartTime()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) interfaceMp3.getStartTime())))
            );
            Log.i("handler", "handler");
            seekbar.setProgress((int) interfaceMp3.getStartTime());

            musica.setText(interfaceMp3.getMusicName());
            artista.setText(interfaceMp3.getMusicArtist());

            //Mudar de musica quando a musica chega no final
            if (interfaceMp3.getStartTime() > interfaceMp3.getFinalTime()-500) {
                Log.i(CATEGORIA, "Trocando de musica!");
                ford.performClick();
            }

            myHandler.postDelayed(this, 500);
        }
    };
    @Override
    public void onDestroy(){
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
        interfaceMp3.realeaseplayers();
        Log.i(CATEGORIA, "Activyti destruida!");
        getActivity().getApplicationContext().unbindService(this);
    }
}
