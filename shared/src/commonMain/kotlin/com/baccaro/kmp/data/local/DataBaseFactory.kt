package com.baccaro.kmp.data.local

import app.cash.sqldelight.db.SqlDriver
import com.baccaro.kmp.ItemDataBase
import com.baccaro.kmp.ItemDataBaseTable
import com.baccaro.kmp.domain.model.CoordModel
import com.baccaro.kmp.domain.model.ItemModel

interface DataBaseDriverFactory{
    fun createDriver():SqlDriver
}

class LocalDatabase(
    databaseDriverFactory: DataBaseDriverFactory
) {
    private val database = ItemDataBase(
        databaseDriverFactory.createDriver()
    )
    private val query = database.itemDataBaseTableQueries

    fun readAllPosts(): List<ItemModel> {
        //println("INFO: Reading the cached data from the local database...")
        return query.selectAllItems()
            .executeAsList()
            .map {
                ItemModel(
                    _id = it.userId.toInt(),
                    name = it.name,
                    country = it.country,
                    coord = CoordModel(it.lon, it.lat),
                    isFavorite = it.isFavorite == 1L
                )
            }
    }

    fun searchItems(text: String): List<ItemModel> {
        return query.searchItems(text)
            .executeAsList()
            .map {
                ItemModel(
                    _id = it.userId.toInt(),
                    name = it.name,
                    country = it.country,
                    coord = CoordModel(it.lon, it.lat),
                    isFavorite = it.isFavorite == 1L
                )
            }
    }

    fun insertAllItems(items: List<ItemModel>) {
        //println("INFO: Caching the data from the network...")
        query.transaction {
            try {
                items.forEach { item ->
                    query.insertItem(
                        ItemDataBaseTable(
                            userId = item._id.toLong(),
                            name = item.name,
                            country = item.country,
                            lon = item.coord.lon,
                            lat = item.coord.lat,
                            isFavorite = if(item.isFavorite) 1 else 0
                        )
                    )
                }
            } catch(e: Exception) {
                println("Error inserting to database ${e.message}")
                // TODO: Log or Handle the error
            }
        }
    }
    fun insertItem(item: ItemModel) {
        try {
            query.insertItem(
                ItemDataBaseTable(
                    userId = item._id.toLong(),
                    name = item.name,
                    country = item.country,
                    lon = item.coord.lon,
                    lat = item.coord.lat,
                    isFavorite = if(item.isFavorite) 1 else 0
                )
            )
        } catch(e: Exception) {
            println("Error inserting single item to database ${e.message}")
            // TODO: Log or Handle the error
        }
    }

    fun updateFavorite(id: Int, isFavorite: Boolean){
        query.transaction {
            query.updateFavorite(if(isFavorite) 1 else 0, id.toLong())
        }
    }
}