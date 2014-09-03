package tapp

import grails.rest.*
@Resource(uri='/creator', formats=['json', 'xml'])

class Creator {

	String name

	static hasMany = [gamecollections: Gamecollection,
	                  games: Game]

	static mapping = {
		version false
	}

	static constraints = {
		name unique: true
	}
}
