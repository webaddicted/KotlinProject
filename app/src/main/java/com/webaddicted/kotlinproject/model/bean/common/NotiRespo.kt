package com.webaddicted.kotlinproject.model.bean.common

data class NotiRespo(
    val canonical_ids: Int,
    val failure: Int,
    val multicast_id: Long,
    val results: List<Result>,
    val success: Int
)