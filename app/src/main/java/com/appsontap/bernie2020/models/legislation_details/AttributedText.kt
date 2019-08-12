package com.appsontap.bernie2020.models.legislation_details


data class AttributedText(val type: TYPE, var text: String = "", var bulletList: List<String> = listOf()){
    enum class TYPE{
        H2,H3,BULLET_LIST,P,LINK
    }
}