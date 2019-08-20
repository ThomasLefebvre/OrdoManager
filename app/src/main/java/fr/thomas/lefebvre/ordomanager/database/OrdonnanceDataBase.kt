package fr.thomas.lefebvre.ordomanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.thomas.lefebvre.ordomanager.dao.OrdonnanceDao
import fr.thomas.lefebvre.ordomanager.model.Ordonnance

@Database(entities = [Ordonnance::class],version=1)
abstract class OrdonnanceDataBase: RoomDatabase() {

    abstract fun ordonnanceDao():OrdonnanceDao
}