package tapp

import grails.rest.*
@Resource(uri='/game',  formats=['json', 'xml'])

class Game {

	String description
	String title
	BigDecimal price
	Gamecollection gamecollection
	Creator creator

	static belongsTo = [Creator, Gamecollection]

	static mapping = {
		version false
	}

	static constraints = {
		description nullable: true
		price nullable: true
	}
}
