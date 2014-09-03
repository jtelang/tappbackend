package tapp

import grails.rest.*
@Resource(uri='/albums', formats=['json', 'xml'])

class Albums {

	Integer disclen
	String extd
	Integer numTracks
	String title
	BigDecimal price
	Genres genres
	Artists artists

	static hasMany = [songses: Songs]
	static belongsTo = [Artists, Genres]

	static constraints = {
		disclen nullable: true
		extd nullable: true
		numTracks nullable: true
		price nullable: true
	}
}


