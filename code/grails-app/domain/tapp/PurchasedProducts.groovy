package tapp

class PurchasedProducts {

    Integer id
    String fullname
    String title
    String retailertitle
    Double amount = 0
    Integer recommended = 0
    Date purchaseddate = new Date()
    Date expirydate = new Date()

    static belongsTo = [ userid : Users, productid : Products, retailerid: Retailers ]

    static mapping = {
        table 'TblPurchasedProducts'
        version false
        id generator: 'identity', column: 'PurchasedID', index: 'PurchasedID'
        columns {
            userid column: 'UserID'
            fullname column: 'FullName'
            productid column: 'ProductID'
            title column: 'Title'
            retailerid column: 'RetailerID'
            retailertitle column: 'RetailerTitle'
            amount column: 'Amount'
            recommended column: 'Recommended'
            purchaseddate column: 'PurchasedDate'
            expirydate column: 'ExpiryDate'
        }
    }

    static constraints = {
    }
}
