import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Read {

    public ArrayList<String> KeyArray = new ArrayList<>();
    public ArrayList<String> InputArray = new ArrayList<>();

    public ArrayList<String> readKeyFile(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("key_file"));

            String line = bufferedReader.readLine();
            for (String i : line.split("-")){
                KeyArray.add(i.trim());
            }
        }catch (IOException io){
            io.printStackTrace();
        }
        return KeyArray;
    }

    public ArrayList<String> readInputFile () {
        try{
            BufferedReader bf = new BufferedReader(new FileReader("input_text"));

            String line = bf.readLine();
            InputArray.add(line);
        } catch (IOException io){
            io.printStackTrace();
        }
        return InputArray;
    }
}
