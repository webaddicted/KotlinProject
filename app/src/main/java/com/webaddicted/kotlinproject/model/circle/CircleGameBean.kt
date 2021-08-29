package com.webaddicted.kotlinproject.model.circle

open class CircleGameBean : Comparator<CircleGameBean> {
    var id: String = ""
    var Name: String = ""
    var Image: String = ""
    override fun compare(m1: CircleGameBean?, m2: CircleGameBean?): Int {
        return m1?.Name?.compareTo(m2?.Name.toString())!!
    }
//    constructor() : this()
//    constructor() : this()

}
