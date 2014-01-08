/**
@author  12A Bui Duc Hanh
 */
package techcamp.nextnumber.utils;

import java.util.Random;

public class CellArray {
	private Celldata[] CA = new Celldata[25];
	private int nextint; // The number have touch.

	// innit the CellArray
	public CellArray() {
		for (int i = 0; i < 25; i++)
			this.CA[i] = new Celldata(0, 0, true);
		nextint = 1;
	}

	// Create data for a game play. if classic mode mode=1; challenge mode = 4
	public void CreateData(int mode) {
		boolean[] values = new boolean[26];
		int effects[] = { Celldata.NOMAL, Celldata.DOUBLE, Celldata.BOOM,Celldata.BLIND };
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
				Random x = new Random();
				int xvalue = x.nextInt(mode);
				this.CA[i].seteffect(effects[xvalue]);
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
			case Celldata.BOOM:
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
		nextint++;
		return k;
	}

	// Check complete the game
	public boolean check_complete() {
		return (nextint == 26);
	}
}
