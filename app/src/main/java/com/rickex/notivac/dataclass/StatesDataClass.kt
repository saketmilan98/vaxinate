package com.rickex.notivac.dataclass

data class StatesDataClass(
    val states: List<State>,
    val ttl: Int
)

data class State(
    val state_id: Int,
    val state_name: String
)