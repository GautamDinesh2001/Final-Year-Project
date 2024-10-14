import java.util.List;
import java.util.ArrayList;
public class SDcal {
	List<List<String>> list= new ArrayList<>(); 
	List<List<String>> listUpdateInSD=new ArrayList<>();
	int count=0;
	double SD=getSD();
	double mean=getMean();
	SDcal(List<List<String>> list)
	{
		//System.out.println("line 11");
		this.list = list;
		count++;
		double SD=0.0;
    	double mean=0.0;
		calSD();
	}
	public void calSD()
	{
		
    	float tempSum=0;
    	List<Double> tempSDForList=new ArrayList<>();
    	/*
    	List<String> str=list.get(i);
    	for(int i=0; i<30;i++)
    	{
    		str.add(String.format("%s", i*-1));
    	}
    	*/
    	List<Integer> flo=new ArrayList<>();
    	for(int i=0;i<list.size();i++)
		{
			List<String> temp=list.get(i);
			//System.out.println(temp.get(2));
			tempSum+=Integer.parseInt(temp.get(2));
		}
    	
		for (int i=0;i<list.size();i++)
		{
			flo.add(Integer.parseInt(list.get(i).get(2)));
			//System.out.println(flo.get(i));
			//tempSum=tempSum+flo.get(i);
		}
    	//System.out.println("sum: "+tempSum);
    	mean=tempSum/flo.size();
    	//System.out.println("mean:"+mean);
    	double tempSDC=0.0;
    	for(int i=0; i<flo.size();i++)
    	{
    		tempSDC+=Math.pow(flo.get(i)-mean, 2);
    	}
    	SD=Math.sqrt(tempSDC/flo.size());
    	//System.out.println("SD:"+SD);
    	/*
    	tempSum=0;
    	tempSDForList.add(mean);
    	tempSDForList.add(SD);
    	updataList(tempSDForList);
    	//System.out.println("0:"+tempSDForList.get(0)+" 1:"+tempSDForList.get(1));
    	mean=0;
    	SD=0;
    	*/
	}
	
	public void printList()
	{
		double tempsum=0;
		for(int i=0;i<list.size();i++)
		{
			List<String> temp=list.get(i);
			System.out.println(temp.get(2));
			tempsum+=Integer.parseInt(temp.get(2));
		}
	}
	
	public void updataList(List<Double> tempList)
	{
			//System.out.println("tempList size:"+tempList.size());
			
			List<String> templistN=new ArrayList<>();
			templistN.add(list.get(0).get(0));
			templistN.add(list.get(1).get(1));
			templistN.add(tempList.get(0).toString());
			templistN.add(tempList.get(1).toString());
			//System.out.println("T:"+list.get(0).get(0)+ " B:"+list.get(1).get(1)+" M:"+tempList.get(0).toString()+" SD:"+ tempList.get(1).toString());
			listUpdateInSD.add(templistN);
			System.out.println("SDcal listUpdateInSD:"+listUpdateInSD.size());
	}
	
	public void resetCount()
	{
		count=0;
	}
	public void printlistUpdateInSDsize()
	{
		System.out.println("size:"+listUpdateInSD.size());
	}
	public List<List<String>> SendListToCal()
	{
		if(listUpdateInSD!=null)
		{
			return listUpdateInSD;
		}
		else
		{
			return null;
		}
		//Calculate cal=new Calculate(listUpdateInSD);
		//cal.showListFromSD();
	}
	public double getSD()
	{
		return SD;
	}
	public double getMean()
	{
		return mean;
	}
}
