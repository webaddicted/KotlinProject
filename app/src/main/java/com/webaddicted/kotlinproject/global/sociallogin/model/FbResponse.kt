package com.webaddicted.kotlinproject.global.sociallogin.model

/**
 * Created by Deepak Sharma(webaddicted) on 29/03/2020
 */

class FbResponse {
    /**
     * id : 787189058140980
     * name : Deepak Sharma
     * first_name : Deepak
     * last_name : Sharma
     * birthday : 06/04/1995
     * email : sharma9024061407@gmail.com
     * cover : {"offset_x":50,"offset_y":24,"source":"https://platform-lookaside.fbsbx.com/platform/coverpic/?asid=787189058140980&ext=1538660478&hash=AeTTZveCK0ySwKtv"}
     * picture : {"data":{"height":200,"is_silhouette":false,"url":"https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=787189058140980&height=200&width=200&ext=1538660478&hash=AeSWwvkif1TkFuSJ","width":200}}
     * photos : {"data":[{"created_time":"2016-05-10T07:33:40+0000","id":"531777723682116"},{"created_time":"2018-03-17T02:31:14+0000","name":"Attend job fair at #JECRC_University as a    #company. \n#Now_Time_Changed.","id":"886082998251585"},{"created_time":"2018-02-17T16:02:40+0000","id":"735086106690479"},{"created_time":"2017-10-04T02:16:41+0000","id":"784561885070364"},{"created_time":"2017-07-18T18:36:37+0000","name":"We spent amazing time ...........now i miss that  moment","id":"737494089777144"},{"created_time":"2017-06-19T17:11:21+0000","id":"722217974638089"},{"created_time":"2017-06-07T17:38:22+0000","id":"716256175234269"},{"created_time":"2017-03-23T04:23:47+0000","name":"Here i m....................","id":"674403329419554"},{"created_time":"2016-06-11T20:55:18+0000","id":"543045392555349"},{"created_time":"2016-06-11T20:55:18+0000","id":"543045675888654"},{"created_time":"2016-06-11T13:31:42+0000","id":"542876152572273"},{"created_time":"2016-05-16T11:44:11+0000","id":"533743106818911"},{"created_time":"2016-03-07T13:54:11+0000","id":"506948342831721"},{"created_time":"2015-04-20T08:59:04+0000","id":"384570968402793"},{"created_time":"2015-04-20T08:58:12+0000","id":"384570775069479"},{"created_time":"2015-04-20T08:55:22+0000","id":"384570081736215"},{"created_time":"2015-04-20T08:55:01+0000","id":"384569998402890"},{"created_time":"2015-04-20T08:54:41+0000","id":"384569895069567"},{"created_time":"2015-04-20T08:54:20+0000","id":"384569725069584"},{"created_time":"2015-04-20T08:54:00+0000","id":"384569568402933"}],"paging":{"cursors":{"before":"TlRNeE56YzNOekl6TmpneU1URTJPakUxTWpRNE9UazBNREk2TXprME1EZAzVOalF3TmpRM09ETTIZD","after":"TXpnME5UWTVOVFk0TkRBeU9UTXpPakUwTWprMU1qQXdORGc2TXprME1EZAzVOalF3TmpRM09ETTIZD"}}}
     */
    var id: String? = ""
    var name: String? = ""
    var first_name: String? = ""
    var last_name: String? = ""
    var birthday: String? = ""
    var email: String? = ""
    var cover: CoverBean? = null
    var picture: PictureBean? = null
    var photos: PhotosBean? = null

    class CoverBean {
        /**
         * offset_x : 50
         * offset_y : 24
         * source : https://platform-lookaside.fbsbx.com/platform/coverpic/?asid=787189058140980&ext=1538660478&hash=AeTTZveCK0ySwKtv
         */
        var offset_x = 0
        var offset_y = 0
        var source: String? = ""

    }

    class PictureBean {
        /**
         * data : {"height":200,"is_silhouette":false,"url":"https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=787189058140980&height=200&width=200&ext=1538660478&hash=AeSWwvkif1TkFuSJ","width":200}
         */
        var data: DataBean? = null

