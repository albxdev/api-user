    package com.emazon.users.adapter.inbound.rest;

    import io.swagger.v3.oas.annotations.Operation;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import com.emazon.users.application.dto.UserDTO;
    import com.emazon.users.application.service.UserService;
    import com.emazon.users.domain.exception.EmailAlreadyExistsException;
    import com.emazon.users.domain.exception.ValidationException;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import jakarta.validation.Valid;


    @RestController
    @RequestMapping("/api/users")
    public class UserController {

        private static final Logger log = LoggerFactory.getLogger(UserController.class);

        private final UserService userService;

        @Autowired
        public UserController(UserService userService) {
            this.userService = userService;
        }

        // Endpoint para crear un usuario auxiliar de bodega
        @Operation(summary="")
        @PostMapping("/create-aux-bodega")
        public ResponseEntity<UserDTO> createAuxBodegaUser(@Valid @RequestBody UserDTO userDTO) { //
            log.debug("Received request to createAuxBodegaUser with data: {}", userDTO);
            try {
                UserDTO createdUser = userService.createAuxBodegaUser(userDTO);
                log.debug("User created successfully: {}", createdUser);
                return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {
                log.error("Error creating user: {}", e.getMessage());
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }

        // anotacion api responses y response.

       // @PostMapping("/create-user")
        //public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
            //try {
               // UserDTO createdUser = userService.createUser(userDTO);
               // return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
          //  } catch (EmailAlreadyExistsException e) {
             //   return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
           // } catch (Exception e) {
              //  return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
          //  }
       // }


    }
