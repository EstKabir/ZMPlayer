package com.estapps.zannamuzicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


public class ServicoMp3 extends Service implements InterfaceMp3{
	private static final String CATEGORIA = "Player ZM";
	MediaPlayer player01;
	MediaPlayer player02;
	int playerAtual=1;

	//Gerenciamento das musicas
	UtilMp3 utilMp3=new UtilMp3();
	int totalMusicas= utilMp3.getListaMusicasInt().size()-1;
	int musicaAtual=0;

	//Gerenciamento do tempo
	private double startTime = 0;
	private double finalTime = 0;
	public double getStartTime(){
		if (playerAtual==1) {
			startTime = player01.getCurrentPosition();
		}else if(playerAtual==2){
			startTime = player02.getCurrentPosition();
		}
		return startTime;
	}

	@Override
	public String getMusicArtist() {
		return utilMp3.ExtractArtista(this,utilMp3.getListaMusicasInt().get(musicaAtual));
	}

	@Override
	public String getMusicName() {
		return utilMp3.ExtractTitulo(this,utilMp3.getListaMusicasInt().get(musicaAtual));
	}

	@Override
	public void realeaseplayers() {
		if (playerAtual==1) {
			player01.stop();
			player01.release();
			player01=null;
			player02.release();
			player02=null;
		}else if(playerAtual==2){
			player02.stop();
			player02.release();
			player02=null;
			player01.release();
			player01=null;
		}


	}

	@Override
	public Integer getMusicID() {
		return utilMp3.getListaMusicasInt().get(musicaAtual);
	}

	@Override
	public String getMusicFileName() {
		return utilMp3.getListaMusicasString().get(musicaAtual);
	}


	public double getFinalTime(){
		if (playerAtual==1) {
			finalTime = player01.getDuration();
		}else if(playerAtual==2){
			finalTime = player02.getDuration();
		}
		return finalTime;
	}

	//Metodos player
	private void RodarMedia(){
		if (playerAtual==1) {
			player01 = MediaPlayer.create(this, utilMp3.getListaMusicasInt().get(musicaAtual));
			player01.start();
			Log.i(CATEGORIA, "Star player 01");
		}else if(playerAtual==2){
			player02 = MediaPlayer.create(this, utilMp3.getListaMusicasInt().get(musicaAtual));
			player02.start();
			Log.i(CATEGORIA, "Star player 02");
		}
	}
	private void ParaMusica(){
		if (playerAtual==1) {
			player01.stop();
			player01.reset();
			Log.i(CATEGORIA, "Stop + reset player 01");
		}else if(playerAtual==2){
			player02.stop();
			player01.reset();
			Log.i(CATEGORIA, "Stop + reset player 02");
		}
	}

	private void TrocarPlayer(){
		if (playerAtual==1) {
			playerAtual=2;
			Log.i(CATEGORIA,"Trocou para o player 2");
		}else if(playerAtual==2){
			playerAtual=1;
			Log.i(CATEGORIA,"Trocou para o player 1");
		}
	}
	private final IBinder conexao = new LocalBinder();

	public class LocalBinder extends Binder{
		public InterfaceMp3 getInterfaceMp3(){
			return ServicoMp3.this;
		}

	}


	@Override
	public IBinder onBind(Intent intent) {
		// retorna a classe ConexaoInterfaceMp3 para a activity utilizar
		Log.i(CATEGORIA,"ServicoMp3onBind(),aqui retorna um bind");
		return conexao;
	}

	@Override
	public void onCreate() {
		Log.i(CATEGORIA, "ServicoMp3 onCreate()");
	}



	// InterfaceMp3.start(mp3)
	public void play() {
		try {
				RodarMedia();
		} catch (Exception e){
			Log.e(CATEGORIA, e.getMessage(), e);
		}
	}

	// InterfaceMp3.pause()
	public void pause() {
		if (playerAtual==1) {
			player01.pause();
			Log.i(CATEGORIA,"pausou player 1");
		}else if(playerAtual==2){
			player02.pause();
			Log.i(CATEGORIA, "pausou player 2");
		}
	}

	// InterfaceMp3.stop()
	public void stop() {
		ParaMusica();
	}

	@Override
	public void ford() {
		try {
			if (musicaAtual==totalMusicas){
				ParaMusica();
				musicaAtual=0;
				TrocarPlayer();
				RodarMedia();
			}else{
				ParaMusica();
				musicaAtual+=1;
				TrocarPlayer();
				RodarMedia();
			}
		} catch (Exception e){
			Log.e(CATEGORIA, e.getMessage(), e);
		}
	}

	@Override
	public void back() {
		try {
			if(getStartTime()<1000.0) {
				if (musicaAtual == 0) {
					ParaMusica();
					musicaAtual = totalMusicas;
					TrocarPlayer();
					RodarMedia();

				} else {
					ParaMusica();
					musicaAtual -= 1;
					TrocarPlayer();
					RodarMedia();
				}
			}else{
				ParaMusica();
				RodarMedia();
			}
		} catch (Exception e){
			Log.e(CATEGORIA, e.getMessage(), e);
		}
	}

	@Override
	public void resume() {
		if (playerAtual==1) {
			player01.start();
			Log.i(CATEGORIA, "resume 01");
		}else if(playerAtual==2){
			player02.start();
			Log.i(CATEGORIA, "resume 02");
		}
	}

	@Override
	public void diminuirsom() {

	}

	@Override
	public void aumentarsom() {

	}


	@Override
	public void onDestroy() {
		Log.i(CATEGORIA, "ServicoMp3 onDestroy(). Aqui o MediaPlayer tamb�m foi encerrado.");
		player01.reset();
	}
}
