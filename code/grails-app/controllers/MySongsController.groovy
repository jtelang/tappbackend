import grails.transaction.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import groovy.sql.Sql
import grails.converters.JSON
import groovy.json.JsonBuilder

class MySongsController  {
    def dataSource
    def index = {
        render "Call /MySongController/show"
    }

    def list = {
        def db = new Sql(dataSource)
        def mediaid = params.songid
        def userid = params.userid

        def responseData = []
        def status = 2

        if (userid)
        {
            def data = []
            try {
                String sqlString = "select albums_id, artists_id, title, media_identifier, store, recommend from media_buy inner join songs on songs.id = media_buy.media_id where media_identifier='song' and media_buy.user_id="+userid;
                if(mediaid)
                {
                    sqlString = "select albums_id, artists_id, title, media_identifier, store, recommend from media_buy inner join songs on songs.id = media_buy.media_id where media_identifier='song' and media_buy.user_id="+userid+" and songs.id="+mediaid;
                }
                db.eachRow(sqlString) {
                    data << [albums_id: it.albums_id, artists_id: it.artists_id, title: it.title, media_identifier: it.media_identifier, store: it.store, recommend: it.recommend]
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
            } //end catch
        } //end if
        else {
            responseData = [
                    'status': 3 ,
                    'message': "missing input parameter",
            ]
        }

        render responseData as JSON
    } //end show
}

