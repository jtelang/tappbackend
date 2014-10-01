package tapp

class Followers {

    Integer id

    static belongsTo = [ userid : Users, followerid : Users ]

    static mapping = {
        table 'TblFollowers'
        version false
        id generator: 'identity', column: 'ID', index: 'ID'
        columns {
            userid column: 'UserID'
            followerid column: 'FollowerID'
        }
    }

    static constraints = {
    }
}
