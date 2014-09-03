import grails.transaction.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import groovy.sql.Sql
import grails.converters.JSON
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class MyContactsController {
    def dataSource
    def index = {
        render "Call /myContactsController/show"
    }

    def sync = {
        def db = new Sql(dataSource)
        def userid = request.JSON.userid
        def phones = request.JSON.phones

        def responseData = []
        def status = 2

        if (userid && phones) {
            def data = []
            try {

                String sqlString = "select user.id as user_id, full_name,phone,photo,bio from user INNER JOIN user_user ON user.id=user_user.user_following_id LEFT OUTER JOIN profile on user_user.user_id = profile.user_id where phone in (" + phones + ") and user_user.user_id =" + userid;
                db.eachRow(sqlString) {
                    data << [user_id: it.user_id, name: it.full_name, phone: it.phone, photo: it.photo, bio: it.bio, userType: "1"]
                    if (status != 0) status = 0
                }

                def numbers
                def values = phones.split(',')
                for (e in values) {
                    int val = 1
                    data.each
                            {
                                if (e == it.phone && val) {
                                    val = 0
                                }
                            }
                    if (val == 1)
                        numbers = numbers ? numbers + "," + e : "" + e
                }
                phones = numbers

                sqlString = "select user.id as user_id, full_name, phone, photo, bio from user LEFT OUTER JOIN profile on user.id = profile.user_id where phone in (" + phones + ")";
                db.eachRow(sqlString) {
                    data << [user_id: it.user_id, name: it.full_name, phone: it.phone, photo: it.photo, bio: it.bio, userType: "2"]
                    if (status != 0) status = 0
                }

                if (phones) {
                    def numbers1
                    def values1 = phones.split(',')
                    for (e in values1) {
                        int val = 1
                        data.each
                                {
                                    if (e == it.phone && val) {
                                        val = 0
                                    }
                                }
                        if (val == 1)
                            numbers1 = numbers1 ? numbers1 + "," + e : "" + e
                    }
                    phones = numbers1

                    if (phones) {
                        def values2 = phones.split(',')
                        for (e in values2) {
                            data << [user_id: 0, name: null, phone: e, photo: null, bio: null, userType: "0"]
                            if (status != 0) status = 0
                        }
                    }
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
                ]
            } //end catch
        } //end if
        else {
            responseData = [
                    'status' : 3,
                    'message': "missing input parameters",
            ]
        }

        render responseData as JSON
    } //end show1
}

