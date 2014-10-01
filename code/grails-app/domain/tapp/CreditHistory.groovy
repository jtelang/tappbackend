package tapp

class CreditHistory {

    Integer id
    String fullname
    String purchasedbyname
    String retailertitle
    Double amount = 0
    Double commission = 0
    Date purchaseddate = new Date()

    static belongsTo = [userid : Users, purchasedbyid : Users, retailerid: Retailers, purchasedid: PurchasedProducts]

    static mapping = {
        table 'TblCreditHistory'
        version false
        id generator: 'identity', column: 'HistoryID', index: 'HistoryID'
        columns {
            purchasedid column: 'PurchasedID'
            userid column: 'UserID'
            fullname column: 'FullName'
            purchasedbyid column: 'PurchasedByID'
            purchasedbyname column: 'PurchasedByName'
            retailerid column: 'RetailerID'
            retailertitle column: 'RetailerTitle'
            amount column: 'Amount'
            commission column: 'Commission'
            purchaseddate column: 'PurchasedDate'
        }
    }

    static constraints = {
    }
}
