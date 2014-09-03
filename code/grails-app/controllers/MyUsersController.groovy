import grails.transaction.*
import tapp.Profile
import tapp.User

import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import groovy.sql.Sql
import grails.converters.JSON

@Transactional
class MyUsersController {

    def dataSource

    def index = {

        render "Call /myUsers/show"
    }

//    def show = {
//
//        def db = new Sql(dataSource)
//        String uid = params.uid
//        println uid
//        def query
//        def schedule_data
//        if (uid) {
//            query = "select (select count(*) from user_user where user_id=user.id) as followers, user.id,user.version,user.credit,user.phone, profile.id profile_id, profile.version, profile.age, profile.bio, profile.city, profile.dob, profile.email, profile.full_name, profile.gender, profile.homepage, profile.photo, profile.timezone, profile.user_id from user LEFT OUTER JOIN profile ON user.id=profile.user_id where user.id = :var"
//            println query
//
//            schedule_data = db.rows(query, [var: uid])
//            render schedule_data as JSON
//        } else {
//            query = "select (select count(*) from user_user where user_id=user.id) as followers, user.id,user.version,user.credit,user.phone, profile.id profile_id, profile.version, profile.age, profile.bio, profile.city, profile.dob, profile.email, profile.full_name, profile.gender, profile.homepage, profile.photo, profile.timezone, profile.user_id from user LEFT OUTER JOIN profile ON user.id=profile.user_id"
//            println query
//            schedule_data = db.rows(query)
//            render schedule_data as JSON
//        }
//
//    }
//
//    def save = {
//        def db = new Sql(dataSource)
//        String uid = params.uid
//        String fid = params.fid
//        def responseData = []
//        def query
//        //def schedule_data
//        if (uid) {
//            try {
//                def data = []
//                def olduser = User.get(uid)
//                if (olduser != null) {
//                    data << [user_id: olduser.id]
//                    responseData = [
//                            'status' : 4,
//                            'message': "User already exist",
//                            'result' : data,
//                    ]
//                }
//
//                query = "insert into user_user(user_following_id, user_id) values (${fid}, ${uid})"
//                db.execute(query)
//
//                responseData = [
//                        'status' : 0,
//                        'message': "success",
//                ]
//            } catch (Exception e) {
//                responseData = [
//                        'status' : 1,
//                        'message': "error",
//                ]
//            }
//        } else {
//            responseData = [
//                    'status' : 3,
//                    'message': "invalid input",
//            ]
//        }
//
//        render responseData as JSON
//    }

    def validateUser = {
        String phone = request.JSON.phone

        def responseData = []
        def query
        if (phone) {
            try {
                def data = []

                def userDomain = User.createCriteria()
                def userInstance = userDomain.get {
                    eq("phone", phone)
                }

                if (userInstance == null) {
                    userInstance = new User()
                    userInstance.phone = phone
                    userInstance.credit = 0.00
                    userInstance.save(false)
                }

                data << [user_id: userInstance?.id]
                responseData = [
                        'status' : 0,
                        'message': "success",
                        'result' : data,
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
                    'message': "missing input parameter",
            ]
        }

        render responseData as JSON
    }

    def userprofile = {
        String userid = params.userid

        def responseData = []
        if (userid) {
            try {
                def userInstance = User.get(Integer.parseInt(userid))
                if (userInstance) {
                    def profileObj = Profile.findByUser(userInstance)
                    responseData = [
                            'status' : 0,
                            'message': "success",
                            'result' : profileObj as JSON,
                    ]
                } else {
                    responseData = [
                            'status' : 4,
                            'message': "user not found",
                    ]
                }
            } catch (Exception e) {
                responseData = [
                        'status' : 1,
                        'message': "error",
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
