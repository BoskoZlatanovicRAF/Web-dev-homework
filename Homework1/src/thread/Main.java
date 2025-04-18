package thread;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {


    public static AtomicInteger totalScore = new AtomicInteger(0);
    public static AtomicInteger totalStudents = new AtomicInteger(0);

    public static void main(String[] args) {
        int n;
        System.out.println("Unesite broj studenata koji brane: ");
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        scanner.close();
        Student[] students = new Student[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            double arrivalTime = rand.nextDouble();
            double defenseTime = 0.5 + (rand.nextDouble() * 0.5);

            students[i] = new Student(arrivalTime, defenseTime, 0, 0);
        }


        BlockingQueue<Student> studentQueue = new LinkedBlockingQueue<>();
        CyclicBarrier professorBarrier = new CyclicBarrier(2);



        Arrays.sort(students, Comparator.comparingDouble(o -> o.arrival));
        for (Student student : students) {
            try {
                studentQueue.put(student);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread professorThread = new Thread(new ProfessorRunnable(professorBarrier, studentQueue));
        Thread professorThread2 = new Thread(new ProfessorRunnable(professorBarrier, studentQueue));
        Thread assistantThread = new Thread(new AssistantRunnable(studentQueue));
        professorThread.start();
        professorThread2.start();
        assistantThread.start();

        try {
            professorThread.join();
            professorThread2.join();
            assistantThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("total students: " + (double) totalStudents.get());
        double averageScore =  totalScore.get() / (double) totalStudents.get();
        System.out.println("Average score je: " + averageScore);
    }

}
