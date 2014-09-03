import grails.transaction.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import groovy.sql.Sql
import grails.converters.JSON
import groovy.json.JsonBuilder

class MyAlbumsController  {
    def dataSource
    def index = {
        render "Call /MyAlbumController/show"
    }

    def list = {
        def db = new Sql(dataSource)
        def mediaid = params.albumid
        def userid = params.userid

        def responseData = []
        def status = 2

        if (userid)
        {
            def data = []
            try {
                String sqlString = "select title, media_identifier, store, recommend from media_buy inner join albums on albums.id = media_buy.media_id where media_identifier='albums' and media_buy.user_id="+userid;
                if(mediaid)
                {
                    sqlString = "select title, media_identifier, store, recommend from media_buy inner join albums on albums.id = media_buy.media_id where media_identifier='albums' and media_buy.user_id="+userid+" and albums.id="+mediaid;
                }
                db.eachRow(sqlString) {
                    data << [title: it.title, media_identifier: it.media_identifier, store: it.store, recommend: it.recommend]
                    if(status !=0) status = 0
                }

                if(status == 0)
                {
                    responseData = [
                            'status': status ,
                            'message': "success",
                            'result': data ,
                    ]
                }
                else
                {
                    responseData = [
                            'status': status ,
                            'message': "no data",
                    ]
                }
            }
            catch (Exception e) {
                responseData = [
                        'status': 1 ,
                        'message': "error",
                ]
            }
        }
        else {
            responseData = [
                    'status': 3 ,
                    'message': "missing input parameter",
            ]
        }

        render responseData as JSON
    }
}

