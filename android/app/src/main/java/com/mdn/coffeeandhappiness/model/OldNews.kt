package com.mdn.coffeeandhappiness.model

class OldNews public constructor() {
    private lateinit var newsList: MutableList<SingleNew>

    init {
        newsList = mutableListOf(
            SingleNew("Ми відкрились!", "We have opened!", "24.02.2022", "news_1")
        )
        newsList.add(
            0,
            SingleNew(
                "Новий заклад на Кульпарківській!",
                "New cafe on Kulparkivska street!",
                "06.08.2022",
                "news_2"
            )
        )
        newsList.add(
            0,
            SingleNew(
                "Тераса з видом на Львів",
                "Terrace with a view on Lviv",
                "03.01.2023",
                "news_3"
            )
        )
    }

    public fun getNews(): MutableList<SingleNew> {
        return newsList
    }


}

data class SingleNew(
    var nameUk: String,
    var nameEn: String,
    var date: String,
    var photo: String
)

