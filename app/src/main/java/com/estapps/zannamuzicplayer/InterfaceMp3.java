package com.estapps.zannamuzicplayer;

/**
 * Interface para fazer o bind do Service MediaPlayerService que a implementa
 * 
 * @author Estevão
 *
 */
public interface InterfaceMp3 {
	public void play();
	public void pause();
	public void stop();
	public void ford();
	public void back();
	public void resume();
	public void diminuirsom();
	public void aumentarsom();
	public double getFinalTime();
	public double getStartTime();
	public String getMusicArtist();
	public String getMusicName();
	public void realeaseplayers();
	public String getMusicFileName();
	public Integer getMusicID();
}
