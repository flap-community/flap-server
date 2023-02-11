package com.flap.flap.model.entity

import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "Users")
data class User(
        @field:Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int?,

        var password: String? = null,
        var nickname: String? = null,

        @field:Email
        var email: String,

        var profileMessage: String? = null,
        var profileImagePath: String? = null,
        @Column(nullable = false)
        val activateDate: Date,
        val createdDate: Date,
        val modifiedDate: Date,
)