
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
public class Calculate {
	int number;
	int maxx;
	int maxy;
	List<List<Integer>> list;
	List<List<Integer>> list2;
	List<List<String>> ListForSD;
	List<float[]> lines;
	List<float[]> lines2;
	int newListsize=0;
	float[][] MatrixA;
	float[] Matrixb;
	Gui gui;
	//LoRaTerminal loraT= new LoRaTerminal();
	//List<float[]> lines2;
	
	
	Calculate(List<List<String>> tempList)
	{
		this.ListForSD=tempList;
	}
	
	Calculate(int num, int MaxX, int MaxY, List<List<Integer>> listForGUI, List<List<Integer>> listForCal)
	{
		this.number=num;
		this.maxx=MaxX;
		this.maxy=MaxY;
		this.list=listForGUI;
		this.list2=listForCal;
		lines=new ArrayList<>();
	}
	private List<float[]> getlines() {
		// TODO Auto-generated method stub
		return this.lines;
	}
	Calculate(int number) {
		this.number = number;
	}
	Calculate(double maxX,double maxY)
	{
		this.maxx=(int)maxX;
		this.maxy=(int)maxY;
	}
	public Calculate() {
		// TODO Auto-generated constructor stub
	}

	/*
	Calculate(List<List<Integer>> list)
	{
		this.list=list;
		/*
		System.out.println("size:"+ this.list.size());
		for(int i=0;i<list.size();i++)
		{
			List<Integer> temp=this.list.get(i);
			System.out.println("F: "+temp.get(0)+" S:"+temp.get(1));
		}
		*/
	//}
	//input method
	/*
	public void input() {
		System.out.println("number:"+number);
		//@SuppressWarnings("resource")
		//Scanner localScanner;
		//Scanner in = new Scanner(System.in);	
		Scanner inputScanner = new Scanner(System.in);
		System.out.println("Input Max X and Max Y :");
		maxx = inputScanner.nextInt()*100;
		maxy = inputScanner.nextInt()*100;
		System.out.println("Input n (x,y) coordinate:");
		for(int i=0; i<number; i++) {
			int x = inputScanner.nextInt();
			int y = inputScanner.nextInt();
			List<Integer> tempList = new ArrayList<>();
			tempList.add(x*100);
			tempList.add(y*100);
			list.add(tempList);
		}	
	}
	*/
	// calculate
	public float[] getEquation(List<Integer> num1, List<Integer> num2) {
		float x1 = num1.get(0)/100;
		float y1 = num1.get(1)/100;
		float x2 = num2.get(0)/100;
		float y2 = num2.get(1)/100;
		if(x1==x2) {
			float [] res = {1,0,x1};
			return res;
		}
		if(y1==y2) {
			float [] res = {0,1,y1};
			return res;
		}
		float a = y1-y2;
		float b = x2-x1;
		float c=0;
		if((a*x1+b*y1)==0)
		{
			c=0;
		}
		else
			{
			c = (a*x1+b*y1);
			}
		
		float[] res = {a,b,c};
		return res;
	}
	
