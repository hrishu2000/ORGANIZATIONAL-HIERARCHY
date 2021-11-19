import java.io.*; 
import java.util.*; 

// Tree node
class Node 
{
	int id;
	int level;
	Node boss;
	Node rightsib;
	Node leftsib;
	Node headchild;
	Node tailchild;
	Node(int d,int l)
	{
		id=d;
		level=l;
	}
}

class NodeAvl
{
	int height;
	Node loc;
	NodeAvl right;
	NodeAvl left;
	NodeAvl(Node n)
	{
		loc=n;
		height=1;
	}
}

class AVLTree{

	NodeAvl root;
	NodeAvl insert(Node n) 
	{
		return insert(root,n);
	}
	NodeAvl insert(NodeAvl node,Node n)
	{
		if(node==null)
		{
			NodeAvl temp=new NodeAvl(n);
			return temp;
		}
		if(n.id>node.loc.id) node.right=insert(node.right,n);
		else if(n.id<node.loc.id) node.left=insert(node.left,n);

		node.height=Math.max(height(node.left),height(node.right))+1;
		int balance=bf(node);
		if(balance>1 && n.id<node.left.loc.id) return rightrotate(node);
		if(balance<-1 && n.id>node.right.loc.id) return leftrotate(node);
		if(balance>1 && n.id>node.left.loc.id) 
		{
			node.left=leftrotate(node.left);
			return rightrotate(node);
		}
		if(balance<-1 && n.id<node.right.loc.id) 
		{
			node.right=rightrotate(node.right);
			return leftrotate(node);
		}
		return node;
	}

	void delete(int item) 
	{
		delete(root,item);
	}
	NodeAvl delete(NodeAvl node, int item) 
	{
	    if (node == null) return node;
	    if (item < node.loc.id) node.left = delete(node.left, item);
	    else if (item > node.loc.id) node.right = delete(node.right, item);
	    else 
	    {
	        if (node.left == null) return node.right;
	        if (node.right == null) return node.left;
	        else 
	        {
		        NodeAvl temp = node.right;
			    while (temp.left != null) temp = temp.left;
		        node.loc = temp.loc;
		        node.right = delete(node.right, temp.loc.id);
	        }
	    }
	    if (node == null) return node;

	    node.height = Math.max(height(node.left), height(node.right)) + 1;
	    int balance = bf(node);
	    if (balance > 1 && bf(node.left)>=0) return rightrotate(node);
	    if (balance > 1 && bf(node.left)<0) 
	    {
	    	node.left=leftrotate(node.left);
	    	return rightrotate(node);
	    } 
	    if (balance < -1 && bf(node.right)<=0) return leftrotate(node); 
	    if (balance < -1 && bf(node.right)>0) 
	    {
	    	node.right=rightrotate(node.right);
	    	return leftrotate(node);
	    } 
	    
	    return node;
    }

	int height(NodeAvl node)
	{
		if(node==null) return 0;
		return node.height;
	}

	int bf(NodeAvl node)
	{
		if(node==null) return 0;
		return height(node.left)-height(node.right);
	}

	NodeAvl rightrotate(NodeAvl c)
	{
		NodeAvl l=c.left;
		NodeAvl lr=l.right;
		l.right=c;
		c.left=lr;
		c.height=Math.max(height(c.left),height(c.right))+1;
		l.height=Math.max(height(l.right),height(l.left))+1;
		return l;
	}

	NodeAvl leftrotate(NodeAvl c)
	{
		NodeAvl r=c.right;
		NodeAvl rl=r.left;
		r.left=c;
		c.right=rl;
		c.height=Math.max(height(c.left),height(c.right))+1;
		r.height=Math.max(height(r.right),height(r.left))+1;
		return r;
	}

	Node at(int item)
	{
		return at(root,item);
	}
	Node at(NodeAvl node,int item)
	{
		if(node==null) return null;
		if(node.loc.id==item) return node.loc;
		if(item>node.loc.id) return at(node.right,item);
		if(item<node.loc.id) return at(node.left,item);
		return null;
	}
}

class Sortforstring implements Comparator<Node>
{
    
    public int compare(Node a, Node b)
    {
        return a.id-b.id;
    }
}


