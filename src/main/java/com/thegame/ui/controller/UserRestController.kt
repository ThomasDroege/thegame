package com.thegame.ui.controller

import com.thegame.business.model.User
import com.thegame.business.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserRestController(private  val userService: UserService) {

    @PostMapping("/add")
    fun addUser(@RequestBody user: User): String {
        userService.saveUser(user)
        return "User added"
    }

    @GetMapping("/getAll")
    fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }
}