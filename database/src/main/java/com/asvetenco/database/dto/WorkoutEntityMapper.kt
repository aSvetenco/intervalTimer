package com.asvetenco.database.dto

import com.asvetenco.database.Mapper
import com.asvetenco.database.entity.LapEntity
import com.asvetenco.database.entity.WorkoutEntity

internal class WorkoutEntityMapper : Mapper<WorkoutEntity, WorkoutDto> {
    override fun mapFromEntity(entity: WorkoutEntity): WorkoutDto {
        val lapDto = entity.lap.let {
            LapDto(it.lapId, it.warmUp, it.coolDown, it.work, it.rest)
        }
        return WorkoutDto(entity.id, entity.title, entity.lapsCount, lapDto)
    }

    override fun mapToEntity(dto: WorkoutDto): WorkoutEntity {
        val lapEntity = dto.lap.let {
            LapEntity(it.id, it.warmUp, it.coolDown, it.work, it.rest)
        }
        return WorkoutEntity(dto.id, dto.title, dto.lapsCount, lapEntity)
    }

}