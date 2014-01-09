/**
@author  12A Bui Duc Hanh
 */
package techcamp.nextnumber.manager;

import java.util.Vector;

import android.app.Activity;
import android.content.SharedPreferences;
public class StorageManager {
	public static final int MAX_LIST=11;
	public static final int ACHIVEMENT = 16;
	public static final String HighScore = "N_HighScore";
	public static final String Achivement = "N_Achivement";
	public static Activity main;
	
	public StorageManager(Activity main){
		this.main=main;
	}
	
	public void Add(int mode, Vector v){
		switch (mode) {
			case 1: AddHighScore(v);break;
			case 2: AddAchivement(v);break;
		}
		
	}
	public void AddHighScore(Vector v) { 
		SharedPreferences settings = this.main.getSharedPreferences(HighScore, 0);
		SharedPreferences.Editor editor = settings.edit();
		for (int i =1;i<MAX_LIST;i++)
		{
			String s = Integer.toString(i);
			Float t= Float.parseFloat(v.get(1).toString());
			String s0= v.get(0).toString();
			Float tx= settings.getFloat(s+"v",1000f);
			if (tx>t) {
				editor.remove(s+"s");
				editor.remove(s+"v");
				editor.commit();
				editor.putString(s+"s", s0);
				editor.putFloat(s+"v", t);
				editor.commit();
				break;
			}
		}
	}
	public Vector returnHighScore(){
		Vector v= new Vector();
		SharedPreferences settings = this.main.getSharedPreferences(HighScore, 0);
		for (int i=1;i<MAX_LIST;i++){
			String name = settings.getString(Integer.toString(i)+"s","NoBody");
			float m = settings.getFloat(Integer.toString(i)+"v",10000f);
			Vector temp= new Vector();
			temp.add(0,name);
			temp.add(1,m);
			v.add(temp);
		}
		return v;
	}
	public void AddAchivement(Vector v) {
		// TODO Auto-generated method stub
		SharedPreferences settings = this.main.getSharedPreferences(Achivement, 0);
		SharedPreferences.Editor editor = settings.edit();
		String key =v.get(0).toString();
		String des = settings.getString(key, null);
		editor.remove(key);
		editor.commit();
		editor.putBoolean(key+"v", true);
		editor.putString(key, des);
		editor.commit();
	}
	public Vector returnAchivement(){
		Vector v= new Vector();
		SharedPreferences settings = this.main.getSharedPreferences(HighScore, 0);
		for (int i=1;i<ACHIVEMENT;i++){
			String name = settings.getString(Integer.toString(i),"NoAchivement");
			boolean m = settings.getBoolean(Integer.toString(i)+"v",false);
			Vector temp= new Vector();
			temp.add(0,name);
			temp.add(1,m);
			v.add(temp);
		}
		return v;
	}
}
