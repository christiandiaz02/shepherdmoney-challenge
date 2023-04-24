package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.service.UserService;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    // wire in the user repository (~ 1 line)
    private UserService userService;
    @Autowired
    private UserController(UserService theUserService) {
        userService = theUserService;
    }


    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        // Creates a user entity with information given in the payload, stores it in the database
        // and returns the id of the user in 200 OK response

        // Initialize User instance to pass into repository
        User newUser = new User();
        newUser.setName(payload.getName());
        newUser.setEmail(payload.getEmail());

        // Saves the new user into the database
        User user = userService.save(newUser);
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // Return 200 OK if a user with the given ID exists, and the deletion is successful
        // Return 400 Bad Request if a user with the ID does not exist
        // The response body could be anything you consider appropriate
        User user = userService.findById(userId);

        if (user == null) {
            return new ResponseEntity<>("The user with ID " + userId + " does not exist",
                    HttpStatus.BAD_REQUEST);
        }

        userService.deleteById(userId);
        return new ResponseEntity<>("User " + userId + " successfully deleted", HttpStatus.OK);
    }
}
