package com.orgzly.android.repos

import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHttpRequest
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.core.Persister
import java.net.URL


class WebDavClient(url: URL, user: String, pass: String) {

    val creds = BasicCredentialsProvider()
    val userpass = UsernamePasswordCredentials(user, pass)
    val client: HttpClient
    val host: HttpHost
    val parser = Persister()
    val baseURL = url

    init {
        creds.setCredentials(AuthScope.ANY, userpass)
        client = HttpClientBuilder.create().setDefaultCredentialsProvider(creds).build()
        host = HttpHost(url.host, url.port, url.protocol)
    }


    fun getAllDocuments(): List<String> {
        return getAllDocuments(baseURL)
    }

    fun getAllDocuments(url: URL): List<String> {
        //TODO what do links do?
        val request = BasicHttpRequest("PROPFIND",
                url.toString())
        request.addHeader("DEPTH", "1")

        val res = client.execute(host, request)
        val status = res.statusLine.statusCode
        if (status != 207) {
            throw InvalidPropFindResponse(status)
        }

        val multiStatus: MultiStatus = parser.read(MultiStatus::class.java, res.entity.content, false)

        return multiStatus.responses.flatMap {
            if(URL(baseURL,it.href).sameFile(url)) {
                emptyList<String>()
            } else if (it.isDirectory) {
                getAllDocuments(URL(url, it.href))
            } else {
                listOf(it.href)
            }
        }
    }

    class InvalidPropFindResponse(status: Int): Error("Expected 207, got $status")

    @Namespace(reference = "DAV:")
    data class  MultiStatus (
            @field:ElementList(inline = true)
            @param:ElementList(inline = true)
            val responses: List<Response>)

    @Namespace(reference = "DAV:")
    data class Response(
            @field:Element(name = "href")
            @param:Element(name = "href")
            val href: String,
            @field:Element(name = "propstat")
            @param:Element(name = "propstat")
            val propStat: PropStat) {
        val status: String
            get() = propStat.status

        val isDirectory: Boolean
            get() = propStat.prop.resourceType.collection != null
    }

    @Namespace(reference = "DAV:")
    data class  PropStat(
            @field:Element(name = "status")
            @param:Element(name = "status")
            val status: String,
            @field:Element(name = "prop")
            @param:Element(name = "prop")
            val prop: Prop)

    //TODO Support links
    @Namespace(reference = "DAV:")
    data class Prop(
            @field:Element(name = "resourcetype")
            @param:Element(name = "resourcetype")
            val resourceType: ResourceType)

    @Namespace(reference = "DAV:")
    class ResourceType {
        @field:Element(required = false, name = "collection")
        var collection: DavCollection? = null
    }

    @Namespace(reference = "DAV:")
    class DavCollection
}