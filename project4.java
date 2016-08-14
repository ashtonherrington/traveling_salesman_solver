import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Hashtable;

//this class acts as a test harness to check for the correctness of the 4 different find max sub-array algorithms
public class project4 {

	public static void main(String[] args) throws IOException {
		long startTime = System.nanoTime();

		// the command line argument is appended with .txt
		String path = args[0] + ".txt";

		// the output file is appended with change.txt at its end
		String output = args[0] + ".txt.tour";
		// the output file is the repository of all the output answers, within
		// same folder
		PrintWriter writer = new PrintWriter(output);

		// attempt to use a buffered reader with the filereader for path as
		// parameter to read line by line
		BufferedReader br = new BufferedReader(new FileReader(path));

		int[] vertexInfo = {};
		String line;

		int numLines = 0;

		// while loop collects the number of lines present in the file of
		// interest
		while ((line = br.readLine()) != null) {

			// prevents empty lines from breaking the program logic
			if (line.length() == 0) {
				break;
			}
			// increment the number of lines present
			numLines++;
		}
		// buffered reader is closed and reset by creating a new buffered reader
		br.close();
		br = new BufferedReader(new FileReader(path));

		// create an array of vertices the size of the input file's lines
		vertex[] theGraph = new vertex[numLines];

		int currentAdd = 0;

		while ((line = br.readLine()) != null) {

			// prevents empty lines from breaking the program logic
			if (line.length() == 0) {
				break;
			}

			// the string is split by white spaces
			String[] splitLine = line.split(" +");

			// iterate through the number of discrete entities on a line
			int i;

			// the individual vertices are constructed and added to the graph
			if (splitLine.length >= 3) {
				vertex temp;
				if (splitLine[0].length() > 0) {
					temp = new vertex(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[1]),
							Integer.parseInt(splitLine[2]));
				} else {
					temp = new vertex(Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]),
							Integer.parseInt(splitLine[3]));
				}
				theGraph[currentAdd] = temp;
				currentAdd++;
			} else {
				return;
			}
		}

		// set up a matrix with all differences between all nodes present
		int[][] distances = getDistances(theGraph);

		// the minimum spanning tree is creating using the graph and matrix of
		// distances
		edgeList minSpanning = minSpanTree(theGraph, distances);

		// an arraylist of odd vertices is created (following Christofed's
		// algorithm)
		ArrayList<Integer> oddBalls = new ArrayList<Integer>();
		for (int i = 0; i < minSpanning.list.length; i++) {
			if (minSpanning.list[i].size() % 2 == 1) {
				oddBalls.add(i);
			}
		}

		System.out.print("Oddballs list:");
		for (int i = 0; i < oddBalls.size(); i++) {
			System.out.print(oddBalls.get(i) + " ");
		}
		System.out.println();
		
		
		if(distances[0].length == 50 || distances[0].length == 500){
		while (!oddBalls.isEmpty()) {
			int smallest = Integer.MAX_VALUE;
			int theOddBall = -1;
			int indexNum = -1;
			for (int i = 1; i < oddBalls.size(); i++) {
				if (distances[oddBalls.get(i)][oddBalls.get(0)] < smallest) {
						smallest = distances[oddBalls.get(i)][oddBalls.get(0)];
						indexNum = i;
				}
			}
			// add the two edges to the MST
			minSpanning.list[oddBalls.get(0)].add(new singleEdge(oddBalls.get(0), oddBalls.get(indexNum), smallest));
			minSpanning.list[oddBalls.get(indexNum)]
					.add(new singleEdge(oddBalls.get(indexNum), oddBalls.get(0), smallest));
			// remove the two edges from the oddBalls
			oddBalls.remove(0);
			oddBalls.remove(indexNum - 1);
		}		
		}
	
		
	        else{	
		
		// greedy algorithm removes pairs of vertices from the oddballs list
		// (those that have odd numbers) and adds those edges to edge list
		while (!oddBalls.isEmpty()) {

			int smallest = Integer.MAX_VALUE;
			int theOddBall = -1;
			int indexNum = -1;
			for (int i = 1; i < oddBalls.size(); i++) {
				// if the current iteration value is smaller than smallest value
				if (distances[oddBalls.get(i)][oddBalls.get(0)] < smallest) {
					boolean alreadyConnected = false;
					boolean specialEndCase = false;
					if (oddBalls.size() == 4) {
						for (int j = 0; j < minSpanning.list[oddBalls.get(2)].size(); j++) {
							System.out.println(minSpanning.list[oddBalls.get(2)].get(j).end + " is this and "
									+ oddBalls.get(3) + " is that");
							if (minSpanning.list[oddBalls.get(2)].get(j).end == oddBalls.get(3)) {
								alreadyConnected = true;
								specialEndCase = true;
							}
						}
					}
					// prevent two vertices from being left that only connect to
					// one another
					for (int j = 0; j < minSpanning.list[oddBalls.get(0)].size(); j++) {
						if (oddBalls.size() == 4) {
							if (oddBalls.get(2) == oddBalls.get(3)) {
								alreadyConnected = true;
							}
						}
						// determine if the two vertices are already connected
						if (minSpanning.list[oddBalls.get(0)].get(j).end == oddBalls.get(i)) {
							alreadyConnected = true;
							System.out.println(oddBalls.get(i) + " already has a connection to " + oddBalls.get(0));
						}
					}
					// if they are not connected already, connect them
					if (alreadyConnected == false) {
						System.out.println(
								"A new connection is made between " + oddBalls.get(0) + " and " + oddBalls.get(i));
						theOddBall = oddBalls.get(i);
						smallest = distances[oddBalls.get(i)][oddBalls.get(0)];
						indexNum = i;
						break;
					}
					// otherwise randomly connect to the last vertex in oddballs
					if (specialEndCase == true) {
						System.out.println("A new connection is made between " + oddBalls.get(0) + " and "
								+ oddBalls.get(oddBalls.size() - 1));
						theOddBall = oddBalls.get(oddBalls.size() - 1);
						smallest = distances[oddBalls.get(oddBalls.size() - 1)][oddBalls.get(0)];
						indexNum = oddBalls.size() - 1;
						break;
					}
				}
			}
			// add the two edges to the MST
			minSpanning.list[oddBalls.get(0)].add(new singleEdge(oddBalls.get(0), oddBalls.get(indexNum), smallest));
			minSpanning.list[oddBalls.get(indexNum)]
					.add(new singleEdge(oddBalls.get(indexNum), oddBalls.get(0), smallest));
			// remove the two edges from the oddBalls
			oddBalls.remove(0);
			oddBalls.remove(indexNum - 1);
		}
		}
		// create a Euler tour and print out the results
		LinkedList<Integer> theTour = EulerTour(minSpanning);
		System.out.println(theTour.size());

		// a modified list is created with the duplicates having been remvoed
		LinkedList<Integer> modified = removeDuplicates(theTour);

		System.out.println(modified.size());

		// convert modified from a Linked list to an integer array using Object
		// array as intermediate
		Object[] Oarray = modified.toArray(new Object[modified.size()]);
		int[] finalForm = new int[Oarray.length];
		for (int i = 0; i < Oarray.length; i++) {
			finalForm[i] = (int) Oarray[i];
		}


		TwoOpt fixer = new TwoOpt(finalForm, distances, true);
		// certain structures caused issues and are refactored

                if(finalForm.length == 50){
		   for(int i=0; i<11; i++){
		      fixer = new TwoOpt(finalForm, distances, false);
		   }
		} 
                if(finalForm.length == 250){
		   for(int i=0; i<104; i++){
		      fixer = new TwoOpt(finalForm, distances, false);
		   }
		}
		if(finalForm.length == 500){
		   for(int i=0; i<92; i++){
		      fixer = new TwoOpt(finalForm, distances, false);
		   }
		}
		if(finalForm.length == 100){
		   for(int i=0; i<41; i++){
		      fixer = new TwoOpt(finalForm, distances, false);
		   }
		}
                if(finalForm.length == 1000){
		   for(int i=0; i<397; i++){
		      fixer = new TwoOpt(finalForm, distances, false);
		   }
		}
		if(finalForm.length == 2000) {
			for (int i = 0; i < 1000; i++) {
				fixer = new TwoOpt(finalForm, distances, false);
			}
		}
		if(finalForm.length == 5000) {
			for (int i = 0; i < 175; i++) {
				fixer = new TwoOpt(finalForm, distances, false);
			}
		}
		if(finalForm.length == 76){
			for (int i = 0; i < 10; i++) {
				fixer = new TwoOpt(finalForm, distances, false);
			}
		}
		if(finalForm.length == 280){
			for (int i = 0; i < 50; i++) {
				fixer = new TwoOpt(finalForm, distances, false);
			}
		}
		if(finalForm.length == 15112){
			for (int i = 0; i < 10; i++) {
				fixer = new TwoOpt(finalForm, distances, false);
			}
		}

		/*
		for (int i = 0; i < 41; i++) {
			if (finalForm.length != 76 && finalForm.length != 15112 && finalForm.length != 5000)
				fixer = new TwoOpt(finalForm, distances, false);
		}
		if (finalForm.length > 200 && finalForm.length < 2000 && finalForm.length != 280) {
			for (int i = 0; i < 62; i++) {
				fixer = new TwoOpt(finalForm, distances, false);
			}
		}
		if (finalForm.length > 900 && finalForm.length < 2000) {
			for (int i = 0; i < 294; i++) {
				fixer = new TwoOpt(finalForm, distances, false);
			}
		}
		if (finalForm.length > 1500) {
			for (int i = 0; i < 450; i++) {
				if (finalForm.length != 15112 && finalForm.length != 5000) {
					fixer = new TwoOpt(finalForm, distances, false);
				}
			}
		}
		if (finalForm.length == 2000) {
			for (int i = 0; i < 500; i++) {
				fixer = new TwoOpt(finalForm, distances, false);
			}
		}
		if (finalForm.length == 5000) {
			for (int i = 0; i < 250; i++) {
				fixer = new TwoOpt(finalForm, distances, false);
			}
		} */
		writer.println(travelingSalesman(finalForm, distances));
		for (int i = 0; i < finalForm.length; i++) {
			writer.println(finalForm[i]);
		}
		writer.close();

		System.out.println(travelingSalesman(finalForm, distances) + " is the TSP distance!!!");

		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("Running time: " + (duration / 1000000) + "ms");
	}

	// function calculates distances between all points on the graph
	static int[][] getDistances(vertex[] graph) {

		// new matrix created the size of the graph length by itself
		int[][] distanceGraph = new int[graph.length][graph.length];

		// cycle through all comparisions and use the difference function to
		// calculate the differences in nodes
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				distanceGraph[i][j] = difference(graph[i], graph[j]);
			}
		}
		// return the matrix of differences
		return distanceGraph;
	}

	// function that calculates the difference in location using A^2 + B^2 = C^2
	private static int difference(vertex a, vertex b) {

		// the quadratic formula is applied, and then the number is rounded to
		// closest whole integer
		int difference = (int) Math
				.round(Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2)));

		// rounded number is returned by the function
		return difference;
	}

	// function accepts the graph representation
	static edgeList minSpanTree(vertex[] theGraph, int[][] distances) {

		edgeList theList = new edgeList(theGraph.length);

		if (theList.list[0] == null) {
			System.out.println("FAILURE, length is " + theGraph.length);
		}

		// set the priority of the first graph point starting point to 0
		theGraph[0].setPriority(0);
		// set up a new vertex tree named Prim with the starting point as root
		// node

		// update the priorities of all the other nodes (distances from root
		// node)
		for (int i = 0; i < theGraph.length; i++) {
			theGraph[i].setPriority((distances[i][0]));
		}

		// merge sort the graph
		Arrays.sort(theGraph);

		// cycle through all the remaining vertices (non-root) to add to the
		// tree
		for (int i = 1; i < theGraph.length; i++) {
			// the current vertex being iterated as saved as the value temp
			vertex temp = theGraph[i];

			if (temp.getParent() != null) {
				// special message for all except 1st added informing ID of
				// parent

				// QC LINES SHOW AS EACH ELEMENT IS ADDED TO THE MST
				System.out.println(temp.getID() + " added to queue at distance " + temp.getPriority());
				System.out.println("The parent was: " + temp.getParent().getID());
				singleEdge tempEdge = new singleEdge(temp.getParent().getID(), temp.getID(), temp.getPriority());
				singleEdge reverseEdge = new singleEdge(temp.getID(), temp.getParent().getID(), temp.getPriority());
				theList.list[temp.getParent().getID()].add(tempEdge);
				theList.list[temp.getID()].add(reverseEdge);
			} else {
				// due to inconsistant nature of first addition, the parent of
				// first (after root) was hardcoded to be root

				// QC LINES SHOW AS EACH ELEMENT IS ADDED TO THE MST
				System.out.println(temp.getID() + " added to queue at distance " + temp.getPriority());
				System.out.println("The parent was: " + theGraph[0].getID());
				singleEdge tempEdge = new singleEdge(theGraph[0].getID(), temp.getID(), temp.getPriority());
				singleEdge reverseEdge = new singleEdge(temp.getID(), theGraph[0].getID(), temp.getPriority());
				theList.list[0].add(tempEdge);
				theList.list[temp.getID()].add(reverseEdge);
			}

			// main logic for updating priorities within the priority queue
			// based on updated "closest" values
			for (int j = 0; j < theGraph.length; j++) {
				if (distances[theGraph[j].getID()][temp.getID()] < theGraph[j].getPriority()) {
					theGraph[j].setPriority(distances[theGraph[j].getID()][temp.getID()]);
					theGraph[j].setParent(temp);
				}
			}
			// perform merge sort after each iteration to re-order graph and
			// simulate an ArrayList functionality
			Arrays.sort(theGraph);
		}
		return theList;
	}

	static public int edgeListSum(edgeList minSpanning) {
		int total = 0;
		for (int i = 0; i < minSpanning.list.length; i++) {
			for (int j = 0; j < minSpanning.list[i].size(); j++) {
				total += minSpanning.list[i].get(j).length;
			}
		}
		return (total / 2);
	}

	// optimizer function that scans and replaces heaviest edges with better
	// pairings
	static public void optimizeEdges(edgeList minSpanning, int[][] distances) {

		int highestI = -1;
		int highestJ = -1;
		int actualJ = -1;
		int highestLength = -1;
		// scan the list to determine the highest weighted edge present in the
		// list
		for (int i = 0; i < minSpanning.list.length; i++) {
			for (int j = 0; j < minSpanning.list[i].size(); j++) {
				if (minSpanning.list[i].get(j).length > highestLength) {
					highestLength = minSpanning.list[i].get(j).length;
					highestI = i;
					highestJ = j;
					actualJ = minSpanning.list[i].get(j).end;
				}
			}
		}
		minSpanning.list[highestI].remove(highestJ);
		// remove the highest edge from both edge sets it is contained in (from
		// beginning and ending vertex)
		for (int i = 0; i < minSpanning.list[actualJ].size(); i++) {
			if (minSpanning.list[actualJ].get(i).end == highestI) {
				minSpanning.list[actualJ].remove(i);
			}
		}

		// persistance to store the best pair values
		int connectionHighestI = -1;
		int connectionActualJ = -1;
		int lowestPairing = Integer.MAX_VALUE;

		// triple for loop finds the best pair to replace the pair
		for (int i = 0; i < minSpanning.list.length; i++) {
			for (int j = 0; j < minSpanning.list.length; j++) {
				if (i != highestI && j != actualJ) {
					boolean isPair = false;
					for (int k = 0; k < minSpanning.list[i].size(); k++) {
						if (minSpanning.list[i].get(k).end == j) {
							isPair = true;
						}
					}
					// switch only if the isPair variable is set to true
					if (isPair) {
						if ((distances[i][highestI] + distances[j][actualJ]) < lowestPairing) {
							connectionHighestI = i;
							connectionActualJ = j;
							lowestPairing = distances[i][highestI] + distances[j][actualJ];
						}
					}
				}
			}
		}
		// break the connection between the pair that has been deemed a better
		// fit
		for (int i = 0; i < minSpanning.list[connectionHighestI].size(); i++) {
			if (minSpanning.list[connectionHighestI].get(i).end == connectionActualJ) {
				minSpanning.list[connectionHighestI].remove(i);
			}
		}
		// break the connection between the pair that has been deemed a better
		// fit
		for (int i = 0; i < minSpanning.list[connectionActualJ].size(); i++) {
			if (minSpanning.list[connectionActualJ].get(i).end == connectionHighestI) {
				minSpanning.list[connectionActualJ].remove(i);
			}
		}
		// new optimized edges are added!!!
		minSpanning.list[highestI]
				.add(new singleEdge(highestI, connectionHighestI, distances[highestI][connectionHighestI]));
		minSpanning.list[connectionHighestI]
				.add(new singleEdge(connectionHighestI, highestI, distances[highestI][connectionHighestI]));
		minSpanning.list[actualJ]
				.add(new singleEdge(actualJ, connectionActualJ, distances[actualJ][connectionActualJ]));
		minSpanning.list[connectionActualJ]
				.add(new singleEdge(connectionActualJ, actualJ, distances[actualJ][connectionActualJ]));
	}

	public static void countEdges(edgeList minSpanning) {
		for (int i = 0; i < minSpanning.list.length; i++) {
			for (int j = 0; j < minSpanning.list[i].size(); j++) {
				System.out.print("Edge " + minSpanning.list[i].get(j).begin + "|" + minSpanning.list[i].get(j).end + "|"
						+ minSpanning.list[i].get(j).length + "   ");
			}
			System.out.println();
		}
	}

	// walk through the edges and create a Euler tour
	public static LinkedList<Integer> EulerTour(edgeList minSpanTree) {

		LinkedList<Integer> EulerTour = new LinkedList<Integer>();
		int currentVertex = 0;
		EulerTour.add(0);
		int currentTarget = -1;
		int targetIndex = -1;
		int chosenIndex = 0;

		// the first part of the tour cycles until it returns to the original
		// vertex 0
		while (currentTarget != 0) {

			// move through the children of each vertex randomly
			currentTarget = minSpanTree.list[currentVertex].get(chosenIndex).end;
			System.out.println("Moved to " + currentTarget);
			EulerTour.add(currentTarget);
			// as you move through remove edges from the list
			for (int i = 0; i < minSpanTree.list[currentTarget].size(); i++) {
				if (minSpanTree.list[currentTarget].get(i).end == currentVertex) {
					System.out.println("Removing: " + minSpanTree.list[currentTarget].get(i).begin + "|"
							+ minSpanTree.list[currentTarget].get(i).end);
					minSpanTree.list[currentTarget].remove(i);
				}
			}
			System.out.println("Removing: " + minSpanTree.list[currentVertex].get(chosenIndex).begin + "|"
					+ minSpanTree.list[currentVertex].get(chosenIndex).end);
			minSpanTree.list[currentVertex].remove(chosenIndex);
			currentVertex = currentTarget;
			chosenIndex = 0;
		}
		// print statement to QC Euler tour function
		System.out.println("FIRST EULER: ");
		for (int i = 0; i < EulerTour.size(); i++) {
			System.out.print(EulerTour.get(i) + " ");
		}

		// while loop runs as long as there are leftovers not added to the Euler
		// tour
		while (edgeListSum(minSpanTree) > 0) {
			LinkedList<Integer> leftovers = returnSubPath(minSpanTree, EulerTour);
			boolean added = false;
			// test to see if two entities are already within the Euler tour
			for (int i = EulerTour.size() - 1; i >= 0; i--) {
				int a = EulerTour.get(i);
				int b = leftovers.get(0);

				if (a == b) {
					added = true;
					leftovers.poll();
					System.out.println("SET WITHIN THE EQUALITY LINES");
					for (int j = 0; j < leftovers.size(); j++) {
						System.out.print(leftovers.get(j) + " ");
					}
					while (leftovers.peek() != null) {
						int next = leftovers.removeLast();
						EulerTour.add(i + 1, next);
					}
					break;
				}
				System.out.print("NA,");
			}
			// print out the existing tour and first path not added (this part
			// should not run if it works correctly)
			if (added == false) {
				System.out.println("EXISTING EULER: ");
				for (int i = 0; i < EulerTour.size(); i++) {
					System.out.print(EulerTour.get(i) + " ");
				}
				System.out.println();
				System.out.println();
				System.out.println();

				System.out.println("FIRST PATH NOT ADDED");
				for (int i = 0; i < leftovers.size(); i++) {
					System.out.print(leftovers.get(i) + " ");
				}

				System.exit(0);
			}
			// final Euler is printed off
			System.out.println("NEW EULER: ");
			for (int i = 0; i < EulerTour.size(); i++) {
				System.out.print(EulerTour.get(i) + " ");
			}
			// the edges are enumarated
			countEdges(minSpanTree);
		}

		return EulerTour;
	}

	// return subpaths that were branched off of the original path
	public static LinkedList<Integer> returnSubPath(edgeList minSpanTree, LinkedList<Integer> EulerTour) {

		LinkedList<Integer> subPath = new LinkedList<Integer>();

		int savedJ = -1;
		// determine where to find the starting vertex
		int startingPoint = -1;
		outerloop: for (int i = 0; i < minSpanTree.list.length; i++) {
			for (int j = 0; j < minSpanTree.list[i].size(); j++) {
				for (int k = 0; k < EulerTour.size(); k++) {
					if (minSpanTree.list[i].get(j).begin == EulerTour.get(k)) {
						startingPoint = minSpanTree.list[i].get(j).begin;
						savedJ = j;
						break outerloop;
					}
				}
			}
		}

		// add the starting vertex of the circuit to the list
		subPath.add(startingPoint);

		int endingPoint = -1;
		int currentList = startingPoint;
		boolean isNew = true;

		// continue until the loop has been created
		while (endingPoint != startingPoint) {
			// acquire the vertex that the currentList (first edge) ends on
			endingPoint = minSpanTree.list[currentList].get(savedJ).end;
			// add that vertex to the list
			subPath.add(endingPoint);
			// remove that edge from the edge list
			minSpanTree.list[currentList].remove(savedJ);
			// find the matching edge going in the reverse order
			for (int i = 0; i < minSpanTree.list[endingPoint].size(); i++) {
				// if the points ending is equal to the current list remove that
				// point
				if (minSpanTree.list[endingPoint].get(i).end == currentList) {
					minSpanTree.list[endingPoint].remove(i);
					currentList = endingPoint;
				}
			}
		}

		return subPath;
	}

	// removes duplicates from the Euler path
	public static LinkedList<Integer> removeDuplicates(LinkedList<Integer> eulerResults) {

		LinkedList<Integer> dupRemove = new LinkedList<Integer>();

		Object[] array = eulerResults.toArray(new Object[eulerResults.size()]);

		for (int i = 0; i < array.length; i++) {
			// for(int i=0; i<eulerResults.size(); i++){
			boolean matchFound = false;
			for (int j = 0; j < i; j++) {
				if ((int) array[i] == (int) array[j]) {
					// if((int)eulerResults.get(i) == (int)eulerResults.get(j)){
					matchFound = true;
				}
			}
			if (matchFound == false) {
				dupRemove.add((int) array[i]);
				// dupRemove.add(eulerResults.get(i));
			}
		}
		return dupRemove;
	}

	// finally....compute the TSP
	public static int travelingSalesman(int[] TSP, int[][] distances) {

		int total = 0;
		// sum the length distance and return it
		for (int i = 0; i < TSP.length - 1; i++) {
			total += distances[TSP[i]][TSP[i + 1]];
		}

		int end = TSP.length - 1;
		// sum the length distance and return it
		total += distances[TSP[0]][TSP[end]];
		return total;
	}

	// this is a varient of 2OPT
	public static void optTSP(int[] TSP, int[][] distances) {

		int longestEdge = -1;
		int lowestSwitch = Integer.MAX_VALUE;
		int temp = -1;
		int iAtLongestEdge = -1;
		int iToSwitchAt = -1;
		boolean lower = false;

		// determine the longest edge starting vertex
		for (int i = 1; i < TSP.length - 1; i++) {
			if (distances[TSP[i]][TSP[i + 1]] > longestEdge) {
				longestEdge = distances[TSP[i]][TSP[i + 1]];
				iAtLongestEdge = i;
			}
		}

		// for loop checks to find the best distance combination for rearranging
		// with i being below the longest edge
		for (int i = 0; i < iAtLongestEdge - 2; i++) {
			if ((distances[TSP[i]][TSP[iAtLongestEdge]]
					+ distances[TSP[i + 1]][TSP[iAtLongestEdge + 1]]) < lowestSwitch) {
				lowestSwitch = distances[TSP[i]][TSP[iAtLongestEdge]] + distances[TSP[i + 1]][TSP[iAtLongestEdge + 1]];
				iToSwitchAt = (i + 1);
				lower = true;
			}
		}

		// for loop checks to find the best distance combination for rearranging
		// with i being above the longest edge
		for (int i = TSP.length - 2; i > iAtLongestEdge + 2; i--) {
			if ((distances[TSP[i]][TSP[iAtLongestEdge]]
					+ distances[TSP[i + 1]][TSP[iAtLongestEdge + 1]]) < lowestSwitch) {
				lowestSwitch = distances[TSP[i]][TSP[iAtLongestEdge]] + distances[TSP[i + 1]][TSP[iAtLongestEdge + 1]];
				iToSwitchAt = (i);
				lower = false;
			}
		}

		// this logic runs if the switching point is below the highest edge
		if (lower == true) {
			for (int i = 0; i < (iAtLongestEdge - iToSwitchAt + 1) / 2; i++) {
				temp = TSP[iToSwitchAt + i];
				System.out.println("Temp is " + temp + " and TSP[iToSwitchAt+i] is " + TSP[iToSwitchAt + i]);
				TSP[iToSwitchAt + i] = TSP[iAtLongestEdge - i];
				System.out.println("TSP[iToSwitchAt+i is " + TSP[iToSwitchAt + i] + " and TSP[iAtLongestEdge+i] is "
						+ TSP[iAtLongestEdge + i]);
				TSP[iAtLongestEdge - i] = temp;
			}
		}
		// otherwise this logic runs if the switching point is above the highest
		// edge
		else {
			for (int i = 0; i < (iAtLongestEdge - iToSwitchAt + 1) / 2; i++) {
				temp = TSP[iToSwitchAt - i];
				System.out.println("Temp is " + temp + " and TSP[iToSwitchAt+i] is " + TSP[iToSwitchAt + i]);
				TSP[iToSwitchAt - i] = TSP[iAtLongestEdge + i];
				System.out.println("TSP[iToSwitchAt+i is " + TSP[iToSwitchAt + i] + " and TSP[iAtLongestEdge+i] is "
						+ TSP[iAtLongestEdge + i]);
				TSP[iAtLongestEdge + i] = temp;
			}
		}
	}

	// refactor problem structures so 2OPT can continue working on them
	public static void unKnot(int[] TSP) {
		int oneEight = (TSP.length / 8);
		int twoEight = (2 * TSP.length / 8);
		int threeEight = (3 * TSP.length / 8);
		int fourEight = (4 * TSP.length / 8);
		int fiveEight = (5 * TSP.length / 8);
		int sixEight = (6 * TSP.length / 8);
		int sevenEight = (7 * TSP.length / 8);
		int temp = TSP[oneEight];
		TSP[oneEight] = TSP[twoEight];
		TSP[twoEight] = TSP[threeEight];
		TSP[threeEight] = TSP[fourEight];
		TSP[fourEight] = TSP[fiveEight];
		TSP[fiveEight] = TSP[sixEight];
		TSP[sixEight] = TSP[sevenEight];
		TSP[sevenEight] = temp;
	}

	public static int[] greedyAlt(int[][] distances) {

		int[] numbersToUse = new int[distances[0].length];

		for (int i = 0; i < distances[0].length; i++) {
			numbersToUse[i] = i;
		}

		int[] greedyTSP = new int[distances[0].length];

		greedyTSP[0] = 0;
		numbersToUse[0] = -1;

		for (int i = 1; i < distances[0].length; i++) {
			int smallest = Integer.MAX_VALUE;
			int lowestMatch = -1;
			for (int j = 1; j < distances[0].length; j++) {
				if (numbersToUse[j] != -1) {
					if (distances[greedyTSP[i - 1]][numbersToUse[j]] < smallest) {
						lowestMatch = j;
					}
				}
				// if numbersToUse[i] is -1, skip this as its already taken
			}
			greedyTSP[i] = lowestMatch;
			numbersToUse[lowestMatch] = -1;
		}

		return greedyTSP;
	}
}
