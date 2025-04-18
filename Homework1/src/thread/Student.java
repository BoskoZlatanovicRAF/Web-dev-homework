package thread;

public class Student {

    int score;
    double arrival;
    double defenseTime;
    int instructorId;

    public Student(double arrival, double defenseTime, int instructorId, int score) {
        this.arrival = arrival;
        this.defenseTime = defenseTime;
        this.instructorId = instructorId;
        this.score = score;
    }

}
