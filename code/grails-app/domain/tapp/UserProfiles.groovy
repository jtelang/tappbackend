package tapp

class UserProfiles {

    Integer id
    String fullname
    String email
    Date dateofbirth = new Date()
    GenderEnum gender = GenderEnum.none
    Integer age
    String webpage
    String city
    String description
    String snap

    static belongsTo = [ userid : Users ]

    static mapping = {
        table 'TblUserProfiles'
        version false
        id generator: 'identity', column: 'ProfileID', index: 'ProfileID'
        columns {
            userid column: 'UserID'
            fullname column: 'FullName'
            email column: 'Email'
            dateofbirth column: 'DateofBirth'
            gender column: 'Gender'
            age column: 'Age'
            webpage column: 'Webpage'
            city column: 'City'
            description column: 'Description'
            snap column: 'Snap'
        }
    }

    static constraints = {
    }
    enum GenderEnum {
        none, male, female
    }
}


