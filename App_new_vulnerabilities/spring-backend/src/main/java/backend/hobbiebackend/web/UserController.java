package backend.hobbiebackend.web;

import backend.hobbiebackend.handler.NotFoundException;
import backend.hobbiebackend.model.dto.AppClientSignUpDto;
import backend.hobbiebackend.model.dto.BusinessRegisterDto;
import backend.hobbiebackend.model.dto.UpdateAppClientDto;
import backend.hobbiebackend.model.dto.UpdateBusinessDto;
import backend.hobbiebackend.model.entities.AppClient;
import backend.hobbiebackend.model.entities.BusinessOwner;
import backend.hobbiebackend.model.entities.UserEntity;
import backend.hobbiebackend.model.entities.enums.UserRoleEnum;
import backend.hobbiebackend.model.jwt.JwtRequest;
import backend.hobbiebackend.model.jwt.JwtResponse;
import backend.hobbiebackend.security.HobbieUserDetailsService;
import backend.hobbiebackend.service.NotificationService;
import backend.hobbiebackend.service.UserService;
import backend.hobbiebackend.utility.JWTUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final JWTUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    private final HobbieUserDetailsService hobbieUserDetailsService;
    //public static final Logger LOGGER = LogManager.getLogger(UserController.class);
    @Autowired
    private DataSource dataSource;


    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, NotificationService notificationService, JWTUtility jwtUtility, AuthenticationManager authenticationManager, HobbieUserDetailsService hobbieUserDetailsService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
        this.hobbieUserDetailsService = hobbieUserDetailsService;
    }
    /*
    @PostMapping("/signup")
    @Operation(summary = "Create new client-user")
    public ResponseEntity<?> signup(@RequestBody AppClientSignUpDto user) {
        System.out.println(user);
        if (this.userService.userExists(user.getUsername(), user.getEmail())) {
            throw new RuntimeException("Username or email address already in use.");
        }
        AppClient client = this.userService.register(user);
        return new ResponseEntity<AppClient>(client, HttpStatus.CREATED);
    }*/
    /*
    @GetMapping("/images/{profileImageUrl}")
    public ResponseEntity<byte[]> getImage(@PathVariable String profileImageUrl) {
        try {
            System.out.println(profileImageUrl);
            File file = ResourceUtils.getFile("/images/"+profileImageUrl);
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } catch (IOException e) {
            // Gestisci eventuali errori
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    */
    @PostMapping("/updateCredit")
    @Operation(summary = "Update user credit")
    public ResponseEntity<?> updateCredit(@RequestBody String base64Encoded) {
        try {
            System.out.println(base64Encoded);
            byte[] decodedBytes = Base64.getDecoder().decode(base64Encoded);
            String serializedObject = new String(decodedBytes, StandardCharsets.UTF_8);
            System.out.println(serializedObject);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(serializedObject);
            int credit = jsonNode.get("credit").asInt();
            long id = jsonNode.get("UserId").asLong();
            AppClient client = this.userService.findAppClientById(id);
            client.setCredit(credit);
            this.userService.saveUpdatedUserClient(client);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating credit");
        }
    }

    @GetMapping("/images")
    public ResponseEntity<byte[]> getImage(@RequestParam("profileImageName") String profileImageUrl) {
        try {
            System.out.println(profileImageUrl);
            File file = ResourceUtils.getFile("/images/" + profileImageUrl);
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            System.out.println(imageBytes);
            return ResponseEntity.ok().body(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signup")
    @Operation(summary = "Create new client-user")
    public ResponseEntity<?> signup(@ModelAttribute AppClientSignUpDto user) {
        try {
            String profileImageUrl = saveProfileImage(user.getProfileImage());
    
            // Creazione dell'utente con l'URL dell'immagine di profilo
            user.setProfileImageUrl(profileImageUrl);
    
            if (this.userService.userExists(user.getUsername(), user.getEmail())) {
                throw new RuntimeException("Username or email address already in use.");
            }

            AppClient client = this.userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(client);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while signing up");
        }
    }

    
    private String saveProfileImage(MultipartFile profileImage) throws IOException {
 
        String uploadDir = "/images/";
    
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        String fileName = profileImage.getOriginalFilename();
        String filePath = uploadDir + File.separator + fileName;
    
        File dest = new File(filePath);
        profileImage.transferTo(dest);
    
        
        return fileName;
    }
    
    @PostMapping("/register")
    @Operation(summary = "Create new business-user")
    public ResponseEntity<?> registerBusiness(@RequestBody BusinessRegisterDto business) {
        if (this.userService.businessExists(business.getBusinessName()) || this.userService.userExists(business.getUsername(), business.getEmail())) {
            throw new RuntimeException("Username or email address already in use.");
        }
        BusinessOwner businessOwner = this.userService.registerBusiness(business);
        return new ResponseEntity<BusinessOwner>(businessOwner, HttpStatus.CREATED);
    }


    @GetMapping("/client")
    @Operation(summary = "show client-user information", security = @SecurityRequirement(name = "bearerAuth"))
    public AppClient showUserDetails(@RequestParam long id) {
        return this.userService.findAppClientById(id);
    }

    @GetMapping("/business")
    @Operation(summary = "Show business-user information", security = @SecurityRequirement(name = "bearerAuth"))
    public BusinessOwner showBusinessDetails(@RequestParam String username) {
        return this.userService.findBusinessByUsername(username);
    }

    @PutMapping("/user")
    @Operation(summary = "Update client-user information (use existing user id)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> updateUser(@RequestBody UpdateAppClientDto user) {
        AppClient client = this.userService.findAppClientById(user.getId());
        client.setPassword(user.getPassword());
        client.setGender(user.getGender());
        client.setFullName(user.getFullName());
        this.userService.saveUpdatedUserClient(client);
        return new ResponseEntity<AppClient>(client, HttpStatus.CREATED);
    }

    @PostMapping("/notification")
    @Operation(summary = "Send notification with password reset link", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> sendNotification(@RequestParam("email") String e) {
        UserEntity userByEmail = this.userService.findUserByEmail(e);
        if (userByEmail == null) {
            throw new NotFoundException("User not found");
        } else {
            this.notificationService.sendNotification(userByEmail);
        }
        return new ResponseEntity<>(userByEmail, HttpStatus.OK);
    }
    
    @PutMapping("/password")
    @Operation(summary = "Update password, (use existing user id)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> setUpNewPassword(@RequestParam Long id, String password) {
        UserEntity userById = this.userService.findUserById(id);
        userById.setPassword(password);
        this.userService.saveUserWithUpdatedPassword(userById);
        return new ResponseEntity<UserEntity>(userById, HttpStatus.CREATED);
    }

    @PutMapping("/business")
    @Operation(summary = "Update business-user, (use existing user id)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> updateBusiness(@RequestBody UpdateBusinessDto business) {
        BusinessOwner businessOwner = this.userService.findBusinessOwnerById(business.getId());
        if (this.userService.businessExists(business.getBusinessName()) && (!businessOwner.getBusinessName().equals(business.getBusinessName()))) {
            throw new RuntimeException("Business name already in use.");
        }
        businessOwner.setBusinessName(business.getBusinessName());
        businessOwner.setPassword(business.getPassword());
        businessOwner.setAddress(business.getAddress());
        this.userService.saveUpdatedUser(businessOwner);

        return new ResponseEntity<BusinessOwner>(businessOwner, HttpStatus.CREATED);
    }




    @DeleteMapping("/user/{id}")
    @Operation(summary = "Delete user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Long> deleteUser(@PathVariable Long id) {
        boolean isRemoved = this.userService.deleteUser(id);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
/*
    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate user and get JWT Token")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails
                = hobbieUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);
        return new JwtResponse(token);
    }
*/  

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            //LOGGER.info("user id: " + jwtRequest.getUsername());
        	String query = "SELECT * FROM users WHERE username = '" + jwtRequest.getUsername() + "' AND password = '" + jwtRequest.getPassword()+"'";
        	Statement statement = connection.createStatement();
            System.out.println(jwtRequest.getPassword());
            System.out.println(jwtRequest.getUsername());
            try (ResultSet resultSet = statement.executeQuery(query)) {
                if (resultSet.next()) {
                    final UserDetails userDetails
                    = hobbieUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
                    String token = jwtUtility.generateToken(userDetails);
                    return new JwtResponse(token);
                } else {
                    throw new Exception("INVALID_CREDENTIALS");
                }
            }
    }
    }

    class UserLoginResponse {
        private String role;
        private long id;
    
        public UserLoginResponse(String role, long id) {
            this.role = role;
            this.id = id;
        }
    
        public String getRole() {
            return role;
        }
    
        public long getId() {
            return id;
        }
    }

    
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Login based on user role after authentication", security = @SecurityRequirement(name = "bearerAuth"))
    public UserLoginResponse logInUser(@RequestParam String username) {
        UserEntity userByUsername = this.userService.findUserByUsername(username);
        if (userByUsername.getRoles().stream()
                .anyMatch(u -> u.getRole().equals(UserRoleEnum.USER))) {
                    long id = userByUsername.getId();
                    UserLoginResponse ULR = new UserLoginResponse("USER",id);
            return ULR;
        } else if (userByUsername.getRoles().stream()
                .anyMatch(u -> u.getRole().equals(UserRoleEnum.BUSINESS_USER))) {
                    long id = userByUsername.getId();
                    UserLoginResponse ULR = new UserLoginResponse("BUSINESS_USER",id);
            return ULR;
        }
        return null;
    }
    /*
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Login based on user role after authentication", security = @SecurityRequirement(name = "bearerAuth"))
    public String logInUser(@RequestParam String username) {
        UserEntity userByUsername = this.userService.findUserByUsername(username);
        if (userByUsername.getRoles().stream()
                .anyMatch(u -> u.getRole().equals(UserRoleEnum.USER))) {
            return "USER";
        } else if (userByUsername.getRoles().stream()
                .anyMatch(u -> u.getRole().equals(UserRoleEnum.BUSINESS_USER))) {
            return "BUSINESS_USER";
        }
        return null;
    }*/
}


