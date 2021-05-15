package com.rickex.notivac.dataclass

data class DistrictDataClass(
    val districts: List<District>,
    val ttl: Int
)

data class District(
    val district_id: Int,
    val district_name: String
)