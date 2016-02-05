import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AlgoAgrawalFaster94;
import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class MiningSmoker {

	ArrayList<Integer> userArr = new ArrayList<Integer>();
	int []userRecruitNum ;
	
	int recruitNum = 1; //user is a strong recruiter if it's recruiters greater and equal to recruitNum;
	
	String feat[] = {"smokingstatus", "age","sex", "homecigarettesperday", "visitedsmokingcessationwebsite", "schoolcategory",
			"internetuse", "race", "smokingstatusint"};
	
	HashMap<Integer, String> id2value = new HashMap<Integer,String>();
	
	ArrayList<Integer> cloumnArr = new ArrayList<Integer>();
	
	ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
	int id = 2;
	
	public void extractRecruit(String path)
	{
		try  
        {   
            InputStream is = new FileInputStream(path);   
  
            Workbook rwb = Workbook.getWorkbook(is);   
  
            //Sheet的下标是从0开始   
  
            //获取第一张Sheet表   
  
            Sheet rst = rwb.getSheet(0);   
  
            //获取Sheet表中所包含的总列数   
  
            int rsColumns = rst.getColumns();   
  
            //获取Sheet表中所包含的总行数   
  
            int rsRows = rst.getRows();   
  
            for (int i = 1; i < rsRows; i++)   
            {   
            	Cell fcell = rst.getCell(0, i);   
            	int usrId = Integer.parseInt(fcell.getContents());
            	userArr.add(usrId);
            }  
            
            userRecruitNum = new int[userArr.size()];
           
            for (int i = 1; i < rsRows; i++)     
            {   
  
            	//System.out.print(i +":");
            	Cell fcell = rst.getCell(91, i);   
            	
            	if(fcell.getContents()=="")
            	{
            		//System.out.println("seed");
            		continue;
            	}
            	
            	int parentId = Integer.parseInt(fcell.getContents());
            
            	int index = userArr.indexOf(parentId);
            
            	userRecruitNum[index]++;
            	
  
            }             
          //  System.out.print(userRecruitNum.get(1));
            //关闭   
  
            rwb.close(); 
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	public void averageRecruit()
	{
		double all = 0;
		for(int i=0; i<userRecruitNum.length; i++)
			all += userRecruitNum[i];
		
		System.out.println(all/userRecruitNum.length);
	}
	
	public boolean contain(String s)
	{
		for(int i=0; i<feat.length; i++)
		{
			if(feat[i].equals(s))
				return true;
		}
		return false;
	}
	
	
	public void extractCloumn(String path)
	{
		try    
        {   
            InputStream is = new FileInputStream(path);   
            Workbook rwb = Workbook.getWorkbook(is);  
            Sheet rst = rwb.getSheet(0);   
            //获取Sheet表中所包含的总列数   
            int rsColumns = rst.getColumns();   
            //获取Sheet表中所包含的总行数   
  
            int rsRows = rst.getRows();   
  
            for(int i=0; i<rsColumns; i++)
            {
            	String f = rst.getCell(i,0).getContents();
            	ArrayList<Integer> columnData = new ArrayList<Integer>();
            	if(contain(f))
            	{
            		cloumnArr.add(i);
            	
	            	HashMap<String,Integer> value = new HashMap<String,Integer> ();
	            	
	            	
	            	String firstS =  "";
	            	for(int j=1; j<rsRows; j++)
	            	{
	            		String f2 = rst.getCell(i,j).getContents();
	            		if(j==1)
	            			firstS = f2;
	            		if(f2=="" || f2==null || f2==" ")
	            			f2 = firstS;
	            		int nId = -1;
	            		if(value.containsKey(f2)) 
	            			nId = value.get(f2);
	            		else
	            		{
	            			nId = id;
	            			id2value.put(id, f2);
	            			value.put(f2, id);
	            			//System.out.print(f2 + "->" + id + " ");
	            			id++;
	            		}
	            		columnData.add(nId);
	            		
	            	}
	            	data.add(columnData); 
	            	//System.out.println();
            	}
            	
            }
           
            // for(int i=0; i<data.size(); i++)
            	 //System.out.println(data.get(i).toString());
          // System.out.println(data.toString());
            //关闭   
  
            rwb.close(); 
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	public void writeData() throws Exception 
	{
		String writeName = "smoker.txt";
	     BufferedWriter writer = new BufferedWriter(new FileWriter(new File(writeName)));   
	    // writer.write(buff);
		// writer.close();
		 
		 String buff = "";
		 
		 for(int i=0; i<data.get(0).size(); i++)
		 {
			 for(int j=0; j<data.size(); j++)
			 {
				 buff += data.get(j).get(i) + " ";
			 }
			 if(userRecruitNum[i]>=recruitNum)
				 buff += "1";
			 else
				 buff += "0";
			 buff += Character.toString('\n');
		 }
		 writer.write(buff);
		 writer.close();
	}
	
	public void mineRule(String output) throws Exception
	{
		String input = "smoker.txt";
		
//		String output = "C:\\patterns\\association_rules.txt";


		
		// STEP 1: Applying the FP-GROWTH algorithm to find frequent itemsets
		double minsupp = 0.1;
		AlgoFPGrowth fpgrowth = new AlgoFPGrowth();
		Itemsets patterns = fpgrowth.runAlgorithm(input, null, minsupp);
//		patterns.printItemsets(database.size());
		fpgrowth.printStats();
		int databaseSize = fpgrowth.getDatabaseSize();
		
		// STEP 2: Generating all rules from the set of frequent itemsets (based on Agrawal & Srikant, 94)
		double  minconf = 0.80;
		AlgoAgrawalFaster94 algoAgrawal = new AlgoAgrawalFaster94();
		algoAgrawal.runAlgorithm(patterns, output, databaseSize, minconf);
		algoAgrawal.printStats();
	}
	
	public void processRule(String output) throws Exception
	{
		  BufferedReader read = new BufferedReader(new FileReader(new File(output)));   
		  
		  String line = read.readLine();
		  
		  String buff = "";
		  int numRule = 0;
		  while(line!=null)
		  {
			  String t[] = line.split("==>");
			  
			  String t2[] = t[1].split("#SUP:");
			  
			  
			  String t3[] = t2[0].trim().split(" ");
			  
			  boolean flag = false;
			  
			  //System.out.println(t3.length + " " + t3[0]);
			  if(t3.length==1)
			  {
				  if(t3[0].equals("0") || t3[0].equals("1"))
				  {
					  flag = true;
					 // break;
				  }
			  }
			  if(flag)
			  {
				 /* String forward = t[0].trim();
				  String term[] = t[0].split(" ");
				  for(int i=0; i<term.length; i++)
				  {
					  int value = Integer.parseInt(term[0]);
					  buff += id2value.get(value) + " ";
				  }
				  	  
				  buff += t[1];*/
				  buff += line;
				  buff += Character.toString('\n');
				  numRule++;
			  }
			  //System.out.println(t2[0]);
			  line = read.readLine();
		  }
		  
		  String writeName = "rule.txt";
		  BufferedWriter writer = new BufferedWriter(new FileWriter(new File(writeName)));   
		  writer.write(buff);
		  writer.close();
		  
		  System.out.println("the number of rules " + numRule);
	}
	
	public void mainFum(String path)
	{
		try
		{
			String output = ".//output.txt";
			extractRecruit(path);
			test();
			//extractCloumn(path);
			
			//writeData();
			
			//mineRule(output);
			
			//processRule(output);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//for(int i=0; i<feat.length; i++)
		//System.out.println(feat[i]);
		//averageRecruit();
		//test();
	}
	
	
	public void test()
	{
		for(int i=0; i<userArr.size(); i++)
		{
			System.out.println(userArr.get(i) + " "+ userRecruitNum[i]);
		}
		
		int num = 0;
		for(int i=0; i<userRecruitNum.length; i++)
			if(userRecruitNum[i]>0)
				num++;
		
		System.out.println(num + "->"+ userRecruitNum.length);
			
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MiningSmoker ms = new MiningSmoker();
		
		ms.mainFum("userforumass.xls");
	}

}
