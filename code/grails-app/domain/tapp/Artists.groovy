package tapp

import grails.rest.*
@Resource(uri='/artists', formats=['json', 'xml'])

class Artists {

	String name

	static hasMany = [albumses: Albums,
	                  songses: Songs]

	static mapping = {
		version false
	}

	static constraints = {
		name unique: true
	}
}
