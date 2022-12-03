

import java.util.*;

public class CatCafe implements Iterable<Cat> {
	public CatNode root;

	public CatCafe() {
	}


	public CatCafe(CatNode dNode) {
		this.root = dNode;
	}

	// Constructor that makes a shallow copy of a CatCafe
	// New CatNode objects, but same Cat objects
	public CatCafe(CatCafe cafe) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		root = copyTree( cafe.root );
	}

	private CatNode copyTree (CatNode fromNode){
		// copy its ref to cat
		if ( fromNode == null)
			return null;
		CatNode toNode = new CatNode(fromNode.catEmployee);
		toNode.junior = copyTree( fromNode.junior );
		toNode.senior = copyTree( toNode.senior);
		return  toNode;
	}


	// add a cat to the cafe database
	public void hire(Cat c) {
		if (root == null)
			root = new CatNode(c);
		else
			root = root.hire(c);
	}

	// removes a specific cat from the cafe database
	public void retire(Cat c) {
		if (root != null)
			root = root.retire(c);
	}

	// get the oldest hire in the cafe
	public Cat findMostSenior() {
		if (root == null)
			return null;

		return root.findMostSenior();
	}

	// get the newest hire in the cafe
	public Cat findMostJunior() {
		if (root == null)
			return null;

		return root.findMostJunior();

	}

	// returns a list of cats containing the top numOfCatsToHonor cats
	// in the cafe with the thickest fur. Cats are sorted in descending
	// order based on their fur thickness.
	public ArrayList<Cat> buildHallOfFame(int numOfCatsToHonor) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		ArrayList<Cat> list1 = new ArrayList<Cat>(); //create a new array list
		ArrayList<Cat> list2 = new ArrayList<Cat>();
		int index_thickest = 0;

		construct(root, list1);
		int n = list1.size();
		for(int i =0; i< n; i++){
			Cat key = list1.get(i);
			int j = i-1;

			while(j >= 0 && list1.get(j).getFurThickness() > key.getFurThickness()){
				list1.get(j+1).equals(list1.get(j));
				j= j-1;

			}
			list1.get(j+1).equals(key);

		}
		for(int i =0; i< numOfCatsToHonor; i++){
			list2.add(list1.get(i));

		}
		return list2;
	}

	// Returns the expected grooming cost the cafe has to incur in the next numDays days
	public double budgetGroomingExpenses(int numDays) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		double expected_amount = 0;
		ArrayList<Cat> list1 = new ArrayList<Cat>(); //create a new array list
		construct(root, list1);
		for(int i = 0; i< list1.size(); i++){
			if(list1.get(i).getDaysToNextGrooming() < numDays){
				expected_amount += list1.get(i).getExpectedGroomingCost();
			}
		}
		return expected_amount;
	}

	public void construct(CatNode c, ArrayList<Cat> list){
		if(c == null){
			return ;
		}
		list.add(c.catEmployee);
		construct(c.junior, list);
		construct(c.senior, list);
	}
	// returns a list of list of Cats.
	// The cats in the list at index 0 need be groomed in the next week.
	// The cats in the list at index i need to be groomed in i weeks.
	// Cats in each sublist are listed in from most senior to most junior.
	public ArrayList<ArrayList<Cat>> getGroomingSchedule() {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		ArrayList<ArrayList<Cat>> list_of_Schedule = new ArrayList<>();
		ArrayList<Cat> flatList = new ArrayList<Cat>();
		construct(root, flatList);
		if ( flatList.isEmpty() )
			return list_of_Schedule;
		// sort desc by grooming days
		final Cat catOfTheLargest = flatList.stream().sorted(new Comparator<Cat>() {
			@Override
			public int compare(Cat o1, Cat o2) {
				return o2.getDaysToNextGrooming() - o1.getDaysToNextGrooming();
			}
		}).findFirst().get();
		// initialize the result list
		for ( int i = 0; i<catOfTheLargest.getDaysToNextGrooming() / 7; i++)
		{
			list_of_Schedule.add( new ArrayList<>());
		}
		// put cats in to result list
		for (int i = 0; i < flatList.size(); i++) {
			final Cat theCat = flatList.get(i);
			int week = theCat.getDaysToNextGrooming() / 7;
					list_of_Schedule.get(week).add(theCat);
			}

		return list_of_Schedule;
	}


	public Iterator<Cat> iterator() {
		return new CatCafeIterator();
	}


	public class CatNode {
		public Cat catEmployee;
		public CatNode junior;
		public CatNode senior;
		public CatNode parent;

		public CatNode(Cat c) {
			this.catEmployee = c;
			this.junior = null;
			this.senior = null;
			this.parent = null;
		}

		// add the c to the tree rooted at this and returns the root of the resulting tree
		public CatNode hire (Cat c) {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			CatNode nodeUp = null;
			if (c.getMonthHired() > catEmployee.getMonthHired()) {
				if (this.junior == null) {
					junior = new CatNode(c);
				}
				else {
					junior = junior.hire(c);
				}
				junior.parent = this;
				if(junior.catEmployee.getFurThickness() > catEmployee.getFurThickness()){
					nodeUp = rotateRight();
				}
			}
			else  {
				if (senior == null) {
					senior = new CatNode(c);
				}
				else {
					senior = senior.hire(c);
				}
				senior.parent = this;
				if(senior.catEmployee.getFurThickness() > catEmployee.getFurThickness()){
					nodeUp = rotateLeft();
				}
			}
			return nodeUp != null ? nodeUp : this;
		}

		private CatNode rotateLeft(){
			CatNode nodeR = senior; // the node to go up
			CatNode oldLeftOfR = nodeR.junior;  // i.e.  node A
			nodeR.junior = this;
			this.senior = oldLeftOfR;
			return nodeR;
		}


		private CatNode rotateRight(){
			CatNode nodeL = junior; // the node to go up
			CatNode oldRightOfL = nodeL.senior;
			nodeL.senior = this;
			this.junior = oldRightOfL;
			return nodeL;
		}

		// remove c from the tree rooted at this and returns the root of the resulting tree
		public CatNode retire(Cat c) {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			CatNode nodeUp = null;
			//Base Case: if the tree is empty
			if (c == null) {
				return null;
			}
			if(c.getMonthHired() > catEmployee.getMonthHired()){
				junior = junior.retire(c);
			}
			else if(c.getMonthHired() < catEmployee.getMonthHired()){
				senior = senior.retire(c);
			}
			else{
				//node with one child
				if(junior == null){
					return senior;
				}
				else if(senior == null){
					return junior;
				}
				else{
					//node with two children
					CatNode temp = junior;
					while(temp.senior != null){
						temp = temp.senior;
					}
					catEmployee = temp.catEmployee;
					junior = junior.retire(temp.catEmployee);
					junior.parent = this;
					if(junior.catEmployee.getFurThickness() > catEmployee.getFurThickness()){
						nodeUp = rotateRight();}
				}
			}
		return nodeUp != null ? nodeUp : this;
		}

		// find the cat with highest seniority in the tree rooted at this
		public Cat findMostSenior() {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			if(root == null){
				return null;
			}
			CatNode current = root;
			while(current.senior != null){
				current = current.senior;
			}
			return current.catEmployee;

		}

		// find the cat with lowest seniority in the tree rooted at this
		public Cat findMostJunior() {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			if(root == null){
				return null;
			}
			CatNode current = root;
			while(current.junior != null){
				current = current.junior;
			}
			return current.catEmployee;
		}

		// Feel free to modify the toString() method if you'd like to see something else displayed.
		public String toString() {
			String result = this.catEmployee.toString() + "\n";
			if (this.junior != null) {
				result += "junior than " + this.catEmployee.toString() + " :\n";
				result += this.junior.toString();
			}
			if (this.senior != null) {
				result += "senior than " + this.catEmployee.toString() + " :\n";
				result += this.senior.toString();
			}
			if (this.parent != null) {
				result += "parent of " + this.catEmployee.toString() + " :\n";
				result += this.parent.catEmployee.toString() +"\n";
			}
			return result;
		}
	}


	private class CatCafeIterator implements Iterator<Cat> {
		// HERE YOU CAN ADD THE FIELDS YOU NEED
		Cat current;
		ArrayList<Cat> catList = new ArrayList<Cat>();
		//put senoiry tree into list???

		private CatCafeIterator() {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			//	current points to head of the list current = list.head
			current = catList.get(0);
		}

		public Cat next(){
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			//return current.data and current = current.next
			Cat tmp = current;
			int pos = catList.indexOf(current);
			current = catList.get(pos+1);
			return tmp;

		}

		public boolean hasNext() {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			return (current != null);
		}


	}

	public static void main(String[] args) {
		Cat B = new Cat("Buttercup", 45, 53, 5, 85.0);
		Cat C = new Cat("Chessur", 8, 23, 2, 250.0);
		Cat J = new Cat("Jonesy", 0, 21, 12, 30.0);
		Cat JJ = new Cat("JIJI", 156, 17, 1, 30.0);
		Cat JTO = new Cat("J. Thomas O'Malley", 21, 10, 9, 20.0);
		Cat MrB = new Cat("Mr. Bigglesworth", 71, 0, 31, 55.0);
		Cat MrsN = new Cat("Mrs. Norris", 100, 68, 15, 115.0);
		Cat T = new Cat("Toulouse", 180, 37, 14, 25.0);


		Cat BC = new Cat("Blofeld's cat", 6, 72, 18, 120.0);
		Cat L = new Cat("Lucifer", 10, 44, 20, 50.0);

	}


}


