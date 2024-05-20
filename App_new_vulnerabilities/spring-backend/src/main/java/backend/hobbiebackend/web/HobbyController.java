package backend.hobbiebackend.web;

import backend.hobbiebackend.model.dto.HobbyInfoDto;
import backend.hobbiebackend.model.dto.HobbyInfoUpdateDto;
import backend.hobbiebackend.model.entities.*;
import backend.hobbiebackend.service.CategoryService;
import backend.hobbiebackend.service.HobbyService;
import backend.hobbiebackend.service.LocationService;
import backend.hobbiebackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@RestController
@RequestMapping("/hobbies")
@CrossOrigin(origins = "http://localhost:4200")
public class HobbyController {
    private final HobbyService hobbyService;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public HobbyController(HobbyService hobbyService, CategoryService categoryService, LocationService locationService, UserService userService, ModelMapper modelMapper) {
        this.hobbyService = hobbyService;
        this.categoryService = categoryService;
        this.locationService = locationService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @Operation(summary = "Create new hobby", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> saveHobby(@RequestBody HobbyInfoDto info) {
        //info.setName(ValidationAndReplace(info.getName()));
        Hobby offer = this.modelMapper.map(info, Hobby.class);
        Category category = this.categoryService.findByName(info.getCategory());
        Location location = this.locationService.getLocationByName(info.getLocation());
        offer.setLocation(location);
        offer.setCategory(category);
        BusinessOwner business = this.userService.findBusinessByUsername(info.getCreator());
        Set<Hobby> hobby_offers = business.getHobby_offers();
        hobby_offers.add(offer);
        business.setHobby_offers(hobby_offers);
        this.hobbyService.createHobby(offer);
        this.userService.saveUpdatedUser(business);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    String ValidationAndReplace (String name){
        Pattern pattern = Pattern.compile("<script>");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            name="Hobbie_default_name";
        }
        name = name.replaceAll("-", "");
        return name;
    }
/*
    @PostMapping("/upload-images")
    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Save images", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> uploadImages(
            @RequestParam("profileImgUrl") MultipartFile profileImgUrl,
            @RequestParam("galleryImgUrl1") MultipartFile galleryImgUrl1,
            @RequestParam("galleryImgUrl2") MultipartFile galleryImgUrl2,
            @RequestParam("galleryImgUrl3") MultipartFile galleryImgUrl3) {
        try {
            // Salvare i file sul server
            saveImageLocally(profileImgUrl);
            saveImageLocally(galleryImgUrl1);
            saveImageLocally(galleryImgUrl2);
            saveImageLocally(galleryImgUrl3);
            return ResponseEntity.ok("Immagini salvate con successo");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel salvataggio delle immagini "+e);
        }
    }
    private void saveImageLocally(MultipartFile file) throws IOException {
        // Estrae il nome del file dal MultipartFile
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "/";
        Path uploadPath = Paths.get(uploadDir);
        try (InputStream inputStream = file.getInputStream()) {
            // Concatena il nome del file alla directory di upload per creare il percorso completo
            Path filePath = uploadPath.resolve(originalFilename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Impossibile salvare il file " + originalFilename, e);
        }
    }
*/

     @PostMapping("/comment")
    public Document receiveComment(@RequestBody String xmlPayload) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = builder.parse(new InputSource(new StringReader(xmlPayload)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return document;
    }
    
    @GetMapping(value = "/is-saved")
    @Operation(summary = "Show if hobby is saved in favorites", security = @SecurityRequirement(name = "bearerAuth"))
    public boolean isHobbySaved(@RequestParam Long id, @RequestParam String username) {
        return this.hobbyService.isHobbySaved(id, username);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Show hobby details", security = @SecurityRequirement(name = "bearerAuth"))
    public Hobby getHobbyDetails(@PathVariable Long id) {
        return this.hobbyService.findHobbieById(id);
    }


    @PostMapping("/save")
    @Operation(summary = "Save hobby in favorites", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Long> save(@RequestParam Long id, @RequestParam String username) {
        Hobby hobby = this.hobbyService.findHobbieById(id);
        boolean isSaved = this.hobbyService.saveHobbyForClient(hobby, username);
        if (!isSaved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Remove hobby from favorites", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Long> removeHobby(@RequestParam Long id, @RequestParam String username) {
        Hobby hobby = this.hobbyService.findHobbieById(id);
        boolean isRemoved = this.hobbyService.removeHobbyForClient(hobby, username);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }


    @PutMapping
    @Operation(summary = "Update hobby,(use existing hobby id)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> updateHobby(@RequestBody HobbyInfoUpdateDto info) throws Exception {
        Hobby offer = this.modelMapper.map(info, Hobby.class);
        Category category = this.categoryService.findByName(info.getCategory());
        Location location = this.locationService.getLocationByName(info.getLocation());
        offer.setLocation(location);
        offer.setCategory(category);
        this.hobbyService.saveUpdatedHobby(offer);
        return new ResponseEntity<Hobby>(offer, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete hobby", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Long> deleteHobby(@PathVariable Long id) throws Exception {
        boolean isRemoved = this.hobbyService.deleteHobby(id);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/saved")
    @Operation(summary = "Show hobbies that are saved in favorites", security = @SecurityRequirement(name = "bearerAuth"))
    public List<Hobby> savedHobbies(@RequestParam String username) {
        AppClient appClient = this.userService.findAppClientByUsername(username);
        return this.hobbyService.findSavedHobbies(appClient);

    }
}

