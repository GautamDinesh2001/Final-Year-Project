import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
public class Compare {
	List<List<String>> listToLLS=new ArrayList<>();
	List<List<String>> list4ToLLS=new ArrayList<>();
	Compare()
	{}
	
	public void compRSSI(List<List<String>> listRecord, List<List<String>> listRealResult,List<List<Integer>>xylist)
	{
		double RssiDiffError=6.0;
		// combine the x y
		{
			for(int i=0; i<listRecord.size();i++)
			{
				List<String> tempListforRecord=new ArrayList<>();
				tempListforRecord=listRecord.get(i);
				//System.out.println(tempListforRecord.get(0));
				for(int j=0;j<xylist.size();j++)
				{
					//System.out.println(xylist.get(j).get(0));
					if(tempListforRecord.get(0).equals(xylist.get(j).get(0).toString()))
					{
						//System.out.println("same");
						listRecord.get(i).add(xylist.get(j).get(1).toString());
						listRecord.get(i).add(xylist.get(j).get(2).toString());
					}
				}  
			}
			for(int i=0; i<listRecord.size();i++)
			{
				List<String> tempListforRecord=new ArrayList<>();
				tempListforRecord=listRecord.get(i);
				//System.out.println(tempListforRecord.get(0));
				for(int j=0;j<xylist.size();j++)
				{
					//System.out.println(xylist.get(j).get(0));
					if(tempListforRecord.get(1).equals(xylist.get(j).get(0).toString()))
					{
						//System.out.println("same");
						listRecord.get(i).add(xylist.get(j).get(1).toString());
						listRecord.get(i).add(xylist.get(j).get(2).toString());
					}
				}
			}
			System.out.println();
			System.out.println("updated listRecord");
			for(int i=0;i<listRecord.size();i++)
			{
				listRecord.get(i).add(calDistance(listRecord.get(i).get(4),listRecord.get(i).get(5),
						listRecord.get(i).get(6),
						listRecord.get(i).get(7)));
				
				listRecord.get(i).add(calAlpha(listRecord.get(i).get(3), listRecord.get(i).get(8)));
				System.out.println(String.format("T %s, B%s, SD %s, M %s, Tx %s, Ty %s, Bx %s, By %s Dis %s Alpha %s", 
													listRecord.get(i).get(0),
													listRecord.get(i).get(1),
													listRecord.get(i).get(2),
													listRecord.get(i).get(3),
													listRecord.get(i).get(4),
													listRecord.get(i).get(5),
													listRecord.get(i).get(6),
													listRecord.get(i).get(7),
													listRecord.get(i).get(8),
													listRecord.get(i).get(9)
													));
			}
			
			// result
			for(int i=0; i<listRealResult.size();i++)
			{
				List<String> tempListforRecord=new ArrayList<>();
				tempListforRecord=listRealResult.get(i);
				//System.out.println(tempListforRecord.get(0));
				for(int j=0;j<xylist.size();j++)
				{
					//System.out.println(xylist.get(j).get(0));
					if(tempListforRecord.get(0).equals(xylist.get(j).get(0).toString()))
					{
						//System.out.println("same");
						listRealResult.get(i).add(xylist.get(j).get(1).toString());
						listRealResult.get(i).add(xylist.get(j).get(2).toString());
					}
				}
			}
			for(int i=0; i<listRealResult.size();i++)
			{
				List<String> tempListforRecord=new ArrayList<>();
				tempListforRecord=listRealResult.get(i);
				//System.out.println(tempListforRecord.get(0));
				for(int j=0;j<xylist.size();j++)
				{
					//System.out.println(xylist.get(j).get(0));
					if(tempListforRecord.get(1).equals(xylist.get(j).get(0).toString()))
					{
						//System.out.println("same");
						listRealResult.get(i).add(xylist.get(j).get(1).toString());
						listRealResult.get(i).add(xylist.get(j).get(2).toString());
					}
				}
			}
			System.out.println("updated listrealResult");
			for(int i=0;i<listRealResult.size();i++)
			{
				listRealResult.get(i).add(calDistance(listRealResult.get(i).get(3),listRealResult.get(i).get(4),
						listRealResult.get(i).get(5),
						listRealResult.get(i).get(6)));
				
				listRealResult.get(i).add(calAlpha(listRealResult.get(i).get(2), listRealResult.get(i).get(7)));
				System.out.println(String.format("T %s, B%s, rssi %s Tx %s, Ty %s, Bx %s, By %s Dis %s Alpha %s", 
						listRealResult.get(i).get(0),
						listRealResult.get(i).get(1),
						listRealResult.get(i).get(2),
						listRealResult.get(i).get(3),
						listRealResult.get(i).get(4),
						listRealResult.get(i).get(5),
						listRealResult.get(i).get(6),
						listRealResult.get(i).get(7),
						listRealResult.get(i).get(8)
													));
			}
		}
		
		//System.out.println(String.format("listRecord size: %s, listRecord.get(0).size:", null))
		System.out.println(String.format("listRecord size: %s , listRealResult size : %s", listRecord.size(),listRealResult.size()));
		if(listRecord.size()==listRealResult.size())
		{
			System.out.println("record==result");
			for(int i=0;i<listRealResult.size();i++)
			{
				if (listRecord.get(i).get(0).equals(listRealResult.get(i).get(0)))
				{
					if(listRecord.get(i).get(1).equals(listRealResult.get(i).get(1)))
					{
						//System.out.println("check RSSI");
						//System.out.println(String.format("sd:%s, m:%", Double.parseDouble(listRecord.get(i).get(2)),Double.parseDouble(listRecord.get(i).get(3))));
						double sd = Double.parseDouble(listRecord.get(i).get(2));
						double mean= Double.parseDouble(listRecord.get(i).get(3));
						double rssi=Double.parseDouble(listRealResult.get(i).get(2));
						double lowerlimit=mean-sd;
						double uplimit=mean+sd;
						double diff= (Math.abs((rssi-mean)/mean))*100;
						System.out.println(String.format(" %s and %serror of %s and %s is: %s", listRecord.get(i).get(0), listRecord.get(i).get(1),mean,rssi, diff));
						//System.out.println(String.format("sd:%s m:%s, r:%s, U:%s, L:%s", sd,mean,rssi,uplimit,lowerlimit));
						if(diff>=RssiDiffError)
						{
							List<String> temp=new ArrayList<>();
							temp.add(listRecord.get(i).get(0));
							temp.add(listRecord.get(i).get(1));
							temp.add(String.valueOf(diff));
							temp.add(listRealResult.get(i).get(8));
							temp.add(listRealResult.get(i).get(3));
							temp.add(listRealResult.get(i).get(4));
							temp.add(listRealResult.get(i).get(5));
							temp.add(listRealResult.get(i).get(6));
							temp.add(listRealResult.get(i).get(7));
							//System.out.println(String.format("T:%s B:%s", temp.get(0),temp.get(1)));
							listToLLS.add(temp);
						}
						else
						{
							System.out.println("no one inside the area");
						}
					}
				}
			}
		}
		else if (listRecord.size()>=listRealResult.size())
		{
			double diff=0;
			double rssi=0;
			System.out.println("record>result");
			for(int i=0;i<listRealResult.size();i++)
			{
				String RecordT=listRecord.get(i).get(0);
				String RecordB=listRecord.get(i).get(1);
				double mean= Double.parseDouble(listRecord.get(i).get(3));
				for(int j=0; j<listRealResult.size();j++)
				{
					
					String ResultT=listRealResult.get(j).get(0);
					String ResultB=listRealResult.get(j).get(1);
					rssi=Double.parseDouble(listRealResult.get(j).get(2));
					
					diff= (Math.abs((rssi-mean)/mean))*100;
					//System.out.println(String.format(" rT %s, rB %s, rssi %s, diff: %s", ResultT, ResultB, rssi,diff));
					if(RecordT.equals(ResultT) && RecordB.equals(ResultB))
					{
						diff= (Math.abs((rssi-mean)/mean))*100;
						//System.out.println(String.format(" rT %s, rB %s, rssi %s, diff: %s", ResultT, ResultB, rssi,diff));
						//System.out.println("get it, ");
						if(diff>=RssiDiffError)
						{
							List<String> temp=new ArrayList<>();
							temp.add(RecordT);
							temp.add(RecordB);
							temp.add(String.valueOf(diff));
							temp.add(listRealResult.get(i).get(8));
							temp.add(listRealResult.get(i).get(3));
							temp.add(listRealResult.get(i).get(4));
							temp.add(listRealResult.get(i).get(5));
							temp.add(listRealResult.get(i).get(6));
							temp.add(listRealResult.get(i).get(7));
							//System.out.println(String.format("T:%s B:%s", temp.get(0),temp.get(1)));
							listToLLS.add(temp);
						}
						else
						{
							System.out.println("no one inside the area");
						}
						
						
						
						break;
					}
					else 
					{
						diff=0;
						System.out.println("error ++");
						List<String> temp=new ArrayList<>();
						temp.add(RecordT);
						temp.add(RecordB);
						temp.add(String.valueOf(diff));
						temp.add(listRecord.get(i).get(9));
						temp.add(listRecord.get(i).get(4));
						temp.add(listRecord.get(i).get(5));
						temp.add(listRecord.get(i).get(6));
						temp.add(listRecord.get(i).get(7));
						temp.add(listRecord.get(i).get(8));
						System.out.println(String.format("***0:%s 1:%s, 2:%s, 3:%s, 4:%s, 5:%s, 6:%s, 7:%s, 8:%s", temp.get(0),temp.get(1)
								,temp.get(2),temp.get(3),temp.get(4),temp.get(5),temp.get(6),temp.get(7),temp.get(8)));
						listToLLS.add(temp);
						j=j-1;
						break;
					}
				}
				System.out.println(String.format(" %s and %serror of %s and %s is: %s",RecordT , RecordB ,mean,rssi, diff));
				//System.out.println(String.format("sd:%s m:%s, r:%s, U:%s, L:%s", sd,mean,rssi,uplimit,lowerlimit));
				
				
			}
		}
		/*
		for (int i=0; i<listToLLS.size();i++)
		{
			System.out.println(String.format("0:%s 1:%s 2:%s 3:%s 4:%s 5:%s 6:%s 7:%s 8:%s ", listToLLS.get(i).get(0),
																							 listToLLS.get(i).get(1),
																							 listToLLS.get(i).get(2),
																							 listToLLS.get(i).get(3),
																							 listToLLS.get(i).get(4),
																							 listToLLS.get(i).get(5),
																							 listToLLS.get(i).get(6),
																							 listToLLS.get(i).get(7),
																							 listToLLS.get(i).get(8)
																							 ));
		}
		*/
		
		// find the max diff 
		{
			double maxDiff=0;
			for(int i=0; i<listToLLS.size();i++)
			{
				//double tempDiff=Double.parseDouble(listToLLS.get(i).get(2));
				
				for(int j=0;j<listToLLS.size();j++)
				{
					if(maxDiff>Double.parseDouble(listToLLS.get(j).get(2)))
					{
						maxDiff=maxDiff;
					}
					else
					{
						maxDiff=Double.parseDouble(listToLLS.get(j).get(2));
					}
				}
				//String maxDiff= Collections.max(listToLLS.get(i));
			}
			System.out.println(maxDiff);
			//listToLLS.sort(listToLLS.get(0).get(2));
		}
		
		// sort
		{
			Double[][] tempString=new Double [listToLLS.size()][3];
			toLLS[] temp2 = new toLLS[listToLLS.size()];
			for(int i=0; i<listToLLS.size();i++)
			{
				tempString[i][2]=Double.parseDouble(listToLLS.get(i).get(2));
				temp2[i]= new toLLS(listToLLS.get(i).get(0),
									listToLLS.get(i).get(1),
									tempString[i][2],
									Double.parseDouble(listToLLS.get(i).get(3)),
									Double.parseDouble(listToLLS.get(i).get(4)),
									Double.parseDouble(listToLLS.get(i).get(5)),
									Double.parseDouble(listToLLS.get(i).get(6)),
									Double.parseDouble(listToLLS.get(i).get(7)),
									Double.parseDouble(listToLLS.get(i).get(8))
									);
			}
			
			Arrays.sort(temp2, new SortbyDiff());
			//Arrays.sort(temp2,Collections.reverseOrder());
			//System.out.println("ascending:"+ temp2.length);//2022-3-30
			for(int i=0;i<temp2.length;i++)
			{
				//System.out.println(temp2[i]);
			}
			//System.out.println("descending:"+temp2.length);//2022-3-30
			for(int i=temp2.length-1;i>=0;i--)
			{
				//System.out.println(temp2[i]);
			}
			
			for(int i=temp2.length-1;i>=0;i--)
			{ 
				List<String> temp =new  ArrayList<>();
				String[] Str=temp2[i].toString().split(",");
				for(int j=0;j<Str.length;j++)
				{
					String[] Str2=Str[j].split("_");
					for(int k=0;k<Str2.length;k++)
					{
						//System.out.println(String.format("%s", Str2[k]));
						temp.add(Str2[k]);
					}
					list4ToLLS.add(temp);
				}
			}
			
			
			// print K
			{
				//System.out.println(String.format("size:%s  .szie: %s", list4ToLLS.size(), list4ToLLS.get(0).size()));//2022-3-30
				for (int i = 0; i<list4ToLLS.size();i++)
				{
					System.out.println(String.format("K: T:%s B:%s Diff:%s Aplha:%s xt:%s yt:%s xb:%s yb:%s dis:%s", 
							list4ToLLS.get(i).get(0),list4ToLLS.get(i).get(1),list4ToLLS.get(i).get(2),list4ToLLS.get(i).get(3),list4ToLLS.get(i).get(4),list4ToLLS.get(i).get(5),list4ToLLS.get(i).get(6),list4ToLLS.get(i).get(7),list4ToLLS.get(i).get(8)));
				}
			}
			
			Arrays.sort(temp2, new SortbyAlphaD() );
			//System.out.println("ascending");//2022-3-30
			for(int i=0;i<temp2.length;i++)
			{
				//System.out.println(temp2[i]);
			}
			//System.out.println("descending");//2022-3-30
			for(int i=temp2.length-1;i>=0;i--)
			{
				//System.out.println(temp2[i]);
			}
		}
	}
	public static String calDistance(String xt,String yt, String xb,String yb)
	{
		Double distance=0.0;
		distance=Math.sqrt(Math.pow(Double.parseDouble(xt)-Double.parseDouble(xb), 2)+Math.pow(Double.parseDouble(yt)-Double.parseDouble(yb),2));
		return distance.toString();
		
	}
	public static String calAlpha(String RefRssi, String Distance)
	{
		
		//Rssi= 20log(a)d/d0;
		Double alpha=0.0;
		double d0=1.0;
		double rssi=Double.parseDouble(RefRssi);
		double distance =Double.parseDouble(Distance);
		double constant=-10;
		alpha=(rssi/constant)/(Math.log10(distance/d0));
		return alpha.toString();
	}

