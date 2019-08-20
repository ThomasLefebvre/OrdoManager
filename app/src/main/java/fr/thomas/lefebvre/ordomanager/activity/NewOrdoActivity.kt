package fr.thomas.lefebvre.ordomanager.activity

import android.app.DatePickerDialog
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import fr.thomas.lefebvre.ordomanager.R
import fr.thomas.lefebvre.ordomanager.database.OrdonnanceDataBase
import fr.thomas.lefebvre.ordomanager.model.Ordonnance
import kotlinx.android.synthetic.main.activity_new_ordo.*
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class NewOrdoActivity : AppCompatActivity() {


    var formaterFR = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var formaterUS = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var coefMult: Int = 1
    var period = Period.ofWeeks(coefMult)
    lateinit var mAdView: AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ordo)
        setToolBarNewOrdo()
        clickBtSaveBottom()
        setCoefMultiAndDateEnd()
        listenerButtonRadio()
        mAdView=findViewById(R.id.adView)
        val adRequest= AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    fun setToolBarNewOrdo() {
        toolbar2.setTitle("")
        setSupportActionBar(toolbar2)
        toolbar2.setNavigationOnClickListener(View.OnClickListener {
            super.onBackPressed()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_ordo, menu)
        return super.onCreateOptionsMenu(menu)
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

            val ordo1 = Ordonnance(0, nom, prenom, dateDebut, datefin, datefinUs, chroniqueIsTrue)

            dao.insertAll(ordo1)
        }
    }

    fun alertMessage() {
        Toast.makeText(this, "Merci de remplir les champs", Toast.LENGTH_LONG).show()
    }

    fun saveOrdo() {
        if (edit_nom.text.toString() != "" && edit_prenom.text.toString() != "" && tv_date_debut_show.text.toString() != "" && tv_date_fin_show.text != "") {
            insertOrdo()
            edit_nom.text = null
            edit_prenom.text = null
            tv_date_debut_show.text = null
            tv_date_fin_show.text = null
            edit_duree.text = null
            edit_duree.visibility = View.GONE
        } else {
            alertMessage()
        }
    }

    fun clickBtSaveBottom() {
        bt_save.setOnClickListener(View.OnClickListener {
            saveOrdo()
            toastMessageSaveOrdo()
            finish()
            super.onBackPressed()

        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_save -> {
                saveOrdo()
                finish()
                super.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun toastMessageSaveOrdo(){
        Toast.makeText(this,"Ordonnance enregistrée",Toast.LENGTH_LONG).show()
    }
}


