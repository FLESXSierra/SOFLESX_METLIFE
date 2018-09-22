package lesx.ui.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;

import javafx.concurrent.Task;
import lesx.utils.LesxString;

public class LesxSecurityTask extends Task<Boolean> {

  private long creation = 0;

  public LesxSecurityTask() {
    // Nothing
  }

  @Override
  protected Boolean call() throws Exception {
    File file = new File(LesxString.SETTINGS_FILE_PATH);
    if (file.exists()) {
      BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
      creation = attr.creationTime()
          .toMillis();
      return readFile();
    }
    else {
      createFile(false);
      return false;
    }
  }

  private void createFile(boolean success) {
    try {
      // LMAO!!
      FileWriter myFile = new FileWriter(LesxString.SETTINGS_FILE_PATH);
      BufferedWriter out = new BufferedWriter(myFile);
      out.write("-Caps_XTeck: 1");
      out.newLine();
      out.write("-3D4nK_5_U_TC: true");
      out.newLine();
      out.write(LesxString.FILE_KEY_1 + ": 1");
      out.newLine();
      out.write("-Open_Files: false");
      out.newLine();
      out.write("-First_Time: false");
      out.newLine();
      out.write(LesxString.FILE_KEY_2 + ": 0");
      out.newLine();
      out.write("-Check_For_File: true");
      out.newLine();
      out.write("-Enable_Security: true");
      out.newLine();
      out.write(LesxString.FILE_KEY_3 + ": false");
      out.newLine();
      if (success) {
        out.write(LesxString.FILE_KEY_4 + ": " + creation);
      }
      else {
        out.write(LesxString.FILE_KEY_4 + ": " + LocalDateTime.now()
            .getNano());
      }
      out.newLine();
      out.write("-File_Key: 2Ab&GkAllAE-7GH");
      out.flush();
      out.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Boolean readFile() {
    try {
      Integer result = 0;
      boolean verify = false;
      BufferedReader reader = new BufferedReader(new FileReader(LesxString.SETTINGS_FILE_PATH));
      String line;
      while ((line = reader.readLine()) != null) {
        String key = line.split(":")[0].trim();
        String value = line.split(":")[1].trim();
        if (LesxString.FILE_KEY_3.equals(key)) {
          if (Boolean.FALSE.equals(Boolean.valueOf(value))) {
            verify = true;
          }
        }
        if (LesxString.FILE_KEY_1.equals(key) || LesxString.FILE_KEY_2.equals(key)) {
          result += Integer.valueOf(value);
        }
        if (verify && LesxString.FILE_KEY_4.equals(key)) {
          if (veryfySuccess(value)) {
            reader.close();
            return true;
          }
          else {
            reader.close();
            createFile(false);
            return false;
          }
        }
      }
      reader.close();
      boolean success = result == 25;
      createFile(success);
      return success;
    }
    catch (Exception e) {
      return false;
    }
  }

  private boolean veryfySuccess(String value) {
    return creation == Long.valueOf(value);
  }

}
