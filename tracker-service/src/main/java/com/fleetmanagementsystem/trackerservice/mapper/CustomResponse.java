package com.fleetmanagementsystem.trackerservice.mapper;

import com.fleetManagementSystem.commons.enums.ResponseTypeEnum;

public record CustomResponse<T>(
        String message,
        ResponseTypeEnum status,
        T data
        //long timestamp
) {
//        public CustomResponse(String message, String status, Object data) {
//                this(message, status, data, System.currentTimeMillis());
//        }
}
