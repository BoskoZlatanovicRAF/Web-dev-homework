package thread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class ProfessorRunnable implements Runnable{

    CyclicBarrier professorBarrier;
    BlockingQueue<Student> blockingQueue;

    public ProfessorRunnable(CyclicBarrier professorBarrier, BlockingQueue<Student> blockingQueue) {
        this.professorBarrier = professorBarrier;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            professorBarrier.await();
    
            while (true) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - startTime) > 5000) { // 5 sekundi pravilo
                    break;  
                }
                Student student = blockingQueue.poll(5, TimeUnit.SECONDS); // ako je queue prazan, sacekace svakako 5 sekundi da se zavrsi, nista nece da radi drugo
                if (student == null) {
                    break; 
                }
                long defenseStartTime = System.currentTimeMillis();
                Thread.sleep((long) (student.defenseTime * 1000)); // simuliranje defence tajmera
                long defenseEndTime = System.currentTimeMillis();
                long defenseTime = defenseEndTime - defenseStartTime;
                if ((defenseEndTime - startTime) > 5000) { // ako neki student zapoceo odbranu a nije zavrsio na vreme
                    student.score = 5; 
                    Main.totalStudents.incrementAndGet();
                } else {
                    student.score = 5 + new Random().nextInt(6); // random score between 5 and 10
                    Main.totalScore.addAndGet(student.score);
                    Main.totalStudents.incrementAndGet();
                }
                System.out.println("Student: " + Main.totalStudents.get()  + " Thread: " + Thread.currentThread().getName() + " Arrival: " + (defenseStartTime - startTime) + " Professor: " + Thread.currentThread().getName() + " TTC: " + defenseTime + ":" + defenseStartTime + " Score: " + student.score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
