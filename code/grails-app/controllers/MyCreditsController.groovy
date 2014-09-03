import groovy.sql.Sql
import grails.converters.JSON
import tapp.MediaBuy
import tapp.User

class MyCreditsController {
    def dataSource
    def show = {

        def db = new Sql(dataSource)
        String sid = params.id
        def query
        def schedule_data
        if (sid) {
            query = "select id, name  from artists where id=:var"
            schedule_data = db.rows(query, [var: sid])
        } else {
            query = "select id, name from artists"
            schedule_data = db.rows(query)
        }

        render schedule_data as JSON
    }


    def updateCredit = {
        def db = new Sql(dataSource)
        def uid = params.uid
        def fid = params.fid
        def mid = params.mid

        def query
        //def schedule_data
        def responseData = []

        if (uid && fid && mid) {
            try {
                def recommendedMedia = MediaBuy.createCriteria()
                def recommendedMediaVal = recommendedMedia.get {
                    and {
                        eq("mediaId", mid)
                        eq("recommend", true)
                        eq("user.id", uid)
                    }
                }

                if (recommendedMediaVal) {
                    def recommendedBy = User.get(Integer.parseInt(uid))
                    recommendedBy.credit = recommendedBy.credit + 1
                    recommendedBy.save(false)

                    def recommendedTo = User.get(Integer.parseInt(fid))
                    recommendedTo.credit = recommendedTo.credit + 1
                    recommendedTo.save(false)

                    responseData = [
                            'status' : 0,
                            'message': "success",
                    ]
                }

                responseData = [
                        'status' : 2,
                        'message': "no data",
                ]

            } catch (Exception e) {
                responseData = [
                        'status' : 1,
                        'message': "error",
                ]
            }
        } else {
            responseData = [
                    'status' : 3,
                    'message': "invalid input",
            ]
        }

        render responseData as JSON
    }
}
