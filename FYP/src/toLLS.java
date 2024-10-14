import java.util.Comparator;

public class toLLS {
	
	String t,b;
	double diff,Alpha,xt,yt,xb,yb,distance;
	public toLLS(String t,String b, Double diff,double Alpha, double xt, double yt ,double xb, double yb, double distance)
	{
		this.t=t;
		this.b=b;
		this.diff=diff;
		this.Alpha=Alpha;
		this.xt=xt;
		this.yt=yt;
		this.xb=xb;
		this.yb=yb;
		this.distance=distance;
	}
	
	public String toString()
	{
		return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s,", this.t,this.b,this.diff,this.Alpha,this.xt,this.yt,this.xb,this.yb,this.distance);
	}


}
