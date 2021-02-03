package com.asvetenco.database

interface Mapper<Entity, Dto> {
    fun mapFromEntity(entity: Entity): Dto
    fun mapToEntity(dto: Dto): Entity
}