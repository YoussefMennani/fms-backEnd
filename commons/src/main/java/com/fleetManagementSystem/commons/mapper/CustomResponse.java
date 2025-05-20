package com.fleetManagementSystem.commons.mapper;

import com.fleetManagementSystem.commons.enums.ResponseTypeEnum;

public record CustomResponse<T>(
        String message,
        ResponseTypeEnum status,
        T data
        ){}
