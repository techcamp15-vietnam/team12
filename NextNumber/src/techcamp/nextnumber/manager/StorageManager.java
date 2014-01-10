/**
@author  12A Bui Duc Hanh
 */
package techcamp.nextnumber.manager;

import java.util.Vector;
import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.TextView;
public class StorageManager {
	public static final int MAX_LIST=9;
	public static final int ACHIVEMENT = 12;
	public static final String HighScore = "N_HighScore";
	public static final String Achivement = "N_Achivement";
	public static Activity main;
	
//	public StorageManager(Activity main){
//		this.main=main;
//	}
	public static void setmain(Activity main){
		StorageManager.main = main;
	}
	public static void AddHighScore(int mode, Vector v) { 
		SharedPreferences settings = main.getSharedPreferences(HighScore, 0);
		SharedPreferences.Editor editor = settings.edit();
		long t= Long.parseLong(v.get(1).toString());
		String s0= v.get(0).toString();
		for (int i =1;i<MAX_LIST;i++)
		{
			String s = Integer.toString(i);
			long tx= settings.getLong(mode+s+"v",1000000);
			String sx= settings.getString(mode+s+"s","Nobody");
			if (tx>t) {
				editor.remove(mode+s+"s");
				editor.remove(mode+s+"v");
				editor.commit();
				editor.putString(mode+s+"s", s0);
				editor.putLong(mode+s+"v", t);
				editor.commit();
				t=tx;s0=sx;
			}
		}
	}
	public static long returnBestime(int mode){
		SharedPreferences settings = main.getSharedPreferences(HighScore, 0);
			long m = settings.getLong(mode+1+"v",1000000);
			return m;
	}
	public static Vector returnHighScore(int mode){
		Vector v= new Vector();
		SharedPreferences settings = main.getSharedPreferences(HighScore, 0);
		for (int i=1;i<MAX_LIST;i++){
			String name = settings.getString(mode+Integer.toString(i)+"s","NoBody");
			long m = settings.getLong(mode+Integer.toString(i)+"v",1000000);
			Vector temp= new Vector();
			temp.add(0,name);
			temp.add(1,m);
			v.add(temp);
		}
		return v;
	}
	public static void AddAchivement(int mode) {
		// TODO Auto-generated method stub
		SharedPreferences settings = main.getSharedPreferences(Achivement, 0);
		SharedPreferences.Editor editor = settings.edit();
		String key =Integer.toString(mode);
		editor.remove(key);
		editor.commit();
		editor.putBoolean(key, true);
		editor.commit();
	}
	public static Vector returnAchivement(){
		Vector v= new Vector();
		SharedPreferences settings = main.getSharedPreferences(Achivement, 0);
		for (int i=0;i<ACHIVEMENT;i++){
			boolean m = settings.getBoolean(Integer.toString(i),false);
			v.add(0,m);
		}
		return v;
	}
	public static int getData(String key){
		SharedPreferences settings = main.getSharedPreferences(Achivement, 0);
		int i= settings.getInt(key, 0);
		return i;
	}
	public static void Update(String key, int values) {
		// TODO Auto-generated method stub
		SharedPreferences settings = main.getSharedPreferences(Achivement, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		editor.commit();
		editor.putInt(key, values);
		editor.commit();
	}
}
