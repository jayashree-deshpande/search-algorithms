import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Map;
import java.lang.String;
import java.util.Collections;



/* class LinkDetails has the informtion of links between any two nodes  */
class LinkDetails
{
	private int hours;
	private int rFactor;
	
	/*constructor*/
	public LinkDetails(int hours,int rFactor)
	{
		this.hours=hours;
		this.rFactor=rFactor;
	}
	
	/* all methods*/
	
	public int getTime() 
	{ 
		return hours;
	}

	public int getrFactor() 
	{ 
		return rFactor;
	}
	
	 @Override
	 public String toString()
     {
       return "("+hours+";"+rFactor+")";
     }

}

/* This class Search has all BFS,DFS,UCS,USbyriskconsideration methods in it*/
public class Search{
	
	/* class 'Node'has information of node */
	public static class Node
    {
		private String nodeName;
		private List<String> path;
		private int pathCost;
		private int riskFactor;
		
		/* contructoe*/
        public Node(String nodeName, List<String> path,int pathCost,int riskFactor)
        {
            this.nodeName = nodeName;
            this.path = path;
            this.pathCost = pathCost;
            this.riskFactor = riskFactor;
        }
        
        /* All methods */
        public String getName()
        {
            return nodeName;
        }
        
        public void setName(String nodeName)
        {
            this. nodeName = nodeName;
        }
     
       public List<String> getPath()
        {
        	return path;
        }
        
		
		public void setPath(List<String> path)
        {
			this.path = path;
        	
        }
		
		public void setPathCost(int pathCost)
		{
			this.pathCost = pathCost;
		}
        
		
		public int getPathCost()
		{
			return pathCost;
		}
		
		public void setRiskFactor(int riskFactor )
		{
			this.riskFactor = riskFactor;
		}
		
		public int getRiskFactor()
		{
			return riskFactor;
		}
		
		@Override
    	public boolean equals(Object o)
    	{
    		if(o==this) return true;
    		if(o==null || !(o instanceof Node)) return false;
    		Node myNode = Node.class.cast(o);
    		return nodeName.equals(myNode.nodeName);
        }
		
		public String toString()
	    {
	        return "("+ nodeName +")" +"(" +  path + ")" + "(" + Integer.toString(pathCost, 0) + ")" 
	        			+ "("+Integer.toString(riskFactor, 0) + ")";
	    }
        
    }

	private Map<String, List<String>> mapRelation;
	private Map<Link, LinkDetails> mapLinkInfo;
	
	/***********************************************************************************/
	/* I used HashMap to store the Graph,there are two hash maps, 
	 * 'mapRelation' gives the information of which 2 nodes which are directly connected
	 * and 'mapLinkInfo' gives the information on each link ie. time in 
	 * hour and risk factor.
	/************************************************************************************/
	
