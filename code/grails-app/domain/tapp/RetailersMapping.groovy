package tapp

class RetailersMapping {

    Integer id
    String retailerlink
    Double price = 0

    static belongsTo = [ retailerid : Retailers, productid : Products ]

    static mapping = {
        table 'TblRetailersMapping'
        version false
        id generator: 'identity', column: 'ID', index: 'ID'
        columns {
            retailerid column: 'RetailerID'
            productid column: 'ProductID'
            retailerlink column: 'RetailerLink'
            price column: 'Price'
        }
    }

    static constraints = {
    }
}
