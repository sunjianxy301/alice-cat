

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
		CatCafe catCafe = new CatCafe();
		CatNode oldNode = cafe.root;

		catCafe.root = oldNode;
		hire(oldNode.catEmployee);

		copy(oldNode, cafe.root);
	}

	private void copy (CatNode oldNode, CatNode newNode){
		if(oldNode.junior != null){
			newNode.hire(oldNode.junior.catEmployee);
			copy(oldNode.junior, newNode.junior);
		}
		if(oldNode.senior != null){
			newNode.hire(oldNode.senior.catEmployee);
			copy(oldNode.senior, newNode.senior);
		}
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

		int position = 0;
		list1.set(0, root.catEmployee); //set root to the first element
		while (root != null){
			while(root.junior != null){
				list1.add(2*position +1 ,root.junior.catEmployee);
				root = root.junior;
				position = 2*position +1;
			}
			position = 0;
			while(root.senior != null){
				list1.add(2*position +2, root.senior.catEmployee);
				root = root.senior;
				position = 2*position +2;
			}

		}
		for(int i =0; i<numOfCatsToHonor; i++){
			list2.add(list1.get(i));
		}
		return list2;
	}

	// Returns the expected grooming cost the cafe has to incur in the next numDays days
	public double budgetGroomingExpenses(int numDays) {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		return 0;
	}

	// returns a list of list of Cats.
	// The cats in the list at index 0 need be groomed in the next week.
	// The cats in the list at index i need to be groomed in i weeks.
	// Cats in each sublist are listed in from most senior to most junior.
	public ArrayList<ArrayList<Cat>> getGroomingSchedule() {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		return null;
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
			if (root == null) {
				root = new CatNode(c);
			}
			else if (c.getMonthHired() < this.catEmployee.getMonthHired()) {
				if (this.junior == null) {
					this.junior = new CatNode(c);
					this.junior.parent = this;
					if(c.getFurThickness() > this.junior.parent.catEmployee.getFurThickness()){
						rotateright(this.junior);}
				}
				else {
					this.junior.hire(c);
					if(c.getFurThickness() > this.parent.catEmployee.getFurThickness()){
						rotateright(this.junior);
					}
				}

			}
			else if (c.getMonthHired() > this.catEmployee.getMonthHired()) {
				if (this.senior == null) {
					this.senior = new CatNode(c);
					this.senior.parent = this;
					if(c.getFurThickness() > this.senior.parent.catEmployee.getFurThickness()){
						rotateleft(this.senior);
					}
				}
				else {
					this.senior.hire(c);
					if(c.getFurThickness() > this.parent.catEmployee.getFurThickness()){
						rotateleft(this.senior);
					}
				}
			}

			return root;
		}

		private void rotateleft(CatNode a){
			CatNode parent = a.parent;
			CatNode rightC = a.senior;

			a.senior = rightC.junior;
			if(rightC.junior != null){
				rightC.junior.parent = a;
			}
			rightC.junior = a;
			a.parent = rightC;

			replaceParentC(parent, a, rightC);
		}
		private void replaceParentC(CatNode parent, CatNode oldChild, CatNode newChild){
			if(parent == null){
				root = newChild;
			} else if(parent.junior == oldChild){
				parent.junior = newChild;
			} else if (parent.senior == oldChild){
				parent.senior = newChild;
			}
			if(newChild != null){
				newChild.parent = parent;
			}
		}
		private void rotateright (CatNode a){
			CatNode parent = a.parent;
			CatNode leftC = a.junior;

			a.junior = leftC.senior;
			if(leftC.senior != null){
				leftC.senior.parent = a;
			}
			leftC.senior = a;
			a.parent = leftC;
			replaceParentC(parent, a, leftC);
		}
		// remove c from the tree rooted at this and returns the root of the resulting tree
		public CatNode retire(Cat c) {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			if(root == null){
				return null;
			} else if( c.getMonthHired() < root.catEmployee.getMonthHired()){
				root.junior.retire(c);
			} else if (c.getMonthHired() > root.catEmployee.getMonthHired()){
				root.senior.retire(c);
			} else if(this.junior == null)
				root = root.senior;
			else if(root.senior == null)
				root = root.junior;
			else {
				root.catEmployee = root.senior.findMostJunior();
				root.senior.retire(root.catEmployee);
			}
			return root;
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


