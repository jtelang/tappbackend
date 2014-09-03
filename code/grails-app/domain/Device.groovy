import grails.rest.*
@Resource(uri='/device')

class Device {

	String token, projectId
	
    static constraints = {
		token(nullable:false, blank:false, unique:true)
		projectId(nullable:true)
    }
}
