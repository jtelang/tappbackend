import grails.transaction.*
import tapp.MediaBuy
import tapp.User

import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import groovy.sql.Sql
import grails.converters.JSON

class MyBuysController {

    def dataSource

    def index = {

        render "Call /myBuys/show"
    }

    def show = {

        def db = new Sql(dataSource)
        String id = params.id
        println id
        def query
        def schedule_data
        if (id) {
            query = "select * from media_buy where user_id=:var"
            schedule_data = db.rows(query, [var: id])
            render schedule_data as JSON
        } else {
            query = "select * from media_buy"
            schedule_data = db.rows(query)
            render schedule_data as JSON
        }

    }

    def save = {
        def db = new Sql(dataSource)
        String uid = params.uid
        String fid = params.fid
        def query
        //def schedule_data
        if (uid) {
            query = "insert into user_user(user_following_id, user_id) values (${fid}, ${uid})"

            try {
                db.execute(query)
                render "OK"
            } catch (Exception e) {
                render "ERROR"
            }
        }
    }
}
