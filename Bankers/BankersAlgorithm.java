package Bankers;

import java.util.ArrayList;
import java.util.Scanner;

public class BankersAlgorithm {
	public static int[][] allocation = new int[5][3];
	public static int[][] maximum = new int[5][3];
	public static int[][] need = new int[5][3];
	public static int[][] available = new int[6][3];
	public static int[] availableTemp = new int[3];
	public static Boolean[] finishProcess = { false, false, false, false, false };

	static Scanner sc = new Scanner(System.in);

	public static void insertionVars() {

		// INPUT THE ALL THE NEEDED VARIABLES FOR EACH PROCESSOR
		for (int i = 0; i < 5; i++) {
			// FOR ALLOCATION INPUTS
			System.out.println("\t==========================");
			System.out.println("\t\t PROCESS " + i);
			System.out.print("\t ALLOCATION INPUTS: ");
			for (int j = 0; j < 3; j++) {
				int allocationInputs = sc.nextInt();
				allocation[i][j] = allocationInputs;
			}

			// FOR MAXIMUM INPUTS
			System.out.print("\t MAXIMUM INPUTS: ");

			for (int k = 0; k < 3; k++) {
				int maximumInputs = sc.nextInt();
				maximum[i][k] = maximumInputs;
			}

		}

		// ASKING THE USER THE THREE INSTANCES
		System.out.println("\t==========================");
		System.out.print("\t INPUT THREE INSTANCES: ");
		for (int i = 0; i < 3; i++) {
			int instanceInputs = sc.nextInt();
			availableTemp[i] = instanceInputs;
		}

	}

	public static void computationMethod() {

		// CREATING THE NEW "AVAILABLE" VALUES
		// ADDING ALL THE ALLOCATION IN EACH LETTER (A, B ,C)
		// LET I BE THE (A, B, C) AND J BE THE PROCESSORS
		for (int i = 0; i < 3; i++) {
			int sumAllocation = 0;
			for (int j = 0; j < 5; j++) {
				sumAllocation += allocation[j][i];
			}
			availableTemp[i] -= sumAllocation;
		}

		// CREATING THE "NEED" VALUES BY USING THE FORMULA (MAXIMUM - ALLOCATION)
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				need[i][j] = maximum[i][j] - allocation[i][j];
			}
		}

		// PASS THE AVAILABLE VALUES TO THE 2D AVAILABLE ARRAY TO DISPLAY IT LATER
		available[0] = availableTemp;
	}

	public static boolean isLessThanOrEqual(int[] a, int[] b) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] > b[i]) {
				return false;
			}
		}
		return true;
	}

	public static <T> boolean has(T[] arr, T o) {
		for (T n : arr) {
			if (n.equals(o))
				return true;
		}

		return false;
	}

	public static boolean all(int[] arr, int o) {
		for (int n : arr) {
			if (n != o) {
				return false;
			}
		}

		return true;
	}

	public static void realAlgorithm() {
		int next = 1;
		ArrayList<Integer> sequence = new ArrayList<>();
		do {
			for (int i = 0; i < finishProcess.length; i++) {
				// TO COMPARE IF THE NEED IS LESS THAN OR EQUAL TO THE AVAILABLE (WORK) NEED <=
				// WORK
				if (!finishProcess[i] && isLessThanOrEqual(need[i], availableTemp)) {
					sequence.add(i);
					for (int j = 0; j < availableTemp.length; j++) {
						available[next][j] = availableTemp[j] + allocation[i][j];
					}
					// PASSES IT TO THE TEMP SO THAT WE CAN USE IT IN THE LOOP AGAIN
					availableTemp = available[next++];
					finishProcess[i] = true;
					break;
				}
			}
		} while (has(finishProcess, false));

		System.out.println("PROCESS \tALLOCATIONS  \t\t MAXIMUM\t     AVAILABLE\t\t\t NEED");
		System.out.println(
				"\t       A     B     C  \t      A     B     C   \t   A     B     C  \t    A     B     C  ");

		for (int i = 0; i < finishProcess.length; i++) {
			System.out.print("  P" + i + "      ");
			showRows(allocation[i]);
			System.out.print("     ");
			showRows(maximum[i]);
			System.out.print("   ");
			showRows(available[i]);
			System.out.print("       ");
			showRows(need[i]);
			System.out.println("");
		}
		System.out.print("\t\t\t\t\t\t      ");
		showRows(available[5]);
		System.out.print("\nSafe state: ");
		for(int n:sequence) {
			System.out.print("P" + n + " ");
		}
	}

	public static void showRows(int[] nums) {
		for (int i = 0; i < nums.length; i++) {
			System.out.print("     " + nums[i]);
		}
	}

	public static void main(String[] args) {
		insertionVars();
		computationMethod();
		realAlgorithm();
	}
}