package com.example.newyorklist.data

/**
 * изображение, заголовок, текст обзора, дату публикации
 * текст обзора не отображается...его надо как то по другому получать...
 */
class Review {
    var Id: Long = 0
    var Name: String? = null
    var Date: String? = null
    var Text: String? = null
    var Img: String? = null
//    var _image: String? = null //отложим пока этот момент

    constructor()
    constructor(name: String, date: String, text: String, img:String?){
        this.Name = name
        this.Date = date
        this.Text = text
        this.Img = img
    }

    constructor(id:Long, name: String, date: String, text: String, img:String?){
        this.Id = id
        this.Name = name
        this.Date = date
        this.Text = text
        this.Img = img
    }
}