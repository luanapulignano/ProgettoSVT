package backend.hobbiebackend.web;

import backend.hobbiebackend.model.entities.AppClient;
import backend.hobbiebackend.model.entities.Hobby;
import backend.hobbiebackend.model.entities.UserEntity;
import backend.hobbiebackend.model.entities.enums.UserRoleEnum;
import backend.hobbiebackend.service.HobbyService;
import backend.hobbiebackend.service.UserRoleService;
import backend.hobbiebackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class HomeController {
    private final HobbyService hobbyService;
    private final UserService userService;

    @Autowired
    public HomeController(HobbyService hobbyService,UserService userService) {
        this.hobbyService = hobbyService;
        this.userService=userService;
    }

    @GetMapping("/home")
    @Operation(summary = "Show client/business homepage", security = @SecurityRequirement(name = "bearerAuth"))
    public Set<Hobby> hobbiesShow(@RequestParam String username, @RequestParam String role) {
        if (role.equals("user")) {
            return this.hobbyService.getAllHobbieMatchesForClient(username);
        }
        return this.hobbyService.getAllHobbiesForBusiness(username);
    }


    @GetMapping("/hello-user")
    @Operation(summary = "Show client/business homepage", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> helloUser(@RequestParam String username) {
        Set<Hobby> hobbyMatches = this.hobbyService.getAllHobbieMatchesForClient(username);
        if (!hobbyMatches.equals(null)){
            StringBuilder responseBuilder = new StringBuilder("<h1>Hobby List:</h1>");
            for (Hobby hobby : hobbyMatches) {

                responseBuilder.append("<p>").append(hobby.getName()).append("</p>");
            }
            String htmlResponse = responseBuilder.toString();
            return ResponseEntity.ok().body(htmlResponse);
        }else{
            return null;
        }
    }

    @PostMapping("/meteo")
    public ResponseEntity<String> inviaURLMeteo(@RequestBody Map<String, String> payload) {
        String urlMeteo = payload.get("url");
        try{
        URL url = new URL(urlMeteo);
        InputStream inputStream = url.openStream();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
    
        
        StringBuilder response = new StringBuilder();
        String inputLine;
        
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        
        in.close();
        inputStream.close();
        return  ResponseEntity.ok().body(response.toString());
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Errore");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'elaborazione della richiesta: " + e.getMessage());
        }
    
    }
}
