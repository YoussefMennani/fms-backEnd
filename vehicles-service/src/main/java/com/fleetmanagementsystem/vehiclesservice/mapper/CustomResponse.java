package com.fleetmanagementsystem.vehiclesservice.mapper;

import com.fleetmanagementsystem.vehiclesservice.utils.ResponseTypeEnum;

public record CustomResponse<T>(
        String message,
        ResponseTypeEnum status,
        T data
        ){}
