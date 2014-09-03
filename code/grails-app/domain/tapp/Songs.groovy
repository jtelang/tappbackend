package tapp

import grails.rest.*
@Resource(uri='/songs',  formats=['json', 'xml'])


class Songs {

	String extt
	Integer offset
	String title
	BigDecimal price
	Artists artists
	Albums albums

	static belongsTo = [Albums, Artists]

	static mapping = {
		version false
	}

	static constraints = {
		extt nullable: true
		offset nullable: true
		price nullable: true
	}
}
