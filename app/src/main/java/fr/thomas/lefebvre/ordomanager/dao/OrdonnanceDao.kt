package fr.thomas.lefebvre.ordomanager.dao

import androidx.room.*
import fr.thomas.lefebvre.ordomanager.model.Ordonnance

@Dao
interface OrdonnanceDao {
    @Query("SELECT* FROM ordonnances ORDER BY dateFinUS ASC, nom ASC")
    fun getOrderByDate():List<Ordonnance>

    @Query("SELECT* FROM ordonnances ORDER BY nom ASC, dateFinUS ASC")
    fun getOrderByName():List<Ordonnance>

    @Query("SELECT* FROM ordonnances WHERE id= :id LIMIT 1")
    fun loadOrdoById(id:Long):Ordonnance

    @Query("SELECT* FROM ordonnances WHERE nom= :nom")
    fun loadOrdoById(nom:String):Ordonnance

    @Insert
    fun insertAll(vararg listCategories:Ordonnance)
    

    @Update
    fun updateOrdonnance(task: Ordonnance)

    @Delete
    fun deleteOrdonnance(task:Ordonnance)

    @Delete
    fun deleteAllOrdonnance(task:List<Ordonnance>)


}