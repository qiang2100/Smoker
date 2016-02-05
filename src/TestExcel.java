import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;



public class TestExcel {

	
	public static void readExcel(String filePath)
	{
		try  
		  
        {   
  
            InputStream is = new FileInputStream(filePath);   
  
            Workbook rwb = Workbook.getWorkbook(is);   
  
            //这里有两种方法获取sheet表:名字和下标（从0开始）   
  
            //Sheet st = rwb.getSheet("original");   
  
           
            
            /**  
            
            //获得第一行第一列单元的值  
 
            Cell c00 = st.getCell(0,0);  
 
            //通用的获取cell值的方式,返回字符串  
 
            String strc00 = c00.getContents();  
 
            //获得cell具体类型值的方式  
 
            if(c00.getType() == CellType.LABEL)  
 
            {  
 
                LabelCell labelc00 = (LabelCell)c00;  
 
                strc00 = labelc00.getString();  
 
            }  
 
            //输出  
 
            System.out.println(strc00);*/  
  
            //Sheet的下标是从0开始   
  
            //获取第一张Sheet表   
  
            Sheet rst = rwb.getSheet(0);   
  
            //获取Sheet表中所包含的总列数   
  
            int rsColumns = rst.getColumns();   
  
            //获取Sheet表中所包含的总行数   
  
            int rsRows = rst.getRows();   
  
            //获取指定单元格的对象引用   
  
            for (int i = 0; i < rsRows; i++)   
  
            {   
  
                for (int j = 0; j < rsColumns; j++)   
  
                {   
  
                    Cell cell = rst.getCell(j, i);   
  
                    System.out.print(cell.getContents() + " ");   
  
                }   
  
                System.out.println();   
  
            }             
  
            //关闭   
  
            rwb.close(); 
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
  
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TestExcel te = new TestExcel();
		
		//te.readExcel("Test.xls");
		
		ArrayList<Integer> teArr = new ArrayList<Integer>();
		
		teArr.add(0);
		
		for(int i=0; i<5; i++)
			teArr.add(0,teArr.get(0)+1);
		
		System.out.println(teArr.toString());
		
		HashSet<Long> set=new HashSet<Long>();
		set.add(22999999l);
		set.add(33333333l);
		set.add(11111111l);
		Iterator<Long> iterator=set.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
		
		ArrayList<Long> aa = new ArrayList<Long> ();
		
		aa.add(1111111l);
		aa.add(2222222l);
		aa.add(1333333l);
		
		aa.add(2, 122222222l);
		
		System.out.println(aa);
		
	}

}
