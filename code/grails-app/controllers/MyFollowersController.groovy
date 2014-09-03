import grails.transaction.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import groovy.sql.Sql
import grails.converters.JSON

@Transactional
class MyFollowersController {

    def dataSource

    def index = {

        render "Call /myFollowers/show and /myFollowers/save"
    }

    def list = {
        def db = new Sql(dataSource)
        String userid = params.userid
        String followerid = params.followerid

        def query
        def data = []
        def responseData = []

        if (userid) {
            try {
                if (followerid) {
                    String sqlString = "select user.id as user_id, full_name,phone,photo,bio from user INNER JOIN user_user ON user.id=user_user.user_following_id LEFT OUTER JOIN profile on user_user.user_id = profile.user_id where user_following_id = " + followerid + " user_user.user_id =" + userid;
                    db.eachRow(sqlString) {
                        data << [user_id: it.user_id, name: it.full_name, phone: it.phone, photo: it.photo, bio: it.bio]
                    }
                } else {
                    String sqlString = "select user.id as user_id, full_name,phone,photo,bio from user INNER JOIN user_user ON user.id=user_user.user_following_id LEFT OUTER JOIN profile on user_user.user_id = profile.user_id where user_user.user_id =" + userid;
                    db.eachRow(sqlString) {
                        data << [user_id: it.user_id, name: it.full_name, phone: it.phone, photo: it.photo, bio: it.bio]
                    }
                }

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

            } catch (Exception e) {
                responseData = [
                        'status' : 1,
                        'message': "error",
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
        def db = new Sql(dataSource)
        String userid = params.userid
        String followerid = params.followerid

        def responseData = []
        if (userid && followerid) {
            try {
                def oldfollowerid = 0
                String sqlString = "select user_following_id from user_user where user_following_id = " + followerid + " user_id =" + userid;
                db.eachRow(sqlString) {
                    oldfollowerid = it.user_following_id
                }

                if (oldfollowerid) {
                    responseData = [
                            'status' : 2,
                            'message': "already following",
                    ]
                } else {
                    def query = "insert into user_user(user_following_id, user_id) values (${followerid}, ${userid})"
                    db.execute(query)
                    responseData = [
                            'status' : 0,
                            'message': "success",
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
                    'message': "missing input parameters",
            ]
        }

        render responseData as JSON
    }

    def remove = {
        def db = new Sql(dataSource)
        String userid = params.userid
        String followerid = params.followerid

        def responseData = []
        if (userid && followerid) {
            try {
                def oldfollowerid = 0
                String sqlString = "select user_following_id from user_user where user_following_id = " + followerid + " user_id =" + userid;
                db.eachRow(sqlString) {
                    oldfollowerid = it.user_following_id
                }

                if (oldfollowerid == 0) {
                    responseData = [
                            'status' : 2,
                            'message': "already removed",
                    ]
                } else {
                    def query = "delete from user_user where user_following_id = " + followerid + " user_id =" + userid;
                    db.execute(query)
                    responseData = [
                            'status' : 0,
                            'message': "success",
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
                    'message': "missing input parameters",
            ]
        }

        render responseData as JSON
    }
}
