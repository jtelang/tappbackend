package tapp

import grails.rest.*
@Resource(uri='/gamecollection',  formats=['json', 'xml'])

class Gamecollection {

	String description
	Integer numGames
	String title
	BigDecimal price
	Gametype gametype
	Creator creator

	static hasMany = [games: Game]
	static belongsTo = [Creator, Gametype]

	static constraints = {
		description nullable: true
		numGames nullable: true
		price nullable: true
	}
}
