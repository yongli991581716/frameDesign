package com.frameDesign.baselib.model.bean.test

data class SYTest(
    var choose: Boolean = false,
    var department1: String = "",
    var department2: String = "",
    var id: String = "",
    var info: List<Info> = listOf(),
    var name: String = "",
    var placeName: String = "",
    var symptomList: List<String> = listOf())

data class Info(
    var content: String = "",
    var title: String = ""
)