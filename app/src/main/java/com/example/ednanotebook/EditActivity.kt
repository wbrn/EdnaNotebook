package com.example.ednanotebook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.example.ednanotebook.databinding.EditActivityBinding
import com.example.ednanotebook.db.DbManager
import com.example.ednanotebook.db.MyIntentConstants






class EditActivity : AppCompatActivity() {


    val imageRequestCode = 10
    var tempImageUri = "empty"
    val DbManager = DbManager( this)
    lateinit var bindingClass: EditActivityBinding


    val getimage = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            bindingClass.imMainImage.setImageURI(it)
            tempImageUri = it.toString() // zadolbalsya! nigde nujnogo otveta ne bilo poka sam ne dopyor do resheniya!



        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = EditActivityBinding.inflate(layoutInflater)
        val view = bindingClass.root
        setContentView(view)
        getMyIntents()

//        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            result: ActivityResult ->
//            if(result.resultCode = RESULT_OK){
//                val text = result.data?.getStringExtra("key1")
//            }
//        }
    }

    override fun onDestroy(){
        super.onDestroy()
        DbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        DbManager.openDB()
    }


    //override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        //if(resultCode == Activity.RESULT_OK && requestCode == imageRequestCode){
            //bindingClass.imMainImage.setImageURI(data?.data)


        //}
    //}

    fun onClickAddImage(view: View) {
        bindingClass.mainImageLayout.visibility = View.GONE
        bindingClass.fbAddImage.visibility = View.GONE
        tempImageUri = "empty" // poka ne smog inache

    }

    fun onClickDeleteImage(view: View) {
        bindingClass.mainImageLayout.visibility = View.GONE
        bindingClass.fbAddImage.visibility = View.GONE
        tempImageUri = "empty" // poka ne smog inache
    }

    fun onClickChooseImage(view: View) {


        getimage.launch("image/*")


       // val intent = Intent(Intent.ACTION_PICK)
       // intent.type = "image/*"
       //startActivityForResult(intent, imageRequestCode)

    }

    fun onClickSave(view: View) {

        val myTitle = bindingClass.edTitle.text.toString()
        val myDesc = bindingClass.edDesc.text.toString()



        if(myTitle != "" && myDesc != ""){
            DbManager.insertToDb(myTitle, myDesc, tempImageUri)

        }



    }

    fun getMyIntents(){

        val i = intent

        if(i != null){
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != "null"){
                bindingClass.fbAddImage.visibility = View.GONE

                bindingClass.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                bindingClass.edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                 if(i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty"){
//                     bindingClass.mainImageLayout.visibility = View.GONE
//                     bindingClass.imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
//                     bindingClass.imButtobDeleteImage.visibility = View.GONE
//                     bindingClass.imButtonEditImage.visibility = View.GONE
                 }
            }
        }
    }
}