	public void populateGraph()
	{
		BufferedReader in=null;
		
		mapRelation = new HashMap<String, List<String>>();
		mapLinkInfo= new HashMap<Link,LinkDetails>();
	     
		try
		{	
			/* reading the given text-file*/
			File file = new File("social-network.txt");
			FileReader reader = new FileReader(file);
			in = new BufferedReader(reader);
			String string;
			String word = null;
			String friend = null;
			int hours = 0;
			int rFactor = 0 ;
			String delims = "[ ]+";
		        
			while ((string = in.readLine()) != null) 
			{
				String[] tokens = string.split(delims);
				word = tokens[0];
				friend = tokens[1];
				hours = Integer.parseInt(tokens[2]);
				rFactor = Integer.parseInt(tokens[3]);
				
				/*storing values into maps*/
				List<String> l1 = mapRelation.get(word);
				if (l1 == null)
				{
					mapRelation.put(word, l1=new ArrayList<String>());
				}
	            l1.add(friend);
	            mapLinkInfo.put(new Link(word,friend),new LinkDetails(hours,rFactor));
	             	
	            List<String> l2 = mapRelation.get(friend);
				if (l2 == null)
				{
					mapRelation.put(friend, l2=new ArrayList<String>());
				}
		        l2.add(word);
		        mapLinkInfo.put(new Link(friend,word),new LinkDetails(hours,rFactor));
	             
			}
		        
			//for (String key : mapRelation.keySet()) 
			//{
	      	 //  System.out.println("------------------------------------------------");
	      	  // System.out.println("key: " + key + " value: " +mapRelation.get(key));
	      	   
			//}
		    
			//System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			
			//for (Link key : mapLinkInfo.keySet()) 
			//{
	     	//   System.out.println("------------------------------------------------");
	     	 //  System.out.println("between keys :" + key + "Time and risk factor : " + mapLinkInfo.get(key));
		     
			//}
		       
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally
		{
			try 
			{
				if(in!=null)
				{
					in.close();
				}
			} 
		    catch (IOException e) 
		    {
		    	e.printStackTrace();
		    }
		        
		}
	}
	
	/***************************************************************************/
	/*                                BFS                                  	   */
	/***************************************************************************/
	public void bfs(String s, String d)
	{
		try {
			PrintStream out = new PrintStream(new FileOutputStream(
					"breadth-first.result.txt"));

			/* create Queue as 'q'*/
			Queue<Node> q = new ArrayDeque<Node>();
			Set<String> visited = new HashSet<String>();

			/* create a List 'startList' */
			List<String> startList = new ArrayList<String>();

			/* add the start node in the queue */
			q.add(new Node(s,startList,0,0));
			visited.add(s);
			boolean pathFound = false;

			/*do the following till 'q' is not empty */
			while (!q.isEmpty()) 
			{
				Node currentNode = q.remove();
				List<String> currentPath = currentNode.getPath();

				if(currentNode.getName().equals(d))
				{
					currentPath.add(d);
					out.println("Result of BFS is:");
					out.println(convertPathToString(currentPath));
					pathFound = true;
					break;

				}
				else
				{
					List<String> newPath = new ArrayList<String>(currentPath);
					newPath.add(currentNode.getName());
					List<String> adj_u = mapRelation.get(currentNode.getName());

					if (adj_u != null) 
					{	
						for (String v : adj_u)
						{
							if(!visited.contains(v))
							{
								q.add(new Node(v,newPath,0,0));
								//out.println("current q is = " + q);
								visited.add(v);
							}	
						}
					}
				}
			}
			if(pathFound == false)
			{
				out.println("");
				out.println("No path found between "+ s + " and " + d);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
}

	/***********************************************************************************/
	/*                                   DFS                                                     */
	/***********************************************************************************/
	
	public void dfs(String s, String d)
	{
		try 
		{
			PrintStream out = new PrintStream(new FileOutputStream(
					"depth-first.result.txt"));
			Stack<Node> stack = new Stack<Node>();
			Set<String> visited = new HashSet<String>();


			List<String> startList = new ArrayList<String>();
			stack.push(new Node(s,startList,0,0));
			visited.add(s);
			boolean pathFound = false;
			while (!stack.isEmpty()) {
				//u = q.remove();
				Node currentNode = stack.pop();
				List<String> currentPath = currentNode.getPath();

				if(currentNode.getName().equals(d))
				{
					currentPath.add(d);
					out.println("Result of DFS is:");
					out.println(convertPathToString(currentPath));
					pathFound = true;
					break;

				}
				else
				{
					List<String> newPath = new ArrayList<String>(currentPath);
					newPath.add(currentNode.getName());
					List<String> adj_u = mapRelation.get(currentNode.getName());
					Collections.reverse(adj_u);
					if (adj_u != null) 
					{	
						//boolean dFound= false;
						for (String v : adj_u)
						{
							if(!visited.contains(v))
							{
								stack.add(new Node(v,newPath,0,0));
								//out.println("current q is = " + stack);
								visited.add(v);
							}	

						}

					}
				}

			}
			if(pathFound == false)
			{
				out.println("");
				out.println("No path found between "+ s + " and " + d);
			}

			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/****************************************************************************/
	/*                       uniform-cost.time                                  */
	/****************************************************************************/
	
	public void timeUsearch(String s, String d)
	{
		try 
		{
			PrintStream out = new PrintStream(new FileOutputStream(
					"uniform-cost.time.result.txt"));
			int initCapacity = 20;
			/* Use of Priority Queue */
			PriorityQueue<Node> pQueue = new PriorityQueue<Node>(initCapacity, new Comparator<Node>()
					{
				public int compare(Node node1, Node node2)
				{
					return (new Integer(node1.getPathCost()).compareTo(new Integer(node2.getPathCost())));
				}			
					});

			Set<String> visited = new HashSet<String>();

			List<String> initialPath = new ArrayList<String>();
			pQueue.add(new Node(s,initialPath,0,0));
			visited.add(s);

			boolean pathFound = false;
			while (!pQueue.isEmpty()) 
			{
				Node currentNode = pQueue.remove();
				List<String> currentPath = currentNode.getPath();
				int currentPathCost = currentNode.getPathCost();

				if(currentNode.getName().equals(d))
				{
					currentPath.add(d);
					out.println("Reult of Uniform-Cost-Time is :");
					out.println(convertPathToString(currentPath));
					out.println("");
					out.println("final path-time is = " + currentPathCost);

					pathFound = true;
					break;

				}
				else
				{
					List<String> newPath = new ArrayList<String>(currentPath);
					newPath.add(currentNode.getName());
					List<String> adj_u = mapRelation.get(currentNode.getName());

					if (adj_u != null) 
					{	
						for (String v : adj_u)
						{
							Link link = new Link(currentNode.getName(),v);

							LinkDetails linkDetails = mapLinkInfo.get(link);
							int newPathCost = linkDetails.getTime() + currentPathCost ;
							Node newNode = new Node(v,newPath,newPathCost,0);
							if(visited.contains(v)) 
							{
								Iterator<Node> itr = pQueue.iterator();
								while(itr.hasNext()) 
								{
									
									Node element = (Node) itr.next();
									if(element.getName().equals(newNode.getName()))
									{
										if(element.getPathCost() > newNode.getPathCost())
										{
											//out.println("Removing element = " + element);
											//System.out.println("Removing element = " + element);
											pQueue.remove(element);
											//out.println("Queue before adding = " + pQueue);
											pQueue.add(newNode);
											//out.println("Queue is = " + pQueue);
											break;
										}
									}

								}

							}
							else
							{
								pQueue.add(newNode);
								visited.add(v);
								//out.println("Queue is = " + pQueue);
							}

						}
					}
				}
			}
			if(pathFound == false)
			{
				out.println("");
				out.println("No path found between "+ s + " and " + d);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/******************************************************************************/
	/*                             uniform-cost.risk                              */
	/******************************************************************************/
	
	public void rFactorUsearch(String s, String d)
	{
		try 
		{
			PrintStream out = new PrintStream(new FileOutputStream(
					"uniform-cost.risk.result.txt"));
			int initCapacity = 20;
			PriorityQueue<Node> pQueue = new PriorityQueue<Node>(initCapacity, new Comparator<Node>() 
					{
				public int compare(Node node1, Node node2)
				{
					return (new Integer(node1.getRiskFactor()).compareTo(new Integer(node2.getRiskFactor())));
				}			
					});

			Set<String> visited = new HashSet<String>();

			List<String> initialPath = new ArrayList<String>();
			pQueue.add(new Node(s,initialPath,0,0));
			visited.add(s);

			boolean pathFound = false;
			while (!pQueue.isEmpty()) 
			{
				Node currentNode = pQueue.remove();
				List<String> currentPath = currentNode.getPath();
				int currentRiskFactor = currentNode.getRiskFactor();

				if(currentNode.getName().equals(d))
				{
					currentPath.add(d);
					out.println("Reult of Uniform-Cost-Risk is :");
					out.println(convertPathToString(currentPath));
					out.println("");
					out.println("final path-risk is = " + currentRiskFactor);

					pathFound = true;
					break;

				}
				else
				{
					List<String> newPath = new ArrayList<String>(currentPath);
					newPath.add(currentNode.getName());
					List<String> adj_u = mapRelation.get(currentNode.getName());

					if (adj_u != null) 
					{	
						for (String v : adj_u)
						{
							Link link = new Link(currentNode.getName(),v);

							LinkDetails linkDetails = mapLinkInfo.get(link);
							int newRiskPath = linkDetails.getrFactor() + currentRiskFactor ;
							Node newNode = new Node(v,newPath,0,newRiskPath);
							if(visited.contains(v)) 
							{
								Iterator<Node> itr = pQueue.iterator();
								while(itr.hasNext()) 
								{
									Node element = (Node) itr.next();
									if(element.getName().equals(newNode.getName()))
									{
										if(element.getRiskFactor() > newNode.getRiskFactor())
										{
											//out.println("Removing element = " + element);
											pQueue.remove(element);
											//out.println("Queue before adding = " + pQueue);
											pQueue.add(newNode);
											//out.println("Queue is = " + pQueue);
											break;
										}
									}

								}

							}
							else
							{
								pQueue.add(newNode);
								visited.add(v);
								//out.println("Queue is = " + pQueue);
							}

						}
					}
				}
			}
			
			if(pathFound == false)
			{
				out.println("");
				out.println("No path found between "+ s + " and " + d);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private String convertPathToString(List<String> currentPath)
	{
		String outputString = "";
		
		for(int i=0; i< currentPath.size() ; ++i)
		{
			if(i == (currentPath.size()-1))
			{
				outputString = outputString.concat(currentPath.get(i));
				
			}
			else
			{
				outputString = outputString.concat(currentPath.get(i)+"-");
		
			}
		}
		return outputString;
	}
	
	/*    'main' method  */
	public static void main(String[] args) 
	{
		
		if(args.length < 3)
		{
			System.out.print("Usage: <bfs/dfs/rishsearch/timesearch> <start name> <end name>");
		}
		
		/* Object creation */
		Search mySearch = new Search();
		/* Call methods */
		/*taking user input*/
		mySearch.populateGraph();
		if (args[0].equals("bfs"))
		{
			mySearch.bfs(args[1], args[2]);
		}
		else if(args[0].equals("dfs"))
		{
			mySearch.dfs(args[1], args[2]);
		}
		else if(args[0].equals("uct"))
		{
			mySearch.timeUsearch(args[1], args[2]);
		}
		else if(args[0].equals("ucr"))
		{
			mySearch.rFactorUsearch(args[1], args[2]);
		}
		else 
			System.out.print(args[0]+" command not found");
		
	}
}

