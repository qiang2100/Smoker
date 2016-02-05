
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;





import java.util.HashSet;
import java.util.Iterator;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class FraudSmoker {

	ArrayList<Integer> userArr = new ArrayList<Integer>();
	ArrayList<Integer> parArr = new ArrayList<Integer>();
	ArrayList<Long> timeArr = new ArrayList<Long>();
	ArrayList<String> timeSArr = new ArrayList<String>();
	ArrayList<Node> rootNode = new ArrayList<Node>();
	
	ArrayList<Node> fraudNode = new ArrayList<Node>();
	
	
	ArrayList<Integer> goodSmoker = new ArrayList<Integer>();
	ArrayList<Integer> badSmoker = new ArrayList<Integer>();
	
	ArrayList<Long> startTime =new ArrayList<Long>();
	ArrayList<Long> endTime =new ArrayList<Long>();
	
	long firstTime = 20;
	
	long otherTime = 5;
	
	int nodeCount = 0;
	//int []userRecruitNum ;
	
	//int recruitNum = 1; //user is a strong recruiter if it's recruiters greater and equal to recruitNum;
	
	//String feat[] = {"smokingstatus", "age","sex", "homecigarettesperday", "visitedsmokingcessationwebsite", "schoolcategory", "internetuse", "race", "smokingstatusint"};
	
	//HashMap<Integer, String> id2value = new HashMap<Integer,String>();
	
	//ArrayList<Integer> cloumnArr = new ArrayList<Integer>();
	
	//ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
	//int id = 2;
	
	
	public String transTime(String time)
	{
		String []stime = time.split("/");
		
		String []stime2 = stime[2].split(" ");
		
		String res = "20" + stime2[0]+"-";
		
		if(stime[0].length()==1)
			res += "0"+stime[0] + "-";
		else
			res += stime[0]+"-";
		
		if(stime[1].length()==1)
			res += "0"+stime[1] + " ";
		else
			res += stime[1]+" ";
		
		res += stime2[1];
		
		//System.out.print(res);
		return res;
	}
	
	
	
	
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
            	String usrId = fcell.getContents();
            	userArr.add(Integer.parseInt(usrId));
            	
            	Cell tcell = rst.getCell(4,i);
            	String time = tcell.getContents();
            	
            	String time2 = transTime(time);
            	timeSArr.add(time2);
            	//SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy HH:mm");   
            	//SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            	//System.out.println(sdf2.format(sdf1.parse(time)));
            	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            	Date dt = df.parse(time2);
            	//System.out.println(dt.toString());   //java.util.Date的含义
            	long lTime = dt.getTime() / 60000 ;
            	timeArr.add(lTime);
            	
            	//String dateString = formatter.format(time);   	
            	//System.out.println(time + " -> "+  lTime);
            	
            	
            	Cell rcell = rst.getCell(91,i);
            	
            	String recru = rcell.getContents();
            	if(recru=="")
            	{
            		parArr.add(-1);
            		
            		Node node = new Node(Integer.parseInt(usrId), lTime);
            		
            		rootNode.add(node);
            	}
            	else
            		parArr.add(Integer.parseInt(recru));
            	
            }  
            
                   
          //  System.out.print(userRecruitNum.get(1));
            //关闭   
  
            rwb.close(); 
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	public void insertNode(Node par)
	{
		int parId = par.userId;
		
		for(int i=0; i<userArr.size(); i++)
		{
			if(parArr.get(i)==parId)
			{
				Node newNode = new Node(userArr.get(i),timeArr.get(i));
				insertNode(newNode);
				par.children.add(newNode);
			}
		}
	}
	
	public void constructTree()
	{
		//System.out.println(rootNode.size());
		
		for(int i=0; i<rootNode.size(); i++)
		{
			insertNode(rootNode.get(i));
		}
	}
	
	
	public void checkChildTree(ArrayList<Node> root)
	{
		//System.out.print(node.userId + " ");
		
		for(int i=0; i<root.size(); i++)
		{
			Node node = root.get(i);
			
			if(node.userId == 7074)
			{
				System.out.println(node.userId);
			}
			if(node.children.size()<2)
			{
				checkChildTree(node.children);
			}else
			{
				ArrayList<Long> chiTime = new ArrayList<Long>();
				for(int j=0; j<node.children.size(); j++)
				{
					long curTime = node.children.get(j).registerTime;
					
					chiTime.add(curTime);
					
					for(int k=chiTime.size()-2; k>=0; k--)
					{
						if(chiTime.get(k)>curTime)
						{
							long pun = chiTime.get(k+1);
							chiTime.set(k+1, chiTime.get(k));
							chiTime.set(k, pun);
						}
							
					}
					//chiTime.add();
				}
				
				ArrayList<Long> interTime = new ArrayList<Long>();
				
				long lastTime = node.registerTime;
				for(int k=0; k<chiTime.size(); k++)
				{
					long curTime = chiTime.get(k);
					
					interTime.add(curTime-lastTime);
					lastTime = curTime;
				}
				
				boolean isFraud = false;
				int count = 0;
				if(interTime.get(0)<=firstTime)
					isFraud = true;
				else
				{
					for(int j=1; j<interTime.size(); j++)
						if(interTime.get(j)<=otherTime)
							count++;
					if(count>=2)
						isFraud = true;
				}
				
				if(isFraud)
				{
					fraudNode.addAll(root);
					root.clear();
					return;
					//root.remove(i);
					//i--;
				}else
				{
					checkChildTree(node.children);
				}
				
			}
			
		}
	}
	
	public void checkTree()
	{
		for(int i=0; i<rootNode.size(); i++)
		{
			Node node = rootNode.get(i);
		
			if(node.children.size()<3)
			{
				checkChildTree(node.children);
			}else
			{
				ArrayList<Long> chiTime = new ArrayList<Long>();
				for(int j=0; j<node.children.size(); j++)
				{
					long curTime = node.children.get(j).registerTime;
					
					chiTime.add(curTime);
					
					for(int k=chiTime.size()-2; k>=0; k--)
					{
						if(chiTime.get(k)>curTime)
						{
							long pun = chiTime.get(k+1);
							chiTime.set(k+1, chiTime.get(k));
							chiTime.set(k, pun);
						}
							
					}
					//chiTime.add();
				}
				
				ArrayList<Long> interTime = new ArrayList<Long>();
				
				long lastTime = node.registerTime;
				for(int k=0; k<chiTime.size(); k++)
				{
					long curTime = chiTime.get(k);
					
					interTime.add(curTime-lastTime);
					lastTime = curTime;
				}
				
				boolean isFraud = false;
				int count = 0;
				if(interTime.get(0)<=firstTime)
					isFraud = true;
				else
				{
					for(int j=1; j<interTime.size(); j++)
						if(interTime.get(j)<=otherTime)
							count++;
					if(count>=2)
						isFraud = true;
				}
				
				if(isFraud)
				{
					fraudNode.add(rootNode.get(i));
					//root.clear();
					//return;
					rootNode.remove(i);
					i--;
				}else
				{
					checkChildTree(node.children);
				}
				
			}
			
		}
			
	}
	
	public void saveGoodChildTree(Node node)
	{
		//System.out.print(node.userId+ " ");
		goodSmoker.add(node.userId);
		nodeCount++;
		for(int i=0; i<node.children.size(); i++)
			saveGoodChildTree(node.children.get(i));
	}
	
	public void saveGoodTree(ArrayList<Node> node)
	{
		for(int i=0; i<node.size(); i++)
		{
			saveGoodChildTree(node.get(i));
			//System.out.println();
		}
	}
	
	public void saveBadChildTree(Node node)
	{
		//System.out.print(node.userId+ " ");
		badSmoker.add(node.userId);
		nodeCount++;
		for(int i=0; i<node.children.size(); i++)
			saveBadChildTree(node.children.get(i));
	}
	
	public void saveBadTree(ArrayList<Node> node)
	{
		for(int i=0; i<node.size(); i++)
		{
			saveBadChildTree(node.get(i));
			//System.out.println();
		}
	}
	
	
	
	public void printChildTree(Node node)
	{
		System.out.print(node.userId+ " ");
		nodeCount++;
		for(int i=0; i<node.children.size(); i++)
			printChildTree(node.children.get(i));
	}
	
	public void printTree(ArrayList<Node> node)
	{
		for(int i=0; i<node.size(); i++)
		{
			printChildTree(node.get(i));
			System.out.println();
		}
	}
	
	
	public void findFraud(String path)
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
            	String usrId = fcell.getContents();
            	int id = Integer.parseInt(usrId);
            	
            	Cell tcell = rst.getCell(4,i);
            	String time = tcell.getContents();
            	
            	String time2 = transTime(time);
            	//timeSArr.add(time2);
            	//SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy HH:mm");   
            	//SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            	//System.out.println(sdf2.format(sdf1.parse(time)));
            	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            	Date dt = df.parse(time2);
            	//System.out.println(dt.toString());   //java.util.Date的含义
            	long lTime = dt.getTime() / 60000 ;
            	//timeArr.add(lTime);
            	
            	if(!userArr.contains(id))
            	{
            		userArr.add(id);
            		startTime.add(lTime);
            		endTime.add(lTime);
            	}
            	else
            	{
            		int index = userArr.indexOf(id);
            		
            		if(startTime.get(index)>lTime)
            			startTime.set(index, lTime);
            		
            		if(endTime.get(index)<lTime)
            			endTime.set(index, lTime);
            	}
            	
            	//String dateString = formatter.format(time);   	
            	//System.out.println(time2 + " -> "+  lTime);
            	
            	
            	
            	
            }  
            
           
            int count = 0;
            for(int i=0; i<userArr.size(); i++)
            {
            	long intervl = endTime.get(i) -startTime.get(i) + 1;
            	if(intervl < 30)
            		count++;
            	System.out.println(userArr.get(i) + " " + intervl);
            	
            }
            System.out.println(userArr.size() + " " + count);
            
                   
          //  System.out.print(userRecruitNum.get(1));
            //关闭   
  
            rwb.close(); 
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	public void mainFum(String path)
	{
		try
		{
			String output = ".//output.txt";
			extractRecruit(path);
			
			constructTree();
			
			//checkTree();
			printTree(rootNode);
			//printTree(fraudNode);
			
			
			//saveGoodTree(rootNode);
			//saveBadTree(fraudNode);
			
			System.out.println(goodSmoker.size() + " " + badSmoker.size());
			//test();
			//extractCloumn(path);
			
			//writeData();
			
			//mineRule(output);
			
			//processRule(output);
			//printExcel("goodSmoker.xls",goodSmoker);
			
			//printExcel("fraudSmoker.xls",badSmoker);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//for(int i=0; i<feat.length; i++)
		//System.out.println(feat[i]);
		//averageRecruit();
		//test();
	}
	
	public void mainFum2(String path)
	{
		try
		{
			findFraud(path);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	public void printExcel(String path, ArrayList<Integer> arr) throws Exception
	{
		WritableWorkbook writeBook = Workbook.createWorkbook(new File(path));  
		WritableSheet firstSheet = writeBook.createSheet("firstpage", 0);// 第一个参数为工作簿的名称，第二个参数为页数  
	   // WritableSheet secondSheet = writeBook.createSheet("第二个工作簿", 0);  
	  
	    // 3、创建单元格(Label)对象，  
	    Label label1 = new Label(0, 0, "userid");// 第一个参数指定单元格的列数、第二个参数指定单元格的行数，第三个指定写的字符串内容  
	    firstSheet.addCell(label1);  
	    Label label2 = new Label(1, 0, "regndate");// 第一个参数指定单元格的列数、第二个参数指定单元格的行数，第三个指定写的字符串内容  
	    firstSheet.addCell(label2);  
	    Label label3 = new Label(2, 0, "referredbyuserid");// 第一个参数指定单元格的列数、第二个参数指定单元格的行数，第三个指定写的字符串内容  
	    firstSheet.addCell(label3);  

	    for(int i=0; i<arr.size(); i++)
	    {
	    	int id = arr.get(i);
	    	
	    	int index = userArr.indexOf(id);
	    	
	    	//int user = userArr.get(index)
	    	Label idLabel = new Label(0,i+1,Integer.toString(id));
	    	 firstSheet.addCell(idLabel);
	    	 
	    	 String time = timeSArr.get(index);
	    	 Label timeLabel = new Label(1,i+1,time);
	    	 firstSheet.addCell(timeLabel);
	    	 
	    	int par = parArr.get(index);
	    	
	    	Label parLabel = new Label(2,i+1,Integer.toString(par));
	    	 firstSheet.addCell(parLabel);
	    	
	    }
	    
	   // Label label2 = new Label(1, 2, "test2");  
	   // secondSheet.addCell(label2);  
	  
	    // 4、打开流，开始写文件  
	    writeBook.write();  
	  
	    // 5、关闭流  
	    writeBook.close();  
	}
	
	public void test()
	{
		for(int i=0; i<userArr.size(); i++)
		{
			System.out.println(userArr.get(i) + " "+ parArr.get(i) + " "+ timeArr.get(i));
		}
			
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FraudSmoker ms = new FraudSmoker();
		
		//ms.mainFum("userforumass.xls");
		ms.mainFum2("s2qwebsiteuse.xls");
	}

}
