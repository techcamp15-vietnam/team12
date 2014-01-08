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
		int effects[] = { Celldata.NOMAL, Celldata.DOUBLE, Celldata.BOOM,
				Celldata.BLIND };
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
			boolean h = false;
			while (!h) {
				Random x = new Random();
				int xvalue = x.nextInt(mode);
				h = values[xvalue];
				this.CA[i].seteffect(effects[xvalue]);
			}
		}
	}

	// Set the value for a CellData which have ID is i
	public void setCellData(int i, int value, int effect, boolean t) {
		this.CA[i].setValue(value);
		this.CA[i].seteffect(effect);
		this.CA[i].settouchable(t);
	}

	public boolean isDone(int i) {
		return (this.CA[i].getValue() < nextint);
	}

	public boolean isNextInt(int i) {
		return (this.CA[i].getValue() == nextint);
	}

	public int touch(int i) {
		if (isNextInt(i)) {
			switch (this.CA[i].geteffect()) {
			case Celldata.BOOM:
				return (go_boom());
			default:
				nextint++;
				this.CA[i].settouchable(false);
			}
			return -1;
		} else
			return -2;
	}

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

	public boolean check_complete() {
		for (int i = 0; i < 25; i++)
			if (this.CA[i].gettouchable())
				return true;
		return false;
	}
}
