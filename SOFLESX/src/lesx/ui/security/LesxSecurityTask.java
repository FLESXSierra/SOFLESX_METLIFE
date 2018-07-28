package lesx.ui.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import javafx.concurrent.Task;
import lesx.utils.LesxString;

public class LesxSecurityTask extends Task<Boolean> {

  private boolean first;

  public LesxSecurityTask(boolean first) {
    this.first = first;
  }

  @Override
  protected Boolean call() throws Exception {
    String fileName = System.getProperty("user.home") + "/startFlesx.txt";
    if (first) {
      try {
        // LMAO!!
        FileWriter myFile = new FileWriter(fileName);
        BufferedWriter out = new BufferedWriter(myFile);
        out.write("-Caps_XTeck: 1");
        out.newLine();
        out.write("-3D4nK_5_U_TC: true");
        out.newLine();
        out.write(LesxString.FILE_KEY_1 + ": 2");
        out.newLine();
        out.write("-Open_Files: false");
        out.newLine();
        out.write("-First_Time: false");
        out.newLine();
        out.write(LesxString.FILE_KEY_2 + ": -1");
        out.newLine();
        out.write("-Check_For_File: true");
        out.newLine();
        out.write("-Enable_Security: true");
        out.newLine();
        out.write("-File_Key: 2Ab&GkAllAE-7GH");
        out.flush();
        out.close();
      }
      catch (Exception e) {
        return false;
      }
    }
    else {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        Integer result = 0;
        while ((line = reader.readLine()) != null) {
          String key = line.split(":")[0].trim();
          String value = line.split(":")[1].trim();
          if (LesxString.FILE_KEY_1.equals(key) || LesxString.FILE_KEY_2.equals(key)) {
            result += Integer.valueOf(value);
          }
        }
        reader.close();
        return result == 1;
      }
      catch (Exception e) {
        return false;
      }
    }
    return true;
  }

}
