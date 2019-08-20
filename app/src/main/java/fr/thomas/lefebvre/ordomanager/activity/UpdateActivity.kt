package fr.thomas.lefebvre.ordomanager.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.room.Room
import fr.thomas.lefebvre.ordomanager.R
import fr.thomas.lefebvre.ordomanager.database.OrdonnanceDataBase
import fr.thomas.lefebvre.ordomanager.model.Ordonnance
import kotlinx.android.synthetic.main.activity_new_ordo.*
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class UpdateActivity : AppCompatActivity() {
    var formaterFR = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var formaterUS = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var coefMult: Int = 1
    var period = Period.ofWeeks(coefMult)
    var idOrdoUpdate:Long=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        setViewUpdateLayout()
        setToolBarNewOrdo()
        loadData()
        clickBtSaveBottom()
        setCoefMultiAndDateEnd()
        listenerButtonRadio()

    }

    override fun onBackPressed() {
        finish()
        startListOrdoActivity()
    }

    fun setToolBarNewOrdo() {
        toolbar2.setTitle("")
        setSupportActionBar(toolbar2)
        toolbar2.setNavigationOnClickListener(View.OnClickListener {
            finish()
            startListOrdoActivity()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_ordo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun setViewUpdateLayout(){
        textView.text="Mettre à jour"
    }

    fun loadData(){
        val idOrdo:Long=intent.extras.getLong("idOrdo")
        idOrdoUpdate=idOrdo
        AsyncTask.execute {

            val db= Room.databaseBuilder(this, OrdonnanceDataBase::class.java,"ordonnance_data_base").build()
            val dao=db.ordonnanceDao()
            val ordo=dao.loadOrdoById(idOrdo)

            runOnUiThread(Runnable {
                edit_nom.setText(ordo.nom)
                edit_prenom.setText(ordo.prenom)
                tv_date_debut_show.setText(ordo.dateDebut)
                tv_date_fin_show.setText(ordo.dateFin)
                if(ordo.chronique){
                    switch_chronique.isChecked=true
                }
            })
        }

    }

    fun clickDatePicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in Toast
            var dayString: String
            if (dayOfMonth < 10) {
                dayString = "0$dayOfMonth"
            } else dayString = "$dayOfMonth"
            var monthString: String
            if ((monthOfYear + 1) < 10) {
                monthString = "0${monthOfYear + 1}"
            } else monthString = "${monthOfYear + 1}"

            var yearString: String = "$year"
            var dateDebut = LocalDate.parse(yearString + "-" + monthString + "-" + dayString).format(formaterFR)
            tv_date_debut_show.text = dateDebut
            tv_date_fin_show.text=null
            edit_duree.visibility = View.VISIBLE

        }, year, month, day)
        dpd.show()
    }

    fun listenerButtonRadio() {
        rb_jour.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            setPeriodAndDateFin()
        })
        rb_semaine.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            setPeriodAndDateFin()
        })
        rb_mois.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            setPeriodAndDateFin()
        })
    }

    fun alertMessage() {
        Toast.makeText(this, "Merci de remplir les champs", Toast.LENGTH_LONG).show()
    }

    fun saveOrdo() {
        if (edit_nom.text.toString() != "" && edit_prenom.text.toString() != "" && tv_date_debut_show.text.toString() != "" && tv_date_fin_show.text != "") {
            insertOrdo()
            finish()
            startListOrdoActivity()
        } else {
            alertMessage()
        }
    }

    fun clickBtSaveBottom() {
        bt_save.setOnClickListener {
            saveOrdo()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_save -> {
                saveOrdo()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setCoefMultiAndDateEnd() {

        edit_duree.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setPeriodAndDateFin()
            }
        })
    }

    fun setPeriodAndDateFin() {
        if (edit_duree.text.toString() != "") {
            coefMult = Integer.parseInt(edit_duree.text.toString())
            if (rb_jour.isChecked) {
                period = Period.ofDays(coefMult)
            } else if (rb_semaine.isChecked) {
                period = Period.ofWeeks(coefMult)
            } else period = Period.ofMonths(coefMult)

            var dateFin = LocalDate.parse(tv_date_debut_show.text.toString(), formaterFR).plus(period)
            var dateFinString = dateFin.format(formaterFR)
            tv_date_fin_show.text = dateFinString
        } else tv_date_fin_show.text = null
    }


    fun insertOrdo() {
        var nom: String = edit_nom.text.toString()
        var prenom: String = edit_prenom.text.toString()
        var dateDebut: String = tv_date_debut_show.text.toString()
        var datefin = tv_date_fin_show.text.toString()
        var datefinUs = LocalDate.parse(datefin, formaterFR).format(formaterUS)
        var chroniqueIsTrue: Boolean = false
        if (switch_chronique.isChecked) {
            chroniqueIsTrue = true
        }
        AsyncTask.execute {
            val db = Room.databaseBuilder(this, OrdonnanceDataBase::class.java, "ordonnance_data_base").build()

            val dao = db.ordonnanceDao()

            val ordo1 = Ordonnance(idOrdoUpdate, nom, prenom, dateDebut, datefin, datefinUs, chroniqueIsTrue)

            dao.updateOrdonnance(ordo1)
        }
    }

    fun startListOrdoActivity(){
       val listOrdoIntent= Intent(this,ListOrdoActivity::class.java)
        startActivity(listOrdoIntent)
    }
}
