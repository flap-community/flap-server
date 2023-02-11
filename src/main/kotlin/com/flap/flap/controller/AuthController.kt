package com.flap.flap.controller

import com.flap.flap.common.exception.UnauthenticatedException
import com.flap.flap.mapper.UserMapper
import com.flap.flap.model.dto.auth.LoginDto
import com.flap.flap.model.dto.auth.RegisterDto
import com.flap.flap.model.entity.User
import com.flap.flap.service.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
class AuthController(private val userService: UserService, private val userMapper: UserMapper) {
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

        //mapper를 써서 LoginDTO를 User로 변환
        val user: User = userMapper.LoginDtoToUser(body)

        //유효한 유저인지 검증하는 로직
        userService.checkValidUser(user)

        //JWT 생성
        val jwt = userService.generateJwt(user)

        //JWT를 쿠키에 저장
        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true
        response.addCookie(cookie)

        return ResponseEntity.ok(user)
    }

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {
        try {
            val user = userService.getValidUser(jwt)
            return ResponseEntity.ok(user)
        } catch (e: Exception) {
            return ResponseEntity.status(401).body(UnauthenticatedException("unauthenticated"))
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