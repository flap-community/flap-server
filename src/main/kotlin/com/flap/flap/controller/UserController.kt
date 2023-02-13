package com.flap.flap.controller

import com.flap.flap.common.exception.UnauthenticatedException
import com.flap.flap.mapper.UserMapper
import com.flap.flap.model.dto.auth.LoginDto
import com.flap.flap.model.dto.auth.RegisterDto
import com.flap.flap.model.entity.User
import com.flap.flap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
    ) {
    @PostMapping("register")
    fun register(@RequestBody body: RegisterDto): ResponseEntity<User> {
        //mapper를 써서 RegisterDTO를 User로 변환
        val user: User = userMapper.registerDtoToUser(body)

        //Service에서 User 저장 로직 수행
        val response: User = userService.saveUser(user)

        return ResponseEntity.ok(response)
    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDto, response: HttpServletResponse): ResponseEntity<Any> {

        //유효한 유저인지 검증하는 로직
        userService.checkValidUser(body)

        //JWT 생성
        val jwt = userService.generateJwt(body)

        //JWT를 쿠키에 저장
        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true
        response.addCookie(cookie)

        return ResponseEntity.ok("success")
    }


    //이거 이렇게 하면 안되고 userid를 받아서 그 id에 해당되는 유저를 리턴하는 방식으로 수정해야함함
   @GetMapping("myprofile")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {
        return try {
            val user = userService.getValidUser(jwt)
            ResponseEntity.ok(user)
        } catch (e: Exception) {
            ResponseEntity.status(401).body(UnauthenticatedException("unauthenticated"))
        }
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(cookie)
    }
}