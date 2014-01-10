/**
@author  12A Bui Duc Hanh
 */
package techcamp.nextnumber.utils;

import java.util.Random;

import techcamp.nextnumber.scenes.GameScene;
import android.util.Log;

public class CellArray {
	public Cell[] CA = new Cell[25];
	public int nextint; // The number have touch.
	private int effect[] = { Cell.DOUBLE, Cell.BONUS, Cell.BLINK, Cell.TRIPLE };

	// innit the CellArray
	public CellArray() {
		for (int i = 0; i < 25; i++)
			this.CA[i] = new Cell(0, Cell.NONE, true);
		nextint = 1;
	}

	// Create data for a game play. if classic mode mode=1; challenge mode = 2
	public void CreateData(int mode) {
		nextint=1;	
		switch (mode) {
		case 1:
			CreateNormal();
			break;
		case 2:
			CreateEffect();
			break;
		}
	}

	// Create the challenge mode data
	private void CreateEffect() {
		CreateNormal();
		Random r = new Random();
		int numofeffect = r.nextInt(6) + 1;
		int numoftype[] = { 0, 0, 0, 0};
		 for (int i=0; i<numofeffect;i++)
         {
             Random x= new Random();
             int k = x.nextInt(11);
             switch (i) {
                 case 5: numoftype[3]++; break;
                 case 1:
                 case 0:
                 case 3:
                 case 7: numoftype[0]++; break;
                 case 2:
                 case 4:
                 case 10:
                 case 8: numoftype[1]++;break;
                 case 9:
                 case 6: numoftype[2]++; break;
             }
         }
		for (int i = 0; i < effect.length; i++) {
			for (int j = 0; j < numoftype[i]; j++) {
				Boolean k = true;
				while (k) {
					Random t = new Random();
					int id = t.nextInt(GameScene.LIMIT_SIZE);
					if (this.CA[id].geteffect() == 0) {
						this.CA[id].seteffect(effect[i]);
						k = false;
					}
				}
			}
		}
	}

	// Create the normal mode data
	private void CreateNormal() {
		boolean[] values = new boolean[26];
		for (int i = 0; i < 26; i++)
			values[i] = true;
		for (int i = 0; i < 25; i++) {

			boolean k = false;
			while (!k) {
				Random r = new Random();
				int rvalue = r.nextInt(25) + 1;
				k = values[rvalue];
				if (k) {
					this.CA[i].setValue(rvalue);
					values[rvalue] = false;
				}
			}
		}

	}

	// Set the value for a CellData which have ID is i
	public void setCellData(int i, int value, int effect, boolean t) {
		this.CA[i].setValue(value);
		this.CA[i].seteffect(effect);
		this.CA[i].settouchable(t);
	}

	// Check the CellData which have ID i is done? (touched?)
	public boolean isDone(int i) {
		return (this.CA[i].getValue() < nextint);
	}

	// Check the CellData which have ID i is have value same as nextint
	public boolean isNextInt(int i) {
		return (this.CA[i].getValue() == nextint);
	}

	// When button which ID i is touched, In data view, It's solved here
	public int touch(int i) {
		if (isNextInt(i)) {
			switch (this.CA[i].geteffect()) {
			case Cell.BONUS:
				return (go_boom());// It return the ID of button bonus
			default:
				nextint++;
				this.CA[i].settouchable(false);
			}
			return -1;// it means right click
		} else
			return -2;// it means wrong click
	}

	// When the Button what's touched has effect boom. It's solved here
	public int go_boom() {
		nextint++;
		int k = 26;
		for (int i = 0; i < 25; i++)
			if (this.CA[i].checkvalue(nextint)) {
				this.CA[i].settouchable(false);
				k = i;
				break;
			}
		if (k < GameScene.LIMIT_SIZE + 1)
			nextint++;
		return k;
	}

	// Check complete the game
	public boolean check_complete() {
		return (nextint >= GameScene.LIMIT_SIZE + 1);
	}
}
