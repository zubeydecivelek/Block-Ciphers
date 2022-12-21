import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reading {

    public Inputs readFiles(String inputFile, String keyFile, String mode){

        ArrayList<String> keyArray = new ArrayList<>();
//        ArrayList<String> inputArray = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(keyFile));

            String line = bufferedReader.readLine();
            for (String i : line.split("-")){
                keyArray.add(i.trim());
            }

//            bufferedReader= new BufferedReader(new FileReader(inputFile));
//
//            line = bufferedReader.readLine();
//            inputArray.add(line);

        }catch (IOException io){
            io.printStackTrace();
        }


        Inputs inputs = new Inputs(keyArray.get(1), keyArray.get(0),keyArray.get(2));

//        if (mode.equals("e"))
//            inputs.setPlainTextString(inputArray.get(0));
//        else if (mode.equals("d"))
//            inputs.setCipherTextString(inputArray.get(0));

        return inputs;

    }
}


