import groovy.sql.Sql
import grails.converters.JSON
import tapp.Followers
import tapp.UserProfiles
import tapp.Users

class FollowersController {

    def dataSource
    def androidGcmService

    def index = {
        render "action: list, input parameters:  userid " +
                "</br>" +
                "action: add, input parameters: userid & followerid "
    }

    def list = {
        def db = new Sql(dataSource)
        def userid = request.JSON.userid

        def responseData = []
        def status = 2

        if (userid) {
            def data = []
            try {
                String sqlString = "select TblUsers.userid as userid, fullname, snap, description from TblUsers INNER JOIN TblFollowers on TblUsers.UserID = TblFollowers.UserID INNER JOIN TblUserProfiles on TblFollowers.UserID = TblUserProfiles.UserID where TblFollowers.FollowerID = " + userid;
                db.eachRow(sqlString) {
                    data << [followerid: it.userid, fullname: it.fullname, snap: it.snap, description: it.description]
                    if (status != 0) status = 0
                }

                if (status == 0) {
                    responseData = [
                            'status' : status,
                            'message': "success",
                            'result' : data,
                    ]
                } else {
                    responseData = [
                            'status' : status,
                            'message': "no data",
                    ]
                }
            }
            catch (Exception e) {
                responseData = [
                        'status' : 1,
                        'message': "error",
                        'result' : e.toString(),
                ]
            }
        } else {
            responseData = [
                    'status' : 3,
                    'message': "missing input parameters",
            ]
        }

        render responseData as JSON
    }

    def add = {
        String userid = request.JSON.userid
        String followerid = request.JSON.followerid

        def responseData = []
        if (userid && followerid) {
            try {
                if ((Integer.parseInt(userid) != Integer.parseInt(followerid))) {

                    def followerObj = Followers.createCriteria()
                    def followerOldInstance = followerObj.get {
                        and {
                            eq("userid.id", Integer.parseInt(userid))
                            eq("followerid.id", Integer.parseInt(followerid))
                        }
                    }

                    if (followerOldInstance == null) {
                        def userInstance = Users.get(Integer.parseInt(userid))
                        def followerInstance = Users.get(Integer.parseInt(followerid))

                        followerOldInstance = new Followers()
                        followerOldInstance.userid = userInstance
                        followerOldInstance.followerid = followerInstance
                        followerOldInstance.save(false)

                        if(userInstance.disabled == 0)
                        {
                            def deviceToken = []
                            deviceToken << userInstance.devicetoken
                            //def message = ["message": followerInstance.countrycode + followerInstance.phonenumber + " added you as friend", "countrycode": followerInstance.countrycode, "phonenumber": followerInstance.phonenumber]
                            def message = ["message": '{"message":"'+ followerInstance.countrycode + followerInstance.phonenumber + ' added you as friend", "countrycode":"'+ followerInstance.countrycode+'", "phonenumber": "'+followerInstance.phonenumber+'"}']
                            androidGcmService.sendMessage(message, deviceToken, "", grailsApplication.config.android.gcm.api.key)
                        }

                        responseData = [
                                'status' : 0,
                                'message': "success",
                        ]
                    } else {
                        responseData = [
                                'status' : 2,
                                'message': "already following",
                        ]
                    }
                }else {
                    responseData = [
                            'status' : 4,
                            'message': "cannot follow yourself",
                    ]
                }
            } catch (Exception e) {
                responseData = [
                        'status' : 1,
                        'message': "error",
                        'result' : e.toString(),
                ]
            }
        } else {
            responseData = [
                    'status' : 3,
                    'message': "missing input parameters",
            ]
        }

        render responseData as JSON
    }
}
