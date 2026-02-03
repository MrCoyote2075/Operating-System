import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("\nEnter the Time Quantum : ");
        int t = in.nextInt(); // time Quantum...

        System.out.print("\nEnter the number of Process : ");
        int n = in.nextInt();

        int[][] process = new int[n][5];

        // Ready Queue...
        Queue<int[]> Q = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("\nArival Time for P" + (i + 1) + " : ");
            int arivalTime = in.nextInt();

            System.out.print("Burst  Time for P" + (i + 1) + " : ");
            int burstTime = in.nextInt();

            process[i][0] = arivalTime; // default 0...
            process[i][1] = burstTime;
            process[i][2] = 0; // Completion Time...
            process[i][3] = 0; // Tournaround Time...
            process[i][4] = 0; // Waiting Time...

        }

        // sorting Based on the Arrival Time...
        Arrays.sort(process, (a, b) -> a[0] - b[0]);

        // initial Process..
        Q.offer(new int[] { 0, process[0][1] });

        int k = 1;
        int time = process[0][0];

        while (!Q.isEmpty()) {
            int[] prc = Q.poll();

            // takes process that arrives on the current process's time quantum ends
            // and inserted to Ready Queue...
            while (k < n && process[k][0] <= (time + t))
                Q.offer(new int[] { k, process[k++][1] });

            // Handling the completing process...
            if (prc[1] <= t) {
                time += prc[1];
                process[prc[0]][2] = time;
            }

            // Handling the current process after the Time Quantum ends
            // then inserted to the Ready Queue ...
            else {
                time += t;
                prc[1] -= t;

                Q.offer(prc);
            }
        }

        for (int ps[] : process) {

            int arivalTime = ps[0];
            int burstTime = ps[1];
            int completionTime = ps[2];

            int tournaroundTime = completionTime - arivalTime;
            int waitingTime = tournaroundTime - burstTime;

            ps[3] = tournaroundTime;
            ps[4] = waitingTime;
        }

        System.out.println("-------------------------------------------\n");
        System.out.println("\nProcess Details....");
        for (int i = 0; i < n; i++) {
            int[] ps = process[i];

            System.out.println("\nProcess-" + (i + 1) + " : \n");
            System.out.println("Arival Time     : " + ps[0]);
            System.out.println("Burst Time      : " + ps[1]);
            System.out.println("Completion Time : " + ps[2]);
            System.out.println("Turnaround Time : " + ps[3]);
            System.out.println("Waiting Time    : " + ps[4]);
            System.out.println("____________________");
        }

        double averageTurnaroundTime = 0;
        double averageWaitingTime = 0;

        for (int ps[] : process) {
            averageTurnaroundTime += ps[3];
            averageWaitingTime += ps[4];
        }

        averageTurnaroundTime /= (double) n;
        averageWaitingTime /= (double) n;

        System.out.println("\n\nAverage Details...\n");
        System.out.printf("Average Turnaround Time : %.2f", averageTurnaroundTime);
        System.out.printf("\nAverage  Waiting   Time : %.2f", averageWaitingTime);

        System.out.println("\n\n\n-----------------------------------------------------------");
        in.close();
    }
}

/*
 * Sample:
 * 
 * PS, (AT), (BT),
 * P1, 0 ms, 5 ms,
 * P2, 4 ms, 2 ms,
 * P3, 5 ms, 4 ms,
 * 
 * Step-by-Step Execution:
 * 
 * 1. Time 0-2 (P1 Executes):
 * P1 starts execution as it arrives at 0 ms.
 * Runs for 2 ms; remaining burst time = 5 - 2 = 3 ms.
 * Ready Queue: [P1].
 * 
 * 2. Time 2-4 (P1 Executes Again):
 * P1 continues execution since no other process has arrived yet.
 * Runs for 2 ms; remaining burst time = 3 - 2 = 1 ms.
 * P2 arrive at 4 ms.
 * Ready Queue: [P2, P1].
 * 
 * 3. Time 4-6 (P2 Executes):
 * P2 starts execution as it arrives at 4 ms.
 * Runs for 2 ms; remaining burst time = 2 - 2 = 0 ms.
 * P3 arrive at 5ms
 * Ready Queue: [P1, P3].
 * 
 * 4. Time 6-7 (P1 Executes):
 * P1 starts execution.
 * Runs for 1 ms; remaining burst time = 1 - 1 = 0 ms.
 * Ready Queue: [P3].
 * 
 * 5. Time 7-9 (P3 Executes):
 * P3 starts execution.
 * Remaining burst time = 4 - 2 = 2 ms.
 * Ready Queue: [P3].
 * 
 * 6. Time 9-11 (P3 Executes Again):
 * P3 resumes execution and runs for 2 ms and complete its execution
 * Remaining burst time = 2 - 2 = 0 ms.
 * Ready Queue: [].
 * 
 * 
 * Now, lets calculate average waiting time and turn around time:
 * 
 * Turnaround Time (TAT = CT - AT)
 * Waiting Time (WT = TAT - BT)
 * 
 * PS, (CT), (TAT), (WT)
 * P1, 7 ms, 7 ms, 2 ms
 * P2, 6 ms, 2 ms, 0 ms
 * P3, 11 ms, 6 ms, 2 ms
 * 
 * 
 * Average Turn around time =
 * ((7 + 2 + 6) / 3) ​= (15 / 3) = 5 ms
 * 
 * Average waiting time =
 * ((2 + 0 + 2) / 3) = (4 / 3) = 1.33 ms
 */