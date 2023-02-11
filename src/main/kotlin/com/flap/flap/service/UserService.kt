package com.flap.flap.service

import com.flap.flap.model.entity.User
import com.flap.flap.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun saveUser(user: User):User {
        return userRepository.save(user)
    }
    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }
    fun getById(id: Int): User? {
        return userRepository.findById(id).orElse(null)
    }
}