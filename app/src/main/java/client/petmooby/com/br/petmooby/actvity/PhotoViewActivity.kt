package client.petmooby.com.br.petmooby.actvity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showAlert
import client.petmooby.com.br.petmooby.util.ImageUtil
import client.petmooby.com.br.petmooby.util.Parameters
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_photo_view.*
import java.io.File

class PhotoViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)
        setupToolbar(R.id.tbPhotoView)
        val url = intent.getStringExtra(Parameters.URL_IMAGE)
        if (url != null && url.isNotBlank()) {
            progressPhotoView.visibility = View.VISIBLE
            ImageUtil.loadImageFromUrl(this, url, photoView, progressPhotoView)
        }else{
            val path = intent.getStringExtra(Parameters.PATH_IMAGE)
            loadImageByPath(photoView, progressPhotoView,path)
        }

    }

    private fun loadImageByPath(photoView: PhotoView, progressPhotoView: ProgressBar, path:String) {

        val image = File(path)
        if (!image.exists()) {
            showAlert(R.string.fileNotFound)
        } else {
            ImageUtil.loadImageFromFile(this, image, photoView, progressPhotoView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menuDeleteIcon ->{
                dialogForDeleteFile()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun dialogForDeleteFile() {
        AlertDialog.Builder(this)
                .setTitle(R.string.Advice)
                .setMessage(R.string.areYouSure)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { _, _ -> setResultDeleteAndFinish() }
                .create()
                .show()
    }

    private fun setResultDeleteAndFinish() {
        val intent = Intent()
        intent.putExtra(Parameters.DELETE, true)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
