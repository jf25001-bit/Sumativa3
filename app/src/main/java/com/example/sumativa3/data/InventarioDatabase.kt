package com.example.sumativa3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ActivoFijo::class], version = 1, exportSchema = false)
abstract class InventarioDatabase : RoomDatabase() {
    abstract fun activoDao(): ActivoDao

    companion object {
        @Volatile
        private var INSTANCE: InventarioDatabase? = null

        fun obtenerBaseDatos(context: Context): InventarioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventarioDatabase::class.java,
                    "inventario_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}