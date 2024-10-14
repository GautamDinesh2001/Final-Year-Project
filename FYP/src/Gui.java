import java.awt.*;
import javax.swing.*;


public class Gui {
	Calculate calculate;
	float[] result;
	JFrame f2;
	JLabel labelC;
	MyCircle circle;
	 public Gui(){
	        calculate = null;
	    }
	    public Gui(Calculate calculate) {
	        this.calculate = calculate;
	        //System.out.println("number:"+this.calculate.getNumber()+" Max X&Y:"+this.calculate.getMaxx()+" "+this.calculate.getMaxy());
	    }
	    /*
	    public Gui(float[] LLSresult)
	    {
	    	this.result=LLSresult;
	    	System.out.println("cX:"+result[0]+" cY:"+result[1]);
	    }
	    */
	public void draw(float[] result)
	{
		f2=new JFrame();
        f2.setTitle("GUI LoRa estimation result");
        //f2.setLayout(null);
        JPanel jp=new JPanel();
        jp.setBackground(Color.white);
        jp.setPreferredSize(new Dimension(1000,1000));
        f2.setSize( 1000, 1000);
        f2.add(jp);
        int diat=50;
        MyRect rect=new MyRect(diat,diat,calculate.getMaxx(),calculate.getMaxy());
        rect.repaint();
        //jp.add(rect);
        f2.add(rect);
        JLabel labelMax = new JLabel(String.format("(%s,%s)",calculate.getMaxx()/100,calculate.getMaxy()/100)+"(m)" );
        labelMax.setBounds(calculate.getMaxx()+70,calculate.getMaxy()+40,60,10);
        f2.add(labelMax);
        
       
        JLabel labelMin = new JLabel(String.format("(%s,%s)",0,0));
        labelMin.setBounds(20,0+40,60,10);
        f2.add(labelMin);
        //rect.setBounds(calculate.getMaxx()/2,calculate.getMaxy()/2,calculate.getMaxx(),calculate.getMaxy());
        
        for(int i=0; i<calculate.getNumber(); i++) {
            int tempx = calculate.getList().get(i).get(0);
            int tempy = calculate.getList().get(i).get(1);
            Triangle trg = new Triangle(diat+tempx, diat+tempy,10,10);
            trg.setBackground(Color.red);
            f2.add(trg);
            //trg.setBounds(tempx,tempy,10,10);
            JLabel label = new JLabel(String.format("(%s,%s)",tempx/100,tempy/100));
            label.setBounds(tempx+70,tempy+40,60,10);
            f2.add(label);
        }
       

        JLabel label = new JLabel(String.format("(%.1f,%.1f)",result[0],result[1]));
        label.setBounds((int)result[0]*100+10,(int)result[1]*100+10,60,10);
        f2.add(label);
        
        circle =new MyCircle(result);
        f2.add(circle);

        f2.setLocationRelativeTo(null); 
        f2.setVisible(true);
        f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void closeJframe()
	{
		//f2.remove(labelC);
		f2.dispose();
	}
	class MyRect extends JPanel
	{
		int x,y,w,h;
	    public MyRect( int x, int y, int w, int h)
	    {
	        super();
	        setBounds( x, y, w+10, h+10);
	        setBackground( Color.white );
	        this.x = x;
	        this.y = y;
	        this.w = w;
	        this.h = h;
	    }

	    public void paint( Graphics g )
	    {
	        g.drawRect(0,0,w,h);
	        paintChildren( g );
	    }
	}
	
	class MyCircle extends JComponent
	{
		private float resultX;
		private float resultY;
		
		public MyCircle(float[] result)
		{
			if(result[0]==0.0 && result[1]==0.0)
			{
				this.resultX=-30;
				this.resultY=-30;
			}
			else
			{
			this.resultX=result[0]*100;
			this.resultY=result[1]*100;
			}
			setBackground(Color.blue);
			
		}
		public void paint(Graphics g)
		{
			Graphics2D g2= (Graphics2D) g;
			Graphics2D g2d= (Graphics2D) g;
			RenderingHints rh=new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON );
			g2.setRenderingHints(rh);
			g2d.setRenderingHints(rh);
		
			
			g2d.setColor(getBackground());
			g2d.setColor(new Color(135,206,235,255));
			
			
			g2d.setColor(new Color(135,206,235,128));
			double g2dw=calculate.getMaxx()*0.1;
			double g2dh=calculate.getMaxy()*0.1;
			g2d.fillOval((int)(resultX+50-50),(int)(resultY+50-50),(int)(calculate.getMaxx()*0.2),(int)(calculate.getMaxy()*0.2));
			
			g2.setColor(Color.BLUE);
			g2.fillOval((int)resultX+50,(int)resultY+50,20,20);
		}
	}
	class Triangle extends JComponent
	{
	    int x,y,w,h;
	    public Triangle( int x, int y, int w, int h)
	    {
	          super();
	          setBounds( x, y, w, h );
	          setBackground( Color.black );
	    }

	    public void paint( Graphics g )
	    {
	        g.setColor( getBackground() );
	          int[] xArr = null;
	          int[] yArr = null;
	      //double temp = 1.7320508;
	      //xArr = new int[] {x-5, x+5, x };
	      //yArr = new int[] {(int)(y-temp/6.0), (int)(y-temp/6.0), (int)(y+temp/3.0)};
	        xArr = new int[] { 0, getWidth()/2 , getWidth() };
	        yArr = new int[] { getHeight(), 0, getHeight() };
	        g.fillPolygon( xArr, yArr, 3 );
	          paintChildren( g );
	    }
	}

}
