package tapp

import grails.rest.*
@Resource(uri='/gametype', formats=['json', 'xml'])

class Gametype {

	String gametype

	static hasMany = [gamecollections: Gamecollection]

	static mapping = {
		version false
	}

	static constraints = {
		gametype nullable: true, unique: true
	}
}
