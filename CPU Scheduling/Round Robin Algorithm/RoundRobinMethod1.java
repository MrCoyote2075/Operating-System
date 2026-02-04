import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RoundRobinMethod1 {
    static class Process {
        public String pname;
        public int arrivalTime;
        public int burstTime;
        public int completionTime;
        public int tournaroundTime;
        public int waitingTime;

        private static int counter = 0;

        public Process(int arrivalTime, int burstTime) {
            this.pname = "P" + ++counter;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;

            this.completionTime = 0;
            this.tournaroundTime = 0;
            this.waitingTime = 0;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("\nEnter the Time Quantum : ");
        int t = in.nextInt(); // time Quantum...

        System.out.print("\nEnter the number of Process : ");
        int n = in.nextInt();

        Process[] process = new Process[n];

        // Ready Queue...
        Queue<int[]> Q = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("\nArrival Time for P" + (i + 1) + " : ");
            int arrivalTime = in.nextInt();

            System.out.print("Burst  Time for P" + (i + 1) + " : ");
            int burstTime = in.nextInt();

            process[i] = new Process(arrivalTime, burstTime);
        }

        // sorting Based on the Arrival Time...
        Arrays.sort(process, (a, b) -> a.arrivalTime - b.arrivalTime);

        // initial Process..
        Q.offer(new int[] { 0, process[0].burstTime });

        int k = 1;
        int time = process[0].arrivalTime;

        while (!Q.isEmpty()) {
            int[] prc = Q.poll();

            // takes process that arrives on the current process's time quantum ends
            // and inserted to Ready Queue...
            while (k < n && process[k].arrivalTime <= (time + t))
                Q.offer(new int[] { k, process[k++].burstTime });

            // Handling the completing process...
            if (prc[1] <= t) {
                time += prc[1];
                process[prc[0]].completionTime = time;
            }

            // Handling the current process after the Time Quantum ends
            // then inserted to the Ready Queue ...
            else {
                time += t;
                prc[1] -= t;

                Q.offer(prc);
            }
        }

        for (Process ps : process) {

            ps.tournaroundTime = ps.completionTime - ps.arrivalTime;
            ps.waitingTime = ps.tournaroundTime - ps.burstTime;

        }

        System.out.println("-------------------------------------------\n");
        System.out.println("\nProcess Details....");
        for (Process ps : process) {

            System.out.println("\nProcess " + ps.pname + " : \n");
            System.out.println("Arival Time     : " + ps.arrivalTime);
            System.out.println("Burst Time      : " + ps.burstTime);
            System.out.println("Completion Time : " + ps.completionTime);
            System.out.println("Turnaround Time : " + ps.tournaroundTime);
            System.out.println("Waiting Time    : " + ps.waitingTime);
            System.out.println("____________________");

        }

        // Calculating the Average Turnaround Time...
        double averageTurnaroundTime = 0;

        // Calculating the Average Turnaround Time...
        double averageWaitingTime = 0;

        for (Process ps : process) {
            averageTurnaroundTime += ps.tournaroundTime;
            averageWaitingTime += ps.waitingTime;
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