	public float[] getEquation2(List<Double> num1, List<Double> num2) {
		float x1 = (float) (num1.get(0)/100);
		float y1 = (float) (num1.get(1)/100);
		float x2 = (float) (num2.get(0)/100);
		float y2 = (float) (num2.get(1)/100);
		if(x1==x2) {
			float [] res = {1,0,x1};
			return res;
		}
		if(y1==y2) {
			float [] res = {0,1,y1};
			return res;
		}
		float a = y1-y2;
		float b = x2-x1;
		float c=0;
		if((a*x1+b*y1)==0)
		{
			c=0;
		}
		else
			{
			c = (a*x1+b*y1);
			}
		
		float[] res = {a,b,c};
		return res;
	}
	//print all equations
	public void printAllEquation() {
		int n = list.size();
		System.out.println("number of Equation:"+n);
		////////////////////////////////////////////////////////////////////
		for(int i=0; i<n; i++) {
			List<Integer> num1 = list.get(i);
			for(int j=i+1; j<n; j++) {
				List<Integer> num2 = list.get(j);
				
				//System.out.println("num1x:"+num1.get(0)+" num1y:"+num1.get(1)+" num2x:"+num2.get(0)+" num2y:"+num2.get(1));
				if(num1.get(0).equals(num2.get(0)))
				{
					
					System.out.println("case1:num1x:"+num1.get(0)+" num1y:"+num1.get(1)+" num2x:"+num2.get(0)+" num2y:"+num2.get(1));
					int num1x=num1.get(0);
					int num2x=num2.get(0);
					if((num2x==maxx/2&& num1x==maxx/2))
					{   
						System.out.println("case1.1:");
						float[] temp = getEquation(num1, num2);
						System.out.println(String.format("(%s,%s) (%s,%s) : %sx+%sy=%s", num1.get(0)/100, num1.get(1)/100, num2.get(0)/100, num2.get(1)/100,temp[0],temp[1],temp[2]));
						lines.add(temp);
					}
				}
				else if(num1.get(1).equals(num2.get(1)))
				{
					int num1y=num1.get(1);
					int num2y=num2.get(2);
					//System.out.println("case2:num1x:"+num1.get(0)+" num1y:"+num1.get(1)+" num2x:"+num2.get(0)+" num2y:"+num2.get(1));
					if(num1y==maxy/2&&num2y==maxy/2)
					{
						System.out.println("case2.2:");
						float[] temp = getEquation(num1, num2);
						System.out.println(String.format("(%s,%s) (%s,%s) : %sx+%sy=%s", num1.get(0)/100, num1.get(1)/100, num2.get(0)/100, num2.get(1)/100,temp[0],temp[1],temp[2]));
						lines.add(temp);
					}
				}
				else
				{
					//System.out.println("case3:num1x:"+num1.get(0)+" num1y:"+num1.get(1)+" num2x:"+num2.get(0)+" num2y:"+num2.get(1));
					//systemOut("Num1: "+ tempNum1+" Num2:"+tempNum2);
					float[] temp = getEquation(num1, num2);
					//System.out.println(String.format("1: %s 2: %s 3: %s", temp[0],temp[1],temp[2]));
					if (temp[0]!=0.0|| temp[1]!=0.0)
					{
						if((num1.get(0)-num2.get(0))!=0)
						{
						String out = String.format("%sx+%sy=%s",temp[0],temp[1],temp[2]);
						System.out.println(String.format("(%s,%s) (%s,%s) : %sx+%sy=%s", num1.get(0)/100, num1.get(1)/100, num2.get(0)/100, num2.get(1)/100,temp[0],temp[1],temp[2]));
						//System.out.print(out);
						lines.add(temp);
						}
					}
				}
			}
		}
	}
	private void systemOut(String string) {
		// TODO Auto-generated method stub
		
	}
	/*
	public <T> ArrayList<T> removeDuplicates(List<float[]> lines2)
    {
  
        // Create a new ArrayList
		List<float[]> newList = new ArrayList();
  
        // Traverse through the first list
        for (float[] element : lines2) {
  
            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
  
                newList.add(element);
            }
        }
  
        // return the new list
        return (ArrayList<T>) newList;
    }
    */
	public void printAllIntersectionPoint() {
		System.out.println("printAllIntersectionPoint");
		int n = newListsize;
		for(int i=0; i<n; i++) {
			float[] line1 = lines.get(i);
			for(int j=i+1; j<n; j++) {
				float[] line2 = lines.get(j);
				if(line1[0]!=line2[0] && line1[1]!=line2[1] && line1[2]!=line2[2]) 
				{
					//System.out.println(String.format("%sx+%sy+%s=0 is the same as %sx+%sy+%s=0",line1[0]/100,line1[1]/100,line1[2]/100,line2[0]/100,line2[1]/100,line2[2]/100));
				
					if((line1[0]/100*line2[1]/100-line2[0]/100*line1[1]/100)!=0)
					{
						double x =(line1[1]/100*line2[2]/100-line2[1]/100*line1[2]/100)/(line1[0]/100*line2[1]/100-line2[0]/100*line1[1]/100);
						double y = (line2[0]/100*line1[2]/100-line1[0]/100*line2[2]/100)/(line1[0]/100*line2[1]/100-line2[0]/100*line1[1]/100);
						System.out.println(String.format("%sx+%sy+%s=0 and %sx+%sy+%s=0 :",line1[0]/100,line1[1]/100,line1[2]/100,line2[0]/100,line2[1]/100,line2[2]/100));
						System.out.println(String.format("(%s,%s)",x,y));
					}
				}
			}
		}
	}
	/*
	public void printlist()
	{
		System.out.println("List<lines> size: "+ lines.size());
		float[] temp2;
		float[] temp;
		float[] temp3= new float[3];
		int tempN=lines.size();
		List<float[]> list2=new ArrayList<>();
		
		for (int i=0; i<tempN;i++)
		{
			temp2=lines.get(i);
			for(int j=i+1; j<tempN;j++)
			{
				temp=lines.get(j);
				if(temp2[0]!=temp[0]&& temp2[1]!=temp[1]&& temp2[2]!=temp[2])
				{
					temp3[0]=temp2[0];
					temp3[1]=temp2[1];
					temp3[2]=temp3[2];
					lines.remove(j);
					//lines2.add(temp3);
				}
			}
		}
		for(int i=0; i<lines.size();i++)
		{
			temp=lines.get(i);
			System.out.println(String.format("%sx+%sy=%s",temp[0]/100,temp[1]/100,temp[2]/100));
		}
		/*
		System.out.println("List<lines2> size: "+ lines2.size());
		for(int i=0; i<lines2.size();i++)
		{
			temp=lines2.get(i);
			System.out.println(String.format("%sx+%sy=%s",temp[0]/100,temp[1]/100,temp[2]/100));
		}
	
	
		//System.out.println("newList2:"+newList2.size());
	}
		*/
	
