package fr.thomas.lefebvre.ordomanager.activity



import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import fr.thomas.lefebvre.ordomanager.adapter.OrdoAdapter
import fr.thomas.lefebvre.ordomanager.R
import fr.thomas.lefebvre.ordomanager.database.OrdonnanceDataBase
import fr.thomas.lefebvre.ordomanager.model.Ordonnance
import kotlinx.android.synthetic.main.activity_list_ordo.*
import androidx.recyclerview.widget.DividerItemDecoration




class ListOrdoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ordo)
       setToolBarListOrdo()
        getOrdo()



    }


    fun setToolBarListOrdo(){
        toolbar.setTitle("")
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
           super.onBackPressed()
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_ordo,menu)
        return super.onCreateOptionsMenu(menu)
    }


    fun setRecyclerView(listOrdo:List<Ordonnance>){
        val recyclerView:RecyclerView=findViewById(fr.thomas.lefebvre.ordomanager.R.id.rv_list_ordo)

        recyclerView.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        val adapter=OrdoAdapter(listOrdo,{ordoClick:Ordonnance->ordoClickDelete(ordoClick)},{ordoClick:Ordonnance->ordoClickUpdate(ordoClick)})
        recyclerView.adapter=adapter

    }


    fun getOrdo(){
        AsyncTask.execute {
            val db= Room.databaseBuilder(this, OrdonnanceDataBase::class.java,"ordonnance_data_base").build()

            val dao=db.ordonnanceDao()

            val listOrdo=dao.get()

            runOnUiThread(Runnable {
                setRecyclerView(listOrdo)
            })
        }
    }

    fun ordoClickDelete(ordoClick: Ordonnance){
        Toast.makeText(this,"Ordonnance supprimée", Toast.LENGTH_LONG).show()
        AsyncTask.execute {
            val db= Room.databaseBuilder(this, OrdonnanceDataBase::class.java,"ordonnance_data_base").build()

            val dao=db.ordonnanceDao()

            val listOrdo=dao.deleteOrdonnance(ordoClick)
        }
        getOrdo()
    }
    fun ordoClickUpdate(ordoClick: Ordonnance){
        finish()
        val updateIntent=Intent(this,UpdateActivity::class.java)
        updateIntent.putExtra("idOrdo",ordoClick.id)
        startActivity(updateIntent)
    }
}