	public List<List<String>> getlistToLLS()
	{
		return this.listToLLS;
	}
	public List<List<String>> getMaxFourRssiDiff()
	{
		List<List<String>>temp1=new ArrayList<>();
		List<String>temp=new ArrayList<>();
		if(list4ToLLS.size()>4)
		{
			for(int i=0;i<list4ToLLS.size()-1;i++)
			{
				if(temp1.size()>=4)
				{
					//System.out.println("line 644 size :"+temp1.size());
					break;
				}
				else //if(temp1.size()>0)
				{
					//if(temp1.get(0))
					//System.out.println("temp1 size: "+temp1.size());
					//System.out.println(String.format("dis: %s",(K.get(i).get(8))));
					//(Double.parseDouble(K.get(i).get(8))!= 300.0 ||Double.parseDouble(K.get(i).get(8))!=600.0)||(Double.parseDouble(K.get(i).get(8))!= 3.0 ||Double.parseDouble(K.get(i).get(8))!=6.0)
					if(!(list4ToLLS.get(i).get(8).equals("300.0")))
					{
						//System.out.println(String.format("in",Double.parseDouble(list4ToLLS.get(i).get(8))));
						if(!(list4ToLLS.get(i).get(8).equals("600.0")))
						{
							//System.out.println(String.format("in2",Double.parseDouble(list4ToLLS.get(i).get(8))));
							if(temp1.size()<1)
							{
							temp=new ArrayList<>();
							temp.add(list4ToLLS.get(i).get(0));
							temp.add(list4ToLLS.get(i).get(1));
							temp.add(list4ToLLS.get(i).get(2));
							temp.add(list4ToLLS.get(i).get(3));
							temp.add(list4ToLLS.get(i).get(4));
							temp.add(list4ToLLS.get(i).get(5));
							temp.add(list4ToLLS.get(i).get(6));
							temp.add(list4ToLLS.get(i).get(7));
							temp.add(list4ToLLS.get(i).get(8));
							temp1.add(temp);
							}
							else 
							{
								//Double.parsDouble(temp1.get(0).get());
								if(!checkParallel(Double.parseDouble(temp.get(4)),Double.parseDouble(temp.get(5)),Double.parseDouble(temp.get(6)),Double.parseDouble(temp.get(7)),Double.parseDouble(list4ToLLS.get(i).get(4)),Double.parseDouble(list4ToLLS.get(i).get(5)),Double.parseDouble(list4ToLLS.get(i).get(6)),Double.parseDouble(list4ToLLS.get(i).get(7))))
								{
									temp=new ArrayList<>();
									temp.add(list4ToLLS.get(i).get(0));
									temp.add(list4ToLLS.get(i).get(1));
									temp.add(list4ToLLS.get(i).get(2));
									temp.add(list4ToLLS.get(i).get(3));
									temp.add(list4ToLLS.get(i).get(4));
									temp.add(list4ToLLS.get(i).get(5));
									temp.add(list4ToLLS.get(i).get(6));
									temp.add(list4ToLLS.get(i).get(7));
									temp.add(list4ToLLS.get(i).get(8));
									temp1.add(temp);
								}
							}
						}
						else if(list4ToLLS.get(i).get(4).equals("300.0")&&list4ToLLS.get(i).get(5).equals("0.0")&&list4ToLLS.get(i).get(6).equals("300.0")&&list4ToLLS.get(i).get(7).equals("600.0"))
						{
							if(temp1.size()<1)
							{
							temp=new ArrayList<>();
							temp.add(list4ToLLS.get(i).get(0));
							temp.add(list4ToLLS.get(i).get(1));
							temp.add(list4ToLLS.get(i).get(2));
							temp.add(list4ToLLS.get(i).get(3));
							temp.add(list4ToLLS.get(i).get(4));
							temp.add(list4ToLLS.get(i).get(5));
							temp.add(list4ToLLS.get(i).get(6));
							temp.add(list4ToLLS.get(i).get(7));
							temp.add(list4ToLLS.get(i).get(8));
							temp1.add(temp);
							}
							else 
							{
								//Double.parsDouble(temp1.get(0).get());
								if(!checkParallel(Double.parseDouble(temp.get(4)),Double.parseDouble(temp.get(5)),Double.parseDouble(temp.get(6)),Double.parseDouble(temp.get(7)),Double.parseDouble(list4ToLLS.get(i).get(4)),Double.parseDouble(list4ToLLS.get(i).get(5)),Double.parseDouble(list4ToLLS.get(i).get(6)),Double.parseDouble(list4ToLLS.get(i).get(7))))
								{
									temp=new ArrayList<>();
									temp.add(list4ToLLS.get(i).get(0));
									temp.add(list4ToLLS.get(i).get(1));
									temp.add(list4ToLLS.get(i).get(2));
									temp.add(list4ToLLS.get(i).get(3));
									temp.add(list4ToLLS.get(i).get(4));
									temp.add(list4ToLLS.get(i).get(5));
									temp.add(list4ToLLS.get(i).get(6));
									temp.add(list4ToLLS.get(i).get(7));
									temp.add(list4ToLLS.get(i).get(8));
									temp1.add(temp);
								}
							}
						}
						else if (list4ToLLS.get(i).get(4).equals("300.0")&&list4ToLLS.get(i).get(5).equals("600.0")&&list4ToLLS.get(i).get(6).equals("300.0")&&list4ToLLS.get(i).get(7).equals("0.0"))
						{
							if(temp1.size()<1)
							{
							temp=new ArrayList<>();
							temp.add(list4ToLLS.get(i).get(0));
							temp.add(list4ToLLS.get(i).get(1));
							temp.add(list4ToLLS.get(i).get(2));
							temp.add(list4ToLLS.get(i).get(3));
							temp.add(list4ToLLS.get(i).get(4));
							temp.add(list4ToLLS.get(i).get(5));
							temp.add(list4ToLLS.get(i).get(6));
							temp.add(list4ToLLS.get(i).get(7));
							temp.add(list4ToLLS.get(i).get(8));
							temp1.add(temp);
							}
							else 
							{
								//Double.parsDouble(temp1.get(0).get());
								if(!checkParallel(Double.parseDouble(temp.get(4)),Double.parseDouble(temp.get(5)),Double.parseDouble(temp.get(6)),Double.parseDouble(temp.get(7)),Double.parseDouble(list4ToLLS.get(i).get(4)),Double.parseDouble(list4ToLLS.get(i).get(5)),Double.parseDouble(list4ToLLS.get(i).get(6)),Double.parseDouble(list4ToLLS.get(i).get(7))))
								{
									temp=new ArrayList<>();
									temp.add(list4ToLLS.get(i).get(0));
									temp.add(list4ToLLS.get(i).get(1));
									temp.add(list4ToLLS.get(i).get(2));
									temp.add(list4ToLLS.get(i).get(3));
									temp.add(list4ToLLS.get(i).get(4));
									temp.add(list4ToLLS.get(i).get(5));
									temp.add(list4ToLLS.get(i).get(6));
									temp.add(list4ToLLS.get(i).get(7));
									temp.add(list4ToLLS.get(i).get(8));
									temp1.add(temp);
								}
							}
						}
						else if (list4ToLLS.get(i).get(4).equals("0.0")&&list4ToLLS.get(i).get(5).equals("300.0")&&list4ToLLS.get(i).get(6).equals("600.0")&&list4ToLLS.get(i).get(7).equals("300.0"))
						{
							if(temp1.size()<1)
							{
							temp=new ArrayList<>();
							temp.add(list4ToLLS.get(i).get(0));
							temp.add(list4ToLLS.get(i).get(1));
							temp.add(list4ToLLS.get(i).get(2));
							temp.add(list4ToLLS.get(i).get(3));
							temp.add(list4ToLLS.get(i).get(4));
							temp.add(list4ToLLS.get(i).get(5));
							temp.add(list4ToLLS.get(i).get(6));
							temp.add(list4ToLLS.get(i).get(7));
							temp.add(list4ToLLS.get(i).get(8));
							temp1.add(temp);
							}
							else 
							{
								//Double.parsDouble(temp1.get(0).get());
								if(!checkParallel(Double.parseDouble(temp.get(4)),Double.parseDouble(temp.get(5)),Double.parseDouble(temp.get(6)),Double.parseDouble(temp.get(7)),Double.parseDouble(list4ToLLS.get(i).get(4)),Double.parseDouble(list4ToLLS.get(i).get(5)),Double.parseDouble(list4ToLLS.get(i).get(6)),Double.parseDouble(list4ToLLS.get(i).get(7))))
								{
									temp=new ArrayList<>();
									temp.add(list4ToLLS.get(i).get(0));
									temp.add(list4ToLLS.get(i).get(1));
									temp.add(list4ToLLS.get(i).get(2));
									temp.add(list4ToLLS.get(i).get(3));
									temp.add(list4ToLLS.get(i).get(4));
									temp.add(list4ToLLS.get(i).get(5));
									temp.add(list4ToLLS.get(i).get(6));
									temp.add(list4ToLLS.get(i).get(7));
									temp.add(list4ToLLS.get(i).get(8));
									temp1.add(temp);
								}
							}
						}
						else if (list4ToLLS.get(i).get(4).equals("600.0")&&list4ToLLS.get(i).get(5).equals("300.0")&&list4ToLLS.get(i).get(6).equals("0.0")&&list4ToLLS.get(i).get(7).equals("300.0"))
						{
							if(temp1.size()<1)
							{
							temp=new ArrayList<>();
							temp.add(list4ToLLS.get(i).get(0));
							temp.add(list4ToLLS.get(i).get(1));
							temp.add(list4ToLLS.get(i).get(2));
							temp.add(list4ToLLS.get(i).get(3));
							temp.add(list4ToLLS.get(i).get(4));
							temp.add(list4ToLLS.get(i).get(5));
							temp.add(list4ToLLS.get(i).get(6));
							temp.add(list4ToLLS.get(i).get(7));
							temp.add(list4ToLLS.get(i).get(8));
							temp1.add(temp);
							}
							else 
							{
								//Double.parsDouble(temp1.get(0).get());
								if(!checkParallel(Double.parseDouble(temp.get(4)),Double.parseDouble(temp.get(5)),Double.parseDouble(temp.get(6)),Double.parseDouble(temp.get(7)),Double.parseDouble(list4ToLLS.get(i).get(4)),Double.parseDouble(list4ToLLS.get(i).get(5)),Double.parseDouble(list4ToLLS.get(i).get(6)),Double.parseDouble(list4ToLLS.get(i).get(7))))
								{
									temp=new ArrayList<>();
									temp.add(list4ToLLS.get(i).get(0));
									temp.add(list4ToLLS.get(i).get(1));
									temp.add(list4ToLLS.get(i).get(2));
									temp.add(list4ToLLS.get(i).get(3));
									temp.add(list4ToLLS.get(i).get(4));
									temp.add(list4ToLLS.get(i).get(5));
									temp.add(list4ToLLS.get(i).get(6));
									temp.add(list4ToLLS.get(i).get(7));
									temp.add(list4ToLLS.get(i).get(8));
									temp1.add(temp);
								}
							}
						}
						else if (list4ToLLS.get(i).get(4).equals("600.0")&&list4ToLLS.get(i).get(5).equals("300.0")&& list4ToLLS.get(i).get(6).equals("0.0")&&list4ToLLS.get(i).get(7).equals("300.0"))
						{
							
							temp=new ArrayList<>();
							temp.add(list4ToLLS.get(i).get(0));
							temp.add(list4ToLLS.get(i).get(1));
							temp.add(list4ToLLS.get(i).get(2));
							temp.add(list4ToLLS.get(i).get(3));
							temp.add(list4ToLLS.get(i).get(4));
							temp.add(list4ToLLS.get(i).get(5));
							temp.add(list4ToLLS.get(i).get(6));
							temp.add(list4ToLLS.get(i).get(7));
							temp.add(list4ToLLS.get(i).get(8));
							temp1.add(temp);
						}
						else if (list4ToLLS.get(i).get(4).equals("0.0")&&list4ToLLS.get(i).get(5).equals("300.0")&& list4ToLLS.get(i).get(6).equals("600.0")&&list4ToLLS.get(i).get(7).equals("300.0"))
						{
							temp=new ArrayList<>();
							temp.add(list4ToLLS.get(i).get(0));
							temp.add(list4ToLLS.get(i).get(1));
							temp.add(list4ToLLS.get(i).get(2));
							temp.add(list4ToLLS.get(i).get(3));
							temp.add(list4ToLLS.get(i).get(4));
							temp.add(list4ToLLS.get(i).get(5));
							temp.add(list4ToLLS.get(i).get(6));
							temp.add(list4ToLLS.get(i).get(7));
							temp.add(list4ToLLS.get(i).get(8));
							temp1.add(temp);
						}
					}
				}
			System.out.println("temp1 size: "+temp1.size());
			}
			return temp1;
		}
		else
		{
			return list4ToLLS;
		}
	}
	
