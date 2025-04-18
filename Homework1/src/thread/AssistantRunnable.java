package thread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class AssistantRunnable implements Runnable{
    BlockingQueue<Student> blockingQueue;

    public AssistantRunnable(BlockingQueue<Student> blockingQueue){
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        Random rand = new Random();
        try {
            while (true) {
                Student student = blockingQueue.poll(5, TimeUnit.SECONDS); // ako je queue prazan, sacekace svakako 5 sekundi da se zavrsi, nista nece da radi drugo
                if (student == null) {
                    break; 
                }
                long currentTime = System.currentTimeMillis();
                if ((currentTime - startTime) > 5000) { // ako je proslo 5 sekundi
                    break; 
                }
                long defenseStartTime = System.currentTimeMillis();
                Thread.sleep((long) (student.defenseTime * 1000)); // simuliranje defence
                long defenseEndTime = System.currentTimeMillis();
                long defenseTime = defenseEndTime - defenseStartTime;
                if ((defenseEndTime - startTime) > 5000) { // ako neki student zapoceo odbranu a nije zavrsio na vreme
                    student.score = 5; 
                    Main.totalStudents.incrementAndGet();
                }
                else{
                    student.score = 5 + rand.nextInt(6);    
                    Main.totalScore.addAndGet(student.score);
                    Main.totalStudents.incrementAndGet();
                }
                System.out.println("Student: " + Main.totalStudents.get() + " Thread: " + Thread.currentThread().getName() + " Arrival: " + (defenseStartTime - startTime) + " Assistant: " + Thread.currentThread().getName() + " TTC: " + defenseTime + ":" + defenseStartTime + " Score: " + student.score);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
