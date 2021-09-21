package com.example.studioweb.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.studioweb.R
import com.example.studioweb.constants.SharedPreferencesConstants
import com.example.studioweb.services.repository.local.SecurityPreferences
import com.example.studioweb.services.repository.sync.ConnectionLiveData
import com.example.studioweb.services.repository.sync.SyncData
import com.example.studioweb.view.utils.CpfMaskTv
import com.example.studioweb.view.utils.DataMaskTv
import com.example.studioweb.view.utils.TelefoneMaskTv
import com.example.studioweb.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_user.*
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class UserActivity : AppCompatActivity() {
    private lateinit var mConnectionLiveData: ConnectionLiveData
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mSyncData: SyncData

    private lateinit var mLoginViewModel: LoginViewModel

    var mCameraRequestCode: Int = 200
    var mGalleryRequestCode: Int = 100


    @ExperimentalUnsignedTypes
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Removendo ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_user)

        mLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        mConnectionLiveData = ConnectionLiveData(application)
        mSecurityPreferences = SecurityPreferences(application)
        mSyncData = SyncData(application)

        // Setando as máscaras nos TextViews.
        tv_cpfCompleto.addTextChangedListener(CpfMaskTv.mask("###.###.###-##", tv_cpfCompleto))
        tv_telefoneCompleto.addTextChangedListener(
            TelefoneMaskTv.insert(
                "(##) # ####-####",
                tv_telefoneCompleto
            )
        )
        tv_nascimentoCompleto.addTextChangedListener(
            DataMaskTv.insert(
                "##/##/####",
                tv_nascimentoCompleto
            )
        )

        observe()
        setData()
        setListeners()
    }

    /**
     * Função responsável por observar as mudanças, e executar uma ação.
     */
    private fun observe() {
        mConnectionLiveData.observe(this, {
            if (it) {
                // Se tiver internet, ele esconde o aviso.
                constraint_offline.visibility = GONE
                tv_offline.visibility = GONE
                image_loading.visibility = GONE
                // E chama a função de sincronização a cada certo intervalo de tempo.
                mSyncData.startSync()
            } else {
                // Caso não tenha internet, mostra o aviso.
                constraint_offline.visibility = VISIBLE
                tv_offline.visibility = VISIBLE
                image_loading.visibility = VISIBLE
            }
        })
    }

    /**
     * Função responsável por colocar os dados do usuário, cadastrados
     * no SharedPreferences, nos TextViews.
     */
    @ExperimentalUnsignedTypes
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {
        // Setando dados do usuário na tela principal.
        tv_nomeCompleto.text = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.NOME)
        tv_emailCompleto.text = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.EMAIL)
        tv_cpfCompleto.text = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.CPF)
        tv_telefoneCompleto.text =
            mSecurityPreferences.get(SharedPreferencesConstants.SHARED.TELEFONE)

        //Formatando Nascimento
        val nascimentoShared =
            mSecurityPreferences.get(SharedPreferencesConstants.SHARED.NASCIMENTO)

        val nascimentoT = nascimentoShared.replaceAfter("T", "")
        val nascimentoInvalido = nascimentoT.replace("T", "")
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val nascimento = LocalDate.parse(nascimentoInvalido, formatter2).format(formatter).toString()

        tv_nascimentoCompleto.text = nascimento

        val cpf = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.CPF)
        val lResult = mLoginViewModel.getProfilePic(cpf)

        if (lResult != null){
            img_logo.setImageBitmap(lResult)
        }

    }

    /**
     * Função responsável por escutar os cliques do usuário e fazer validação de campos.
     */
    private fun setListeners() {
        //Listener do botão de Logout
        tv_logout.setOnClickListener {
            Toast.makeText(this, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show()
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Listener do botão de Alterar Dados
        tv_alterarDados.setOnClickListener {
            intent = Intent(this, EditUserActivity::class.java)
            startActivity(intent)
        }

        //Listener do botão de voltar
        image_back.setOnClickListener {
            intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Listener do Editar Foto
        img_trocaFoto.setOnClickListener {
            val dialogueBox = AlertDialog.Builder(this)
            val dialogueOptions = arrayOf("Galeria")
            dialogueBox.setTitle("Escolha:")
            dialogueBox.setItems(dialogueOptions
            ) { _, p1 ->
                if (dialogueOptions[p1] == "Galeria") {
                    openGallery()
                }
            }
            dialogueBox.show()
        }
    }

    private fun openGallery() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent,mGalleryRequestCode)
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),mGalleryRequestCode)
        }
    }

    private fun openCamera() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
        {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent,mCameraRequestCode)
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),mCameraRequestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            mCameraRequestCode->
            {
                if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED )
                {
                    openCamera()
                }
                else
                {
                    Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
                }
            }
            mGalleryRequestCode->
            {
                if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    openGallery()
                }
                else
                {
                    Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            mCameraRequestCode->
            {
                if(resultCode== Activity.RESULT_OK)
                {
                    val img = data?.extras?.get("data")

                    img_logo.setImageBitmap(img as Bitmap)
                }
            }
            mGalleryRequestCode->
            {
                if(resultCode== RESULT_OK)
                {
                    val uri = data!!.data
                    val image = getBitmap(this.contentResolver,uri)

                    val cpf = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.CPF)
                    mLoginViewModel.updateProfilePic(cpf, getBytes(image))

                    img_logo.setImageBitmap(image)
                }
            }
        }
    }

    private fun getBytes(image: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        image.compress(CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }


}
