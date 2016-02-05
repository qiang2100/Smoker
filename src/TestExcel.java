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
  
            //���������ַ�����ȡsheet��:���ֺ��±꣨��0��ʼ��   
  
            //Sheet st = rwb.getSheet("original");   
  
           
            
            /**  
            
            //��õ�һ�е�һ�е�Ԫ��ֵ  
 
            Cell c00 = st.getCell(0,0);  
 
            //ͨ�õĻ�ȡcellֵ�ķ�ʽ,�����ַ���  
 
            String strc00 = c00.getContents();  
 
            //���cell��������ֵ�ķ�ʽ  
 
            if(c00.getType() == CellType.LABEL)  
 
            {  
 
                LabelCell labelc00 = (LabelCell)c00;  
 
                strc00 = labelc00.getString();  
 
            }  
 
            //���  
 
            System.out.println(strc00);*/  
  
            //Sheet���±��Ǵ�0��ʼ   
  
            //��ȡ��һ��Sheet��   
  
            Sheet rst = rwb.getSheet(0);   
  
            //��ȡSheet������������������   
  
            int rsColumns = rst.getColumns();   
  
            //��ȡSheet������������������   
  
            int rsRows = rst.getRows();   
  
            //��ȡָ����Ԫ��Ķ�������   
  
            for (int i = 0; i < rsRows; i++)   
  
            {   
  
                for (int j = 0; j < rsColumns; j++)   
  
                {   
  
                    Cell cell = rst.getCell(j, i);   
  
                    System.out.print(cell.getContents() + " ");   
  
                }   
  
                System.out.println();   
  
            }             
  
            //�ر�   
  
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
