package com.learner20.javacompletablefuture.controller;

import com.learner20.javacompletablefuture.entity.User;
import com.learner20.javacompletablefuture.service.UserService;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping( value = "/users",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files")MultipartFile[] files) throws Exception {
        for(MultipartFile file: files){
            userService.saveUser(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value="/users" , produces="application/json")
    public CompletableFuture<ResponseEntity> findAllUsers(){
        return userService.findAllUsers().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "getUsersByThread", produces = "application/json")
    public ResponseEntity getUsers(){
        long start=System.currentTimeMillis();
        List<CompletableFuture<List<User>>> requests=new ArrayList<>();
        for(int i=0;i<200;i++){
            CompletableFuture<List<User>> user=userService.findAllUsers();
            requests.add(user);
        }
        CompletableFuture.allOf(requests.toArray(new CompletableFuture[requests.size()])).join();
        long end=System.currentTimeMillis();
        System.out.println("time taken is :-"+(end-start));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
