package com.orgzly.android.repos

import android.net.Uri
import java.io.File

class WebDavRepo : Repo {
    override fun requiresConnection(): Boolean = true

    override fun getUri(): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBooks(): MutableList<VersionedRook> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveBook(fileName: String?, destination: File?): VersionedRook {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun storeBook(file: File?, fileName: String?): VersionedRook {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renameBook(from: Uri?, name: String?): VersionedRook {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}