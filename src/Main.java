import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("vortex.in.sem.persons.bin")) {
            int byteRead;
            int enter=0;
            while ((byteRead = fis.read()) != -1) {
                System.out.print(byteRead + " ");
                enter++;
                if(enter==4){
                    System.out.println();
                    enter=0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 