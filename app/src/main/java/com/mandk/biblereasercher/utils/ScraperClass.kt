package com.mandk.biblereasercher.pages


import android.util.Log
import androidx.compose.runtime.Composable
import com.mandk.biblereasercher.Book
import com.mandk.biblereasercher.Translation
import com.mandk.biblereasercher.UserSelection
import com.mandk.biblereasercher.utils.Scraper
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div
import it.skrape.selects.html5.li
import it.skrape.selects.html5.ul


/**
 * Implementation of interface Scraper
 *
 * Used as a utility to scrape data from website
 */
class ScraperClass : Scraper {

    @Composable
    override fun scrapeForTranslations() : MutableList<Translation> {
        val translations: MutableList<Translation> = mutableListOf(Translation("Biblia Tysiąclecia", "/Biblia/Tysiaclecia"))

        try {
            skrape(HttpFetcher)
            {
                request {
                    url = "http://biblia-online.pl/BibliaSzukaj"
                    timeout = 5000
                }
                response {
                    htmlDocument {
                        ul{
                            withClass="dropdown-menu"
                            findFirst{
                                li{
                                    findFirst{
                                        ul{
                                            findFirst{
                                                li{
                                                    findAll {
                                                        forEach{
                                                            if(!it.hasClass("dropdown-header")){
                                                                translations.add(
                                                                    Translation
                                                                        (
                                                                        name = it.findFirst("a").text,
                                                                        url = it.findFirst("a").attribute("href")
                                                                    ))
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.message?.let { Log.d("error scraping translations", it) }
            ErrorPage(e.message)
        }
        return translations
    }

    @Composable
    override fun scrapeForChapters(urlString : String): MutableList<String> {
        val chapterOptions: MutableList<String> = ArrayList()
        // create list of chapters according to the translation
        try {
            skrape(HttpFetcher)
            {
                request {
                    url = "http://biblia-online.pl/${urlString}"
                    timeout = 5000
                }
                response {
                    htmlDocument {
                        div {
                            withClass = "mnav-chptrs-cell"
                            findFirst {
                                this.a {
                                    findAll {
                                        forEach {
                                            chapterOptions.add(it.text)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.message?.let { Log.d("error scraping chapters", it) }
            ErrorPage(e.message)
        }
        return chapterOptions
    }

    @Composable
    override fun scrapeForBook(translationUrl: String, testament: String) : MutableList<Book> {
        val bookOptions: MutableList<Book> = ArrayList()
        // create list of chapters according to the translation
        try{
            skrape(HttpFetcher)
            {
                request {
                    if(translationUrl!="") {
                        url =
                            "http://biblia-online.pl/Biblia/ListaKsiag/${translationUrl.split("/")[2]}/"
                    }
                    else
                        return@request
                }
                response {
                    htmlDocument {
                        div {
                            withId = "ot-books-list"
                            if (testament == "Stary Testament") {
                                findFirst {
                                    this.div {
                                        withClass = "book-list-item"
                                        findAll {
                                            forEachIndexed { index, it ->
                                                val fullBookName =
                                                    it.findFirst("a").text
                                                val abbrBookName =
                                                    it.findFirst("span").text
                                                val url =
                                                    it.findFirst("a").attribute("href")
                                                bookOptions.add(
                                                    Book(
                                                        index,
                                                        fullBookName,
                                                        abbrBookName,
                                                        url
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                findSecond {
                                    this.div {
                                        withClass = "book-list-item"
                                        findAll {
                                            forEachIndexed { index, it ->
                                                val fullBookName =
                                                    it.findFirst("a").text
                                                val abbrBookName =
                                                    it.findFirst("span").text
                                                val url =
                                                    it.findFirst("a").attribute("href")
                                                bookOptions.add(
                                                    Book(
                                                        index,
                                                        fullBookName,
                                                        abbrBookName,
                                                        url
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.message?.let { Log.d("error scraping books", it) }
            ErrorPage(e.message)
        }
        return bookOptions
    }


    override fun skrapeText(selectedValue : UserSelection) : String {
        var textOnScreen = ""
        try{
            skrape(HttpFetcher) {
                // make an HTTP GET request to the specified URL
                request {
                    // TODO Has to be dynamically changed by the user
                    val temp = "http://biblia-online.pl/${selectedValue.book?.url}"
                    url = temp.substring(0, temp.length - 4) + "/${selectedValue.chapter}"
                    Log.d("connected at", url)
                }
                response {
                    htmlDocument {
                        "div.vr" {
                            findAll {
                                forEach {
                                    val text = it.findFirst(".vtbl-txt").text
                                    val nr = it.findFirst(".vtbl-num").text
                                    textOnScreen += "$nr: $text\n"
//                            werset = Werset(
//                                text = text,
//                                nr = nr
//                            )
//                            rozdzial.add(werset)
//                            werset.printWerset()
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (e: Exception)
        {
            // TODO has to navigate to ErrorPage if failed
            e.message?.let { Log.d("error scraping text", it) }
        }

        return textOnScreen
    }

}