	public boolean checkParallel(Double xt, Double yt, Double xb, Double yb ,Double xt2,Double yt2, Double xb2,Double yb2)
	{
		double[] equation1=getEquation(xt,yt,xb,yb);
		double[] equation2=getEquation(xt2,yt2,xb2,yb2);
		double m1=-1*equation1[0]/equation1[1];
		double m2=-1*equation2[0]/equation2[1];
		System.out.println(String.format("m1: %s, m2: %s", m1, m2));
		if(m1==m2)
		{
			return true;
		}
		
		return false;
	}
	public double[] getEquation(double xt,double yt,double xb, double yb)
	{
		double[] resulte=new double[3];
		//float x1 = (float) (num1.get(0)/100);
		//float y1 = (float) (num1.get(1)/100);
		//float x2 = (float) (num2.get(0)/100);
		//y2 = (float) (num2.get(1)/100);
		if(xt==xb) {
			resulte[0]=1;
			resulte[1]=0;
			resulte[2]=xt;
			return resulte;
		}
		if(yt==yb) {
			resulte[0]=0;
			resulte[1]=1;
			resulte[2]=yt;
			return resulte;
		}
		double a = yt-yb;
		double b = xt-xb;
		double c=0;
		if((a*xt+b*yt)==0)
		{
			c=0;
		}
		else
			{
			c = (a*xt+b*yt);
			}
		
		resulte[0]=a;
		resulte[1]=b;
		resulte[2]=c;
		return resulte;
	}
	
}
  