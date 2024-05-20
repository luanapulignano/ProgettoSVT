package backend.hobbiebackend.web;

import backend.hobbiebackend.model.entities.Test;
import backend.hobbiebackend.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }
    
    @PostMapping("/test")
    @Operation(summary = "Save test results", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> saveTestResults(@RequestBody Test results) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String username = authentication.getName();
    	if (username.equals(results.getUsername())){
    		this.testService.saveTestResults(results);
    		return new ResponseEntity<>(HttpStatus.CREATED);
    		}
		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

}
