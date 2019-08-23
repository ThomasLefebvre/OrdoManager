package fr.thomas.lefebvre.ordomanager.activity

import android.content.Intent
import android.os.AsyncTask

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

import fr.thomas.lefebvre.ordomanager.R
import fr.thomas.lefebvre.ordomanager.database.OrdonnanceDataBase
import fr.thomas.lefebvre.ordomanager.model.Ordonnance
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mAdView:AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startListOrdoActivity()
        startNewOrdoActivity()

        MobileAds.initialize(this)

        mAdView=findViewById(R.id.adView)
        val adRequest=AdRequest.Builder().build()
        mAdView.loadAd(adRequest)




    }

    fun startListOrdoActivity(){
        bt_liste_ordo.setOnClickListener(View.OnClickListener {
            val listOrdoIntent=Intent(this,ListOrdoActivity::class.java)
            startActivity(listOrdoIntent)
        })
    }

    fun startNewOrdoActivity(){
        bt_new_ordo.setOnClickListener(View.OnClickListener {
            val newOrdoIntent=Intent(this,NewOrdoActivity::class.java)
            startActivity(newOrdoIntent)
        })
    }


}
