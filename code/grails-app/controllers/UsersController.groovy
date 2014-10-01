import grails.transaction.*
import tapp.*
import groovy.sql.Sql
import grails.converters.JSON

class UsersController {

    def dataSource

    def index = {
        render "action: validate, input parameters: phonenumber &  devicetoken" +
                "</br>" +
                "action: profile, input parameters: userid"
    }

    def validate = {
        def db = new Sql(dataSource)
        String countrycode = request.JSON.countrycode
        String phonenumber = request.JSON.phonenumber
        String devicetoken = request.JSON.devicetoken
        String deviceid = request.JSON.deviceid

        def responseData = []
        if (phonenumber && devicetoken) {
            try {
                def userInstance = null
                def userNotExist = true
                String sqlString = "select userid from TblUsers where CONCAT(CountryCode,PhoneNumber) like CONCAT('%','" + countrycode + "','" + phonenumber + "')";
                db.eachRow(sqlString) {
                    userInstance = Users.findById((int)it.userid)
                    if (userInstance.disabled == 1) {
                        db.executeUpdate("UPDATE TblUsers SET Disabled = 1 WHERE DeviceID = '" + deviceid + "'")
                    }
                    userInstance.devicetoken = devicetoken
                    userInstance.deviceid = deviceid
                    userInstance.disabled = 0
                    userInstance.save(false)
                    userNotExist = false
                }

                if(userNotExist){
                    db.executeUpdate("UPDATE TblUsers SET Disabled = 1 WHERE DeviceID = '" + deviceid + "'")
                    userInstance = new Users()
                    userInstance.countrycode = countrycode
                    userInstance.phonenumber = phonenumber
                    userInstance.currentcredit = 0.00
                    userInstance.pendingcredit = 0.00
                    userInstance.devicetoken = devicetoken
                    userInstance.deviceid = deviceid
                    if (userInstance.save(false)) {
                        def profileInstance = new UserProfiles()
                        profileInstance.userid = userInstance
                        profileInstance.save(false)
                    }
                }

                def profile = UserProfiles.findByUserid(userInstance)?.fullname ? 1 : 0

                def data = JSON.parse("{userid: " + userInstance.id + ", profile: " + profile + "}");
                responseData = [
                        'status' : 0,
                        'message': "success",
                        'result' : data,
                ]
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
                    'message': "missing input parameter",
            ]
        }

        render responseData as JSON
    }

    def profile = {
        def db = new Sql(dataSource)
        String userid = request.JSON.userid

        def responseData = []
        if (userid) {
            try {
                def userInstance = Users.get(Integer.parseInt(userid))
                if (userInstance) {
                    def profileObj = UserProfiles.findByUserid(userInstance)

                    def count = 0
                    String sqlString = "select count(*) as total from TblFollowers where FollowerID=" + userid
                    db.eachRow(sqlString) {
                        count = it.total
                    }

                    def data = JSON.parse("{fullname: '" + profileObj?.fullname + "', currentcredit: '" + userInstance.currentcredit + "', pendingcredit: '" + userInstance.pendingcredit + "', email: '" + profileObj?.email + "', dateofbirth: '" + profileObj?.dateofbirth + "', gender: '" + profileObj?.gender + "', age: '" + profileObj?.age + "', webpage: '" + profileObj?.webpage + "', city: '" + profileObj?.city + "', description: '" + profileObj?.description + "', snap: '" + profileObj?.snap + "', followers: '" + count + "' }");
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
                    'message': "missing input parameter",
            ]
        }

        render responseData as JSON
    }

    def profileUpdate = {
        def db = new Sql(dataSource)
        String userid = request.JSON.userid
        String fullname = request.JSON.fullname
        String email = request.JSON.email
        String dateofbirth = request.JSON.dateofbirth
        String gender = request.JSON.gender
        String age = request.JSON.age
        String webpage = request.JSON.webpage
        String city = request.JSON.city
        String description = request.JSON.description

        def responseData = []
        if (userid) {
            try {
                def userInstance = Users.get(Integer.parseInt(userid))
                if (userInstance) {
                    def profileObj = UserProfiles.findByUserid(userInstance)
                    if (fullname != null && fullname?.length() > 0 && profileObj.fullname != fullname) {
                        db.executeUpdate("UPDATE TblPurchasedProducts SET FullName ='" + fullname + "' WHERE UserID = " + profileObj.userid.id)
                        db.executeUpdate("UPDATE TblCreditHistory SET FullName ='" + fullname + "' WHERE UserID = " + profileObj.userid.id)
                        db.executeUpdate("UPDATE TblCreditHistory SET PurchasedByName ='" + fullname + "' WHERE PurchasedByID = " + profileObj.userid.id)
                        profileObj.fullname = fullname
                    }

                    if (email != null && email?.length() > 0)
                        profileObj.email = email

//                    if(dateofbirth != null && dateofbirth?.length() > 0)
//                    profileObj.dateofbirth = new Date(dateofbirth)
//
//                    if(gender != null && gender?.length() > 0)
//                    profileObj.gender = gender
//
//                    if(age != null && age?.length() > 0)
//                    profileObj.age = Integer.parseInt(age)
//
//                    if(webpage != null && webpage?.length() > 0)
//                    profileObj.webpage = webpage
//
//                    if(city != null && city?.length() > 0)
//                    profileObj.city = city

                    if (description != null && description?.length() > 0)
                        profileObj.description = description
                    profileObj.save(false)

                    responseData = [
                            'status' : 0,
                            'message': "success",
                    ]
                } else {
                    responseData = [
                            'status' : 2,
                            'message': "no data",
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
                    'message': "missing input parameter",
            ]
        }

        render responseData as JSON
    }
}
