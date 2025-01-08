package com.baccaro.kmp.domain.model

sealed class GenericModel{
    data class ListModel(val data: List<ItemModel>) : GenericModel()
    data class ItemModel(val id:Int, val title:String, val imageUrl:String, val description:String) : GenericModel()
}