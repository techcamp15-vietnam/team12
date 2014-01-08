package techcamp.nextnumber.utils;

public class Celldata {
	private int value;
	private int effect;
	private boolean touchable;
	public static final int NOMAL = 0;
	public static final int DOUBLE = 1;
	public static final int BOOM = 2;
	public static final int BLIND = 3;
	public Celldata(int value, int effect,boolean t){
		this.value=value;
		this.effect=effect;
		this.touchable=t;
	}
	
	public void setValue(int t)
	{
		this.value=t;
	}
	public void seteffect(int t){
		this.effect=t;
	}
	public void settouchable(boolean t){
		this.touchable =t;
	}
	
	public boolean checkvalue(int i){
		return (this.value==i);
	}
	
	public int getValue()
	{
		return this.value;
	}
	public int geteffect(){
		return this.effect;
	}	
	public boolean gettouchable(){
		return this.touchable;
	}
}
