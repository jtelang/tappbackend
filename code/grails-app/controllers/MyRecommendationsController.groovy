import grails.transaction.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import groovy.sql.Sql
import grails.converters.JSON

class MyRecommendationsController  {

    def dataSource

    def index = {

 	render "Call /myBuys/show"
    }

    def show = {

    	def db = new Sql(dataSource)
	String uid = params.uid
	println uid
	def query
	def schedule_data
        if (uid) 
        {	
	query = "select price,title from media_buy,songs where user_id=:var AND media_buy.recommend=true AND media_buy.media_identifier='song' AND media_buy.media_id=songs.id UNION select price,title from media_buy,albums where user_id=:var AND media_buy.recommend=true AND media_buy.media_identifier='album' AND media_buy.media_id=albums.id UNION select price,title from media_buy,game where user_id=:var AND media_buy.recommend=true AND media_buy.media_identifier='game' AND media_buy.media_id=game.id"

	println query	
	schedule_data = db.rows(query, [var: uid]) 
    	render schedule_data as JSON    
	}
	else
        {
	render "ERROR"	
	}
	
    }
}
