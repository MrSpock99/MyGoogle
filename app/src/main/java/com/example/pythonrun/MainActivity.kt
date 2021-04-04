package com.example.pythonrun

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jsoup.Jsoup
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private val index = "1 -> https://startandroid.ru/ru/\n" +
            "2 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/73-urok-33-hranenie-dannyh-preferences.html\n" +
            "3 -> https://startandroid.ru/en/lessons/535-lesson-33-data-storing-preferences.html\n" +
            "4 -> https://startandroid.ru/en/lessons/232-lesson-24-activity-lifecycle-example-about-changing-states-with-two-activities.html\n" +
            "5 -> https://startandroid.ru/ru/courses/dagger-2.html\n" +
            "6 -> https://startandroid.ru/en/lessons/230-lesson-23-activity-lifecycle-activity-states.html\n" +
            "7 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/72-urok-32-pishem-prostoj-brauzer.html\n" +
            "8 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/80-urok-40-layoutinflater-uchimsja-ispolzovat.html\n" +
            "9 -> https://startandroid.ru/en/lessons/218-lesson-14-menu-groups-order-menuinflater-and-xml-menu.html\n" +
            "10 -> https://startandroid.ru/ru/courses/dagger-2/16-course/dagger2/424-urok-1.html\n" +
            "11 -> https://startandroid.ru/en/lessons/234-lesson-25-task-what-is-it-and-how-it-is-formed.html\n" +
            "12 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/70-urok-31-zachem-u-intent-est-atribut-data-chto-takoe-uri-vyzyvaem-sistemnye-prilozhenija.html\n" +
            "13 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/61-urok-24-activity-lifecycle-primer-smeny-sostojanij-s-dvumja-activity.html\n" +
            "14 -> https://startandroid.ru/en/lessons/237-lesson-26-intent-filter-practice.html\n" +
            "15 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/60-urok-23-activity-lifecycle-v-kakih-sostojanijah-mozhet-byt-activity.html\n" +
            "16 -> https://startandroid.ru/ru/login.html?view=registration\n" +
            "17 -> https://startandroid.ru/ru/16-course/dagger2/424-urok-1.html\n" +
            "18 -> https://startandroid.ru/en/lessons/210-lesson-9-event-listeners-with-button-example.html\n" +
            "19 -> https://startandroid.ru/en/lessons/216-lesson-13-creating-a-simple-menu.html\n" +
            "20 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/67-urok-28-extras-peredaem-dannye-s-pomoschju-intent.html\n" +
            "21 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/79-urok-39-onupgrade-obnovljaem-bd-v-sqlite.html\n" +
            "22 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/82-urok42-spisok-listview.html\n" +
            "23 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/46-urok-14-menju-gruppy-porjadok.html\n" +
            "24 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/45-urok-13-sozdanie-prostogo-menju.html\n" +
            "25 -> https://startandroid.ru/en/lessons/199-lesson-1-introduction.html\n" +
            "26 -> https://startandroid.ru/en/lessons/209-lesson-8-working-with-layout-elements-from-code.html\n" +
            "27 -> https://startandroid.ru/en/18-courses/dagger-2/432-lesson-1-introduction.html\n" +
            "28 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/62-urok-25-task-chto-eto-takoe-i-kak-formiruetsja.html\n" +
            "29 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/59-urok-22-intent-intent-filter-context-teorija.html\n" +
            "30 -> https://startandroid.ru/en/lessons/522-lesson-31-intent-attributes-uri-and-data-launch-system-apps.html\n" +
            "31 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/64-urok-26-intent-filter-praktika.html\n" +
            "32 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/19-urok-12-logi-i-vsplyvajuschie-soobschenija.html\n" +
            "33 -> https://startandroid.ru/en/lessons/224-lesson-18-changing-layoutparams-in-a-running-application.html\n" +
            "34 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/177-urok-107-android-3-actionbar-razmeschenie-elementov.html\n" +
            "35 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/65-urok-27-chitaem-action-iz-intent.html\n" +
            "36 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom.html\n" +
            "37 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/16-urok-9-obrabotchiki-sobytij-na-primere-button.html\n" +
            "38 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/398-urok-169-opengl-shejdery.html\n" +
            "39 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/68-urok-29-vyzyvaem-activity-i-poluchaem-rezultat-metod-startactivityforresult.html\n" +
            "40 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/178-urok-108-android-3-actionbar-navigatsija-taby-i-vypadajuschij-spisok.html\n" +
            "41 -> https://startandroid.ru/en/lessons/208-lesson-7-layout-parameters-for-view-elements.html\n" +
            "42 -> https://startandroid.ru/en/lessons/222-lesson-17-creating-view-components-in-a-running-application.html\n" +
            "43 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/18-urok-11-papka-resvalues-ispolzuem-resursy-prilozhenija.html\n" +
            "44 -> https://startandroid.ru/en/lessons/238-lesson-27-reading-action-from-intent.html\n" +
            "45 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/69-urok-30-podrobnee-pro-onactivityresult-zachem-nuzhny-requestcode-i-resultcode.html\n" +
            "46 -> https://startandroid.ru/en/lessons/521-lesson-30-more-details-about-onactivityresult.html\n" +
            "47 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/51-urok-18-menjaem-layoutparams-v-rabochem-prilozhenii.html\n" +
            "48 -> https://startandroid.ru/en/courses-en/dagger-2.html\n" +
            "49 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/176-urok-106-android-3-fragments-vzaimodejstvie-s-activity.html\n" +
            "50 -> https://startandroid.ru/en/lessons/572-lesson-42-listview.html\n" +
            "51 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/142-urok-79-xmlpullparser-parsim-xml.html\n" +
            "52 -> https://startandroid.ru/ru/blog.html\n" +
            "53 -> https://startandroid.ru/en/lessons/570-lesson-40-learning-to-use-layoutinflater.html\n" +
            "54 -> https://startandroid.ru/ru/about/reklama.html\n" +
            "55 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/182-urok-112-android-3-actionbar-dinamicheskoe-razmeschenie-elementov.html\n" +
            "56 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/83-urok-43-odinochnyj-i-mnozhestvennyj-vybor-v-list.html\n" +
            "57 -> https://startandroid.ru/en/lessons/220-lesson-16-creating-layout-programmatically-layoutparams.html\n" +
            "58 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/78-urok-38-tranzaktsii-v-sqlite.html\n" +
            "59 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/274-urok-135-loader-loadermanager-asynctaskloader.html\n" +
            "60 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/13-urok-4-elementy-ekrana-i-ih-svojstva.html\n" +
            "61 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/81-urok-41-ispolzuem-layoutinflater-dlja-sozdanija-spiska.html\n" +
            "62 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/175-urok-105-android-3-fragments-dinamicheskaja-rabota.html\n" +
            "63 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/329-urok-149-risovanie-tekst.html\n" +
            "64 -> https://startandroid.ru/en/lessons/550-lesson-39-onupgrade-database-migrating.html\n" +
            "65 -> https://startandroid.ru/en/lessons/202-lesson-4-layout-elements-and-their-properties.html\n" +
            "66 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/47-urok-15-kontekstnoe-menju.html\n" +
            "67 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/14-urok-5-layout-kak-ispolzovat-smena-orientatsii-ekrana.html\n" +
            "68 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/49-16-layoutparams.html\n" +
            "69 -> https://startandroid.ru/en/lessons/225-lesson-19-creating-a-simple-calculator.html\n" +
            "70 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/9-urok-2-ustanovka-i-nastrojka-sredy-razrabotki.html\n" +
            "71 -> https://startandroid.ru/en/lessons/536-lesson-34-storing-data-sqlite.html\n" +
            "72 -> https://startandroid.ru/ru/courses/architecture-components.html\n" +
            "73 -> https://startandroid.ru/en/lessons/219-lesson-15-context-menu.html\n" +
            "74 -> https://startandroid.ru/ru/uroki/100-pdf.html\n" +
            "75 -> https://startandroid.ru/en/lessons/201-lesson3-creating-avd-the-first-application-android-project-structure.html\n" +
            "76 -> https://startandroid.ru/ru/about/ob-avtore.html\n" +
            "77 -> https://startandroid.ru/en/lessons/215-lesson-12-logs-and-toast-messages.html\n" +
            "78 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/15-urok-6-vidy-layouts-kljuchevye-otlichija-i-svojstva.html\n" +
            "79 -> https://startandroid.ru/en/lessons/241-lesson-28-extras-passing-data-using-intent.html\n" +
            "80 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/74-urok-34-hranenie-dannyh-sqlite.html\n" +
            "81 -> https://startandroid.ru/en/lessons/523-lesson-32-code-simple-browser.html\n" +
            "82 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/38-urok-7-layout-parametry-dlja-view-elementov.html\n" +
            "83 -> https://startandroid.ru/en/lessons/203-lesson-5-layout-file-fo-ractivity-xml-representation-changing-screen-orientation.html\n" +
            "84 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/54-urok-19-pishem-prostoj-kalkuljator.html\n" +
            "85 -> https://startandroid.ru/en/lessons/229-lesson-22-intent-intent-filter-context-theory.html\n" +
            "86 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/4-urok-1-vvedenie.html\n" +
            "87 -> https://startandroid.ru/en/lessons.html\n" +
            "88 -> https://startandroid.ru/ru/login.html\n" +
            "89 -> https://startandroid.ru/en/login.html\n" +
            "90 -> https://startandroid.ru/en/lessons/214-lesson-11-resvalues-folder-using-application-resources.html\n" +
            "91 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/12-urok-3-sozdanie-avd-pervoe-prilozhenie-struktura-android-proekta.html\n" +
            "92 -> https://startandroid.ru/en/lessons/207-lesson-6-layout-types-key-differences-and-properties.html\n" +
            "93 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/24-urok-8-rabotaem-s-elementami-ekrana-iz-koda.html\n" +
            "94 -> https://startandroid.ru/en/lessons/213-lesson-10-optimizing-event-listeners-implementation.html\n" +
            "95 -> https://startandroid.ru/ru/novosti.html\n" +
            "96 -> https://startandroid.ru/en/lessons/200-lesson-2-installing-and-configuring-development-environment-eclipse-and-sdk-tools.html\n" +
            "97 -> https://startandroid.ru/en/lessons/227-lesson-21-creating-and-starting-an-activity.html\n" +
            "98 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/17-urok-10-optimiziruem-realizatsiju-obrabotchikov.html\n" +
            "99 -> https://startandroid.ru/ru/courses/vse-kursy.html\n" +
            "100 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/58-urok-21-sozdanie-i-vyzov-activity.html\n" +
            "101 -> https://startandroid.ru/ru/courses/performance.html\n" +
            "102 -> https://startandroid.ru/ru/uroki.html\n" +
            "103 -> https://startandroid.ru/ru/courses/rxjava.html\n" +
            "104 -> https://startandroid.ru/ru/courses/kotlin.html\n" +
            "105 -> https://startandroid.ru/ru/chats.html\n" +
            "106 -> https://startandroid.ru/ru/courses/testing.html\n" +
            "107 -> https://startandroid.ru/ru/about/osajte.html\n" +
            "108 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/489-urok-180-constraintlayout-osnovy.html\n" +
            "109 -> https://startandroid.ru/en/\n" +
            "110 -> https://startandroid.ru/ru/uroki/vse-uroki-spiskom/157-urok-92-service-prostoj-primer.html\n"

    private val adapter = SearchAdapter(listOf()) {
        openInExternalBrowser(this, it.url)
    }

    private fun openInExternalBrowser(context: Context?, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context?.let {
            if (intent.resolveActivity(it.packageManager) != null) {
                it.startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val python = Python.getInstance()
        val pythonFile = python.getModule("main_")
        pythonFile.callAttr("main")

        findViewById<EditText>(R.id.vSearch).textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.toString().length > 2) {
                    search(it.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            findViewById<View>(R.id.vProgress).visibility = View.VISIBLE
                            findViewById<View>(R.id.vResults).visibility = View.GONE
                        }
                        .subscribe({ searchedIndexList ->
                            val urlList = mutableListOf<String>()
                            val indexList = index.split("\n")

                            searchedIndexList.forEach {
                                if (it == "")
                                    return@forEach
                                val a = indexList[it.trim().toInt() - 1]
                                val match = a.substringAfter("-> ")
                                urlList.add(match)
                            }
                            query(urlList)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doAfterSuccess {
                                    findViewById<View>(R.id.vProgress).visibility = View.GONE
                                    findViewById<View>(R.id.vResults).visibility = View.VISIBLE
                                }
                                .subscribe({
                                    adapter.submitList(it)
                                }, {})
                        }, {})

                }
            }
        findViewById<RecyclerView>(R.id.vResults).adapter = adapter
    }

    private fun query(urlList: List<String>): Single<List<Page>> {
        return Single.create {
            val adapterList = mutableListOf<Page>()

            urlList.forEach {
                val doc = Jsoup.connect(it).get()
                val titleElements = doc.getElementsByClass("article-title")
                if (titleElements.isNotEmpty()) {
                    adapterList.add(Page(it, title = titleElements[0].text()))
                }
            }
            it.onSuccess(adapterList)
        }
    }

    private fun search(text: String): Single<List<String>> {
        return Single.create {
            val python = Python.getInstance()
            val pythonFile = python.getModule("main_")
            var strList = pythonFile.callAttr(
                "search",
                text
            ).toString()

            strList = strList.replace("[", "")
            strList = strList.replace("]", "")
            it.onSuccess(
                strList.split(",")
            )
        }
    }
}