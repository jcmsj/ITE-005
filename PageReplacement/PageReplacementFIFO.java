package PageReplacement;
import java.util.LinkedList;
import java.util.Scanner;

public class PageReplacementFIFO {
	final static Scanner sc = new Scanner(System.in);
	static LinkedList<Integer> referenceString = new LinkedList<>();
	static LinkedList<Integer> currentPage = new LinkedList<>();
	static Integer[][] frames = {};
	static int columnIndex = 0;
	static int pageSize;
	static int frameSize;
	static int targetCount;

	static void userInput() {
        System.out.print("Enter number of frames: ");
		frameSize = sc.nextInt();
		targetCount = frameSize - 1;
        System.out.print("Enter number of pages: ");
		pageSize = sc.nextInt();

		// INITIALIZE INTEGER ARRAY
		frames = new Integer[frameSize][pageSize + 1];
		System.out.print("Enter reference string (" + pageSize + " inputs): ");

		for (int i = 0; i < pageSize; i++) {
			referenceString.add(sc.nextInt());
		}
	}


	static void counterTracker() {
		if (columnIndex != targetCount) {
			columnIndex++;
		} else {
			columnIndex = 0;
		}
	}

	static void mainAlgorithm() {
		int faults = 0;
		int hits = 0;
		/* Keep getting the head of LL */
		for (int i = 0; i < referenceString.size(); i++) {
			Integer n = referenceString.get(i);
			if(currentPage.contains(n)) {
				hits++;
			} else {
				if (currentPage.size() == frameSize) {
					currentPage.removeFirst();
				}
				currentPage.offer(n);	
				faults++;
			}
			// Limit LL size by frame size
			for (int k = 0; k < currentPage.size(); k++) {
				frames[k][columnIndex] = currentPage.get(k);
			}
			columnIndex++;
		}
			
        System.out.println("FIFO Page Replacement Process:");

		System.out.print("   ");
		for (int n: referenceString) {
			System.out.print(n + "  ");
		}
		System.out.println();
		for (int i = 0; i < frameSize; i++) {
			System.out.print("F" + (i + 1) + " ");
			for (int j = 0; j < pageSize; j++) {
				if (frames[i][j] == null) {
                    System.out.print("   ");
                } else {
                    System.out.print(frames[i][j] + "  ");
                }
			}

			System.out.print("\n");
		}

		System.out.println("Hits = " + hits + "\nFaults = " + faults);
	}

	public static void main(String[] args) {
		userInput();
		mainAlgorithm();
	}
}