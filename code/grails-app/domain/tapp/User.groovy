package tapp

import grails.rest.*
@Resource(uri='/user', formats=['json', 'xml'])


class User {
    String phone 
    //boolean enabled = true
    //boolean accountExpired
    //boolean accountLocked
    //Date dateCreated
    BigDecimal credit
    //String gcmToken 

    static hasOne = [ profile: Profile ]
    static hasMany = [ mediabuys: MediaBuy, following: User ]

    static transients = ['springSecurityService']

    static constraints = {
        credit nullable: true
        phone size: 8..20, unique: true, blank: false
        profile nullable: true
        //gcmToken nullable: true, maxSize: 45

    }

    //static searchable = {
    //    except = ["passwordHash"]
    //}

    static mapping = {
        mediabuys sort: "dateCreated", order: "desc"
    }

//    Set<Role> getAuthorities() {
//        UserRole.findAllByUser(this).collect { it.role } as Set
//    }

    String toString() { return "User $phone (id: $id)" }
    String getDisplayString() { return phone }
}


