package net.harutiro.jetpackcomposecamera

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import net.harutiro.jetpackcomposecamera.ui.theme.JetpackComposeCameraTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeCameraTheme{
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TakePicture()
                }
            }
        }
    }
}

@Composable
fun TakePicture() {

    val context = LocalContext.current

    var uriRemember by remember { mutableStateOf("") }

    var uri = ""


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { _ ->
        uriRemember = uri
    }

    Column {
        Button(onClick = {


            // 保存先のフォルダー
            val cFolder: File? = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)

            //        *名前関係*       //
            //　フォーマット作成
            val fileDate: String = SimpleDateFormat("ddHHmmss", Locale.US).format(Date())
            //　名前作成
            val fileName: String = String.format("CameraIntent_%s.jpg", fileDate)

            //uriの前作成
            val cameraFile: File = File(cFolder, fileName)

            //uri最終作成
            val currentPhotoUri = FileProvider.getUriForFile(context, context.packageName.toString() + ".fileprovider", cameraFile)

            uri = currentPhotoUri.toString()

            launcher.launch(currentPhotoUri)

        }) {
            Text(text = "Take Picture")
        }
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(data = uriRemember)
                    .build()
            ),
            contentDescription = "My Picture",
            modifier = Modifier.fillMaxSize()
        )
    }
}