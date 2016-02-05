import java.util.ArrayList;


public class Node {
	
	long registerTime;
	
	int userId;
	
	
	
	ArrayList<Node> children = new ArrayList<Node>();

	public Node(int id, long time)
	{
		userId = id;
		registerTime = time;
	}
}
