package com.allstate.compozed.othello.controller;

import com.allstate.compozed.othello.domain.game.GameBoard;
import com.allstate.compozed.othello.domain.game.Row;
import com.allstate.compozed.othello.repository.GameBoardRepository;
import com.allstate.compozed.othello.repository.RowRepository;
import com.allstate.compozed.othello.util.EmailUtil;
import com.allstate.compozed.othello.domain.response.OthelloResponse;
import com.allstate.compozed.othello.domain.user.User;
import com.allstate.compozed.othello.repository.UserRepository;
import com.sendgrid.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by localadmin on 4/3/17.
 */

@RestController
@RequestMapping("/user")
public class OthelloController {

    UserRepository userRepository;

RowRepository rowRepository;
    GameBoardRepository gameBoardRepository;

    @Autowired
    public OthelloController(UserRepository userRepository, GameBoardRepository gameBoardRepository,RowRepository rowRepository)
    {
        this.userRepository = userRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.rowRepository = rowRepository;
    }

    @PostMapping("/")
    public User registerUser(@RequestBody User user) {

        User newUser = userRepository.save(user);

        try {
            EmailUtil.sendEmail("Welcome to Othello Team 4", "Thank you for registering for Othello made by Team 4", newUser.getEmailAddress());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return newUser;
    }

    @ResponseBody
    @PostMapping("/login/")
    public ResponseEntity<OthelloResponse> loginUser(@RequestBody User user) {


        OthelloResponse othelloResponse = new OthelloResponse();

        User loggedInUser = userRepository.findByEmailAddress(user.getEmailAddress());

        if(loggedInUser.getEmailAddress() == null) {
            othelloResponse.setEmailAddress(user.getEmailAddress());
            othelloResponse.setStatusCode(HttpStatus.NOT_FOUND);
            othelloResponse.setMessage("USER NOT FOUND");
        } else if (user.getPassword().equals(loggedInUser.getPassword())) {
            othelloResponse.setStatusCode(HttpStatus.OK);
            othelloResponse.setEmailAddress(loggedInUser.getEmailAddress());
        }
        else
        {
            othelloResponse.setEmailAddress(user.getEmailAddress());
            othelloResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(othelloResponse,othelloResponse.getStatusCode());

    }

    @ResponseBody
    @PostMapping("/recover/")
    public ResponseEntity<OthelloResponse> recoverPassword(@RequestBody User user) {

        User loggedInUser = userRepository.findByEmailAddress(user.getEmailAddress());
        OthelloResponse othelloResponse = new OthelloResponse();
        if (loggedInUser != null) {
            othelloResponse.setStatusCode(HttpStatus.OK);
            othelloResponse.setEmailAddress(loggedInUser.getEmailAddress());
            Response response = null;
            try {
                response = EmailUtil.sendEmail("Password Recover", String.format("Your password is below <br/>%n %s", loggedInUser.getPassword()), loggedInUser.getEmailAddress());
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
            if(response.statusCode == 202) {
                othelloResponse.setEmailAddress(user.getEmailAddress());
                othelloResponse.setStatusCode(HttpStatus.valueOf(response.statusCode));
                othelloResponse.setMessage("Password recovery email sent successfully.");
            } else {
                othelloResponse.setEmailAddress(user.getEmailAddress());
                othelloResponse.setStatusCode(HttpStatus.valueOf(response.statusCode));
                othelloResponse.setMessage("Password recover email failed to be sent.");
            }
        } else {
            othelloResponse.setEmailAddress(user.getEmailAddress());
            othelloResponse.setStatusCode(HttpStatus.NOT_FOUND);
            othelloResponse.setMessage("USER NOT FOUND");
        }

        return new ResponseEntity<>(othelloResponse,othelloResponse.getStatusCode());
    }

    @ResponseBody
    @PutMapping("/game/{id}/")
    public ResponseEntity<GameBoard> saveGameBoard(@RequestBody GameBoard gameBoard, @PathVariable Long id) {
        GameBoard oldGameBoard = gameBoardRepository.findOne(id);
        for (Row row : gameBoard.getRowList())
        {
            row.setGameBoard(oldGameBoard);
        }
        oldGameBoard.setRowList(gameBoard.getRowList());
        return new ResponseEntity<>(gameBoardRepository.save(oldGameBoard),HttpStatus.OK);
    }


    @ResponseBody
    @PostMapping("/{id}/game/")
    public ResponseEntity<GameBoard> createGameBoard(@PathVariable Long id) {

        User  user = userRepository.findOne(id);

        GameBoard gameBoard = new GameBoard();

        gameBoard.setUser(user);

        Row row = new Row();
        row.setRow("X,X,X,X,X,X,X,X");
        row.setGameBoard(gameBoard);
        gameBoard.getRowList().add(row);

        row = new Row();
        row.setRow("X,X,X,X,X,X,X,X");
        row.setGameBoard(gameBoard);
        gameBoard.getRowList().add(row);

        row = new Row();
        row.setRow("X,X,X,X,X,X,X,X");
        row.setGameBoard(gameBoard);
        gameBoard.getRowList().add(row);

        row = new Row();
        row.setRow("X,X,X,X,X,X,X,X");
        row.setGameBoard(gameBoard);
        gameBoard.getRowList().add(row);

        row = new Row();
        row.setRow("X,X,X,X,X,X,X,X");
        row.setGameBoard(gameBoard);
        gameBoard.getRowList().add(row);

        row = new Row();
        row.setRow("X,X,X,X,X,X,X,X");
        row.setGameBoard(gameBoard);
        gameBoard.getRowList().add(row);

        row = new Row();
        row.setRow("X,X,X,X,X,X,X,X");
        row.setGameBoard(gameBoard);
        gameBoard.getRowList().add(row);

        row = new Row();
        row.setRow("X,X,X,X,X,X,X,X");
        row.setGameBoard(gameBoard);
        gameBoard.getRowList().add(row);


        return new ResponseEntity<>(gameBoardRepository.save(gameBoard),HttpStatus.OK);

    }


}
