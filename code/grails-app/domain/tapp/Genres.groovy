package tapp

import grails.rest.*
@Resource(uri='/genres',  formats=['json', 'xml'])



class Genres {

	String genre

	static hasMany = [albumses: Albums]

	static mapping = {
		version false
	}

	static constraints = {
		genre nullable: true, unique: true
	}
}
