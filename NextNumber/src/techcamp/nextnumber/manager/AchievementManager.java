/**
@author  12A Bui Duc Hanh
 */
package techcamp.nextnumber.manager;

import java.util.Vector;

import android.util.Log;

public class AchievementManager {
	public static int game_play;
	public static int num_double;
	public static int num_triple;
	public static int num_bonus;
	public static int num_blink;
	public static int num_win;
	public static long best_time;
	public static String game_plays="Game Play";
	public static String num_doubles="Double Number";
	public static String num_triples="Triple Number";
	public static String num_bonuss="Bonus Number";
	public static String num_blinks="Blink_Number";
	public static String num_wins="Game win";
	public static boolean[] status = new boolean[12];
	public static final String[] AchieveName={"The way of Success","Try hard spirit","Little Luck","Ninja Tracker","Never Give Up","Slowdown","Fast and Furios",
	"I'm winner", "Master Number","ChosenOne","Lucky Boy","Try hard Imortal"};
	public static final String[] AchieveDes={"Complete a game.\n Only one way to success. Go step and step.","Complete a game have double click number\n Do the same work, different result.",
		"Complete a game have boom number\n Do less, more effective","Complete a game have Blink Number\n You can run but you can't hide","Complete a game have Triple click number\n Lose not means failure. Failure means give up",
		"Complete a game less than 50s\n Don't hurry! this is game", "Complete a game less than 10s\n We're fast. We have power","Win one game in mutliplayer\n You're good, I'm better","Complete a game less than 4s\n Number in my breath!",
		"Win 50 games in multiplay\n I'm only defeated by myself!"," Touch 10 Bonus Numbers \n The luck behide me","Touch 10 Triple Numbers\n So hard. but it ok!"};
	public static void loadAchi(){
		game_play=StorageManager.getData(game_plays);
		num_double=StorageManager.getData(num_doubles);
		num_triple=StorageManager.getData(num_triples);
		num_bonus=StorageManager.getData(num_bonuss);
		num_blink=StorageManager.getData(num_blinks);
		num_win=StorageManager.getData(num_wins);
		best_time=StorageManager.returnBestime(0);
	}
	public static void getStatus(){
		Vector v = StorageManager.returnAchivement();
		for (int i =0;i<v.size();i++){
			status[i]=Boolean.valueOf(v.get(i).toString());
		}
	}
	public static void UpdateStatus(){
		for(int i=0;i<12;i++){
			Log.i("Err",""+i);
			if (!status[i]){
				switch (i){
				case 0: 
					if (game_play>0) StorageManager.AddAchivement(i); status[i]=true; break;
				case 1:
					if (num_double>0) StorageManager.AddAchivement(i); status[i]=true; break;
				case 2:
					if (num_bonus>0) StorageManager.AddAchivement(i); status[i]=true; break;
				case 3:
					if (num_blink>0) StorageManager.AddAchivement(i); status[i]=true; break;
				case 4:
					if (num_triple>0) StorageManager.AddAchivement(i); status[i]=true; break;
				case 5:
					if (best_time<50000) StorageManager.AddAchivement(i); status[i]=true; break;
				case 6:
					if (best_time<10000) StorageManager.AddAchivement(i); status[i]=true; break;
				case 7:
					if (num_win>0) StorageManager.AddAchivement(i); status[i]=true; break;
				case 8: 
					if (best_time<4000) StorageManager.AddAchivement(i); status[i]=true; break;
				case 9:
					if (num_win>49) StorageManager.AddAchivement(i); status[i]=true; break;
				case 10:
					if (num_bonus>9) StorageManager.AddAchivement(i); status[i]=true; break;
				case 11: 
					if (num_triple>9) StorageManager.AddAchivement(i); status[i]=true; break;
				}
			}
		}
	}
	
	// Update the Achievemnt. 
	public static void Update(){
		StorageManager.Update(game_plays,game_play);
		StorageManager.Update(num_doubles,num_double);
		StorageManager.Update(num_triples,num_triple);
		StorageManager.Update(num_bonuss,num_bonus);
		StorageManager.Update(num_blinks,num_blink);
		StorageManager.Update(num_wins,num_win);
		for(int i=1;i<12;i++){
			if (status[i]) StorageManager.AddAchivement(i);
		}
	}
}