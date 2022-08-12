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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class EditActivity : AppCompatActivity() {


    var id = 0
    var isEditState = false
    val imageRequestCode = 10
    var tempImageUri = "empty"
    val DbManager = DbManager(this)
    lateinit var bindingClass: EditActivityBinding


    val getimage = registerForActivityResult(
        ActivityResultContracts.OpenDocument(),
        ActivityResultCallback {
            bindingClass.imMainImage.setImageURI(it)
            tempImageUri =
                it.toString() // zadolbalsya! nigde nujnogo otveta ne bilo poka sam ne dopyor do resheniya!
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)


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

    override fun onDestroy() {
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
        bindingClass.mainImageLayout.visibility = View.VISIBLE
        bindingClass.fbAddImage.visibility = View.GONE
//        tempImageUri = "empty" // poka ne smog inache

    }

    fun onClickDeleteImage(view: View) {
        bindingClass.mainImageLayout.visibility = View.GONE
        bindingClass.fbAddImage.visibility = View.VISIBLE
        tempImageUri = "empty" // poka ne smog inache
    }

    fun onClickChooseImage(view: View) {


        getimage.launch(arrayOf("image/*"))


        // val intent = Intent(Intent.ACTION_PICK)
        // intent.type = "image/*"
        //startActivityForResult(intent, imageRequestCode)

    }

    fun onClickSave(view: View) {

        val myTitle = bindingClass.edTitle.text.toString()
        val myDesc = bindingClass.edDesc.text.toString()



        if (myTitle != "" && myDesc != "") {

            CoroutineScope(Dispatchers.Main).launch {
                if (isEditState) {
                    DbManager.updateItem(myTitle, myDesc, tempImageUri, id, getCurrentTime())
                } else {
                    DbManager.insertToDb(myTitle, myDesc, tempImageUri, getCurrentTime())
                }

                finish()
            }


        }


    }


    fun onEditEnable(view: View){
        bindingClass.edTitle.isEnabled = true
        bindingClass.edDesc.isEnabled = true
        bindingClass.fbEdit.visibility = View.GONE
        bindingClass.fbAddImage.visibility = View.VISIBLE
        if(tempImageUri == "empty")return
        bindingClass.imButtonEditImage.visibility = View.VISIBLE
        bindingClass.imButtobDeleteImage.visibility = View.VISIBLE

    }

    fun getMyIntents() {
        bindingClass.fbEdit.visibility = View.GONE
        val i = intent

        if (i != null) {
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                bindingClass.fbAddImage.visibility = View.GONE

                bindingClass.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                isEditState = true
                bindingClass.edTitle.isEnabled = false
                bindingClass.edDesc.isEnabled = false
                bindingClass.fbEdit.visibility = View.VISIBLE
                bindingClass.edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {
                    bindingClass.mainImageLayout.visibility = View.VISIBLE
                    tempImageUri = i.getStringExtra(MyIntentConstants.I_URI_KEY)!!
                    bindingClass.imMainImage.setImageURI(
                        Uri.parse(tempImageUri

                        )
                    )
                    bindingClass.imButtobDeleteImage.visibility = View.GONE
                    bindingClass.imButtonEditImage.visibility = View.GONE
                }
            }
        }
    }



    private fun getCurrentTime():String{
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }
}