	public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMaxx() {
        return maxx;
    }

    public void setMaxx(int maxx) {
        this.maxx = maxx;
    }

    public int getMaxy() {
        return maxy;
    }

    public void setMaxy(int maxy) {
        this.maxy = maxy;
    }

    public List<List<Integer>> getList() {
        return list;
    }

    public void setList(List<List<Integer>> list) {
        this.list = list;
    }
    public List<List<Integer>> getList2()
    {
    	return list2;
    }
    public void showResult()
    {
    	System.out.println("num:"+number+" Max X&Y"+maxx+" "+maxy+" list:"+list);
    }
	public void showList2(List<List<Integer>> list)
	{
		for(int i=0;i<list.size();i++)
		{
			System.out.println(list.get(i).get(0)+" "+list.get(i).get(1)+" "+list.get(i).get(2));
		}
	}
	public void showListFromSD()
	{
		for(int i=0; i<ListForSD.size();i++)
		{
			System.out.println(String.format("T: %s B: %s M: %f SD: %f", ListForSD.get(i).get(0),ListForSD.get(i).get(1),
																		 ListForSD.get(i).get(2),ListForSD.get(i).get(3)));
		}
	}
	public void updataListInCal()
	{
		
	}
	public void printlines()
	{
		System.out.println("lines size:"+lines.size());
		for(int i=0; i<lines.size();i++)
		{
			System.out.println(String.format("a:%s, b:%s, c:%s", lines.get(i)[0],lines.get(i)[1],lines.get(i)[2]));
		}
	}
	public void calequation(List<List<Integer>> llsList)
	{
		lines2=new ArrayList<>();
		List<Integer> num1=new ArrayList<>();
		List<Integer> num2=new ArrayList<>();
		System.out.println("llslist size:"+llsList.size());
		float[][] tempMatrixA= new float[llsList.size()][3];
		float[] tempMatrixb=new float[llsList.size()];
		//maxx=800;
		//maxy=800;
		for (int i=0; i<llsList.size();i++)
		{	
			num1=new ArrayList<>();
			num2=new ArrayList<>();
			num1.add(llsList.get(i).get(1));
			num1.add(llsList.get(i).get(2));
			num2.add(llsList.get(i).get(4));
			num2.add(llsList.get(i).get(5));
			float[] temp= getEquation(num1,num2);
			if(num1.get(0).equals(num2.get(0)))
			{
				System.out.println("case1:num1x:"+num1.get(0)+" num1y:"+num1.get(1)+" num2x:"+num2.get(0)+" num2y:"+num2.get(1)+"maxx/2:"+(maxx/2));
				if((int)num2.get(0)==(int)(maxx/2))
				{   
					if((int)temp[0]==0 && (int)temp[1]==0 && (int)temp[1]==0)
					{
						System.out.println("all 0");
					}
					else
					{
						tempMatrixA[i][0]=temp[0];
						tempMatrixA[i][1]=temp[1];
						tempMatrixA[i][2]=1;
						tempMatrixb[i]=temp[2];
						lines2.add(temp);
					}
				}
			}
			else if(num1.get(1).equals(num2.get(1)))
			{
				System.out.println("case2:num1x:"+num1.get(0)+" num1y:"+num1.get(1)+" num2x:"+num2.get(0)+" num2y:"+num2.get(1)+" maxy/2: "+(maxy/2));
				if((int)num1.get(1)==(int)(maxy/2))
				{
					System.out.println("case2.2:");
					
					if((int)temp[0]==0 && (int)temp[1]==0 && (int)temp[1]==0)
					{
						System.out.println("all 0");
					}
					else
					{
						tempMatrixA[i][0]=temp[0];
						tempMatrixA[i][1]=temp[1];
						tempMatrixA[i][2]=1;
						tempMatrixb[i]=temp[2];
						lines2.add(temp);
					}
				}
			}
			else
			{
				//System.out.println("case3:num1x:"+num1.get(0)+" num1y:"+num1.get(1)+" num2x:"+num2.get(0)+" num2y:"+num2.get(1));
				//systemOut("Num1: "+ tempNum1+" Num2:"+tempNum2);
				//float[] temp = getEquation(num1, num2);
				//System.out.println(String.format("1: %s 2: %s 3: %s", temp[0],temp[1],temp[2]));
				if (temp[0]!=0.0|| temp[1]!=0.0)
				{
					if((num1.get(0)-num2.get(0))!=0)
					{
					String out = String.format("%sx+%sy=%s",temp[0],temp[1],temp[2]);
					System.out.println(String.format("(%s,%s) (%s,%s) : %sx+%sy=%s", num1.get(0)/100, num1.get(1)/100, num2.get(0)/100, num2.get(1)/100,temp[0],temp[1],temp[2]));
					//System.out.print(out);
					if((int)temp[0]==0 && (int)temp[1]==0 && (int)temp[1]==0)
					{
						System.out.println("all 0");
					}
					else
					{
						tempMatrixA[i][0]=temp[0];
						tempMatrixA[i][1]=temp[1];
						tempMatrixA[i][2]=1;
						tempMatrixb[i]=temp[2];
						lines2.add(temp);
					}
					}
				}
			}
			
			//System.out.println(String.format("temp: 0:%s, 1:%s, 2:%s", temp[0],temp[1],temp[2]));
			//System.out.println(String.format("(%s,%s) & (%s, %s) equation: %sx+%sy=%s", num1.get(0),num1.get(1),num2.get(0), num2.get(1), temp[0],temp[1],temp[2]));
			
		}
	}
	
