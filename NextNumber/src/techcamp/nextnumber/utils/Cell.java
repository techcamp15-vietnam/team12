/**
@author 12A Bui Duc Hanh
 */
package techcamp.nextnumber.utils;

public class Cell {
	private int value;
	private int effect;
	private boolean touchable;
	public static final int NONE = 0;
	public static final int BONUS = 11;
	public static final int DOUBLE = 22;
	public static final int TRIPLE = 33;
	public static final int BLINK = 44;

	public Cell(int value, int effect, boolean t) {
		this.value = value;
		this.effect = effect;
		this.touchable = t;
	}

	public void setValue(int t) {
		this.value = t;
	}

	public void seteffect(int t) {
		this.effect = t;
	}

	public void settouchable(boolean t) {
		this.touchable = t;
	}

	public boolean checkvalue(int i) {
		return (this.value == i);
	}

	public int getValue() {
		return this.value;
	}

	public int geteffect() {
		return this.effect;
	}

	public boolean gettouchable() {
		return this.touchable;
	}
}
