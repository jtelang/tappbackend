package tapp


import grails.rest.*
@Resource(uri='/profile',  formats=['json', 'xml'])

class Profile {
    byte[] photo
    String fullName
    Integer age
    Date dob	
    //String phone 
    String bio
    String homepage
    String email
    String timezone
    String city 
    String gender
	

    static belongsTo = [ user : User ]

    static constraints = {
        fullName blank: false
	age nullable: true
	dob nullable: true
 	gender nullable: true
        //phone nullable: false
        bio nullable: true, maxSize: 1000
        homepage url: true, nullable: true
        email email: true, blank: true 
        photo nullable: true, maxSize: 2 * 1024 * 1024
        city nullable: true
        timezone nullable: true
    }

    String toString() { return "Profile of $fullName (id: $id)" }
    String getDisplayString() { return fullName }
}
