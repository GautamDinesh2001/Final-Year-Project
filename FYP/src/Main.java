
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.util.CombinatoricsUtils;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression;


public class Main 
{
	public static void main(String[] args) throws IOException 
	{
		Scanner inputScanner = new Scanner(System.in);
		while(true)
		{
			List<Object> inOutputStream = SerialPortCom.getInOutputStream(inputScanner);
			if(inOutputStream.size()==1)
			{
				if(((String)inOutputStream.get(0)).equalsIgnoreCase("exit"))
				{
					break;
				}
			}else if(inOutputStream.size()==3){
				if(new LoRaTerminal(inOutputStream).start(inputScanner))
				{
					break;
				}
			}
		}
		inputScanner.close();
	  }
}