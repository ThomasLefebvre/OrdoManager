package fr.thomas.lefebvre.ordomanager.activity



import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import kotlinx.android.synthetic.main.alert_dialog_sort.*
import kotlinx.android.synthetic.main.alert_dialog_sort.view.*



class ListOrdoActivity : AppCompatActivity() {


    var SHARED_PREFS:String="sharedPrefs"
    var RB_DATE_IS_CHECKED:String="rbDateIsChecked"
    var RB_NAME_IS_CHECKED:String="rbNameIsChecked"





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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_tri -> {
               alertDialogSort()
            }
        }
        return super.onOptionsItemSelected(item)
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
            val listOrdo:List<Ordonnance>
            if(loadSortPreferencesDate()){
                 listOrdo=dao.getOrderByDate()
            }
            else listOrdo=dao.getOrderByName()

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


    fun alertDialogSort(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_sort,null)

        val mBuilderDialog= AlertDialog.Builder(this)
            .setView(mDialogView)
        mDialogView.rb_date_dialog.isChecked=loadSortPreferencesDate()
        mDialogView.rb_name_dialog.isChecked=loadSortPreferencesDNom()

        val mAlertDialog=mBuilderDialog.show()

        mDialogView.bt_save_sort.setOnClickListener(View.OnClickListener {
            saveSortPreferences(mDialogView)
            mAlertDialog.dismiss()
            finish()
            startListOrdoActivity()
        })
    }

    fun saveSortPreferences(view: View){
        val sharedPreferences=getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        Log.i("SHARED","${view.rb_date_dialog.isChecked}")
        Log.i("SHARED","${view.rb_name_dialog.isChecked}")
        editor.putBoolean(RB_DATE_IS_CHECKED,view.rb_date_dialog.isChecked)
        editor.putBoolean(RB_NAME_IS_CHECKED,view.rb_name_dialog.isChecked)
        editor.apply()

    }

    fun loadSortPreferencesDate():Boolean {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(RB_DATE_IS_CHECKED, true)
    }

    fun loadSortPreferencesDNom():Boolean{
        val sharedPreferences=getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(RB_NAME_IS_CHECKED,false)
    }

    fun startListOrdoActivity(){
        var listOrdoIntent=Intent(this,ListOrdoActivity::class.java)
        startActivity(listOrdoIntent)
    }

}
