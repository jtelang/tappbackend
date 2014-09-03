package tapp

import grails.rest.*
@Resource(uri='/mediabuy',  formats=['json', 'xml'] )

class MediaBuy {

	Date dateCreated
	Boolean recommend
	Integer mediaId
	String mediaIdentifier
	String store
	User user

	static belongsTo = [User]

	static constraints = {
		recommend nullable: true
		mediaId nullable: true, maxSize: 45
		mediaIdentifier nullable: true, maxSize: 6
		store nullable: true, maxSize: 7
	}
}
