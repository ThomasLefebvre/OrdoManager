package fr.thomas.lefebvre.ordomanager.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import fr.thomas.lefebvre.ordomanager.R
import fr.thomas.lefebvre.ordomanager.R.*
import fr.thomas.lefebvre.ordomanager.model.Ordonnance
import java.time.LocalDate

class OrdoAdapter(val ordoAAfficher:List<Ordonnance>,val listener: (Ordonnance) -> Unit,val listener2: (Ordonnance) -> Unit): RecyclerView.Adapter<OrdoAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrdoAdapter.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(layout.rv_item_ordo,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return ordoAAfficher.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(p0: OrdoAdapter.ViewHolder, p1: Int) {
        p0.bind(ordoAAfficher[p1],listener,listener2)
    }

    class ViewHolder(elementList: View):RecyclerView.ViewHolder(elementList){

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(ordonnance:Ordonnance, listener:(Ordonnance)->Unit,listener2:(Ordonnance)->Unit){
            val colorRedString="#AF0D31"
            val colorBlue="#1F7DC9"
            val colorBlueLight="#75C8EE"
            //SET RECYCLERVIEW ITEM
            val textViewName: TextView =itemView.findViewById(id.tv_name_final)
            val textViewSurName: TextView =itemView.findViewById(id.tv_surname_final)
            val textViewDate:TextView=itemView.findViewById(id.tv_date_debut_final)
            val textViewDateFin:TextView=itemView.findViewById(id.tv_date_fin_final)
            val textViewAlert:TextView=itemView.findViewById(id.tv_alert_message)
            val btClear:ImageButton=itemView.findViewById(id.bt_delete)
            val btUpdate:ImageButton=itemView.findViewById(id.bt_update)
            val constraintLayout:ConstraintLayout=itemView.findViewById(id.constraint_layout_rv)
            textViewName.text=ordonnance.nom
            textViewSurName.text=ordonnance.prenom
            textViewDate.text=ordonnance.dateDebut
            textViewDateFin.text=ordonnance.dateFin

            btClear.setOnClickListener {
                listener(ordonnance) }
            btUpdate.setOnClickListener {
                listener2(ordonnance) }
            if (ordonnance.chronique==true){
                    constraintLayout.setBackgroundColor(Color.parseColor(colorBlue))
                }
            else{
                constraintLayout.setBackgroundColor(Color.parseColor(colorBlueLight))
            }
            var dateNow= LocalDate.now()
            if(ordonnance.dateFinTri<dateNow.toString()){
                textViewDateFin.setTextColor(Color.parseColor(colorRedString))
                if (ordonnance.chronique==true){
                    textViewAlert.text="A RENOUVELER"
                }
                else textViewAlert.text="TERMINEE"


            }
            else{
                textViewAlert.text=""
            }



        }

    }
}