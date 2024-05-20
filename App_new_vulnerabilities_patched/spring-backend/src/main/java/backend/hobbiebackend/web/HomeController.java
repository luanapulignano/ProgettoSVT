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
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class HomeController {
    private final HobbyService hobbyService;
    private final UserService userService;

    private static final PolicyFactory POLICY_FACTORY = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
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
    //XSS stored fixed con il sanitazer OWASP
    @GetMapping("/hello-user")
    @Operation(summary = "Show client/business homepage", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> helloUser(@RequestParam String username) {
        Set<Hobby> hobbyMatches = this.hobbyService.getAllHobbieMatchesForClient(username);
        if (hobbyMatches != null) {
            StringBuilder responseBuilder = new StringBuilder("<h1>Hobby List:</h1>");
            for (Hobby hobby : hobbyMatches) {
                String sanitizedHobbyName = POLICY_FACTORY.sanitize(hobby.getName());
                responseBuilder.append("<p>").append(sanitizedHobbyName).append("</p>");
            }
            String htmlResponse = responseBuilder.toString();
            return ResponseEntity.ok().body(htmlResponse);
        } else {
            return ResponseEntity.ok().body("<h1>No hobbies found</h1>");
        }
    }

    /* Attacco XSS stored, vulnerabilità introdotta da noi
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
    */
@PostMapping("/meteo")
public ResponseEntity<String> inviaURLMeteo(@RequestBody Map<String, String> payload) {
    String urlMeteo = payload.get("url");
    List<String> allowedDomains = Arrays.asList("api.openweathermap.org");
    
    try {
        URI uri = new URI(urlMeteo);
        String host = uri.getHost();
        
        if (!allowedDomains.contains(host)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Dominio non consentito.");
        }
        
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
        return ResponseEntity.ok().body(response.toString());
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'elaborazione della richiesta: " + e.getMessage());
    }
    }
}
