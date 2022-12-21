import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class FileOperations {

    public static byte[] readInputFile(String fileName) throws IOException{
        Path path = Paths.get(fileName);
        byte[] data = Files.readAllBytes(path);
        return data;
    }

    public static Inputs readKeyFile(String fileName, String mode) throws IOException {
        ArrayList<String> keyArray = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

        String line = bufferedReader.readLine();
        for (String i : line.split("-")){
            keyArray.add(i.trim());
        }

        Inputs inputs = new Inputs(keyArray.get(1), keyArray.get(0),keyArray.get(2));

        return inputs;
    }

    public static void write(String fileName, byte[] data) throws IOException{
        Path path = Paths.get(fileName);
        Files.write(path, data);
    }

    public static void logFile(String input, String out, String op, String algo, String mode,  String time) throws IOException{
        String fileName = "run.log";
        String data = input + " " + out + " " + op + " " + algo + " " + mode + " " + time;
        File file = new File(fileName);

        if (file.exists()){
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write("\n" + data);
            fileWriter.close();
        } else {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(data);
            fileWriter.close();
        }

    }

}
