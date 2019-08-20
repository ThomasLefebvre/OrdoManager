package fr.thomas.lefebvre.ordomanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ordonnances")

data class Ordonnance
(
    @ColumnInfo(name="id")@PrimaryKey(autoGenerate = true)var id:Long=0,
    @ColumnInfo(name="nom")var nom:String,
    @ColumnInfo(name="prenom")var prenom:String,
    @ColumnInfo(name="dateDebut")var dateDebut:String,
    @ColumnInfo(name="dateFinFR")var dateFin:String,
    @ColumnInfo(name="dateFinUS")var dateFinTri:String,
    @ColumnInfo(name="chronique")var chronique:Boolean
)