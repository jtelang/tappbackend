package tapp

class Retailers {

    Integer id
    String retailertitle

    static mapping = {
        table 'TblRetailers'
        version false
        id generator: 'identity', column: 'RetailerID', index: 'RetailerID'
        columns {
            retailertitle column: 'RetailerTitle'
        }
    }

    static constraints = {
    }
}