        class DataBean {
            /**
             * height : 200
             * is_silhouette : false
             * url : https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=787189058140980&height=200&width=200&ext=1538660478&hash=AeSWwvkif1TkFuSJ
             * width : 200
             */
            var height = 0
            var isIs_silhouette = false
            var url: String? = ""
            var width = 0

        }
    }

    class PhotosBean {
        /**
         * data : [{"created_time":"2016-05-10T07:33:40+0000","id":"531777723682116"},{"created_time":"2018-03-17T02:31:14+0000","name":"Attend job fair at #JECRC_University as a    #company. \n#Now_Time_Changed.","id":"886082998251585"},{"created_time":"2018-02-17T16:02:40+0000","id":"735086106690479"},{"created_time":"2017-10-04T02:16:41+0000","id":"784561885070364"},{"created_time":"2017-07-18T18:36:37+0000","name":"We spent amazing time ...........now i miss that  moment","id":"737494089777144"},{"created_time":"2017-06-19T17:11:21+0000","id":"722217974638089"},{"created_time":"2017-06-07T17:38:22+0000","id":"716256175234269"},{"created_time":"2017-03-23T04:23:47+0000","name":"Here i m....................","id":"674403329419554"},{"created_time":"2016-06-11T20:55:18+0000","id":"543045392555349"},{"created_time":"2016-06-11T20:55:18+0000","id":"543045675888654"},{"created_time":"2016-06-11T13:31:42+0000","id":"542876152572273"},{"created_time":"2016-05-16T11:44:11+0000","id":"533743106818911"},{"created_time":"2016-03-07T13:54:11+0000","id":"506948342831721"},{"created_time":"2015-04-20T08:59:04+0000","id":"384570968402793"},{"created_time":"2015-04-20T08:58:12+0000","id":"384570775069479"},{"created_time":"2015-04-20T08:55:22+0000","id":"384570081736215"},{"created_time":"2015-04-20T08:55:01+0000","id":"384569998402890"},{"created_time":"2015-04-20T08:54:41+0000","id":"384569895069567"},{"created_time":"2015-04-20T08:54:20+0000","id":"384569725069584"},{"created_time":"2015-04-20T08:54:00+0000","id":"384569568402933"}]
         * paging : {"cursors":{"before":"TlRNeE56YzNOekl6TmpneU1URTJPakUxTWpRNE9UazBNREk2TXprME1EZAzVOalF3TmpRM09ETTIZD","after":"TXpnME5UWTVOVFk0TkRBeU9UTXpPakUwTWprMU1qQXdORGc2TXprME1EZAzVOalF3TmpRM09ETTIZD"}}
         */
        var paging: PagingBean? = null
        var data: List<DataBeanX>? = null

        class PagingBean {
            /**
             * cursors : {"before":"TlRNeE56YzNOekl6TmpneU1URTJPakUxTWpRNE9UazBNREk2TXprME1EZAzVOalF3TmpRM09ETTIZD","after":"TXpnME5UWTVOVFk0TkRBeU9UTXpPakUwTWprMU1qQXdORGc2TXprME1EZAzVOalF3TmpRM09ETTIZD"}
             */
            var cursors: CursorsBean? = null

            class CursorsBean {
                /**
                 * before : TlRNeE56YzNOekl6TmpneU1URTJPakUxTWpRNE9UazBNREk2TXprME1EZAzVOalF3TmpRM09ETTIZD
                 * after : TXpnME5UWTVOVFk0TkRBeU9UTXpPakUwTWprMU1qQXdORGc2TXprME1EZAzVOalF3TmpRM09ETTIZD
                 */
                var before: String? = ""
                var after: String? = ""
            }
        }

        class DataBeanX {
            /**
             * created_time : 2016-05-10T07:33:40+0000
             * id : 531777723682116
             * name : Attend job fair at #JECRC_University as a    #company.
             * #Now_Time_Changed.
             */
            var created_time: String? = ""
            var id: String? = ""
            var name: String? = ""

        }
    }
}