	public void calequation2(List<List<String>> llsList)
	{
		lines2=new ArrayList<>();
		List<Double> num1=new ArrayList<>();
		List<Double> num2=new ArrayList<>();
		System.out.println("llslist size:"+llsList.size());
		float[][] tempMatrixA= new float[llsList.size()][3];
		float[] tempMatrixb=new float[llsList.size()];
		//maxx=800;
		//maxy=800;
		for (int i=0; i<llsList.size();i++)
		{	
			num1=new ArrayList<>();
			num2=new ArrayList<>();
			num1.add(Double.parseDouble(llsList.get(i).get(4)));
			num1.add(Double.parseDouble(llsList.get(i).get(5)));
			num2.add(Double.parseDouble(llsList.get(i).get(6)));
			num2.add(Double.parseDouble(llsList.get(i).get(7)));
			float[] temp= getEquation2(num1,num2);
			if(num1.get(0).equals(num2.get(0)))
			{
				System.out.println("case1:num1x:"+num1.get(0)+" num1y:"+num1.get(1)+" num2x:"+num2.get(0)+" num2y:"+num2.get(1)+"maxx/2:"+(maxx/2));
				if(num2.get(0)==(int)(maxx/2))
				{   
					if((int)temp[0]==0 && (int)temp[1]==0 && (int)temp[1]==0)
					{
						System.out.println("all 0");
					}
					else
					{
						tempMatrixA[i][0]=temp[0];
						tempMatrixA[i][1]=temp[1];
						tempMatrixA[i][2]=1;
						tempMatrixb[i]=temp[2];
						lines2.add(temp);
					}
				}
			}
			else if(num1.get(1).equals(num2.get(1)))
			{
				System.out.println("case2:num1x:"+num1.get(0)+" num1y:"+num1.get(1)+" num2x:"+num2.get(0)+" num2y:"+num2.get(1));
				if(num1.get(1)==(double)(maxy/2))
				{
					System.out.println("case2.2:");
					
					if((int)temp[0]==0 && (int)temp[1]==0 && (int)temp[1]==0)
					{
						System.out.println("all 0");
					}
					else
					{
						System.out.println(String.format("0:%s 1:%s 2:%s", temp[0],temp[1],temp[2]));
						tempMatrixA[i][0]=temp[0];
						tempMatrixA[i][1]=temp[1];
						tempMatrixA[i][2]=1;
						tempMatrixb[i]=temp[2];
						lines2.add(temp);
					}
				}
			}
			else
			{
				//System.out.println("case3:num1x:"+num1.get(0)+" num1y:"+num1.get(1)+" num2x:"+num2.get(0)+" num2y:"+num2.get(1));
				//systemOut("Num1: "+ tempNum1+" Num2:"+tempNum2);
				//float[] temp = getEquation(num1, num2);
				//System.out.println(String.format("1: %s 2: %s 3: %s", temp[0],temp[1],temp[2]));
				if (temp[0]!=0.0|| temp[1]!=0.0)
				{
					if((num1.get(0)-num2.get(0))!=0)
					{
					String out = String.format("%sx+%sy=%s",temp[0],temp[1],temp[2]);
					System.out.println(String.format("(%s,%s) (%s,%s) : %sx+%sy=%s", num1.get(0), num1.get(1), num2.get(0), num2.get(1),temp[0],temp[1],temp[2]));
					//System.out.print(out);
					if((int)temp[0]==0 && (int)temp[1]==0 && (int)temp[1]==0)
					{
						System.out.println("all 0");
					}
					else
					{
						tempMatrixA[i][0]=temp[0];
						tempMatrixA[i][1]=temp[1];
						tempMatrixA[i][2]=1;
						tempMatrixb[i]=temp[2];
						lines2.add(temp);
					}
					}
				}
			}
			
			//System.out.println(String.format("temp: 0:%s, 1:%s, 2:%s", temp[0],temp[1],temp[2]));
			//System.out.println(String.format("(%s,%s) & (%s, %s) equation: %sx+%sy=%s", num1.get(0),num1.get(1),num2.get(0), num2.get(1), temp[0],temp[1],temp[2]));
			
		}
	}
	
