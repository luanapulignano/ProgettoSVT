package backend.hobbiebackend.security;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PasswordManager {
	
    public List<String> readPasswordFile() {
        List<String> passwords = new ArrayList<>();
        try {
        FileInputStream fis = new FileInputStream("password.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

        String line;
        while ((line = reader.readLine()) != null) {
            passwords.add(line);
        }

        reader.close();
        return passwords;
        } catch (Exception e) {
        	System.out.println("Reading file error");
        	return passwords;
        }
    }
}
