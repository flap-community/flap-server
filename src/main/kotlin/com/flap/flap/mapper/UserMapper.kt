package com.flap.flap.mapper

import com.flap.flap.model.dto.auth.RegisterDto
import com.flap.flap.model.entity.User
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    abstract fun registerDtoToUser(body: RegisterDto): User

}