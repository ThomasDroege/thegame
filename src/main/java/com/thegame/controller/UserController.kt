package com.thegame.controller

import com.thegame.model.User
import com.thegame.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private  val userService: UserService) {

    @PostMapping("/add")
    fun addUser(@RequestBody user:User): String {
        userService.saveUser(user)
        return "User added"
    }

    @GetMapping("/getAll")
    fun getAllUser(): List<User> {
        return userService.getAllUsers()
    }
}