public class OrgHierarchy implements OrgHierarchyInterface{

//root node
Node root;
AVLTree map=new AVLTree();
int size=0;

public boolean isEmpty()
{
	if(root==null) return true;
	return false;
} 

public int size()
{
	return size;
}

public int level(int id) throws IllegalIDException, EmptyTreeException
{
	if(root==null) throw new EmptyTreeException("EmptyTree");
	else
	{
		Node loc=map.at(id);
		if(loc==null) throw new IllegalIDException("IllegalID");
		else return loc.level;
	}
} 

public void hireOwner(int id) throws NotEmptyException{
	if(root!=null) throw new NotEmptyException("NotEmptyTree");
	else
	{
		root=new Node(id,1);
		map.root=map.insert(root);
		size++;
	}
}

public void hireEmployee(int id, int bossid) throws IllegalIDException, EmptyTreeException{
	//your implementation
	if(root==null) throw new EmptyTreeException("EmptyTree");
	else
	{
		Node loc=map.at(bossid); 
		if(loc==null) throw new IllegalIDException("IllegalID");
		else
		{
			Node hire=new Node(id,loc.level+1);
			hire.boss=loc;
			if(loc.headchild==null)
			{
				loc.headchild=hire;
				loc.tailchild=hire;
			}
			else if(loc.headchild!=null)
			{
				loc.tailchild.rightsib=hire;
				hire.leftsib=loc.tailchild;
				loc.tailchild=hire;
			}
			map.root=map.insert(hire);
			size++;
		}
	}
} 

public void fireEmployee(int id) throws IllegalIDException,EmptyTreeException{
	//your implementation
	if(root==null) throw new EmptyTreeException("EmptyTree");
	Node loc=map.at(id);
	if(loc==null || loc==root || loc.headchild!=null) throw new IllegalIDException("IllegalID");
 	if(loc.leftsib==null)
 	{
 		if(loc.rightsib==null)
 		{
 			Node boss=loc.boss;
 			boss.headchild=null;
 			boss.tailchild=null;
 		} 
 		else
 		{
 			Node boss=loc.boss;
 			boss.headchild=loc.rightsib;
 			loc.rightsib.leftsib=null;
 		}
 	}
 	else
 	{
 		if(loc.rightsib==null)
 		{
 			Node boss=loc.boss;
 			boss.tailchild=loc.leftsib;
 			boss.tailchild.rightsib=null;
 		}
 		else
 		{
 			Node prev=loc.leftsib;
 			prev.rightsib=loc.rightsib;
 			loc.rightsib.leftsib=prev;
 		}
 	}
 	size--;
 	map.delete(id);
}
public void fireEmployee(int id, int manageid) throws IllegalIDException,EmptyTreeException{
	if(root==null) throw new EmptyTreeException("EmptyTree");
	Node loc=map.at(id);
	Node manager=map.at(manageid);
	if(loc==null || loc==root || manager==null || manager.level!=loc.level) throw new IllegalIDException("IllegalID");
	Node reschildhead=loc.headchild;
	Node reschildtail=loc.tailchild;
	if(reschildhead==null) fireEmployee(id);
	else
	{ 
		Node temp=reschildhead;
		while(temp!=null)
		{
			temp.boss=manager;
			temp=temp.rightsib;
		}
	 	if(loc.leftsib==null)
	 	{
	 		Node boss=loc.boss;
	 		boss.headchild=loc.rightsib;
	 		loc.rightsib.leftsib=null;
	 	}
	 	else
	 	{
	 		if(loc.rightsib==null)
	 		{
	 			Node boss=loc.boss;
	 			boss.tailchild=loc.leftsib;
	 			boss.tailchild.rightsib=null;
	 		}
	 		else
	 		{
	 			Node prev=loc.leftsib;
	 			prev.rightsib=loc.rightsib;
	 			loc.rightsib.leftsib=prev;
	 		}
	 	}
	 	if(manager.headchild==null)
	 	{
	 		manager.headchild=reschildhead;
	 		manager.tailchild=reschildtail;
	 	}
	 	else
	 	{
	 		manager.tailchild.rightsib=reschildhead;
	 		reschildhead.leftsib=manager.tailchild;
	 		manager.tailchild=reschildtail;
	 	}
	 	size--;
 		map.delete(id);
	}
} 

public int boss(int id) throws IllegalIDException,EmptyTreeException{
	if(root==null) throw new EmptyTreeException("EmptyTree");
	Node loc=map.at(id);
	if(loc==null) throw new IllegalIDException("IllegalID");
	if(loc==root) return -1;
	return loc.boss.id;
}

public int lowestCommonBoss(int id1, int id2) throws IllegalIDException,EmptyTreeException{
	if(root==null) throw new EmptyTreeException("EmptyTree");
	Node loc1=map.at(id1);
	Node loc2=map.at(id2);
	Node temp1=loc1;
	Node temp2=loc2;
	if(loc1==null || loc2==null) throw new IllegalIDException("IllegalID");
	if(loc1==root || loc2==root) return -1;
	if(loc1.level>loc2.level) while(temp1.level>temp2.level) temp1=temp1.boss;
	else while(temp1.level<temp2.level) temp2=temp2.boss;
	while(temp1!=temp2)
	{
		temp1=temp1.boss;
		temp2=temp2.boss;
	}
	if(temp1.id==id1 || temp2.id==id2) 
	{
		if(temp1==root) return -1;
		return temp1.boss.id;
	}
	return temp1.id;
}

public String toString(int id) throws IllegalIDException, EmptyTreeException{
	if(root==null) throw new EmptyTreeException("EmptyTree");
	Node man = map.at(id);
	if(man==null) throw new IllegalIDException("IllegalID");

	String res = "";

	res = res+ man.id;
	Vector<Node>v = new Vector<Node>();
	Node temp = man.headchild;
	while(temp !=null)
	{
		v.add(temp);
		temp = temp.rightsib;
	}

	Collections.sort(v, new Sortforstring());
	Vector<Node>vec;
	while(v.size()>0)
	{
		res+=",";
		for(int i=0; i<v.size()-1; i++)
		{
			res=res+ v.get(i).id +" ";
		}
		res+=v.get(v.size()-1).id;
		vec = new Vector<Node>(v);
		v.clear();
		for(int i=0; i<vec.size(); i++)
		{
			temp = map.at(vec.get(i).id);
			Node temp2 = temp.headchild;
			while(temp2 != null)
			{
				v.add(temp2);
				temp2 = temp2.rightsib;
			}
		}
		Collections.sort(v, new Sortforstring());
	}
	return res;
}

}