	public float[][] getMatrixA()
	{
		
		for(int i=0; i<lines2.size();i++)
		{
			if((int)lines2.get(i)[0]==0 &&(int)lines2.get(i)[1]==0&&(int)lines2.get(i)[2]==0)
			{
				lines2.remove(i);
			}
		}
		System.out.println(String.format("new lines2 size:", lines2.size()));
		MatrixA=new float[lines2.size()][3];
		for(int i=0;i<MatrixA.length;i++)
		{
			MatrixA[i][0]=lines2.get(i)[0];
			MatrixA[i][1]=lines2.get(i)[1];
			MatrixA[i][2]=1;
			System.out.println(String.format("%sx+%sy", MatrixA[i][0],MatrixA[i][1]));
		}
		return MatrixA;
	}
	public float[] getMatrixb()
	{	Matrixb=new float[lines2.size()];
		for(int i=0;i<lines2.size();i++)
		{	
			Matrixb[i]=lines2.get(i)[2];
			
			System.out.println(String.format("c: = %s", Matrixb[i]));
		}
		return Matrixb;
	}
	
	public float[] calIntersectionPoint(List<float[]> equation)
	{
		int n = equation.size();
		float[] tempResultToGuiInCircle= new float[2];
		for(int i=0; i<n; i++) {
			float[] line1 = equation.get(i);
			for(int j=i+1; j<n; j++) {
				float[] line2 = equation.get(j);
				if(line1[0]!=line2[0] && line1[1]!=line2[1] && line1[2]!=line2[2]) 
				{
					//System.out.println(String.format("%sx+%sy+%s=0 is the same as %sx+%sy+%s=0",line1[0]/100,line1[1]/100,line1[2]/100,line2[0]/100,line2[1]/100,line2[2]/100));
				
					if((line1[0]/100*line2[1]/100-line2[0]/100*line1[1]/100)!=0)
					{
						double x =(line1[1]/100*line2[2]/100-line2[1]/100*line1[2]/100)/(line1[0]/100*line2[1]/100-line2[0]/100*line1[1]/100);
						double y = (line2[0]/100*line1[2]/100-line1[0]/100*line2[2]/100)/(line1[0]/100*line2[1]/100-line2[0]/100*line1[1]/100);
						System.out.println(String.format("%sx+%sy+%s=0 and %sx+%sy+%s=0 :",line1[0]/100,line1[1]/100,line1[2]/100,line2[0]/100,line2[1]/100,line2[2]/100));
						System.out.println(String.format("(%s,%s)",x,y));
						tempResultToGuiInCircle[0]=(float) x;
						tempResultToGuiInCircle[1]=(float) y;

					}
				}
			}
		}
		return tempResultToGuiInCircle;
	}
	public List<float[]> getLines2()
	{
		for(int i=0;i<lines2.size();i++)
		{
			System.out.println(String.format("%s %s %s", lines2.get(i)[0],lines2.get(i)[1],lines2.get(i)[2]));
		}
			
		return lines2;
	}
	
}
