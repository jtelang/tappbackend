import grails.converters.JSON
import groovy.sql.Sql
import tapp.Followers
import tapp.UserProfiles
import tapp.Users

class ContactsController {
    def dataSource
    def index = {
        render "action: sync, input parameters: userid & phonenumbers"
    }

    def sync = {
        def db = new Sql(dataSource)
        def userid = request.JSON.userid
        def phonenumbers = request.JSON.phonenumbers

        def responseData = []
        def phoneList = [:]
        def status = 2

        if (userid && phonenumbers) {
            def data = []
            try {
                def currentUserInstace = Users.get(Integer.parseInt(userid))
                def values = phonenumbers.split(',')
                for (oldNum in values) {
                    if(oldNum.toString().length() > 5) {
                        String sqlString = "select userid from TblUsers where CONCAT(CountryCode,PhoneNumber) like '%" + oldNum + "'";
                        db.eachRow(sqlString) {
                            def userInstace = Users.get(it.userid)
                            if (userInstace) {
                                def profileInstance = UserProfiles.findByUserid(userInstace)
                                def followerObj = Followers.createCriteria()
                                def followingInstance = followerObj.get {
                                    and {
                                        eq("userid", userInstace)
                                        eq("followerid", currentUserInstace)
                                    }
                                }

                                followerObj = Followers.createCriteria()
                                def followerInstance = followerObj.get {
                                    and {
                                        eq("userid", currentUserInstace)
                                        eq("followerid", userInstace)
                                    }
                                }
                                data << [userid: userInstace.id, fullname: profileInstance.fullname, phonenumber: oldNum, snap: profileInstance.snap, description: profileInstance.description, following: followingInstance ? 1 : 0, follower: followerInstance ? 1 : 0]
                            }
                        }
                    }
                }

//                String uids = ""
//                def values = phonenumbers.split(',')
//                for (oldNum in values) {
//                    if(oldNum.toString().length() > 0) {
//                        String sqlString = "select userid from TblUsers where PhoneNumber like '%" + oldNum + "'";
//                        println(sqlString)
//                        db.eachRow(sqlString) {
//                            phoneList[it.userid] = oldNum
//                            if (uids.length() == 0)
//                                uids = uids + it.userid
//                            else
//                                uids = uids + "," + it.userid
//                        }
//                    }
//                }
//
//                if(uids.length() > 0) {
//                    String sqlString = "select TblUserProfiles.userid, fullname, email, snap, description  from TblFollowers INNER JOIN TblUserProfiles Using(UserID) where UserID IN ("+uids+") and FollowerID =" + userid;
//                    db.eachRow(sqlString) {
//                        data << [userid: it.userid, fullname: it.fullname, email: it.email, phonenumber: phoneList.find { row -> row.key == it.userid }?.value, snap: it.snap, description: it.description, userType: "1"]
//                        if (status != 0) status = 0
//                    }
//
//                    def ids = ""
//                    values = uids.split(',')
//                    for (e in values) {
//                        int val = 1
//                        data.each
//                                {
//                                    if (Integer.parseInt(e) == it.userid && val) {
//                                        val = 0
//                                    }
//                                }
//                        if (val == 1)
//                            ids = ids ? ids + "," + e : "" + e
//                    }
//                    uids = ids
//                    if(uids.length() > 0) {
//                        sqlString = "select TblUsers.userid as userid, fullname, email, phonenumber, snap, description  from TblUsers INNER JOIN TblUserProfiles USING(userid) where TblUsers.userid in (" + uids + ")";
//                        db.eachRow(sqlString) {
//                            data << [userid: it.userid, fullname: it.fullname, email: it.email, phonenumber: phoneList.find { row -> row.key == it.userid }?.value, snap: it.snap, description: it.description, userType: "2"]
//                            if (status != 0) status = 0
//                        }
//                    }
//                }
//
                if (data.size() > 0) {
                    responseData = [
                            'status' : 0,
                            'message': "success",
                            'result' : data,
                    ]
                } else {
                    responseData = [
                            'status' : 2,
                            'message': "no data",
                    ]
                }
            }
            catch (Exception e) {
                responseData = [
                        'status' : 1,
                        'message': "error",
                        'result': e.toString(),
                ]
            }
        }
        else {
            responseData = [
                    'status' : 3,
                    'message': "missing input parameters",
            ]
        }

        render responseData as JSON
    }
}
